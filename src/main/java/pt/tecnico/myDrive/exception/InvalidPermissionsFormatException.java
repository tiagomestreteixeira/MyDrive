package pt.tecnico.myDrive.exception;

public class InvalidPermissionsFormatException extends MyDriveException {
    String name;

    public InvalidPermissionsFormatException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "The permission '" + name + "' is invalid. Please use a valid permission mask. Ranges are 'rwxdrwxd/--------'.";
    }
}