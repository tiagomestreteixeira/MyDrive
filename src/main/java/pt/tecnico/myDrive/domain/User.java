package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;

import java.util.Stack;

public class User extends User_Base {

    static final Logger log = LogManager.getRootLogger();
	private static final String USER_DEFAULT_UMASK = "rwxd----";
	private static final int USERNAME_MIN_LENGTH = 3;
	private static final int PASSWORD_MIN_LENGTH = 8;

    protected User() {
        super();
    }

	public User (MyDrive md, String username, String name, String umask, String password){
		init(md,username,name,umask,password);
	}

	public User (MyDrive md, String username){
		init(md,username,username,USER_DEFAULT_UMASK,username);
	}

	public User(MyDrive md, String username, Element xml) {
		super();
		xmlImport(md,username, xml);
	}

	@Override
	public void setPassword(String pass) throws MyDriveException {
		if (pass == null || pass.length()<PASSWORD_MIN_LENGTH){
			throw new InvalidPasswordException(pass, " : password has fewer than "
					+ Integer.toString(PASSWORD_MIN_LENGTH));
		}
		setPasswordInternal(pass);
	}

	protected void setPasswordInternal(String pass) throws MyDriveException {
		super.setPassword(pass);
	}

	@Override
	public String getPassword() throws MyDriveException {
		throw new NoPermissionException("User.getPassword()");
	}

	protected void init(MyDrive md, String username, String name, String umask, String password){
		setUsername(username);
		setName(name);
		setPassword(password);
		setUmask(umask);
		setHomeDir(md.getSuperUser().makeDir("/home/"+username));
		getHomeDir().setOwner(md.getSuperUser(), this);
		getHomeDir().setPermissions(umask);
		md.addUser(this);
	}

	  @Override
	  public void addFile(File fileToBeAdded) throws UserAlreadyExistsException{
		  for(File f : getFileSet()) {
			  if(f.getName().equals(fileToBeAdded.getName()) && f.getPath().equals(fileToBeAdded.getPath()))
				  throw new FileAlreadyExistsException(fileToBeAdded.getName());
		  }

		  super.addFile(fileToBeAdded);
	  }

	  public File getFileByName(String pathname){
		  for (File file: getFileSet())
			  if (file.getPath().equals(pathname))
				  return file;
		  return null;
	  }

	public File getFileById(int id){
		for (File file: getFileSet())
			if (file.getId().equals(id))
				return file;
		return null;
	}

	  public boolean hasFile(String fileName){
		  return getFileByName(fileName)!= null;
	  }
	  public boolean hasFile(int id){
		return getFileById(id)!= null;
	}

	  public boolean isAlphanumeric(String str) {
		  for (int i=0; i<str.length(); i++) {
			  char c = str.charAt(i);
			  if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
		      return false;
		    }
		  return true;
		}

	  @Override
	  public void setUsername(String username) throws InvalidUsernameException /*UserAlreadyExistsException*/ {

	    if (username == null || username.length()<USERNAME_MIN_LENGTH){
	      throw new InvalidUsernameException(username, " : username has fewer than "
				  										 + Integer.toString(USERNAME_MIN_LENGTH));
	    }
		  if(isAlphanumeric(username)){
	    	super.setUsername(username);
	    } else {
	    	throw new InvalidUsernameException(username, " has non-alphanumeric characters");
	    }
	  }

	  @Override
	  public void setUmask(String umask) throws InvalidPermissionsFormatException{
		  if (umask.matches("(r|-)(w|-)(x|-)(d|-)(r|-)(w|-)(x|-)(d|-)")){
	      	super.setUmask(umask);
	    	} else {
	      	throw new InvalidPermissionsFormatException(umask + " you're setting in user " + getUsername() + " mask");
		  }
	  }

	  public void remove(){
		    for(File f: getFileSet()){
		      f.remove();
		    }
		    setMyDrive(null);

		    for(Login l : this.getLoginsSet()){
		    	this.removeLogins(l);
		    	l.delete();
		    }
		    deleteDomainObject();
		  }

