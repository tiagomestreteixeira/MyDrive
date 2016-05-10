package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.InvalidLoginTokenException;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;
import pt.tecnico.myDrive.exception.UserPasswordDoesNotMatchException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


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
	public void success() {
		LoginUserService service = new LoginUserService(USER, USER);
		service.execute();
		loginId1 = service.result();

		assertEquals("Username form login and expected do not match.", user.getUsername(), md.getLoginFromId(loginId1).getUser().getUsername());
	}

	@Test
	public void LoginTwice(){
		LoginUserService service = new LoginUserService(USER, USER);
		service.execute();
		loginId1 = service.result();

		service.execute();
		loginId2 = service.result();

		assertEquals("Username form login and expected do not match.", user.getUsername(), md.getLoginFromId(loginId1).getUser().getUsername());
		assertEquals("Username from second user doens't match the expected.", user.getUsername(), md.getLoginFromId(loginId2).getUser().getUsername());
		assertEquals("Username from login1 doesn't match login2.", md.getLoginFromId(loginId2).getUser().getUsername(), md.getLoginFromId(loginId2).getUser().getUsername());
		assertNotEquals("Login IDs should be different.", loginId1, loginId2);
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

	@Test(expected = InvalidLoginTokenException.class)
	public void testLoginDeletion() throws Exception {
		long id = 0;
		try {
			LoginUserService service = new LoginUserService(USER, USER);
			service.execute();
			id = service.result();
			System.out.println("IDDDDDD" + Long.toString(id));
			Login login = MyDrive.getInstance().getLoginFromId(id);
			login.setLoginDate(new DateTime(0));
			service.execute();
		} catch (Exception e) {
			e.printStackTrace();
			fail("should not fail");
		}
		MyDrive.getInstance().getLoginFromId(id);
	}
}
