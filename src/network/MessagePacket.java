package network;

public class MessagePacket extends Packet {
	
	private String message;
	
	public MessagePacket(PacketType pt) {
		this(pt, "");
	}
	
	public MessagePacket(PacketType packetType , String message) {
		super(packetType);
		this.setMessage(message);
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String msg) {
		this.message = msg;
	}
	

}
