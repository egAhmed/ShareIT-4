package GUIImplementation;
import java.awt.*;
import javax.swing.*;
public class RunGUI {
	public static void main(String args[])
	{
		GUILogic  frame = null;
		try {
		frame = new GUILogic();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(new Dimension(1000,1000));
		System.out.println("ss: frame visible" );
		frame.setVisible(true);
		} catch(Exception e) {
			if(frame != null) {
				JOptionPane.showMessageDialog(frame, "Sorry, some unexpected error occured");
				frame.dispose();
			}
		}
	}
}
