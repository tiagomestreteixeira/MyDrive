package pt.tecnico.myDrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.*;


public class LoginUserTest extends AbstractServiceTest{

	private MyDrive md;
	private SuperUser root;
	private User user;
	private long loginId;
	
	private static final String USER =  "testUser"; //Password is = user name
	private static final String USERPW = "testUserPW";
	private static final String WRONGPW = "testUserWrongPW";
	private static final String UNKNOWNUSER = "unknownTestUser";
	
	
	@Override
	protected void populate() {
        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        user = new User(md, USER);	
	}
	
	@Test
	public void success(){
		LoginUserService service = new LoginUserService(USER, USERPW);
		service.execute();
		loginId = service.result();
		
		
	}

}
