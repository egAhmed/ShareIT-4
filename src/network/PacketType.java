package network;

public enum PacketType {
	FileSendPermitPacket,
	FileSendAcceptPacket,
	FileSendRejectPacket,
	FilePacket,
	GetFilePacket,
	RequestDirectoryPacket,
	RecievedDirectoryPacket,
	DisconnectPacket,
	DirectTransferDeniedPacket,
	MessagePacket,
	ClientAlreadyConnectedPacket,
	ConnectedPacket,
	;
}
