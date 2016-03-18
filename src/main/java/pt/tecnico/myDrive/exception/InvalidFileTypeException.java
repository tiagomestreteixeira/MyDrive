package pt.tecnico.myDrive.exception;

public class InvalidFileTypeException extends MyDriveException {
    String method;
    String file;

    public InvalidFileTypeException(String file,String method) {
        this.method = method;
        this.file = file;
    }

    @Override
    public String getMessage() {
        return "The method '" + method + "' can not be called on file '" + file + "'.";
    }
}
