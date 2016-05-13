package pt.tecnico.myDrive.presentation;

public class MdShell extends Shell {

	public static void main() throws Exception {
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
	}
}
