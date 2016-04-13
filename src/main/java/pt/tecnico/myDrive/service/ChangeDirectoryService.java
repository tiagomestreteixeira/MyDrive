package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ChangeDirectoryService extends MyDriveService {
	private Login login;
	private User user;
	private Dir currentDir;
	private String pathname;

	private String result;

	public ChangeDirectoryService(long token, String pathname) {
		login = getMyDrive().getLoginFromId(token);
		user = login.getUser();
		currentDir = login.getCurrentDir();
		if (pathname.startsWith("/"))
			this.pathname = pathname;
		else
			this.pathname = currentDir.getPath() + "/" + pathname;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		Dir d = (Dir) user.lookup(pathname);
		login.setCurrentDir(d);
		result = d.getPath();
	}

	public String result() {
		return result;
	}
}
