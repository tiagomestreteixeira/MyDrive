package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;

import java.util.Set;

public class Dir extends Dir_Base {

    public Dir(){
		super();
    }

	public Dir(String name, User user, Dir directory, String permissions){
		super();
		init(name, user, directory, permissions);
	}

	public Dir(Element dirElement) throws ImportDocumentException {
		xmlImport(dirElement,"Dir");
	}

	@Override
	protected void init(String name, User user, Dir directory, String permissions) throws MyDriveException {
		super.init(name, user, directory, permissions);
		new Link(".", user, this, permissions, this.getPath());
		new Link("..", user, this, permissions, this.getDir().getPath());
	}

	@Override
	public File lookup(User user, String path) {
		log.debug("Lookup Dir");
		log.debug("Path: " + path);

		if (path.equals("")) {
			log.debug("path empty");
			return this;
		}

		String[] split = path.split("/", 2);
		File f = getFileByName(user, split[0]);
		log.debug("Split Length: " + split.length);
		log.debug("Split 0: " + split[0]);
		if (split.length == 1) {
			log.debug("I'msafhadskuofhahoadshfgliusdhipudhvi ipuahavhd pupo");
			return getFileByName(user, split[0]).lookup(user, "");
		}
		return f.lookup(user, split[1]);
	}

	@Override
	public int getSize() {
		return getFileSet().size();
	}

	public Set<File> getFileSet(User user) {
		if (user.checkPermission(this, 'r')) {
			return super.getFileSet();
		} else {
			throw new NoPermissionException("Dir.getFileSet()");
		}
	}

	@Override
	public void delete(User user) throws MyDriveException {
		if (getPath().equals("/") || getPath().equals("/home"))
			throw new FileCanNotBeRemovedException(getPath());
		if (user.checkPermission(this, 'd')){
			for (File f : getFileSet()) {
				f.delete(user);
			}
			this.remove();
		}
		else
			throw new NoPermissionException("Dir.delete()");
	}

	@Override
	public void addFile(File fileToBeAdded) throws FileAlreadyExistsException {
		if(hasFile(fileToBeAdded.getName()))
			throw new FileAlreadyExistsException(fileToBeAdded.getName());
		this.setLastModification(new DateTime());
		super.addFile(fileToBeAdded);
	}

	@Override
	public void removeFile(File fileToBeRemoved) throws FileDoesNotExistException {
		if(!hasFile(fileToBeRemoved.getName()))
			throw new FileDoesNotExistException(fileToBeRemoved.getName());
		this.setLastModification(new DateTime());
		super.removeFile(fileToBeRemoved);
	}

	@Override
	public File getFileByName(User user, String name){
		if (user.checkPermission(this, 'x')) {
			if (name.equals("."))
				return this;
			if (name.equals(".."))
				return getDir();

			for(File file : getFileSet())
				if(file.getName().equals(name))
					return file;
			throw new FileDoesNotExistException(name);
		} else {
			throw new NoPermissionException("Dir.getFileByName()");
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
	protected void remove() throws MyDriveException {
		getUser().removeFile(this);
		getDir().removeFile(this);
		if (getHomeOwner() != null){
			//Do not allow?
			setHomeOwner(null);
		}
		deleteDomainObject();
	}

	@Override
	public String getType(){
		return "Dir";
	}
}
