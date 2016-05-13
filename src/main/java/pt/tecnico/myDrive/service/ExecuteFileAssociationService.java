package pt.tecnico.myDrive.service;

public class ExecuteFileAssociationService extends MyDriveService {

    private Long token;
    private String filename;
    private String executedApp;

    public ExecuteFileAssociationService(Long token,String filename) {
        this.token = token;
        this.filename = filename;
        this.executedApp = "";
    }

    public final void dispatch() {
        // TODO: mockup example
    }
}