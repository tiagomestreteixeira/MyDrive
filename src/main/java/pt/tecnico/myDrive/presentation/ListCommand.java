package pt.tecnico.myDrive.presentation;

public class ListCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: ls [path]\n";

    public ListCommand(Shell sh) {
        super(sh, "list", "List current directory if no argument is given or list given path directory." + USAGE_MSG);
    }

	@Override
	void execute(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
