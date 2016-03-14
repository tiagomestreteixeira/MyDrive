package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;

public class Dir extends Dir_Base {
    
	
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
			System.out.println(file.getName());
	}
	
	
	public File getFileByName(String name){
		for(File file : getFileSet())
			if(file.getName().equals(name))
				return file;
		return null;
	}
	
	public boolean hasFile(String fileName){
		return getFileByName(fileName) != null;
	}
	
}
