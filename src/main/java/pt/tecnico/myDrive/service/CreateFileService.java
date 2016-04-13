package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileService extends MyDriveService {
	private long token;
	private String filename;
	private String fileType;
	private String content;

	public CreateFileService(long token, String filename, String fileType, String content) {
		this.token = token;
		this.filename = filename;
		this.fileType = fileType;
		this.content = content;
	}

	public CreateFileService(long token, String filename, String fileType) {
		this.token = token;
		this.filename = filename;
		this.fileType = fileType;
		this.content = "";
	}

	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		Login login = md.getLoginFromId(token);
		User user = login.getUser();
		Dir currentDir = login.getCurrentDir();

		if (md.isTokenValid(token)) {
			switch (fileType) {
				case "Dir":
					new Dir(filename, user, currentDir, user.getUmask());
					break;
				case "Plain":
					new PlainFile(filename, user, currentDir, user.getUmask(), content);
					break;
				case "Link":
					new Link(filename, user, currentDir, user.getUmask(), content);
					break;
				case "App":
					new App(filename, user, currentDir, user.getUmask(), content);
					break;
				default:
					throw new InvalidFileTypeException(fileType, "CreateFileService");
			}
		}
	}
}
