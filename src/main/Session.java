package main;

import java.util.*;
import java.io.*;

import java.net.*;
import java.nio.file.Paths;

import GUIImplementation.GUILogic;
import exceptions.ShareItException;
import network.*;


public class Session {
	
	private static Client client;// convert to array list for multiple clients
	private static Server server;
	private static int deviceType = -1; // 0 for server , 1 for client
	private static int PORT = 21132;
	private static int BACKLOG = 10;
	private static int SENDBUFFER = 100;
	private static GUILogic gui;
	public static int MessagePort = 33322;
	private static String pathToSave = Paths.get("..\\ShareItTransferedfiles").toAbsolutePath().toString();
	private static String sendFilePath = "";
	private static int CLIENTPORT = 22222;
	private static String currentDirectory = "C:\\";
	private static boolean allowDirectFileTransfer = false;
	private static String disconnectMessage = "Disconnected";
	private static String sentSuccessfulMessage = "File sent successfully";
	private static List<History> history = new ArrayList<History>();
	private static List<File> files = new ArrayList<File>();
	
	private static ServerAcceptThread sat;
	private static ClientListenerThread clt;
	private static ServerListenerThread slt;
	private static MessageListenereThread mlt;
	
	public static GUILogic getGUI() {
		return Session.gui;
	}
	
	public static void setGUI(GUILogic g) {
		Session.gui = g;
	}
	
	public static String getCurrentDirectory() {
		return Session.currentDirectory;
	}
	
	public static void setCurrentDirectory(String cd) {
		Session.currentDirectory = cd;
	}
	
	
	public static void setFiles(List<File> files) {
		Session.files = files;
		Session.gui.setFiles();
	}
	
	public static List<File> getFiles() {
		return Session.files;
	}
	
	public static List<History> getHistory(){
		return Session.history;
	}
	
