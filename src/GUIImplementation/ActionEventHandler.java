package GUIImplementation;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import main.Session;
import network.Communication;

public class ActionEventHandler implements ActionListener {
	private GUILogic gui;
	
	public ActionEventHandler(GUILogic g) {
		gui = g;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("ss: In action performed" );
		if(e.getSource() == gui.connectItem) {
			JLabel IPAddressLabel,portNoLabel;
			JDialog connectDialog = new JDialog(gui, "Connect", true);

			connectDialog.setSize(400, 300);
			connectDialog.setLocationRelativeTo(null);
			connectDialog.setLayout(new FlowLayout(FlowLayout.CENTER));

			IPAddressLabel = new JLabel("IPAddress");
			JTextField IPAddressTextField = new JTextField(15);

			portNoLabel = new JLabel("Port No.:");
			JTextField portNoTextField = new JTextField(15);


			JButton submit = new JButton("Submit");	
			submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String IPAddress = new String(IPAddressTextField.getText());
					String portNo = new String(portNoTextField.getText());
					try {
						Session.connectToServer(InetAddress.getByName(IPAddress), Integer.parseInt(portNo));
						connectDialog.dispose();
						gui.setVisibility();
					}
					catch(Exception ex) {
						connectDialog.dispose();
						JOptionPane.showMessageDialog(gui, "Error connecting to server" ,"Error" , JOptionPane.ERROR_MESSAGE );
						ex.printStackTrace();
					}
				}
					

			});

			JPanel createPanel = new JPanel(new GridBagLayout());
			connectDialog.getContentPane().add(createPanel);
			addComp(createPanel, IPAddressLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
			addComp(createPanel, IPAddressTextField, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
			addComp(createPanel, portNoLabel, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
			addComp(createPanel, portNoTextField, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
			addComp(createPanel, submit, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
			connectDialog.setVisible(true);
		}
		else if(e.getSource() == gui.createItem) {
			//System.out.println("create Item" );
			try {
				System.out.println("ss: create action" );
				int rvalue = JOptionPane.showConfirmDialog(gui, "Do you want to allow Direct File Transfer. Allowing this will let client get any file on your device.");
				if(rvalue == JOptionPane.OK_OPTION) {
					Session.setAllowDirectFileTransfer(true);
				}
				else {
					Session.setAllowDirectFileTransfer(false);
				}
				Session.createServer();
				gui.connectionDetailsTextArea.setText("IP of server: " +Session.getServer().getIP().getHostAddress() + "\nPort " + Session.getPort());
				gui.setVisibility();
				
				//JOptionPane.showMessageDialog(gui, "Server: "+Session.getServer() ,"Error" , JOptionPane.ERROR_MESSAGE );
				//JOptionPane.showMessageDialog(gui, "here: ","Error" , JOptionPane.ERROR_MESSAGE );
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(gui, "Unknown Host" ,"Error" , JOptionPane.ERROR_MESSAGE );
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(gui, "Error: " + e1.getMessage() ,"Error" , JOptionPane.ERROR_MESSAGE );
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == gui.filePathItem) {
			String tempFilePath;
			System.out.println("in handler");
			JFileChooser chooser = new JFileChooser();
		    chooser.setCurrentDirectory(new java.io.File("."));
		    chooser.setDialogTitle("Select Folder");
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
		    
		    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		      System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		      tempFilePath=chooser.getSelectedFile().toString();
		      tempFilePath = tempFilePath + "\\ShareItTransferedFiles";
		      new File(tempFilePath).mkdirs();
		      Session.setPath(tempFilePath);
		      
		    } else {
		      System.out.println("No Selection ");
		    }
		  
		}
		else if(e.getSource() == gui.disconnectMessageItem) {
			final JDialog disconnectDialog = new JDialog(gui, "Disconnect", true);
			JLabel disconnectMessageLabel =new JLabel("Enter Disconnect Message:");
			final JTextField disconnectMessageTextField = new JTextField(15);
			
			disconnectDialog.setSize(400, 300);
			disconnectDialog.setLocationRelativeTo(null);
			disconnectDialog.setLayout(new FlowLayout(FlowLayout.CENTER));
			JButton submit = new JButton("Submit");	
			submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String disconnectMessage = new String(disconnectMessageTextField.getText());
					
					try {
						Session.setDisconnectMessage(disconnectMessage);
						disconnectDialog.dispose();
					}
					catch(Exception ex) {
						disconnectDialog.dispose();
						JOptionPane.showMessageDialog(gui, "Error setting disconnect message" ,"Error" , JOptionPane.ERROR_MESSAGE );
						ex.printStackTrace();
					}
				}
					

			});
			
			
			
			
			JPanel createPanel = new JPanel(new GridBagLayout());
			disconnectDialog.getContentPane().add(createPanel);
			addComp(createPanel, disconnectMessageLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
			addComp(createPanel, disconnectMessageTextField, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
			
			addComp(createPanel, submit, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
			disconnectDialog.setVisible(true);
			
		}
		else if(e.getSource() == gui.sentSuccessfulItem) {
			final JDialog sentSuccessfulDialog = new JDialog(gui, "Sent Message", true);
			JLabel sentSuccessfulMessageLabel =new JLabel("Enter Sent Message:");
			final JTextField sentSuccessfulMessageTextField = new JTextField(15);
			
			sentSuccessfulDialog.setSize(400, 300);
			sentSuccessfulDialog.setLocationRelativeTo(null);
			sentSuccessfulDialog.setLayout(new FlowLayout(FlowLayout.CENTER));
			JButton submit = new JButton("Submit");	
			submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String sentSuccessfulMessage = new String(sentSuccessfulMessageTextField.getText());
					
					try {
						Session.setDisconnectMessage(sentSuccessfulMessage);
						sentSuccessfulDialog.dispose();
					}
					catch(Exception ex) {
						sentSuccessfulDialog.dispose();
						JOptionPane.showMessageDialog(gui, "Error setting disconnect message" ,"Error" , JOptionPane.ERROR_MESSAGE );
						ex.printStackTrace();
					}
				}
					

			});
			
			
			
			
			JPanel createPanel = new JPanel(new GridBagLayout());
			sentSuccessfulDialog.getContentPane().add(createPanel);
			addComp(createPanel, sentSuccessfulMessageLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
			addComp(createPanel, sentSuccessfulMessageTextField, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
			
			addComp(createPanel, submit, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
			sentSuccessfulDialog.setVisible(true);
			
		}
		else if(e.getSource() == gui.selectFile) {
			JFileChooser fileOpen = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int r = fileOpen.showOpenDialog(null);
			if (r == JFileChooser.APPROVE_OPTION) {
				System.out.println("ShareIt : File selected" + fileOpen.getSelectedFile().getAbsolutePath());
				 gui.fileName.setText(fileOpen.getSelectedFile().getName());
				 Session.setSendFilePath(fileOpen.getSelectedFile().getAbsolutePath());
			}
//				gui.fileName.setText(fileOpen.getSelectedFile().getName());
	        else {
	        	JOptionPane.showMessageDialog(this.gui, "User didn't select any file");
	        	gui.fileName.setText("");
	        }
		}
		else if(e.getSource() == gui.sendFile) {
			try {
				System.out.println("ShareIt : client sending in actino Event handler" );
				Session.sendFile(Session.getSendFilePath(), false);
			} catch (ClassNotFoundException | IOException e1) {
				
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == gui.getDirectory) {
			try {
				Communication com = new Communication();
				if(gui.fileListModel.getSize() == 0) {
					System.out.println("ShareIt: in getDirectory = 0");
					com.getFiles("C:\\");
					
				}
				else {
					System.out.println("ShareIt: in getDirectory != 0");
					int index = gui.fileList.getSelectedIndex();
					File file = Session.getFiles().get(index);
					com.getFiles(file.getAbsolutePath());
				}
				
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == gui.backButton) {
			try {
				if(!(Session.getCurrentDirectory().equals("C:\\"))) {
					Communication com = new Communication();
					String str = Session.getCurrentDirectory();
				    int index=str.lastIndexOf("\\");
				    System.out.println("ShareIt : Action handler backButton " + str.substring(0,index));
				    
				    str = str.substring(0,index);
				    if(str.equals("C:")) {
				    	str += "\\";
				    }
					com.getFiles(str);
					
				}
				else {
					JOptionPane.showMessageDialog(gui, "You are already on root directory");
				}
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == gui.disconnectItem) {
			try {
				if(Session.getDeviceType() != -1) {
					
					Communication  com = new Communication();
				
					com.sendDisconnectPacket();
					
					
					Session.closeConnection();
					
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == gui.sendMessageButton) {
			try {
				String message=gui.sendMessageTextField.getText();
				Session.addMessage("You: " + message);
				
				Communication com = new Communication();
				if(Session.getDeviceType() == 0) { //server
					com.sendMessageToClient(message);
				}
				else {
					com.sendMessageToServer(message);
				}
				
				gui.sendMessageTextField.setText("");
			}
			catch(IOException io) {
				io.printStackTrace();
			}
		}
		
	}
	
	
	
	public static void addComp(JPanel thePanel, Component comp, int xPos, int yPos, int compWidth, int compHeight, int place,
			int stretch) {

		GridBagConstraints gridConstraints = new GridBagConstraints();

		gridConstraints.gridx = xPos;

		gridConstraints.gridy = yPos;

		gridConstraints.gridwidth = compWidth;

		gridConstraints.gridheight = compHeight;

		gridConstraints.weightx = 100;

		gridConstraints.weighty = 100;

		gridConstraints.insets = new Insets(5, 5, 5, 5);

		gridConstraints.anchor = place;

		gridConstraints.fill = stretch;

		thePanel.add(comp, gridConstraints);

	}
}
