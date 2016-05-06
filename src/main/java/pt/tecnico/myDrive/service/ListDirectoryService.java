package pt.tecnico.myDrive.service;

import java.util.ArrayList;
import java.util.List;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.service.dto.FileDto;

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

		if (!pathname.startsWith("/"))
			pathname = currentDir.getPath() + "/" + pathname;

		Dir d = (Dir) user.lookup(pathname);

		for (File f : d.getFileSet()) {
			if (f instanceof Dir) {
				fileList.add((new FileDto(f.getId(), f.getSize(), f.getName(), f.getLastModification(), f.getPermissions(), "Dir", f.getFileOwner().getUsername())));
				continue;
			}
			if (f instanceof Link) {
				fileList.add((new FileDto(f.getId(), f.getSize(), f.getName(), f.getLastModification(), f.getPermissions(), "Link", ((Link) f).getContent(), f.getFileOwner().getUsername())));
				continue;
			}
			if (f instanceof App) {
				fileList.add((new FileDto(f.getId(), f.getSize(), f.getName(), f.getLastModification(), f.getPermissions(), "App", ((App) f).getContent(), f.getFileOwner().getUsername())));
				continue;
			}
			if (f instanceof PlainFile) {
				fileList.add((new FileDto(f.getId(), f.getSize(), f.getName(), f.getLastModification(), f.getPermissions(), "PlainFile", ((PlainFile) f).getContent(), f.getFileOwner().getUsername())));
				continue;
			}
		}

	}

	public List<FileDto> result() {
		return fileList;
	}
}