package pt.tecnico.myDrive.exception;

public class InvalidPasswordException extends MyDriveException {

	private static final long serialVersionUID = 1L;

	private String pass;
	private String errorMessage;

	public InvalidPasswordException(String pass, String errorMessage) {
		this.errorMessage = errorMessage;
		this.pass = pass;
	}



	@Override
	public String getMessage() {
		return "Password length cannot be less than 8 characters:" + pass + errorMessage;
	}
}