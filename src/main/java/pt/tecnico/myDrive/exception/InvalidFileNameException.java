package pt.tecnico.myDrive.exception;

public class InvalidFileNameException extends MyDriveException {
    String name;

    public InvalidFileNameException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "The name '" + name + "' is invalid. A file name cannot contain '\\0' or '\\'.";
    }
}