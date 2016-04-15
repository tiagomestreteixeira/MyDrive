package pt.tecnico.myDrive.service;

import java.util.ArrayList;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ListDirectoryService extends MyDriveService {
	
	private long loginId;
	private String pathname;
	private ArrayList<String> fileList;
	private ArrayList<String> result;
	private Dir currentDir;
	
	public ListDirectoryService(long token, String pathname) {
		this.loginId = token;
		this.pathname = pathname;
		fileList = new ArrayList<String>();
	}

	@Override
	protected void dispatch() throws MyDriveException {
		Login login = getMyDrive().getLoginFromId(loginId);
		if (getMyDrive().isTokenValid(loginId)) {
			login.refreshToken();
			currentDir = login.getCurrentDir();
		}
		
		
		fileList = currentDir.listFileSet();
		
		for(String file : fileList){
			System.out.println(file);
		}
		
		result = fileList;
	}

	public ArrayList<String> result() {
		return result;
	}
}
