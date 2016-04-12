package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ReadFileService extends MyDriveService {
	private User user;
	private Dir currentDir;
	private String filename;

	private String result;

	public ReadFileService(long token, String filename) {
		Login login = getMyDrive().getLoginFromId(token);
		user = login.getUser();
		currentDir = login.getCurrentDir();
		this.filename = filename;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		result = currentDir.getFileByName(user, filename).read(user);
	}

	public String result() {
		return result;
	}
}
