package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.User;

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
		if (md.isTokenValid(token)) {
			Login login = md.getLoginFromId(token);
			login.refreshToken();
			Dir currentDir = login.getCurrentDir();
			User user = login.getUser();
			currentDir.getFileByName(user, filename).remove();
		}
	}
}
