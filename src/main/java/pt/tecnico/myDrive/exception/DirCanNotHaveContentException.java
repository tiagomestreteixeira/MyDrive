package pt.tecnico.myDrive.exception;


public class DirCanNotHaveContentException extends MyDriveException{

    private static final String DEFAULT_PATH_LONGER_MSG = "Dirs haven't content like Plain Files";

    public DirCanNotHaveContentException() {
        super(DEFAULT_PATH_LONGER_MSG);
    }

    public DirCanNotHaveContentException(String path) {
        super(DEFAULT_PATH_LONGER_MSG + ". Path :" + path);
    }

    public DirCanNotHaveContentException(String path, String fileName) {
        super(DEFAULT_PATH_LONGER_MSG + ". Path :" + path + "\nDirname : " + fileName);
    }
}
