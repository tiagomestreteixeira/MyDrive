package pt.tecnico.myDrive.presentation;

public class TokenCommand extends MdCommand {

    private static final String USAGE_MSG = "USAGE: login username [password]\n";

    public TokenCommand(Shell sh) {
        super(sh, "token", "Log into the system providing the username and login token.\n" + USAGE_MSG);
    }

    public void execute(String[] args) throws Exception {
        if (args.length < 1){
            shell().println(getUsername());
            shell().println(getToken().toString());
        }
        throw new RuntimeException(USAGE_MSG);
    }
}