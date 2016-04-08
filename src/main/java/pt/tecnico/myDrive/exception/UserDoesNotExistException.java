package pt.tecnico.myDrive.exception;

public class UserDoesNotExistException extends MyDriveException {
	private String username;

	public UserDoesNotExistException(String username) {
		this.username = username;
	}

	@Override
	public String getMessage() {
		return "The username " + username + " was not found.";
	}
}