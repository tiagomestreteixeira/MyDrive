package pt.tecnico.myDrive.domain;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;




public class File extends File_Base {

    private static final int MAX_PATH_LENGTH = 1024;

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
        setDir(directory);
        setLastModification(new DateTime());
        checkPathLengthConstrain(directory,name);

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
        return getDir();
    }

    public File getFileByName(User u, String s) throws InvalidFileTypeException{
        throw new InvalidFileTypeException (this.getName(), "getFileByName");
    }

    public User getFileOwner() {
        return getUser();
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
        User u = getUser();
        if (u.equals(user)) {
            return true;
            }
        return false;
    }

    @Override
    public void setDir(Dir directory) {
        directory.addFile(this);
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
    public void setUser(User user) {
       throw new NoPermissionException("addUser");
    }

    public void setOwner(User user) {
        User u = getUser();
        if (u != null) {
            u.removeFile(this);
        }

        if (user == null) {
            super.setUser(SuperUser.getInstance());
            return;
        }
        user.addFile(this);
    }

    public void remove() {
        User u = getUser();
        u.removeFile(this);
        getDir().removeFile(this);
        deleteDomainObject();
    }

    public void xmlImport(Element FileDomainElement, String elementDomain) throws ImportDocumentException {

        String path,
                name,
                ownerUsername,
                defaultPermissions;

        path = name = ownerUsername = defaultPermissions = null;

        for (Element child : FileDomainElement.getChildren()) {

            if (child.getName().equals("path"))
                path = child.getText();
            if (child.getName().equals("name"))
                name = child.getText();
            if (child.getName().equals("owner"))
                ownerUsername = child.getText();
            if (child.getName().equals("perm"))
                defaultPermissions = child.getText();
            log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
        }

        if (path == null)
            throw new ImportDocumentException(elementDomain, "<path> node cannot be read properly.");
        if (name == null)
            throw new ImportDocumentException(elementDomain, "<name> node cannot be read properly.");
        if (ownerUsername == null)
            ownerUsername = "root";

        User owner = MyDrive.getInstance().getUserByUsername(ownerUsername);

        if (defaultPermissions == null) {
            if (owner == null) {
                owner = MyDrive.getInstance().getUserByUsername("root");
            }
        }

        defaultPermissions = owner.getUmask();

        init(name, owner, (Dir) SuperUser.getInstance().makeDir(path), defaultPermissions);
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
        Element lastModifiedDateElement = new Element("lastModification");

        nameElement.addContent(getName());
        pathElement.addContent(getPath());
        ownerElement.addContent(getFileOwner().getUsername());
        permElement.addContent(getPermissions());

        DateTime lastModification = getLastModification();
        String lastModificationConvertedToString="";

        if(lastModification != null){
            lastModificationConvertedToString = lastModification.toString();
        }

        lastModifiedDateElement.addContent(lastModificationConvertedToString);

        el.addContent(nameElement);
        el.addContent(pathElement);
        el.addContent(ownerElement);
        el.addContent(permElement);
        el.addContent(lastModifiedDateElement);

        return el;
    }


    public void checkPathLengthConstrain(Dir directory, String fileName) throws FilePathTooLongException {
        String path = directory.getPath();

        if((path.length() + fileName.length() + 1)> MAX_PATH_LENGTH)
            throw new FilePathTooLongException(path,fileName);
    }
}