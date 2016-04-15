package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

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


	
}
