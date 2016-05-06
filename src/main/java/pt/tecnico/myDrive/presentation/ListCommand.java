package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ChangeDirectoryService;
import pt.tecnico.myDrive.service.ListDirectoryService;
import pt.tecnico.myDrive.service.dto.FileDto;

public class ListCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: ls [path]\n";

    public ListCommand(Shell sh) {
        super(sh, "list", "List given path directory contents or current directory if no argument is given, with the following order:\n" +
		        "Permissions \t Size \t File Id \t Last Modification \t Owner \t File Type \t File Name \n" + USAGE_MSG);
    }

	private void executeList(Long token) {
		ListDirectoryService listDirectoryService = new ListDirectoryService(token);
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
	void execute(String[] args) throws Exception {
		if(args.length > 1)
			throw new RuntimeException(USAGE_MSG);

		if (args.length == 0) {
			Long token = shell().getCurrentToken();
	        executeList(token);
		}else{
			String path = args[0];
			Long token = shell().getCurrentToken();
			ChangeDirectoryService cds = new ChangeDirectoryService(token, path);
			cds.execute();
			executeList(token);
		}

	}
}
