package pt.tecnico.myDrive.domain;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.IntegerPatternConverter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;
import pt.tecnico.myDrive.exception.*;


public class File extends File_Base {

    protected File() { /* for derived classes */ }

    static final Logger log = LogManager.getRootLogger();
    public File(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
    }

    public File(String name, Dir directory, String permissions) throws MyDriveException {
        init(name, SuperUser.getInstance() , directory, permissions);
    }

    protected void init(String name, User user, Dir directory, String permissions) throws MyDriveException {


        setId(MyDrive.getInstance().getNewId());
        setOwner(user);
        setName(name);
        setPermissions(permissions);
        addDir(directory);
        setLastModification(new DateTime());

    }

    public String read(User user) throws MyDriveException {
        throw new NoPermissionException("read");
    }

    public void write(User user, String content) throws MyDriveException {
        throw new NoPermissionException("write");
    }

    public void execute(User user) throws MyDriveException {
        throw new NoPermissionException("execute");
    }

    public void delete(User user) throws MyDriveException {
        if (user.checkPermission(this, 'd')) {
            this.remove();
        } else {
            throw new NoPermissionException("delete");
        }
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

    public String getFormatedName() {
        return "File " + getPermissions() + " " + getFileOwner().getName() +  " " + getId() + " " + getName();
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

    public File getFileByName(User u, String s) throws InvalidFileTypeException{
        throw new InvalidFileTypeException (this.getName(), "getFileByName");
    }

    public User getFileOwner() {
        for (User u : getUserSet()) {
            return u;
        }
        return null;
    }

    public String getPath(){
        String path = "";
        File f = this;
        while (!f.getName().equals("/")){
            log.debug(f.getName());
            path = "/"+f.getName()+path;
            f = f.getDirectory();
        }
        if (path == ""){
            return "/";
        } else {
            return path;
        }
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
        super.addDir(directory);
    }

    @Override
    public void setName(String name) {
        if (name.contains("\0")) {
            throw new InvalidFileNameException(name);
        }
        if (name.contains("/")) {
            if (this.isOwner(SuperUser.getInstance())) {
                super.setName(name);
            }
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
        for (Dir d : getDirSet())
            d.removeFile(this);
        deleteDomainObject();
    }


    public Element xmlExport(){
        return null;
    }

    public Element xmlExportHelper(Element el) {

        el.setAttribute("id", Integer.toString(this.getId()));

        Element nameElement = new Element("name");
        Element pathElement = new Element("path");
        Element ownerElement = new Element("owner");
        Element permElement = new Element("perm");

        nameElement.addContent(getName());
        pathElement.addContent(getPath());
        ownerElement.addContent(getFileOwner().getUsername());
        permElement.addContent(getPermissions());


        el.addContent(nameElement);
        el.addContent(pathElement);
        el.addContent(ownerElement);
        el.addContent(permElement);

        return el;
    }
}