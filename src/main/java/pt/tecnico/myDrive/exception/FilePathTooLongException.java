package pt.tecnico.myDrive.exception;

public class FilePathTooLongException extends MyDriveException {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_PATH_LONGER_MSG = "Path Longer than 1024 characters";

    public FilePathTooLongException(String path) {
        super(DEFAULT_PATH_LONGER_MSG + ". Path :" + path);
    }

    public FilePathTooLongException(String path, String fileName) {
        super(DEFAULT_PATH_LONGER_MSG + ". Path :" + path + "\nFilename : " + fileName);
    }

    public FilePathTooLongException(String fileElement, String fileName, String path) {
        super(DEFAULT_PATH_LONGER_MSG + " in a :\n" + fileElement + "\nWith the filename "
                + fileName + ".\n" + "Path provided: " + path);
    }
}
