package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class WriteFileService extends MyDriveService {

	private User user;
	private Dir currentDir;
	private String filename;
	private String content;
	private String result;

	public WriteFileService(long token, String filename, String content) {
		Login login = getMyDrive().getLoginFromId(token);
		user = login.getUser();
		currentDir = login.getCurrentDir();
		this.filename = filename;
		this.content = content;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		//currentDir.getFileByName(user, filename).write(user, content);
	}
}
