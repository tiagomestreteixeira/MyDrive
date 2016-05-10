package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ExecuteFileService extends MyDriveService {
	private long token;
	private String filename;
	private String fileType;
	private String content;

	public ExecuteFileService(long token, String path, String[] args) {
		this.token = token;
		this.filename = filename;
		this.fileType = fileType;
		this.content = content;
	}


	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		Login login = md.getLoginFromId(token);
		login.refreshToken();
		User user = login.getUser();
		Dir currentDir = login.getCurrentDir();
	}
}
