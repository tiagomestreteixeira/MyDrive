package pt.tecnico.myDrive.exception;


public class DirHaveNoContentException extends MyDriveException{

    private static final String DEFAULT_PATH_LONGER_MSG = "Dirs haven't content like Plain Files";

    public DirHaveNoContentException() {
        super(DEFAULT_PATH_LONGER_MSG);
    }

    public DirHaveNoContentException(String path) {
        super(DEFAULT_PATH_LONGER_MSG + ". Path :" + path);
    }

    public DirHaveNoContentException(String path, String fileName) {
        super(DEFAULT_PATH_LONGER_MSG + ". Path :" + path + "\nDirname : " + fileName);
    }
}