    @Override
    public void setMyDrive(MyDrive md) {
        if (md == null)
            super.setMyDrive(null);
        else
            md.addUser(this);
    }

	public boolean checkPermission(File file, Character c){
		if (file.isOwner(this)){
			switch (c){
				case 'r':
					return file.getPermissions().matches("r.......");
				case 'w':
					return file.getPermissions().matches(".w......");
				case 'x':
					return file.getPermissions().matches("..x.....");
				case 'd':
					return file.getPermissions().matches("...d....");
			}
		}
		switch (c){
			case 'r':
				return file.getPermissions().matches("....r...");
			case 'w':
				return file.getPermissions().matches(".....w..");
			case 'x':
				return file.getPermissions().matches("......x.");
			case 'd':
				return file.getPermissions().matches(".......d");
		}
		return false;
	}

	public boolean setPermissions (File file, String newPermissions){
		if (file.isOwner(this)){
			file.setPermissions(newPermissions);
			log.info("Set Permissions to "+ file.getName()+ ": Access Granted.");
			return true;
		} else {
			log.info("Set Permissions to "+ file.getName()+ ": Access Denied.");
			return false;
		}
	}



	public boolean checkPassword (String attempt) {
		return attempt.equals(super.getPassword());
	}

	private Stack<String> toStack (String pathname) {
		String[] params = pathname.split("/");
		Stack<String> st = new Stack<>();
		for (int i = params.length - 1; i > 0; i--) {
			st.push(params[i]);
		}
		return st;
	}

	public File lookup(String pathname) throws MyDriveException {
		MyDrive md = getMyDrive();
		Dir rootDir = md.getRootDir();
		if (pathname.isEmpty())
			throw new FileDoesNotExistException("empty");

		return rootDir.lookup(this, pathname.substring(1));
	}


	public Dir makeDir(String pathname){

		File file = this.getMyDrive().getRootDir();

		Stack<String> st = toStack(pathname);
		while (!st.empty()) {
				String temp = st.pop();
				Dir d = (Dir)file;
			try {
				file = file.getFileByName(this, temp);
			} catch (FileDoesNotExistException e) {
				file = new Dir(temp,this.getMyDrive().getSuperUser(),d,this.getMyDrive().getSuperUser().getUmask());
			}

		}
		file.setOwner(this.getMyDrive().getSuperUser(),this);
		file.setPermissions(this.getUmask());
		return (Dir)file;
	}

    public void xmlImport(MyDrive md, String username, Element userElement) throws ImportDocumentException {

        String defaultMask = USER_DEFAULT_UMASK;
        String defaultName = username;
        String defaultPassword = username;

        for (Element child : userElement.getChildren()) {
            if (child.getName().equals("mask"))
                defaultMask = child.getText();
            if (child.getName().equals("name"))
                defaultName = child.getText();
            if (child.getName().equals("password"))
                defaultPassword = child.getText();
        }

		init(md,username,defaultName,defaultMask,defaultPassword);
    }

	/*public boolean equals(User u){
		return (u.getUsername().equals(this.getUsername()));
	}*/

	public Element xmlExport() {
		Element userNode = new Element("user");
		userNode.setAttribute("username", getUsername());

        Element passwordElement = new Element("password");
		Element nameElement = new Element("name");
        Element homeElement = new Element("home");
        Element maskElement = new Element("mask");

        passwordElement.addContent(super.getPassword());
		nameElement.addContent(getName());
        homeElement.addContent(getHomeDir().getPath());
        maskElement.addContent(getUmask());

        userNode.addContent(passwordElement);
        userNode.addContent(nameElement);
        userNode.addContent(homeElement);
        userNode.addContent(maskElement);

		return userNode;
	}

	public boolean isLoginValid(DateTime loginDate) {
		return loginDate.plusHours(2).isAfterNow();
	}
}
