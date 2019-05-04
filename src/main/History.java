package main;

import java.io.*;
import java.net.*;

public class History implements Serializable {
	
	private File file;
	private InetAddress senderIP;
	private InetAddress recieverIP;
	
	public History(File f , InetAddress sip , InetAddress rip) {
		this.file = f;
		this.senderIP = sip;
		this.recieverIP = rip;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public InetAddress getSenderIP() {
		return this.senderIP;
	}
	
	public void setSenderIP(InetAddress i) {
		senderIP = i;
	}
	
	public InetAddress getRecieverIP() {
		return this.recieverIP;
	}
	
	public void setRecieverIP(InetAddress i) {
		recieverIP = i;
	}
	
	
	
}
