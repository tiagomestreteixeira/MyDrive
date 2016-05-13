package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.exception.InvalidLoginTokenException;
import pt.tecnico.myDrive.service.LogoutUserService;

public class MdShell extends Shell {
	private UsersManager userManager;

	public void loginGuestUser(){
		try {
			this.get("login").execute("nobody".split(" "));
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	public void logoutGuestUser(){
		try {
			new LogoutUserService(userManager.getTokenByUsername("nobody")).execute();
		}
		catch (InvalidLoginTokenException e) { log.debug("user nobody is not logged in"); }
	}

	public String getCurrentUsername() {
		return userManager.getCurrentUsername();
	}

	public void setCurrentUsername(String username) {
		userManager.setCurrentUsername(username);
	}

	public Long getCurrentToken() {
		return userManager.getCurrentToken();
	}

	public void setCurrentToken(Long token) {
		userManager.setCurrentToken(token);
	}

	public void addUser(String username,Long token){
		userManager.addUser(username,token);
	}

	public static void main() throws Exception {
		UsersManager usersManager = new UsersManager();
		MdShell sh = new MdShell();
		sh.execute();
	}

	public MdShell() {
		super("myDrive");
		// TODO: Add each command here
		new LoginCommand(this);
		new TokenCommand(this);
		new LsCommand(this);
		new UpdateCommand(this);
		new CWDCommand(this);
		new DoCommand(this);
		new EnvCommand(this);

		loginGuestUser();
	}
}
