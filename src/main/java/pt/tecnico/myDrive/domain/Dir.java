package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class Dir extends Dir_Base {

	public static Dir getRootDir() {
		SuperUser root = SuperUser.getInstance();

		Dir rootDir = (Dir) root.getFileByName("/");

		if (rootDir == null) {
			rootDir = new Dir();
			rootDir.init("/", root, rootDir, "rwxdr-x-");
		}

		return rootDir;
	}
	
    public Dir(){
		super();
    }
	
	public Dir(String name, User user, Dir directory, String permissions){
		super();
		init(name, user, directory, permissions);
	}
	
	@Override
	public void addFile(File fileToBeAdded) throws FileAlreadyExistsException {
		if(hasFile(fileToBeAdded.getName()))
			throw new FileAlreadyExistsException(fileToBeAdded.getName());
		
		super.addFile(fileToBeAdded);
	}
	
	@Override
	public void removeFile(File fileToBeRemoved) throws FileDoesNotExistException {
		if(!hasFile(fileToBeRemoved.getName()))
			throw new FileDoesNotExistException(fileToBeRemoved.getName());
		
		super.removeFile(fileToBeRemoved);
	}

	public void listFileSet(){
		for(File file : getFileSet())
			System.out.println(file.getFormatedName());
	}
	
	@Override
	public File getFileByName(User user, String name){
		if (user.checkPermission(this, 'x')) {
			for(File file : getFileSet())
				if(file.getName().equals(name))
					return file;
			return null;
		} else {
			throw new NoPermissionException("getFileByName");
		}
	}
	
	public boolean hasFile(String fileName){
		for(File file : getFileSet())
			if(file.getName().equals(fileName))
				return true;
		return false;
	}

	@Override
	public Element xmlExport(){
		Element dirElement =  new Element("dir");
		dirElement = xmlExportHelper(dirElement);
		return dirElement;
	}

	@Override
	public void remove() {
		for (User u : getUserSet())
			u.removeFile(this);
		for (Dir d : getDirSet())
			d.removeFile(this);
		getFileSet().forEach(File::remove);
		deleteDomainObject();
	}

	@Override
	public String getFormatedName() {
		return "Dir " + getPermissions() + " " + getFileOwner().getName() +  " " + getId() + " " + getName();
	}

	public void xmlImport(Element dirElement) throws ImportDocumentException {

		String path,
				name,
				ownerUsername,
				defaultPermissions;

		path = name = ownerUsername = defaultPermissions = null;

		for (Element child : dirElement.getChildren()) {

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
			throw new ImportDocumentException("Dir", "<path> node cannot be read properly.");
		if (name == null)
			throw new ImportDocumentException("Dir", "<name> node cannot be read properly.");
		if (ownerUsername == null)
			ownerUsername = "root";

		User owner = MyDrive.getInstance().getUserByUsername(ownerUsername);

		if (defaultPermissions == null) {
			if (owner == null) {
				owner = MyDrive.getInstance().getUserByUsername("root");
			}
		}

		/*setId(MyDrive.getInstance().getNewId());
		Dir d = owner.makeDir(path);
		defaultPermissions = owner.getUmask();
		setName(name);
		setPermissions(defaultPermissions);
		setOwner(owner);
		addDir(d);*/

		setId(MyDrive.getInstance().getNewId());
		setName(name);
		setPermissions(defaultPermissions);
		setOwner(owner);
		//setContent(contents);
		addDir((Dir) SuperUser.getInstance().makeDir(path));
	}

}
