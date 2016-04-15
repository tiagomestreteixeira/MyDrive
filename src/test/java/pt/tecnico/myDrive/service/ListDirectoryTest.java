package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

public class ListDirectoryTest extends AbstractServiceTest{

	static Logger log = LogManager.getLogger();
	private long login;
	private String user;
	private User userObject;
	private SuperUser root;
	private MyDrive md;
	private String testFile = "testPlainFile";
	private String testString;
	private ArrayList<String> fileList = new ArrayList<String>();
	private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

	
	@Override
	protected void populate() {
		user = "Andre";
		md = MyDriveService.getMyDrive();
		userObject = new User(md, user);
		login = md.createLogin(user,user);
		root = MyDriveService.getMyDrive().getSuperUser();
		Dir dir = new Dir("teste", userObject, userObject.getHomeDir(), userObject.getUmask());
		PlainFile test = new PlainFile(testFile, userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS, "contentOf:\n\nPlainFile");
		
		testString = "Dir " + test.getPermissions() + " " + test.getFileOwner() + " " + test.getId() + " " + test.getName();
	}

	@Test
	public void success(){
		final String pathname = "/home/Andre/";
		fileList.add(testString);
		ListDirectoryService service = new ListDirectoryService(login, pathname);
		service.execute();
		ArrayList<String> result = service.result();
		assertEquals("Directory listing is not the same", result, fileList);
	}
	
	@Test(expected=DirectoryHasNoFilesException.class)
	public void listEmptyDirectory(){
		final String pathname = "/home/Andre/Empty";
		new Dir("Empty", userObject, userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS );
		ListDirectoryService service = new ListDirectoryService(login, pathname);
		service.execute();		
	}
	
	@Test(expected=NoPermissionException.class)
	public void noPermissionDirectoryList(){
		final String pathname = "/home/Andre/NoPerm";
		new Dir("NoPerm", userObject, userObject.getHomeDir(), "--------");
		ListDirectoryService service = new ListDirectoryService(login, pathname);
		service.execute();
	}
	
	@Test(expected=FileDoesNotExistException.class)
	public void NonExistantDirectoryList(){
		final String pathname = "/home/Andre/NA";
		ListDirectoryService service = new ListDirectoryService(login, pathname);
		service.execute();
	}
}
