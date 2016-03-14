package pt.tecnico.myDrive.exception;

public class FileAlreadyExistsException extends MyDriveException {

    private String conflictingName;

    public FileAlreadyExistsException(String conflictingName) {
        this.conflictingName = conflictingName;
    }

    public String getMessage() {
        return "The file: '" + this.conflictingName + "' already exists in this directory.";
    }
}
	