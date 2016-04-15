package pt.tecnico.myDrive.service;

import java.util.ArrayList;
import java.util.List;

import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.exception.DirectoryHasNoFilesException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;

public class ListDirectoryService extends MyDriveService {

	private long loginId;
	private List<FileDto> fileList;
	private Dir currentDir;

	public ListDirectoryService(long token) {
		this.loginId = token;

	}

	@Override
	protected void dispatch() throws DirectoryHasNoFilesException{
		fileList = new ArrayList<FileDto>();

		Login login = getMyDrive().getLoginFromId(loginId);

		login.refreshToken();
		currentDir = login.getCurrentDir();

		if(currentDir.getFileSet().isEmpty()) throw new DirectoryHasNoFilesException();
		
		for(File f : currentDir.getFileSet()){
			if(f instanceof Dir){
				fileList.add((new FileDto(f.getId(), f.getName(), f.getLastModification(), f.getPermissions(), "Dir")));
			}
			if(f instanceof PlainFile){
				fileList.add((new FileDto(f.getId(), f.getName(), f.getLastModification(), f.getPermissions(), "PlainFile", ((PlainFile) f).getContent())));
			}
			if(f instanceof Link){
				fileList.add((new FileDto(f.getId(), f.getName(), f.getLastModification(), f.getPermissions(), "Link", ((Link) f).getContent())));
			}
			if(f instanceof App){
				fileList.add((new FileDto(f.getId(), f.getName(), f.getLastModification(), f.getPermissions(), "App", ((App) f).getContent())));
			}
		}

	}

	public List<FileDto> result() {
		return fileList;
	}
}