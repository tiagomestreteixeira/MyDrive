package pt.tecnico.myDrive.presentation;

public abstract class MdCommand extends Command {

    public MdCommand(Shell sh, String n) { super(sh, n); }
    public MdCommand(Shell sh, String n, String h) { super(sh, n, h); }

}