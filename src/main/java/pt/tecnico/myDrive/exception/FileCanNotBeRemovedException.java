package pt.tecnico.myDrive.exception;


public class FileCanNotBeRemovedException extends MyDriveException {
	String filename;

	public FileCanNotBeRemovedException(String filename) {
		this.filename = filename;
	}

	@Override
	public String getMessage() {
		return "The file '" + filename + "' can't be removed.";
	}
}