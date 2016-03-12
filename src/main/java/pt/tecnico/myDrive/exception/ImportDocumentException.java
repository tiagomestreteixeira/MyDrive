package pt.tecnico.myDrive.exception;


public class ImportDocumentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public ImportDocumentException() {
        super("Error importing from XML");
    }
    public ImportDocumentException(String msg, String domainElement) {
        super("Error in importing " + domainElement + " from XML : " + msg);
    }
}