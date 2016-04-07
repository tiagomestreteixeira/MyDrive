package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;

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
	public void remove() throws MyDriveException {
		for (User u : getUserSet())
			u.removeFile(this);
		for (Dir d : getDirSet())
			d.removeFile(this);
		getFileSet().forEach(File::remove);
		if (getHomeOwner() != null){
			//Do not allow?
			setHomeOwner(null);
		}
		deleteDomainObject();
	}

	@Override
	public String getFormatedName() {
		return "Dir " + getPermissions() + " " + getFileOwner().getName() +  " " + getId() + " " + getName();
	}

	public void xmlImport(Element dirElement) throws ImportDocumentException {
		xmlImport(dirElement,"Dir");
	}

}
