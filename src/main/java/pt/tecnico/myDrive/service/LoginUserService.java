package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.domain.*;


public class LoginUserService extends MyDriveService {
	private String username;
	private String password;
	private MyDrive md;
	private long token;

	public LoginUserService(String user, String pass) {
		username = user;
		password = pass;
	}

	@Override
	protected void dispatch() throws MyDriveException {

		md = MyDrive.getInstance();
		token = md.createLogin(username, password);
	}

	public long result() {
		return token;
	}
}

