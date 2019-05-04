package network;

import java.io.*;

import java.net.*;

import main.Session;


public class Server {
	
	private InetAddress ip;
	private int port;
	private int backlog;
	private Socket socket;
	private ServerSocket ss;
	
	
	public Server(InetAddress ip , int port , int backlog) throws IOException {
		this.ip = ip;
		this.port = port;
		this.backlog = backlog;
	}
	
	public void createSocket() throws IOException {
		ss = new ServerSocket(this.port , this.backlog, this.ip);
		System.out.println("ss: before thread" );
		Session.setServer(this);
		ServerAcceptThread sat = new ServerAcceptThread("Accept Thread" , this);
		Session.setServerAcceptThread(sat);
	}
	
	/*public Socket connectToClient() {
		
		try {
			Socket clientSocket = this.serverSocket.accept();
			
			return clientSocket;
		
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
		
	}*/
	
	public InetAddress getIP() {
		return this.ip;	
	}
	
	public void setIP(InetAddress ip) {
		this.ip = ip;
	}
	
	
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public int getBacklog() {
		return this.backlog;
	}
	
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void setServerSocket(ServerSocket sss) {
		if(Session.getDeviceType() == 1) {
			//TODO: Throw exception
			
			return;
		}
		this.ss = sss;
	}
	
	public ServerSocket getServerSocket() {
		if(Session.getDeviceType() == 1) {
			//TODO: Throw Exception
			return null;
		}
		return this.ss;
	}
	
	
	@Override
	public String toString() {
		return "IP:"+ip.getHostAddress()+" port:" + port;
	}
}
