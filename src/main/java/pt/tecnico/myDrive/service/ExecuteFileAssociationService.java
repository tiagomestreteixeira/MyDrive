package pt.tecnico.myDrive.service;

public class ExecuteFileAssociationService extends MyDriveService {

    private String filename;
    private String executedApp;

    public ExecuteFileAssociationService(String filename) {
        this.filename = filename;
        this.executedApp = "";
    }

    public final void dispatch() {
        // TODO: mockup example
    }

    public final String result() {
        return executedApp;
    }
}