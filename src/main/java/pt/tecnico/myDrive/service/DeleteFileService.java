package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;

import pt.tecnico.myDrive.exception.MyDriveException;

public class DeleteFileService extends MyDriveService {

	private String filename;
	private long token;

	public DeleteFileService(long token, String filename) {
		this.filename = filename;
		this.token = token;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = MyDrive.getInstance();
		Login login = md.getLoginFromId(token);
		login.refreshToken();
		File currentDir = (File) login.getCurrentDir();
		User user = login.getUser();
		currentDir.getFileByName(user, filename).delete(user);
	}
}
