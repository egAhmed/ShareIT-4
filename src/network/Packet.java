package network;

import java.io.*;

public abstract class Packet implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private PacketType packetType;
	
	
	public Packet(PacketType packetType) {
		this.setPacketType(packetType);	
	}
	
	
	
	public PacketType getPacketType() {
		return this.packetType;
	}
	
	public void setPacketType(PacketType p) {
		this.packetType = p;
	}
	
	
//	@Override
//	public boolean equals(Object o) {
//		if(o instanceof Packet) {
//			Packet p = (Packet) o;
//			return (p.getPacketType() == this.packetType ) ? true: false;
//		}
//		return false;
//	}

}
