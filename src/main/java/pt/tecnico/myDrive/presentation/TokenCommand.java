package pt.tecnico.myDrive.presentation;

public class TokenCommand extends MdCommand {

    private static final String USAGE_MSG = "USAGE: token [username]\n";
    private MdShell mdShell;

    public TokenCommand(MdShell sh) {
        super(sh, "token", "Change Current User to User with username provided and prints the respective token.\n" +
                "When username is not provided the current user and token are printed.\n"+ USAGE_MSG);
        mdShell = sh;
    }

    private void printUserAndToken(String username,String pass){
        mdShell.println("username : " + username + "\ntoken     : " + pass);
    }

    public void execute(String[] args) throws Exception {
        if(args.length>1)
            throw new RuntimeException(USAGE_MSG);

        if(args.length==1)
            mdShell.setCurrentUsername(args[0]);

        printUserAndToken(mdShell.getCurrentUsername(), mdShell.getCurrentToken().toString());

    }
}