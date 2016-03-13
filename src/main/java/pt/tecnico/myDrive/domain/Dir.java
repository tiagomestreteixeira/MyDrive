package pt.tecnico

import pt.tecnico.myDrive.exception.FileAlreadyExistsException;


public class Dir extends Dir_Base {
    
	
    public Dir(){
		super();
		init("/", "root", this, "rwxd----");
    }
	
	public Dir(String name, String user, Dir directory, String permissions){
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
		
	}
	
	
	public File getFileByName(String name){
		for(File file: getFileSet())
			if(file.getName().equals(name))
				return file;
		return null;
	}
	
	public boolean hasFile(String fileName){
		return getFileByName(fileName) != null;
	}
	
}
