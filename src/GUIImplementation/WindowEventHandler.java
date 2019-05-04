package GUIImplementation;

import java.awt.event.WindowAdapter;
import java.net.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import network.*;

import javax.swing.JOptionPane;

import main.Session;
import java.io.*;

public class WindowEventHandler extends WindowAdapter{
	GUILogic gui;
	public WindowEventHandler(GUILogic gui) {
		this.gui = gui;
	}
	
	
	@Override
	public void windowClosing(WindowEvent e) {
			try {
				if(Session.getServer() != null){
					
						if(Session.getServer().getServerSocket() != null) {
							Session.getServer().getServerSocket().close();
						}
				
				}
				
				if(Session.getDeviceType() != -1) {
					if(Session.getDeviceType() == 0) {
						//Server
						if(Session.getClient() != null && !Session.getClient().getSocket().isClosed()) {
							Communication com = new Communication();
							com.sendDisconnectPacket();
						}
						
						
					}
					else {
					
						//Client
						if(Session.getServer() != null && !Session.getServer().getSocket().isClosed()) {
							Communication com = new Communication();
							com.sendDisconnectPacket();
						}
					
					}
					
					
					
				}
				
				if(Session.getMessageListenereThread() != null) {
					if(Session.getMessageListenereThread().ss != null) {
						Session.getMessageListenereThread().ss.close();
					}
				}
				
				if(Session.getServerListenerThread() != null) {
					if(Session.getServerListenerThread().ss != null) {
						Session.getServerListenerThread().ss.close();
					}
				}
				
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("..\\history.txt")));
				
				oos.writeObject(Session.getHistory());
				
				
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch(NullPointerException e1) {
				e1.printStackTrace();
			}
			finally {
				gui.dispose();
			}
		}
	
}
