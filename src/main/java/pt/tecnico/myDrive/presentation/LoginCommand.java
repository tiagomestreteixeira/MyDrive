package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.LoginUserService;

public class LoginCommand extends MdCommand{

    private static final String USAGE_MSG = "USAGE: login username [password]\n";
    private Long token;

    public LoginCommand(Shell sh) {
        super(sh, "login", "Log into the system providing the username and login token.\n" + USAGE_MSG);
    }


    private void executeLogin(String user, String pass){
        LoginUserService loginUserService = new LoginUserService(user, pass);
        loginUserService.execute();
        token = loginUserService.result();
        log.info("Token generated: ", token.toString());
    }

    public void execute(String[] args) throws Exception {
        if (args.length != 2){
            if(args.length == 1 && args[0].equals("Guest"))
                executeLogin("nobody",null);
            else
                throw new RuntimeException(USAGE_MSG);
        }
        executeLogin(args[0],args[1]);
    }
}