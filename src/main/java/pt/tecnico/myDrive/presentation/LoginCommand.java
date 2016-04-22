package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.LoginUserService;

public class LoginCommand extends MdCommand{

    private static final String USAGE_MSG = "USAGE: login username [password]\n";

    public LoginCommand(Shell sh) {
        super(sh, "login", "Log into the system providing the username and login token.\n" + USAGE_MSG);
    }

    public void execute(String[] args) {
        if (args.length != 2)
            throw new RuntimeException(USAGE_MSG);
        new LoginUserService(args[0],args[1]).execute();
    }
}