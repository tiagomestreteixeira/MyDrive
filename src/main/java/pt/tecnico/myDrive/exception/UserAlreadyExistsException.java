package pt.tecnico.myDrive.exception;

public class UserAlreadyExistsException extends MyDriveException {

	private static final long serialVersionUID = 1L;

    private String conflictingName;

    public UserAlreadyExistsException(String conflictingName) {
        this.conflictingName = conflictingName;
    }

    public String getConflictingName() {
        return conflictingName;

    }

    @Override
    public String getMessage() {
        return "This name " + conflictingName + " is already being used";
    }
}