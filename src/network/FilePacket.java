package network;

import java.io.*;

public class FilePacket extends Packet {
	
	private File file;
	private byte[] fileArr;
	
	public FilePacket(PacketType p , File f) {
		this(p , f, null);
	}
	
	public FilePacket(PacketType p , File f , byte[] buff) {
		super(p);
		this.setFile(f);
		this.setFileArr(buff);
	}
	
	public FilePacket(PacketType p ) {
		this(p , null);
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void setFile(File f) {
		this.file = f;
	}
	
	public byte[] getFileArr() {
		return this.fileArr;
	}
	
	public void setFileArr(byte[] buff) {
		this.fileArr = buff;
	}

}
