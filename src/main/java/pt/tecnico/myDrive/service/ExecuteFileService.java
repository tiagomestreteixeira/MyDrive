package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.DirCanNotHaveContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ExecuteFileService extends MyDriveService {
	private long token;
	private String pathname;
	private String[] args;

	public ExecuteFileService(long token, String pathname, String[] args) {
		this.token = token;
		this.pathname = pathname;
		this.args = args;
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = MyDrive.getInstance();
		Login login = md.getLoginFromId(token);
		login.refreshToken();
		User user = login.getUser();
        Dir currentDir = login.getCurrentDir();
        if (!pathname.startsWith("/")) {
            if (currentDir.getPath().equals("/")) {
                pathname = currentDir.getPath() + pathname;
            } else {
                pathname = currentDir.getPath() + "/" + pathname;
            }
        }
		File file = user.lookup(pathname);
		file.execute(user,args);

	}
}

