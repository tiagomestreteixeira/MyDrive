package pt.tecnico.myDrive.presentation;

public class MdShell extends Shell {

    public static void main(String[] args) throws Exception {
        MdShell sh = new MdShell();
        sh.execute();
    }

    public MdShell() { // add commands here
        super("myDrive");
    }
}