package pt.tecnico.myDrive.exception;

public class VariableNotFoundException extends MyDriveException {
	public VariableNotFoundException(String variableName) {
		super("The variable " + variableName + " was not found.");
	}
}
