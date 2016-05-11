package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.exception.MyDriveException;

public class LogoutUserService extends MyDriveService {

	private long token;

	public LogoutUserService(Long token) {
		this.token = token;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		getMyDrive().removeLogin(token);
	}

}

