package pt.tecnico.myDrive.domain;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;




public class File extends File_Base {

    private static final int MAX_PATH_LENGTH = 1024;

    protected File() { /* for derived classes */ }

    static final Logger log = LogManager.getRootLogger();

    protected void init(String name, User user, Dir directory, String permissions) throws MyDriveException {

        if (user.checkPermission(directory, 'w'))  {
            setId(directory.getFileOwner().getMyDrive().getNewId());
            setName(name);
            setPermissions(permissions);
            setDir(directory);
            setUser(user);
            setLastModification(new DateTime());
            checkPathLengthConstrain(directory, name);
        } else {
            throw new NoPermissionException("File.init()");
        }
    }

    public String read(User user) throws MyDriveException {
        throw new NoPermissionException("File.read()");
    }

    public void write(User user, String content) throws MyDriveException {
        throw new NoPermissionException("File.write()");
    }

    public void execute(User user) throws MyDriveException {
        throw new NoPermissionException("File.execute()");
    }

    public void delete(User user) throws MyDriveException {
        if (user.checkPermission(this, 'd')) {
            this.remove();
        } else {
            throw new NoPermissionException("File.delete()");
        }
    }

    public File lookup(User user, String path) throws MyDriveException {
        log.debug("Lookup File");
        if (path.equals("")) {
            return this;
        }
        throw new FileDoesNotExistException(path);
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
            path = "/"+f.getName()+path;
            f = f.getDir();
        }
        if (path == ""){
            return "/";
        } else {
            return path;
        }
    }

    public int getSize(){
        throw new NoPermissionException("File.getSize()");
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
            if (this.isOwner(getFileOwner().getMyDrive().getSuperUser())) {
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

    public void setOwner(User requester, User newOwner) throws MyDriveException {

        SuperUser superUser = getDir().getFileOwner().getMyDrive().getSuperUser();
		User fileOwner = getFileOwner();

		if (requester.equals(fileOwner) || requester.equals(superUser)) {
            if(!newOwner.hasFile(this.getPath())){
                newOwner.addFile(this);
                super.setUser(newOwner);
            }
			return;
		}
		throw new NoPermissionException("File.setOwner()");
	}

    protected void remove() {
        User u = getUser();
        u.removeFile(this);
        getDir().removeFile(this);
        deleteDomainObject();
    }

    public void checkPathLengthConstrain(Dir directory, String fileName) throws FilePathTooLongException {
        String path = directory.getPath();

        if((path.length() + fileName.length() + 1)> MAX_PATH_LENGTH)
            throw new FilePathTooLongException(path,fileName);
    }

    public void xmlImport(Element FileDomainElement, String elementDomain) throws ImportDocumentException {
        String path, name, ownerUsername, defaultPermissions;
        path = name = ownerUsername = defaultPermissions = "";

        for (Element child : FileDomainElement.getChildren()) {
            if (child.getName().equals("path"))
                path = child.getText();
            if (child.getName().equals("name"))
                name = child.getText();
            if (child.getName().equals("owner"))
                ownerUsername = child.getText();
            if (child.getName().equals("perm"))
                defaultPermissions = child.getText();
        }

        if (path.isEmpty())
            throw new ImportDocumentException(elementDomain, "<path> node cannot be read properly.");
        if (name.isEmpty())
            throw new ImportDocumentException(elementDomain, "<name> node cannot be read properly.");
        if (ownerUsername.isEmpty())
            ownerUsername = "root";

        MyDrive md = MyDrive.getInstance();
        User owner = md.getUserByUsername(ownerUsername);

        if (defaultPermissions.isEmpty()) {
            if (ownerUsername.isEmpty()) owner = md.getUserByUsername("root");
            defaultPermissions = owner.getUmask();
        }
        init(name, owner, owner.makeDir(path), defaultPermissions);
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
        String lastModificationConvertedToString = lastModification.toString();


        lastModifiedDateElement.addContent(lastModificationConvertedToString);

        el.addContent(pathElement);
        el.addContent(nameElement);
        el.addContent(ownerElement);
        el.addContent(permElement);
        el.addContent(lastModifiedDateElement);

        return el;
    }

}