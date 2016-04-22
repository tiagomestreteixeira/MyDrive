package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.presentation.Command;
import pt.tecnico.myDrive.presentation.Shell;

public abstract class MdCommand extends Command {
    public MdCommand(Shell sh, String n) { super(sh, n); }
    public MdCommand(Shell sh, String n, String h) { super(sh, n, h); }
}