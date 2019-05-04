package exceptions;

public class ShareItException extends Exception{
	
	String msg;
	
	public ShareItException(String msg) {
		super(msg);
	}
	
	public String getMessage() {
		return msg;
	}

}
