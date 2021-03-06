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
        SuperUser root = new SuperUser(this);
        Dir rootDir = new Dir();
	    rootDir.setUser(root);
	    rootDir.init("/",root,rootDir,root.getUmask());
        root.setHomeDir(root.makeDir("/home/root"));
	    new Guest(this);

    }

    public void cleanup() {
        for(User u : getUserSet()){
            try {
	            u.remove();
            } catch (Exception e) {
				// Ignores warnings for SuperUser and Guest.
            }
        }
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
        throw new UserDoesNotExistException(username);
    }

	@Override
	public void addUser(User user) {
		try {
			getUserByUsername(user.getUsername());
			throw new UserAlreadyExistsException(user.getUsername());
		} catch (UserDoesNotExistException e) {
			super.addUser(user);
		}
	}

	public boolean hasUser(String user){
		return (getUserByUsername(user) != null);
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

	@Override
	public void addLogins(Login logins) {
		throw new NoPermissionException("MyDrive.addLogins()");
	}

	@Override
	public void removeLogins(Login logins) {
		throw new NoPermissionException("MyDrive.removeLogins()");
	}

	public boolean loginIdExists(long identifier) {
		for (Login l : super.getLoginsSet()) {
			if (l.getIdentifier() == identifier) {
				return true;
			}
		}
		return false;
	}

	public long createLogin(String username, String password) {
		loginMaintenance();
		User user = this.getUserByUsername(username);

		if (user != null) {
			if (user.checkPassword(password)) {
				Login login = new Login(user);
				while (loginIdExists(login.getIdentifier())) {
                    login = new Login(user);
				}
				super.addLogins(login);
				return login.getIdentifier();
			}
			throw new UserPasswordDoesNotMatchException(username);
		} else {
			throw new UserDoesNotExistException(username);
		}
	}

	public void removeLogin(long login) {
        if(!this.loginIdExists(login)){
            throw new InvalidLoginTokenException(login);
        }
        for (Login session : super.getLoginsSet()) {
			if (session.getIdentifier() == login) {
				super.removeLogins(session);
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
            if(username.isEmpty())
                throw new ImportDocumentException("User", "attribute username cannot be read properly");

            new User(this,username,node);
        }

        for (Element node: element.getChildren("dir")) {
            new Dir(node);
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
