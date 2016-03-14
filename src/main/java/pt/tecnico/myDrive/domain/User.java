package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.ImportDocumentException;

import java.util.Stack;

public class User extends User_Base {

    static final Logger log = LogManager.getRootLogger();

    public User() {
        super();
    }
	  
	  public User (String username, String name, String umask, String home, String password){
		  
	    setUsername(username);
	    setName(username);
	    setPassword(username);
	    setMask(umask);
	    setHome(home);
	  }
	  
	  public File createFile(String name, User user, Dir directory, String permissions){
		  File file= new File(name,user,directory,permissions);
		  return file;
	  }
	  
	  @Override
	  public void addFile(File fileToBeAdded) throws UserAlreadyExistsException{
		  if(hasFile(fileToBeAdded.getName()))
			  throw new FileAlreadyExistsException(fileToBeAdded.getName());
		  
		  super.addFile(fileToBeAdded);
	  }
	  
	  public File getFileByName(String name){
		  for (File file: getFileSet())
			  if (file.getName().equals(name))
				  return file;
		  return null;
	  }
	  
	  public boolean hasFile(String fileName){
		  return getFileByName(fileName)!= null;
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
		  
	    if (username == null){
	      throw new InvalidUsernameException("Username cannot be empty");
	    }
	    /*if (username.equals("root")){
	      throw new UserAlreadyExistsException(username);
	    }*/
		  if(isAlphanumeric(username)){
	    	super.setUsername(username);
	    } else {
	    	throw new InvalidUsernameException("not valid");
	    }
	  }  
	 
	  public void setMask(String umask) throws InvalidUsernameException{
	    if (umask.equals("rwxd----")){
	      umask=umask;
	    } else {
	      throw new InvalidUsernameException("Mask not valid");
	    }
	  }
	 	
	  public void remove(){
	    for(File f: getFileSet()){
	      f.remove();
	    }
	    setMyDrive(null);
	    deleteDomainObject();
	  }

    public User(MyDrive md, String username, Element xml) {
        super();
        xmlImport(username, xml);
        setMyDrive(md);
    }

    @Override
    public void setMyDrive(MyDrive md) {
        if (md == null)
            super.setMyDrive(null);
        else
            md.addUser(this);
    }

	private Stack<String> toStack (String pathname) {
		String[] params = pathname.split("/");
		Stack<String> st = new Stack<>();
		for (int i = params.length - 1; i > 0; i--) {
			log.debug(params[i]);
			st.push(params[i]);
		}
		return st;
	}

	public File lookup(String pathname){

		File file = Dir.getRootDir();
		Stack<String> st = toStack(pathname);

		while (!st.empty()) {
			if (file instanceof Dir)
				file = ((Dir) file).getFileByName(st.pop());
			//TODO: Check for links.
			//TODO: Check Permissions.
		}
		return file;
	}
	public Dir makeDir(String pathname){

		File file = Dir.getRootDir();
		Stack<String> st = toStack(pathname);
		while (!st.empty()) {
			if (file instanceof Dir) {
				String temp = st.pop();
				Dir d = (Dir)file;
				file = ((Dir) file).getFileByName(temp);
				if (file == null) {
					file = new Dir(temp,this,d,this.getUmask());
				}
			}
			//TODO: Check for links.
			//TODO: Check Permissions.
		}
		return (Dir)file;
	}

    public void xmlImport(String username, Element userElement) throws ImportDocumentException {

        String defaultHome = "/home/" + username;
        String defaultMask = "rwxd----";
        String defaultName = username;
        String defaultPassword = username;

        for (Element child : userElement.getChildren()) {
            if (child.getName().equals("home"))
                defaultHome = child.getText();
            if (child.getName().equals("mask"))
                defaultMask = child.getText();
            if (child.getName().equals("name"))
                defaultName = child.getText();
            if (child.getName().equals("password"))
                defaultPassword = child.getText();
            log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
        }

        setUsername(username);
        setHome(defaultHome);
        setUmask(defaultMask);
        setName(defaultName);
        setPassword(defaultPassword);
    }

	public Element xmlExport() {
		Element userNode = new Element("user");
		userNode.setAttribute("username", getUsername());

		Element nameElement = new Element("name");
		Element maskElement = new Element("mask");
		Element homeElement = new Element("home");
		Element passwordElement = new Element("password");

		nameElement.addContent(getName());
		maskElement.addContent(getUmask());
		homeElement.addContent(getHome());
		passwordElement.addContent(getPassword());

		userNode.addContent(nameElement);
		userNode.addContent(maskElement);
		userNode.addContent(homeElement);
		userNode.addContent(passwordElement);

		//for(File f : getFileSet())
		//	userNode.addContent(f.xmlExport());

		return userNode;
	}
}
