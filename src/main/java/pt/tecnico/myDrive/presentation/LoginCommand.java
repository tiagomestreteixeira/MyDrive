package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.LoginUserService;

public class LoginCommand extends MdCommand{

    private static final String USAGE_MSG = "USAGE: login username [password]\n";
    private MdShell mdShell;

    public LoginCommand(MdShell sh) {
        super(sh, "login", "Log into the system providing the username and login token.\n" + USAGE_MSG);
        mdShell = sh;
    }

    private void executeLogin(String user, String pass){
        LoginUserService loginUserService = new LoginUserService(user, pass);
        loginUserService.execute();
        mdShell.addUser(user,loginUserService.result());
        log.info("Token generated: " + mdShell.getCurrentToken());
        if(mdShell.getCurrentUsername().equals("nobody"))
            mdShell.logoutGuestUser();
    }

    public void execute(String[] args) throws Exception {
        if(args.length>2 || args.length<1)
            throw new RuntimeException(USAGE_MSG);
        String pass = args.length == 1 ? args[0] : args[1];
        executeLogin(args[0],pass);
    }
}