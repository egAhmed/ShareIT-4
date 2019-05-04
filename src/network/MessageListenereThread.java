package network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import main.History;
import main.Session;

public class MessageListenereThread implements Runnable{

	private String name;
	private Thread t;
	public volatile boolean end = false;
	public ServerSocket ss = null;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private InetAddress ip;
	
	public MessageListenereThread(String n, InetAddress ip) {
		this.name = n;
		this.ip = ip;
		t = new Thread(this ,name);
		System.out.println("ss: before message listener start" );
		t.setDaemon(true);
		t.start();
	}
	
	
	
	@Override
	public void run() {
		
		Socket socket = null;
		try {
			
			
			
			
			
			System.out.println("ShareIt: in serverlistenerThread before while");
			while(true) {
				if(end) {
					break;
				}
//				if(this.server.getSocket().isClosed()) {
//					break;
//				}
				
				ss = new ServerSocket(Session.MessagePort ,10 , ip);
				System.out.println("ShareIt: in MessageListenereThread before accept");
				socket = ss.accept();
				this.ois = new ObjectInputStream(socket.getInputStream());
				this.oos = new ObjectOutputStream(socket.getOutputStream());
				
				System.out.println("ShareIt: in MessageListenereThread before Streams");
				
				System.out.println("Shareit : Waiting for server to write");
				Object object = ois.readObject(); // wait for client to send;
				
				
				if(object instanceof Packet) {
					Packet p = (Packet) object;
					
					switch(p.getPacketType()) {
						case MessagePacket:
							MessagePacket msg2 = (MessagePacket) p;
							Session.addMessage("Peer: " + msg2.getMessage());
						break;
					}
					
				}
				socket.close();
				ss.close();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			
			// Send an invalid packet message to client
			
			e.printStackTrace();
		} 
		finally {
			try {
				if(ss!= null && !ss.isClosed())
				ss.close();
				if(socket != null && !socket.isClosed())
				socket.close();
			}
			catch(Exception e) {
				System.out.println("unexpected error");
				e.printStackTrace();
			}
		}
	}

	public synchronized void stop() {
		end = true;
		if(ss != null) {
			try {
				ss.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public Thread getThread() {
		return this.t;
	}
}
