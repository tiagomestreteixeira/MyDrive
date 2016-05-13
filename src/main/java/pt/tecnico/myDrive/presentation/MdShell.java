package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.LogoutUserService;

public class MdShell extends Shell {
	protected static UsersManager userManager = new UsersManager();

	public static void main(String[] args) throws Exception {
		MdShell sh = new MdShell();
		sh.execute();

		new LogoutUserService(userManager.getTokenByUsername("nobody")).execute();
	}

	public MdShell() {
		super("myDrive");
		new LoginCommand(this);
		new TokenCommand(this);
		new LsCommand(this);
		new UpdateCommand(this);
		new CWDCommand(this);
		new DoCommand(this);
		new EnvCommand(this);
		loginGuestUser();
	}

	public void loginGuestUser(){
		try {
			this.get("login").execute("nobody".split(" "));
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	public void logoutGuestUser(){
		try {
			new LogoutUserService(userManager.getTokenByUsername("nobody")).execute();
		}catch (Exception e){}
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
	
	public void addUser(String username,Long token){
		userManager.addUser(username,token);
	}
}
