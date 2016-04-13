package pt.tecnico.myDrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;
import pt.tecnico.myDrive.exception.UserPasswordDoesNotMatchException;


public class LoginUserTest extends AbstractServiceTest{

	private MyDrive md;
	private SuperUser root;
	private User user;
	private long loginId1;
	private long loginId2;
	
	private static final String USER =  "testUser"; //Password is = user name
	private static final String USERPW = "testUserPW";
	private static final String WRONGPW = "testUserWrongPW";
	private static final String UNKNOWNUSER = "unknownTestUser";
	
	
	protected void populate() {
        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        user = new User(md, USER);	
	}
	
	@Test
	public void success(){
		LoginUserService service = new LoginUserService(USER, USERPW);
		service.execute();
		loginId1 = service.result();
		
		assertEquals(user, md.getUserFromLoginId(loginId1));
	}

	@Test
	public void LoginTwice(){
		LoginUserService service = new LoginUserService(USER, USERPW);
		service.execute();
		loginId1 = service.result();
		
		service.execute();
		loginId2 = service.result();
		
		assertEquals(user, md.getUserFromLoginId(loginId1));
		assertEquals(user, md.getUserFromLoginId(loginId2));
		assertEquals(md.getUserFromLoginId(loginId1), md.getUserFromLoginId(loginId2));
	}
	
	@Test (expected = UserPasswordDoesNotMatchException.class)
	public void LoginWrongPassword(){
		
		LoginUserService service = new LoginUserService(USER, WRONGPW);
		service.execute();
	}
	
	@Test (expected = UserDoesNotExistException.class)
	public void LoginNoUser(){
		LoginUserService service = new LoginUserService(UNKNOWNUSER, USERPW);
		service.execute();
	}
}
