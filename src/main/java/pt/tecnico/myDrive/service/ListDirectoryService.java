package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.ArrayList;
import java.util.List;

public class ListDirectoryService extends MyDriveService {

	private long loginId;
	private List<FileDto> fileList;
	private Dir currentDir;
	private String pathname;

	public ListDirectoryService(long token, String pathname) {
		this.loginId = token;
		this.pathname = pathname;
	}

	@Override
	protected void dispatch() {
		fileList = new ArrayList<FileDto>();

		Login login = getMyDrive().getLoginFromId(loginId);
		User user = login.getUser();

		login.refreshToken();
		currentDir = login.getCurrentDir();

		if (!pathname.startsWith("/")) {
			if (currentDir.getPath().equals("/")) {
				pathname = currentDir.getPath() + pathname;

			} else {
				pathname = currentDir.getPath() + "/" + pathname;
			}
		}

		Dir d = (Dir) user.lookup(pathname);

		for (File f : d.getFileSet(user)) {
			if (f instanceof Dir) {
				fileList.add((new FileDto(f.getId(),
						f.getSize(),
						f.getName(),
						f.getLastModification(),
						f.getPermissions(),
						"Dir",
						f.getFileOwner().getUsername())));
			}
			else{
				fileList.add((new FileDto(f.getId(),
						f.getSize(),
						f.getName(),
						f.getLastModification(),
						f.getPermissions(),
						f.getClass().getSimpleName(),
						((PlainFile) f).getContent(),
						f.getFileOwner().getUsername())));
			}
		}

	}

	public List<FileDto> result() {
		return fileList;
	}
}