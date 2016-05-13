package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.WriteFileService;

public class UpdateCommand extends MdCommand {
	private static final String USAGE_MSG = "USAGE: update path text";
	private MdShell mdShell;

	public UpdateCommand(MdShell sh) {
		super(sh, "update", "Writes the text to the file specified by the path\n" + USAGE_MSG);
		mdShell = sh;
	}

	@Override
	public void execute(String[] args) throws Exception {
		if (args.length != 2) {
			throw new RuntimeException(USAGE_MSG);
		}

		WriteFileService service = new WriteFileService(mdShell.getCurrentToken(), args[0], args[1]);
		service.execute();
	}
}
