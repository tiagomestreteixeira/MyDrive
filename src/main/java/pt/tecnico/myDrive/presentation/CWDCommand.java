package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ChangeDirectoryService;

public class CWDCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: cwd [path]\n";

	public CWDCommand(Shell sh) {
		super(sh, "cwd", "Change Current working directory to the one specified by [path] and lists the new path.\n" +
				"When [path] is not provided the current directory path is shown.\n" + USAGE_MSG);
	}

	private void executeCWD(long token, String path) {
		ChangeDirectoryService service = new ChangeDirectoryService(token, path);
		service.execute();
		System.out.println("[Current Directory]: " + service.result());
	}


	@Override
	public void execute(String[] args) throws Exception {
	}
}
