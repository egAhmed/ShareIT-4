package network;

import java.util.*;
import java.io.*;

public class DirectoryPacket extends MessagePacket{
	
	private List<File> files;
	
	public DirectoryPacket(PacketType pt, String msg,List<File> files) {
		super(pt ,msg);
		this.files = files;
	}
	
	public List<File> getFiles(){
		return this.files;
	}

}
