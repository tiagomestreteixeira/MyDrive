package pt.tecnico.myDrive.domain;

public class SuperUser extends SuperUser_Base {
    public SuperUser(MyDrive md) {
        setUsername("root");
        setName("root");
        setPassword("***");
        setUmask("rwxdr-x-");
        md.addUser(this);
    }

    @Override
    public boolean checkPermission(File file, Character c) {
        return true;
    }

    public boolean setPermissions(File file, String newPermissions) {
        file.setPermissions(newPermissions);
        return true;
    }
}



