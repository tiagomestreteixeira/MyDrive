package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;

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


	@Override
	public Element xmlExport(){
		Element dirElement =  new Element("dir");
		dirElement = xmlExportHelper(dirElement);
		return dirElement;

	}

}
