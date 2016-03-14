package pt.tecnico.myDrive.domain;


import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;


public class File extends File_Base {

    protected File() { /* for derived classes */ }

    public File(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
    }



    protected void init(String name, User user, Dir directory, String permissions) throws MyDriveException {


        setId(MyDrive.getInstance().getNewId());
        setName(name);
        setOwner(user);
        setPermissions(permissions);
        addDir(directory);
        setLastModification(new DateTime());

    }

    @Override
    public Integer getId(){
        return super.getId();
    }

    @Override
    public String getPermissions(){
        return super.getPermissions();
    }

    @Override
    public String getName(){
        return super.getName();
    }

    public DateTime getLastModification(){
        return super.getLastModification();
    }

    public Dir getDirectory(){
        for (Dir d : getDirSet()) {
            return d;
        }
        return null;
    }

    public User getFileOwner() {
        for (User u : getUserSet()) {
            return u;
        }
        return null;
    }

    public boolean isOwner(User user) {
        for (User u : getUserSet()) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addDir(Dir directory) {
        for (Dir d : getDirSet()) {
            for (File f : d.getFileSet())
                if (f.getName().equals(this.getName()))
                        throw new FileAlreadyExistsException(this.getName());
        }
    }

    @Override
    public void setName(String name) {
        if (name.contains("\0") /*|| name.contains("/")*/) {
            throw new InvalidFileNameException(name);
        }
        super.setName(name);
    }

    @Override
    public void setPermissions(String permissions) {
        if (permissions.matches("(r|-)(w|-)(x|-)(d|-)(r|-)(w|-)(x|-)(d|-)")) {
            super.setPermissions(permissions);
        } else
            throw new InvalidPermissionsFormatException(permissions);
    }

    @Override
    public void addUser(User user) {
       throw new NoPermissionException("addUser");
    }

    public void setOwner(User user) {
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