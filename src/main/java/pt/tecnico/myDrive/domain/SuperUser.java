package pt.tecnico.myDrive.domain;

public class SuperUser extends SuperUser_Base {
    private SuperUser() {
        setUsername("root");
        setName("root");
        setPassword("***");
        setUmask("rwxdr-x-");
        setHome("/home/root");
        MyDrive.getInstance().addUser(this);
    }

    public static SuperUser getInstance() {
        SuperUser root = (SuperUser) MyDrive.getInstance().getUserByUsername("root");

        if (root == null) {
            return new SuperUser();
        }

        return root;
    }

    public boolean checkPermission(File file) {
        return true;
    }

    public boolean setPermissions(File file, String newPermissions) {
        file.setPermissions(newPermissions);
        return true;
    }
}



