package pt.tecnico.myDrive.exception;

public class MethodNotValidException extends MyDriveException {
	private String method;

	public MethodNotValidException(String method) {
		this.method = method;
	}

	@Override
	public String getMessage() {
		return "The method " + method + " is not valid.";
	}
}
