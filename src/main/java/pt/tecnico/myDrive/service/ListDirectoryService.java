package pt.tecnico.myDrive.service;

import java.util.ArrayList;
import java.util.List;

import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.exception.DirCanNotHaveContentException;
import pt.tecnico.myDrive.service.dto.FileDto;

public class ListDirectoryService extends MyDriveService {

	private long loginId;
	private List<FileDto> fileList;
	private Dir currentDir;

	public ListDirectoryService(long token) {
		this.loginId = token;

	}

	@Override
	protected void dispatch() {
		fileList = new ArrayList<FileDto>();

		Login login = getMyDrive().getLoginFromId(loginId);

		login.refreshToken();
		currentDir = login.getCurrentDir();

		for (File f : currentDir.getFileSet()) {
			if(f.getType().equals("Dir"))
				fileList.add((new FileDto(f.getId(), f.getSize(), f.getName(), f.getLastModification(), f.getPermissions(), f.getType(), f.getFileOwner().getUsername())));
			else {
				fileList.add((new FileDto(f.getId(), f.getSize(), f.getName(), f.getLastModification(), f.getPermissions(), f.getType(), ((PlainFile) f).getContent(), f.getFileOwner().getUsername())));
			}
		}

	}

	public List<FileDto> result() {
		return fileList;
	}
}