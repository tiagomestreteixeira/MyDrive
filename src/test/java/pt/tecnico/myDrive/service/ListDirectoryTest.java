package pt.tecnico.myDrive.service;

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
	
	@Override
	protected void populate() {
		user = "Andre";
		md = MyDriveService.getMyDrive();
		userObject = new User(md, user);
		login = md.createLogin(user,user);
		root = MyDriveService.getMyDrive().getSuperUser();
		Dir dir = new Dir("teste", userObject, userObject.getHomeDir(), userObject.getUmask());
		
	}

}
