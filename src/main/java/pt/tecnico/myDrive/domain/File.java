package pt.tecnico.myDrive.domain;


import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.InvalidFileNameException;
import pt.tecnico.myDrive.exception.InvalidPermissionsFormatException;


public class File extends File_Base {


    protected File() { /* for derived classes */ }

    public File(String name, User user, Dir directory, String permissions)
            throws InvalidFileNameException, InvalidPermissionsFormatException {
        init(user, name, directory, permissions);
    }

    protected void init(User user, String name, Dir directory, String permissions)
            throws InvalidFileNameException, InvalidPermissionsFormatException {

        MyDrive md = MyDrive.getInstance();

        setId(md.getNewId());
        setName(name);
        setOwner(user);
        setPermissions(permissions);
        addDir(directory);
        setLastModification(new DateTime());

    }

    @Override
    public Integer getId(){
        // TODO: Check if access should be allowed
        return super.getId();
    }

    @Override
    public String getPermissions(){
        // TODO: Check if access should be allowed
        return super.getPermissions();
    }

    @Override
    public String getName(){
        // TODO: Check if access should be allowed
        return super.getName();
    }

    public DateTime getLastModification(){
        // TODO: Check if access should be allowed
        return super.getLastModification();
    }

    public Dir getDirectory(){
        // TODO: Check if access should be allowed
        for (Dir d : getDirSet()) {
            return d;
        }
        return null;
    }

    public User getFileOwner() {
        // TODO: Check if access should be allowed
        for (User u : getUserSet()) {
                return u;
            }
        return null;
    }

    public boolean isOwner(User user) {
        // TODO: Check if access should be allowed
        for (User u : getUserSet()) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;}

    @Override
    public void setName(String name) {
        // TODO: Check if access should be allowed
        if (name.contains("\0") /*|| name.contains("/")*/) {
            throw new InvalidFileNameException(name);
        }
        super.setName(name);
    }

    @Override
    public void setPermissions(String permissions) {
        // TODO: Check if access should be allowed
        if (permissions.matches("(r|-)(w|-)(x|-)(d|-)(r|-)(w|-)(x|-)(d|-)")) {
            super.setPermissions(permissions);
        } else
        throw new InvalidPermissionsFormatException(permissions);
    }

    @Override
    public void addUser(User user) {
        // TODO: Check if access should be allowed
        if (user == null) {
            super.addUser(SuperUser.getInstance());
            return;
        }

        user.addFile(this);
    }

    public void setOwner(User user) {
        // TODO: Check if access should be allowed
        for (User u : getUserSet())
            u.removeFile(this);

        if (user == null) {
            super.addUser(SuperUser.getInstance());
            return;
        }
        user.addFile(this);
    }

    public void remove() {
        for (User u : getUserSet())
            u.removeFile(this);
        deleteDomainObject();
    }


}