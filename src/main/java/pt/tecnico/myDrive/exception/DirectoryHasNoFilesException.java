package pt.tecnico.myDrive.exception;

public class DirectoryHasNoFilesException extends MyDriveException{
	private static final long serialVersionUID = 1L;

	
	public DirectoryHasNoFilesException(){
		super("This directory has no files to list.");
	}
	
}
