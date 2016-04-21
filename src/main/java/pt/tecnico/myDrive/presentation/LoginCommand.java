package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.LoginUserService;

public class LoginCommand extends MdCommand{

    public LoginCommand(Shell sh) { super(sh, "Login", "Log into the system providing the username and login token "); }
    public void execute(String[] args) {
        if (args.length != 2)
            throw new RuntimeException("USAGE: "+name()+" username");
        new LoginUserService(args[0],args[1]).execute();
    }
}