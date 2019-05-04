package GUIImplementation;

import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;

import main.History;
import main.Session;
import java.util.*;
import java.awt.*;
import java.io.File;
import java.net.InetAddress;

public class GUILogic extends JFrame{
	public JMenuBar menubar;
	public JMenu optionsMenu;
	public JMenu filePath;
	public JMenuItem connectItem, createItem,filePathItem, disconnectItem , disconnectMessageItem, sentSuccessfulItem;
	public JPanel centerPanel = new JPanel(new GridLayout(0, 2));
	public JPanel leftPanel = new JPanel(new BorderLayout(0,10));
	public JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	
	public JPanel leftCenterPanel = new JPanel(new BorderLayout(0,10));
	
	public JPanel leftCenterNorthPanel = new JPanel(new BorderLayout(0,10));
	public JPanel leftCenterSouthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightPanel = new JPanel(new BorderLayout(0,10));
	public JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightCenterPanel = new JPanel(new BorderLayout(0,10));
	public JPanel rightBottomPanel = new JPanel(new BorderLayout(0,10));
	public JPanel rightBottomNorthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightBottomCenterPanel = new JPanel(new BorderLayout(0,10));
	public JPanel bottomPanel = new JPanel(new GridLayout(0,2));
	public JTextArea connectionDetailsTextArea , errorTextArea;
	public JButton selectFile,sendFile, backButton,sendMessageButton;
	public JButton getDirectory;
	public DefaultListModel connectedClientListModel = new DefaultListModel();
	public JList connectedClients = new JList(connectedClientListModel);
	public DefaultListModel messagesListModel = new DefaultListModel();
	public JList messages = new JList(messagesListModel);
	public DefaultListModel fileListModel = new DefaultListModel();
	public JList fileList = new JList(fileListModel);
	public JTextField fileName,sendMessageTextField;
	public JLabel historyLabel,sendMessageLabel;
	
	public String[] columnNames = {"File" , "Sender" , "Reciever","Size" };
	public Object[][] data = new Object[0][4];
	public DefaultTableModel tableModel = new DefaultTableModel(data , columnNames);
	public JTable historyTable;
	
