package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileService extends MyDriveService {

	public CreateFileService(long token, String filename, String fileType, String content) {
	}

	public CreateFileService(long token, String filename, String fileType) {

	}

	public String getFileType(){
		return null;
	}

	public String result() {
		return null;
	}

	@Override
	protected void dispatch() throws MyDriveException {

	}
}
