package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ChangeDirectoryService;
import pt.tecnico.myDrive.service.ListDirectoryService;
import pt.tecnico.myDrive.service.dto.FileDto;

public class ListCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: ls [path]\n";

    public ListCommand(Shell sh) {
        super(sh, "list", "List given path directory contents or current directory if no argument is given. " + USAGE_MSG);
    }

    private void executeList(Long token){
    	ListDirectoryService listDirectoryService = new ListDirectoryService(token);
    	listDirectoryService.execute();
    	for(FileDto f : listDirectoryService.result()){
    		shell().println(f.getType()+ " " + f.getPermissions() + " " + f.getOwner() + " " + f.getId() + " " + f.getLastModification() + " "+ f.getFilename());
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
			shell().println("Files for: " + path);
			executeList(token);
		}

	}
}