	public static void loadHistory()  {
		File f = new File("..\\history.txt");
		if(f.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				Session.history = (List<History>) ois.readObject();
				Session.gui.refreshTable();
			} catch (IOException|ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void setHistory(List<History> files) {
		Session.history = files;
	}
	
	public static void addHistory(History history) {
		System.out.println("In add history");
		Session.history.add(history);
		Session.gui.refreshTable();
	}
	
	public static Client getClient() {
		return Session.client;
	}
	
	public static void setClient(Client client) {
		Session.client = client;
	}
	
	public static String getSendFilePath() {
		return Session.sendFilePath;
	}
	
	public static void setSendFilePath(String f) {
		Session.sendFilePath = f;
	}
	
	public static Server getServer() {
		return Session.server;
	}
	
	public static void setServer(Server server) {
		Session.server = server;
	}
	
	public static boolean isServer() {
		return deviceType == 0 ? true :false;
	}
	
	public static boolean isClient() {
		return deviceType == 1 ? true :false;
	}
	
	public static int getDeviceType() {
		return Session.deviceType;
	}
	
	public static void setDeviceType(int device) {
		Session.deviceType = device;
	}
	
	public static int getPort() {
		return Session.PORT;
	}
	
	public static int getClientPort() {
		return Session.CLIENTPORT;
	}
	
	public static int getSendBuffer() {
		return Session.getSendBuffer();
	}
	
	public static String getPath() {
		return Session.pathToSave;
	}
	
	public static void setPath(String p) {
		Session.pathToSave = p;
	}
	
	public static void showConnectedClient() {
		Session.gui.addClient();
	}
	
	public static boolean getAllowDirectFileTransfer() {
		return allowDirectFileTransfer;
	}
	
	public static void setAllowDirectFileTransfer(boolean access) {
		allowDirectFileTransfer = access;
	}
	
	public static String getDisconnectMessage() {
		return Session.disconnectMessage;
	}
	
	public static void setDisconnectMessage(String p) {
		Session.disconnectMessage = p;
	}
	
	public static String getSentSuccessfulMessage() {
		return Session.sentSuccessfulMessage;
	}
	
	public static void setSentSuccessfulMessage(String p) {
		Session.sentSuccessfulMessage = p;
	}
	
	public static void createServer() throws UnknownHostException, IOException {
		Server s = new Server(InetAddress.getLocalHost() ,Session.PORT, Session.BACKLOG );
		Session.setDeviceType(0); // for server
		s.createSocket();
		//Socket clientSocket = s.connectToClient();
		
		//Session.client;
	}
	
	public static void connectToServer(InetAddress ip , int port) throws IOException, ClassNotFoundException, ShareItException {
		Client client = new Client();
		Socket soc = client.connect(ip, port);
		Session.server = new Server(ip,port,Session.BACKLOG);
		deviceType = 1; // Client
		Session.server.setSocket(soc);
		
		
	}
	
	
	public static boolean askForPermission(File file, Object object) {
		
		if(object instanceof Client) {
			Client client = (Client) object;
			System.out.println("ShareIt : CLient ask for permission Session");
			boolean hasPermission = Session.gui.askForPermissoin(file , client.getSocket().getInetAddress() , client.getSocket().getPort());
			
//			if(hasPermission) {
//				System.out.println("ShareIt: in askpermission client ");
//				History history = new History(file , Session.getClient().getIP() , Session.getServer().getIP());
//				Session.addHistory(history);
//			}
			return hasPermission;	
		}
		else {
			Server server = (Server) object;
			System.out.println("ShareIt : Server ask for permission Session");
			boolean hasPermission = Session.gui.askForPermissoin(file , server.getSocket().getInetAddress() , server.getSocket().getPort());
			
			
			
			return hasPermission;
		}
		
		
	}

	public static void sendFile(String fileString,boolean toAll) throws ClassNotFoundException, IOException {
		System.out.println("ShareIt: in Session sending file " + fileString);
		File file = new File(fileString);
		Communication com = new Communication();
		System.out.println("ShareIt: Before callingg send file on communication" );
		if(Session.getDeviceType() == 1) {
			System.out.println("ShareIt: In session sendign to server");
			
			boolean sent = com.sendFileToServer(file);
			if(sent) {
				History history = new History(file , Session.getClient().getIP() , Session.getServer().getIP());
				Session.addHistory(history);	
			}
			
		}
		else {
			System.out.println("ShareIt: In session sendign to clients");
			
			boolean sent = com.sendFileToClient(file, true);
			if(sent) {

				History history = new History(file , Session.getServer().getIP() , Session.getClient().getIP());
				Session.addHistory(history);
			}
		}
		
	}
	
	public static void showConnectionDetails(String details) {
		gui.showConnectionDetails(details);
	}
	
	public static void showErrorMessage(String details , String color) {
		
		gui.showErrorMessage(details, color);
	}
	
	public static void closeConnection() throws IOException {
		
		
		if(Session.getDeviceType() == 0) {// server
			if(Session.getServer().getServerSocket() != null) {
				Session.getServer().getServerSocket().close();
			}
			Session.getClient().getSocket().close();
			Session.setClient(null);
			Session.setServer(null);
			Session.setPath(Paths.get("..\\ShareItTransferedfiles").toAbsolutePath().toString());
			if(Session.getServerAcceptThread() != null) {
				Session.getServerAcceptThread().stop();
				Session.setServerAcceptThread(null);
			}
			
			if(Session.getClientListenerThread() != null) {
				Session.getClientListenerThread().stop();
				Session.setClientListenerThread(null);
			}
			
			if(Session.getMessageListenereThread() != null) {
				if(Session.getMessageListenereThread().ss != null){
					Session.getMessageListenereThread().ss.close();
				}
				
				Session.getMessageListenereThread().stop();
				Session.setMessageListenereThread(null);
			}
			Session.gui.fileListModel.clear();
			showConnectionDetails("Connection is Closed");
			showErrorMessage("", "green");
		}
		else if(Session.getDeviceType() == 1) {// client
			Session.getServer().getSocket().close();
			Session.setServer(null);
			Session.setClient(null);
			Session.setPath(Paths.get("..\\ShareItTransferedfiles").toAbsolutePath().toString());
			System.out.println("here");
			if(Session.getServerListenerThread() != null) {
				if(Session.getServerListenerThread().ss != null){
					Session.getServerListenerThread().ss.close();
					System.out.println("here");
				}
				Session.getServerListenerThread().stop();
				Session.setServerListenerThread(null);
			}
			Session.gui.fileListModel.clear();
			showConnectionDetails("Connection is Closed");
			showErrorMessage("", "green");
		}
		
		deviceType = -1;
		gui.setVisibility();
		
	}
	
	public static void addMessage(String msg) {
		gui.addMessage(msg);
	}
	
	
	
	public static void main(String[] args) {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println("Host name:" + ip.getHostName());
			System.out.println("Host address:" + ip.getHostAddress());
			File folder = new File("\\");
			System.out.println("Hello World");
			File[] files = folder.listFiles();
			for(File file: files) {
				System.out.println(file);
			}
			
			
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	
	public static ServerAcceptThread getServerAcceptThread() {
		return Session.sat;
	}
	
	public static void setServerAcceptThread(ServerAcceptThread s) {
		Session.sat = s;
	}
	
	public static ClientListenerThread getClientListenerThread() {
		return Session.clt;
	}
	
	public static void setClientListenerThread(ClientListenerThread s) {
		Session.clt = s;
	}
	
	public static ServerListenerThread getServerListenerThread() {
		return Session.slt;
	}
	
	public static void setServerListenerThread(ServerListenerThread s) {
		Session.slt = s;
	}
	
	public static MessageListenereThread getMessageListenereThread() {
		return Session.mlt;
	}
	
	public static void setMessageListenereThread(MessageListenereThread s) {
		Session.mlt = s;
	}

}
