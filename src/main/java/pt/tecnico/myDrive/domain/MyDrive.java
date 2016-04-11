package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;

import java.util.Set;

public class MyDrive extends MyDrive_Base {
    static final Logger log = LogManager.getRootLogger();

    private MyDrive() {
		// TODO: Create basic structure
        setRoot(FenixFramework.getDomainRoot());
        super.setIdCounter(0);
        
        setSession(new Session());
        
        SuperUser root = SuperUser.getInstance();
        Dir rootDir = new Dir();
        rootDir.init("/", root, rootDir, root.getUmask());
        root.setHomeDir(root.makeDir("/home/root"));
    }

    public void cleanup() {
        for(User u : getUserSet())
            u.remove();
        setRoot(null);
        deleteDomainObject();
    }

    public static MyDrive getInstance() {
        MyDrive md = FenixFramework.getDomainRoot().getMyDrive();

        if (md != null) {
            return md;
        }
        
        return new MyDrive();
    }

    public SuperUser getSuperUser() {
        return SuperUser.getInstance();
    }

    public Dir getRootDir() {
        return Dir.getRootDir();
    }

    public User getUserByUsername(String username) {
        for (User user : getUserSet()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public void removeUser(String username) {
        User user = getUserByUsername(username);
        this.removeUser(user);
    }

    @Override
    public void removeUser(User user) {
        super.removeUser(user);
        user.remove();
    }

    @Override
    public void addUser(User user) {
        if (getUserByUsername(user.getUsername()) == null) {
            super.addUser(user);
        }
		else
        throw new UserAlreadyExistsException(user.getName());
    }

    @Override
    public Set<User> getUserSet() {
        // TODO: Check if access should be allowed
        return super.getUserSet();
    }

    @Override
    public Integer getIdCounter() {
        throw new NoPermissionException("getIdCounter");
    }

    @Override
    public void setIdCounter(Integer idCounter) {
        throw new NoPermissionException("setIdCounter");
    }

    public int getNewId() {
        int id = super.getIdCounter();
        id++;
        super.setIdCounter(id);
        return id;
    }

    public void xmlImport(Element element) throws ImportDocumentException {

        for (Element node: element.getChildren("user")) {
            String username = node.getAttribute("username").getValue();

            if(username == null)
                throw new ImportDocumentException("User", "attribute username cannot be read properly");

            User user = getUserByUsername(username);
            if(user != null)
                 throw new UserAlreadyExistsException(username);

            new User(this,username,node);
        }

        for (Element node: element.getChildren("dir")) {
            (new Dir()).xmlImport(node);
        }

        for (Element node: element.getChildren("plain")) {
            new PlainFile(node);
        }

        for (Element node: element.getChildren("link")) {
            new Link(node);
        }

        for (Element node: element.getChildren("app")) {
            new App(node);
        }

    }


    public Document xmlExport() {
        Element myDriveElement = new Element("myDrive");
        Document doc = new Document(myDriveElement);

        for (User u: getUserSet()) {
            myDriveElement.addContent(u.xmlExport());
        }

        for (User u: getUserSet()) {
            for(File file : u.getFileSet()){
                myDriveElement.addContent(file.xmlExport());
            }
        }

        return doc;
    }

}
