package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.LoginUserService;

public class LoginCommand extends MdCommand{

    private static final String USAGE_MSG = "USAGE: login username [password]\n";
    private Long token;

    public LoginCommand(Shell sh) {
        super(sh, "login", "Log into the system providing the username and login token.\n" + USAGE_MSG);
    }

    public void execute(String[] args) throws Exception {
        LoginUserService loginUserService;
        if (args.length != 2){
            if(args.length == 1 && args[0].equals("Guest")) {
                loginUserService = new LoginUserService(args[0], null);
                loginUserService.execute();
                token = loginUserService.result();
                return;
            }
            throw new RuntimeException(USAGE_MSG);
        }

        if(args.length > 2)
            throw new RuntimeException(USAGE_MSG);

        loginUserService = new LoginUserService(args[0],args[1]);
        loginUserService.execute();
        token = loginUserService.result();
        System.out.println("Token generated: " + token.toString() + "\n");
    }
}