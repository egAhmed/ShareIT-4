package network;

import java.net.*;
import java.util.*;

import exceptions.ShareItException;
import main.Session;

import java.io.*;

public class Client {
	
	private int port;
	private InetAddress clientIP;
	private Socket clientSocket;
	
	public Client() {}
	
	public void setSocket(Socket cs) {
		this.clientSocket = cs;
	}

	public Socket getSocket() {
		return this.clientSocket;
	}
	
	public void setIP(InetAddress clientIP) {
		this.clientIP = clientIP;
	}
	
	public InetAddress getIP() {
		return this.clientIP;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public Socket connect(InetAddress ip , int port) throws IOException, ShareItException, ClassNotFoundException {
		
			Socket s = new Socket(ip , port);
			
			
			
			
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			Object object = ois.readObject();
			
			if(object instanceof Packet) {
			
				Packet p = (Packet) object;
				
				if(p.getPacketType() == PacketType.ClientAlreadyConnectedPacket) {
					MessagePacket msg = (MessagePacket) p;
					
					Session.showConnectionDetails(msg.getMessage());
					
				}
				else if(p.getPacketType() == PacketType.ConnectedPacket) {
					
					MessagePacket msg = (MessagePacket) p;
					
					Session.showConnectionDetails(msg.getMessage());

					Server server = new Server(ip,port,10);
					server.setSocket(s);
					
					this.setIP(s.getLocalAddress());
					this.setPort(s.getLocalPort());
					Session.setClient(this);
					
					System.out.println("ShareIt : In Client connect: " + s.getLocalAddress());
					ServerListenerThread slt= new ServerListenerThread("server listener", server, s.getLocalAddress());
					
					Session.setServerListenerThread(slt);
					System.out.println("ShareIt: server listener created");
							
				}
				
						
				return  s;
			}
			else {
				throw new ShareItException("Some unexpected error occured");
			}
			
		
	}
	
	
	
	@Override
	public String toString() {
		return "IP: " + this.clientIP.getHostAddress() + " Port: " +this.port + " Host: " + this.clientIP.getHostName(); 
	}
	
	
	
}
