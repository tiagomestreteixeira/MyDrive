package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ChangeDirectoryService;
import pt.tecnico.myDrive.service.ListDirectoryService;
import pt.tecnico.myDrive.service.dto.FileDto;

public class LsCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: ls [path]\n";

    public LsCommand(Shell sh) {
        super(sh, "list", "List given path directory contents or current directory if no argument is given, with the following order:\n" +
		        "Permissions \t Size \t File Id \t Last Modification \t Owner \t File Type \t File Name \n" + USAGE_MSG);
    }

	private void executeList(Long token, String pathname) {
		ListDirectoryService listDirectoryService = new ListDirectoryService(token, pathname);
		listDirectoryService.execute();
		for (FileDto f : listDirectoryService.result()) {
			shell().println(f.getPermissions()
					+ "\t" + f.getSize()
					+ "\t" + f.getId()
					+ "\t" + f.getLastModification()
					+ "\t" + f.getOwner()
					+ "\t" + f.getType()
					+ "\t" + f.getFilename());
		}
	}

	@Override
	public void execute(String[] args) throws Exception {
		Long token = shell().getCurrentToken();

		if(args.length > 1)
			throw new RuntimeException(USAGE_MSG);

		if (args.length == 0) {

	        executeList(token, ".");
		}else{
			String path = args[0];
			executeList(token, path);
		}

	}
}