	public GUILogic() {
		super("ShareIT");
		this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		setLayout(new BorderLayout());
		
		
		menubar = new JMenuBar();
		
		optionsMenu = new JMenu("Options");
		filePath = new JMenu("Settings");
		this.createItem = new JMenuItem("Create");
		this.connectItem = new JMenuItem("Connect");
		this.disconnectItem = new JMenuItem("Disconnect");
		this.filePathItem = new JMenuItem("Set File Path");
		//this.disconnectMessageItem = new JMenuItem("Set Disconnection message");
		//this.sentSuccessfulItem = new JMenuItem("Set successful message");
		
		optionsMenu.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		filePath.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		
		createItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		connectItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		filePathItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		disconnectItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		//sentSuccessfulItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		//disconnectMessageItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		
		
		
		optionsMenu.add(createItem);
		optionsMenu.add(connectItem);
		optionsMenu.add(disconnectItem);
		//filePath.add(disconnectMessageItem);
		filePath.add(filePathItem);
		
		menubar.add(optionsMenu);
		menubar.add(filePath);
		this.setJMenuBar(menubar);
		centerPanel.add(leftPanel);
		centerPanel.add(rightPanel);
		rightPanel.add(rightTopPanel,BorderLayout.NORTH);
		rightPanel.add(rightCenterPanel,BorderLayout.CENTER);
		rightPanel.add(rightBottomPanel,BorderLayout.SOUTH);
		rightBottomPanel.add(rightBottomNorthPanel,BorderLayout.NORTH);
		rightBottomPanel.add(rightBottomCenterPanel,BorderLayout.CENTER);
		
		leftCenterPanel.add(leftCenterNorthPanel, BorderLayout.CENTER);
		leftCenterPanel.add(leftCenterSouthPanel , BorderLayout.SOUTH);
		
		rightPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		rightTopPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		rightCenterPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		rightBottomPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		
		
		connectionDetailsTextArea = new JTextArea(2, 15);
		bottomPanel.add(connectionDetailsTextArea, BorderLayout.CENTER);
		connectionDetailsTextArea
		.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		connectionDetailsTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		connectionDetailsTextArea.setEditable(false);
		
		errorTextArea = new JTextArea(2, 15);
		bottomPanel.add(errorTextArea, BorderLayout.CENTER);
		errorTextArea
		.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		errorTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		errorTextArea.setEditable(false);
		
		
		selectFile = new JButton("Select");
		sendFile = new JButton("Send");
		fileName = new JTextField(15);
		fileName.setEditable(false);
		rightTopPanel.add(selectFile);
		rightTopPanel.add(fileName);
		rightTopPanel.add(sendFile);
		selectFile.setPreferredSize(new Dimension(100, 50));
		sendFile.setPreferredSize(new Dimension(100, 50));
		fileName.setPreferredSize(new Dimension(100, 50));
		
		
		
		getDirectory = new JButton("Get Directory");
		backButton = new JButton("Previous Directory");
		getDirectory.setPreferredSize(new Dimension(150, 50));
		leftPanel.add(leftTopPanel,BorderLayout.NORTH);
		leftPanel.add(leftCenterPanel,BorderLayout.CENTER);
		leftPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		leftTopPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		leftCenterPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		leftTopPanel.add(connectedClients);
		
		//fileList.setPreferredSize(new Dimension(300 , 300));
		
		
		
		leftCenterNorthPanel.add(new JScrollPane(fileList) , BorderLayout.CENTER);
		leftCenterSouthPanel.add(backButton );
		leftTopPanel.add(getDirectory);
		
		historyLabel = new JLabel("History:");
		sendMessageLabel = new JLabel("Send Message: ");
		sendMessageTextField = new JTextField(15);
		sendMessageButton = new JButton("Send");
		//sendMessageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN , 30));
		historyLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN , 30));
		rightCenterPanel.add(historyLabel, BorderLayout.NORTH);
		rightBottomNorthPanel.add(sendMessageLabel);
		rightBottomNorthPanel.add(sendMessageTextField);
		rightBottomNorthPanel.add(sendMessageButton);
		rightBottomCenterPanel.add(new JScrollPane(messages),BorderLayout.CENTER);
		historyTable = new JTable(data, columnNames);
		rightCenterPanel.add(new JScrollPane(historyTable) , BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		add(centerPanel , BorderLayout.CENTER);
		
		
		Session.setGUI(this);
		Session.loadHistory();
		this.setVisibility();
		this.setupListeneres();
	}
	
	public void addClient() {
		connectedClientListModel.addElement(Session.getClient());
	}
	
	public void setFiles() {
		fileListModel.removeAllElements();
		for(File file : Session.getFiles()) {
			fileListModel.addElement(file.getAbsolutePath());
		}
	}
	
	
	
	public void setVisibility() {
		leftPanel.setVisible(true);
		rightPanel.setVisible(true);
		centerPanel.setVisible(true);
		
		if(Session.getDeviceType() == 0) {
			connectedClients.setVisible(true);
			getDirectory.setVisible(false);
			leftCenterPanel.setVisible(false);
			disconnectItem.setEnabled(true);
			connectItem.setEnabled(false);
			createItem.setEnabled(false);
		}
		else if(Session.getDeviceType() == 1){
			connectedClients.setVisible(false);
			getDirectory.setVisible(true);
			leftCenterPanel.setVisible(true);
			disconnectItem.setEnabled(true);
			connectItem.setEnabled(false);
			createItem.setEnabled(false);
		}
		else {
			connectedClients.setVisible(false);
			getDirectory.setVisible(false);
			leftCenterPanel.setVisible(false);
			disconnectItem.setEnabled(false);
			connectItem.setEnabled(true);
			createItem.setEnabled(true);
		}
	}
	
	public void setupListeneres() {
		System.out.println("ss: setup listener" );
		getDirectory.addActionListener(new ActionEventHandler(GUILogic.this));
		connectItem.addActionListener(new ActionEventHandler(GUILogic.this));
		createItem.addActionListener(new ActionEventHandler(GUILogic.this));
		filePathItem.addActionListener(new ActionEventHandler(GUILogic.this));
		disconnectItem.addActionListener(new ActionEventHandler(GUILogic.this));
		selectFile.addActionListener(new ActionEventHandler(GUILogic.this));
		sendFile.addActionListener(new ActionEventHandler(GUILogic.this));
		backButton.addActionListener((new ActionEventHandler(GUILogic.this)));
		sendMessageButton.addActionListener((new ActionEventHandler(GUILogic.this)));
		//disconnectMessageItem.addActionListener(new ActionEventHandler(GUILogic.this));
		//sentSuccessfulItem.addActionListener(new ActionEventHandler(GUILogic.this));
		this.addWindowListener(new WindowEventHandler(this));
	}

	public boolean askForPermissoin(File file, InetAddress inetAddress, int port) {
		// TODO Show a dialog asking for permission
		System.out.println("ShareIt : Server In asForPermission GUI " + file.getName());
		int permit = JOptionPane.showConfirmDialog(this, "Accept File " + file.getName());
		
		return (permit == JOptionPane.OK_OPTION) ? true : false;
	}
	
	public void refreshTable() {
		while(this.tableModel.getRowCount() > 0) {
			tableModel.removeRow(0);
		}
		fetchData();
		for(int i = 0 ; i < data.length; i++) {
			tableModel.addRow(data[i]);
		}
		historyTable.setModel(tableModel);
	}
	
	public void fetchData() {
		int i = 0;
		List<History> history = Session.getHistory();
		System.out.println("in fetch data");
		data = new Object[history.size()][4];
		for(History h: history) {
			if(h != null) {
				data[i][0] = h.getFile().getName();
				data[i][1] = h.getSenderIP().getHostName();
				data[i][2] = h.getRecieverIP().getHostName();
				data[i][3] = h.getFile().length();
			}
			i++;
		}
		
	}
	
	public void showConnectionDetails(String details) {
		this.connectionDetailsTextArea.setText(details);
	}
	
	public void showErrorMessage(String details , String color) {
		
		if(color.equals("red")) {
			this.errorTextArea.setForeground(Color.RED);
			this.errorTextArea.setText(details);
		}
		else if(color.equals("green")) {
			this.errorTextArea.setForeground(Color.GREEN);
			this.errorTextArea.setText(details);
		}
	}
	
	
	public void addMessage(String msg) {
		messagesListModel.addElement(msg);
	}

}
