package pt.tecnico.myDrive.exception;


public class InvalidFileTypeCreateFileServiceException extends MyDriveException{

    private static final String DEFAULT_INVALID_FiLE_TYPE_MSG = "Only Dir,Plain,App and Link file Types are accepted";

    public InvalidFileTypeCreateFileServiceException() {
        super(DEFAULT_INVALID_FiLE_TYPE_MSG);
    }

    public InvalidFileTypeCreateFileServiceException(String typeGiven) {
        super(DEFAULT_INVALID_FiLE_TYPE_MSG + ". File Type given :" + typeGiven);
    }

}
