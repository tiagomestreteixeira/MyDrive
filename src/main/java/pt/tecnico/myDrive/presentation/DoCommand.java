package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ExecuteFileService;

import java.util.Arrays;

public class DoCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: do path [args]\n";

	public DoCommand(Shell sh) {
		super(sh, "do", "Executes the file in the give path with the given args.\n" + USAGE_MSG);
	}

	@Override
	public void execute(String[] args) throws Exception {
		ExecuteFileService service;
		String[] exeArgs = Arrays.copyOfRange(args, 1, args.length);

		if (args.length >= 1) {
			service = new ExecuteFileService(shell().getCurrentToken(), args[0], exeArgs);
			service.execute();
		} else {
			throw new RuntimeException(USAGE_MSG);
		}
	}
}
