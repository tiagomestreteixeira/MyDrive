package pt.tecnico.myDrive.exception;

public class FileDoesNotExistException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	
	private String missingName;
	
	public FileDoesNotExistException(String missingName){
		this.missingName = missingName;
	}
	
	public String getMessage(){
		return "The file: '" + this.missingName + "' does not exist.";
	}
	
}