package pt.tecnico.myDrive.exception;

public class NoPermissionException extends MyDriveException {
	String method;

	public NoPermissionException(String method) {
		this.method = method;
	}

	@Override
	public String getMessage() {
		return "The method '" + method + "' can not be called.";
	}
}
