package pt.tecnico.myDrive.presentation;

public abstract class MdCommand extends Command {

    protected String username;
    protected Long token;
    protected String currentDir;

    public MdCommand(Shell sh, String n) { super(sh, n); }
    public MdCommand(Shell sh, String n, String h) { super(sh, n, h); }

    public String getCurrentDir() { return currentDir; }
    public void setCurrentDir(String currentDir) { this.currentDir = currentDir; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getToken() { return token; }
    public void setToken(Long token) { this.token = token; }
}