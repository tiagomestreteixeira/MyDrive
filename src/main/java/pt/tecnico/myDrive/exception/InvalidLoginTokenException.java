package pt.tecnico.myDrive.exception;

public class InvalidLoginTokenException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	
	private long identifier;
	
	public InvalidLoginTokenException(long identifier){
		this.identifier = identifier;
	}
	
	
	@Override
	public String getMessage(){
		return "The login token " + " is invalid.";
	}
	
}