package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.*;

import java.util.Set;

public class MyDrive extends MyDrive_Base {
    static final Logger log = LogManager.getRootLogger();

    private MyDrive() {
        setRoot(FenixFramework.getDomainRoot());
        super.setIdCounter(0);
        SuperUser root = SuperUser.getInstance();
        Dir rootDir = new Dir();
	    rootDir.setUser(root);
	    rootDir.init("/",root,rootDir,root.getUmask());
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
        return (SuperUser)getUserByUsername("root");
    }

    public Dir getRootDir() {
        return (Dir)getSuperUser().getFileByName("/");
    }

    public User getUserByUsername(String username) {
        for (User user : getUserSet()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
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
    public Integer getIdCounter() {
        throw new NoPermissionException("MyDrive.getIdCounter()");
    }

    @Override
    public void setIdCounter(Integer idCounter) {
        throw new NoPermissionException("MyDrive.setIdCounter()");
    }

    public int getNewId() {
        int id = super.getIdCounter();
        id++;
        super.setIdCounter(id);
        return id;
    }

    @Override
    public Set<Login> getLoginsSet() throws MyDriveException{
    	throw new NoPermissionException("MyDrive.getLoginsSet()");
    }

	public long createLogin(String username, String password) {
		loginMaintenance();
		User user = this.getUserByUsername(username);

		if (user != null) {
			if (password.equals(user.getPassword())) {
				Login login = new Login(user);
				this.addLogins(login);
				return login.getIdentifier();
			}
			throw new UserPasswordDoesNotMatchException(username);
		} else {
			throw new UserDoesNotExistException(username);
		}
	}

	private void removeLogin(long login) {
		for (Login session : super.getLoginsSet()) {
			if (session.getIdentifier() == login) {
				this.removeLogins(session);
				session.delete();
			}
		}
	}

	private void loginMaintenance() {
		for (Login session : super.getLoginsSet()) {
			if (!session.isValid()) {
				this.removeLogin(session.getIdentifier());
			}
		}
	}

	public Login getLoginFromId(long identifier) throws MyDriveException {
		for (Login l : super.getLoginsSet()) {
			if (l.getIdentifier() == identifier) {
				if (!l.isValid())
					break;
				return l;
			}
		}

		log.warn("This login is not valid: " + identifier);
		throw new InvalidLoginTokenException(identifier);
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
