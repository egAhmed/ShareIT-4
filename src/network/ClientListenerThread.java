package network;

import java.net.*;
import java.time.LocalDateTime;
import java.util.*;


import exceptions.ShareItException;
import main.History;
import main.Session;

import java.io.*;

public class ClientListenerThread implements Runnable{

	
	private String name;
	private Thread t;
	public volatile boolean end = false;
	private Client client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientListenerThread(String n, Client client) {
		this.name = n;
		this.client = client;
		
		t = new Thread(this ,name);
		System.out.println("ss: before client listener start" );
		t.setDaemon(true);
		t.start();
	}
	
	
	
	@Override
	public void run() {
		try {
			
			
			while(true) {
				if(end) {
					break;
				}
				if(this.client.getSocket().isClosed()) {
					break;
				}
				this.ois = new ObjectInputStream(this.client.getSocket().getInputStream());
				this.oos = new ObjectOutputStream(this.client.getSocket().getOutputStream());
				
				System.out.println("Shareit : Waiting for client to write");
				Object object = ois.readObject(); // wait for client to send;
				
				
				if(object instanceof Packet) {
					Packet p = (Packet) object;
					
					switch(p.getPacketType()) {
						case FileSendPermitPacket:
							System.out.println("Client Listner: permit packet");
							FilePacket fp = (FilePacket) p;
							
							boolean hasPermission = Session.askForPermission(fp.getFile() , this.client);
							
							if(hasPermission) {
								
								FilePacket filePacket = new FilePacket(PacketType.FileSendAcceptPacket , fp.getFile());
								
								oos.writeObject(filePacket);
								System.out.println("ShareIt : Server Recieving file" );

								//Code to read File
								File dir = new File(Session.getPath());
								if(!dir.exists()) {
									dir.mkdir();
								}
								int pos = fp.getFile().getName().lastIndexOf(".");
								String fileName = "";
								if(pos >0 && pos < fp.getFile().getName().length() -1) {
									fileName = fp.getFile().getName().substring(0, pos);
									fileName += "_"+ System.currentTimeMillis();
									fileName += fp.getFile().getName().substring(pos , fp.getFile().getName().length());
								}
								else {
									fileName = fp.getFile().getName();
									fileName += "_"+ System.currentTimeMillis();
								}
								
								
								File newFile = new File(Session.getPath() + "\\" + fileName );
								FileOutputStream fos = new FileOutputStream(newFile);
								
								
								
								byte[] fileArr =  fp.getFileArr();
								fos.write(fileArr, 0, fileArr.length);
						        System.out.println("ShareIt : ServerFile transfer success");
						         
						        fos.close();
					        
								History history = new History(newFile , Session.getClient().getIP() , Session.getServer().getIP());
								Session.addHistory(history);
								
								Session.showErrorMessage("File Recieved " + fp.getFile().getName(), "green");
//						        ois.close();
//						        oos.close();
//								
								
							}
							else {
								
								FilePacket filePacket = new FilePacket(PacketType.FileSendRejectPacket , fp.getFile());
								
								oos.writeObject(filePacket);
								
							}
							
						break;
						case RequestDirectoryPacket:
							MessagePacket msg = (MessagePacket) p;
							
							if(Session.getAllowDirectFileTransfer()) {
								String directory = msg.getMessage();
								System.out.println("ShareIt: Server RequestDirectoryPacket " + directory);
								File folder = new File(directory);
								System.out.println("Hello World");
								
								if(folder.isDirectory()) {
									File[] files = folder.listFiles();
									if(files != null) {
										List<File> fileList = new ArrayList<File>(Arrays.asList(files));
										
										DirectoryPacket dir = new DirectoryPacket(PacketType.RecievedDirectoryPacket , directory , fileList);
										oos.writeObject(dir);
									}
									else {
										List<File> fileList = new ArrayList<File>();
										DirectoryPacket dir = new DirectoryPacket(PacketType.RecievedDirectoryPacket , directory , fileList);
										oos.writeObject(dir);
									}
									
								}
								else {
									Communication com = new Communication();
									
									com.sendFileToClient(folder, false);
									System.out.println("ShareIt: Server RequestDirectoryPacket else condition "+directory);
									int index=directory.lastIndexOf('\\');
									System.out.println(index);
									directory = directory.substring(0,index);
									
									if(directory.equals("C:") || directory.equals("")) {
										directory = "C:\\";
									}
								    System.out.println("ShareIt: Server RequestDirectoryPacket else condition "+directory);
								    File lastFolder = new File(directory);
								    File[] files = lastFolder.listFiles();
									List<File> fileList = new ArrayList<File>(Arrays.asList(files));
									
									DirectoryPacket dir = new DirectoryPacket(PacketType.RecievedDirectoryPacket , directory , fileList);
									
									oos.writeObject(dir);
									
									Session.showErrorMessage("Client downloaded " + folder.getName(), "green");
								}
							}
							else {
								MessagePacket msg1 = new MessagePacket(PacketType.DirectTransferDeniedPacket , "Server does not permit direct file transfer");
								
								oos.writeObject(msg1);
							
							}
						break;
						case DisconnectPacket:
							MessagePacket dp = (MessagePacket) p;
							Session.showConnectionDetails("Client Disconnected: message:" + dp.getMessage());
							Session.closeConnection();
						break;
//						case MessagePacket:
//							MessagePacket msg2 = (MessagePacket) p;
//							Session.addMessage("Peer: " + msg2.getMessage());
//						break;
					}
					
				}
			
			
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			
			// Send an invalid packet message to client
			
			e.printStackTrace();
		} 
	}

	public synchronized void stop() {
		end = true;
	}
	
	public Thread getThread() {
		return this.t;
	}
	
}
