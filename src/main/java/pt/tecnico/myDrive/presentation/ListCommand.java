package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ListDirectoryService;

public class ListCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: ls [path]\n";

    public ListCommand(Shell sh) {
        super(sh, "list", "List given path directory contents or current directory if no argument is given. " + USAGE_MSG);
    }

    private void executeList(Long token){
    	ListDirectoryService listDirectoryService = new ListDirectoryService(token);
    	listDirectoryService.execute();
    	shell().println("HI");
    }
    
	@Override
	void execute(String[] args) throws Exception {
		if(args.length>1)
            throw new RuntimeException(USAGE_MSG);
		
        
       
		Long token = shell().getCurrentToken();
        executeList(token);
	}
}
