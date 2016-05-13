package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class CleanupService extends MyDriveService{


	public CleanupService() {

	}

	@Override
	protected void dispatch() throws MyDriveException {
			getMyDrive().cleanup();
	}
}