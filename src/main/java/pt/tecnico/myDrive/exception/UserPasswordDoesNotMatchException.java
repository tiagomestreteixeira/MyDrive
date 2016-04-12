package pt.tecnico.myDrive.exception;

public class UserPasswordDoesNotMatchException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	
	private String username;
	
	public UserPasswordDoesNotMatchException(String username){
		this.username = username;
	}
	
	@Override
	public String getMessage(){
		return "The username " + username + "and password do not match.";
	}
}
