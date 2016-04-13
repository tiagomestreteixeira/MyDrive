package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ChangeDirectoryService extends MyDriveService {
	private long token;
	private String pathname;

	private String result;

	public ChangeDirectoryService(long token, String pathname) {
		this.token = token;
		this.pathname = pathname;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		Login login = getMyDrive().getLoginFromId(token);
		if (getMyDrive().isTokenValid(token)) {
			login.refreshToken();
			User user = login.getUser();
			Dir currentDir = login.getCurrentDir();

			if (!pathname.startsWith("/"))
				pathname = currentDir.getPath() + "/" + pathname;

			Dir d = (Dir) user.lookup(pathname);
			login.setCurrentDir(d);
			result = d.getPath();
		}
	}

	public String result() {
		return result;
	}
}
