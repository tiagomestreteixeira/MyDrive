package pt.tecnico.myDrive.exception;

public class NotValidEnvVariableException extends MyDriveException{
	
    private static final String NOT_VALID_VARIABLE = "Environment variable has invalid type name or value";

    public NotValidEnvVariableException() {
        super(NOT_VALID_VARIABLE);
    }
}
