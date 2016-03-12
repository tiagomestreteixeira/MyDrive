package pt.tecnico.myDrive.domain;

public class SuperUser extends SuperUser_Base {
    SuperUser root;

    private SuperUser() {
        setUsername("root");
        setName("root");
        setPassword("***");
        setUmask("rwxdr-x-");
        setHome("/home/root");
    }

    public SuperUser getInstance() {
        if (root == null)
            return new SuperUser();
        return root;
    }
}
