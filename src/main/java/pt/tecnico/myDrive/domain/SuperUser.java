package pt.tecnico.myDrive.domain;

public class SuperUser extends SuperUser_Base {
    private static SuperUser root;

    private SuperUser() {
        setUsername("root");
        setName("root");
        setPassword("***");
        setUmask("rwxdr-x-");
        setHome("/home/root");
    }

    public static SuperUser getInstance() {
        if (root == null)
            return new SuperUser();
        return root;
    }

    public boolean checkPermission(File file) {
        return true;
    }
}
