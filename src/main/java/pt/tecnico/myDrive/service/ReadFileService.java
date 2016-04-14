package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ReadFileService extends MyDriveService {
	private long token;
	private String filename;

	private String result;

	public ReadFileService(long token, String filename) {
		this.token = token;
		this.filename = filename;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		if (getMyDrive().isTokenValid(token)) {
			Login login = getMyDrive().getLoginFromId(token);
			User user = login.getUser();
			Dir currentDir = login.getCurrentDir();

			result = currentDir.getFileByName(user, filename).read(user);
		}
	}

	public String result() {
		return result;
	}
}
