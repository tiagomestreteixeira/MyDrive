package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidLoginTokenException;

import static junit.framework.TestCase.*;

public class LogoutUserTest extends AbstractServiceTest {

	private long login;
	private MyDrive md;
	private static final String USER =  "testUser";
	private static final long WRONGLOGIN = 123456789;

	protected void populate() {

		md = MyDriveService.getMyDrive();
		new User(md, USER);
		LoginUserService service = new LoginUserService(USER, USER);
		service.execute();
		login = service.result();
	}

	@Test
	public void success(){
		LogoutUserService service = new LogoutUserService(login);
		service.execute();
		assertFalse("Login should not exist", md.loginIdExists(login));
	}

	@Test (expected = InvalidLoginTokenException.class)
	public void logoutTwice(){
		LogoutUserService service = new LogoutUserService(login);
		service.execute();
		LogoutUserService service2 = new LogoutUserService(login);
		service2.execute();
	}

	@Test (expected = InvalidLoginTokenException.class)
	public void logoutWrongLogin(){
		LogoutUserService service = new LogoutUserService(WRONGLOGIN);
		service.execute();
	}

	@Test
	public void logoutGuest(){
		LoginUserService service = new LoginUserService("nobody", null);
		service.execute();
		login = service.result();
		assertTrue("Login exists", md.loginIdExists(login));
		LogoutUserService service2 = new LogoutUserService(login);
		service2.execute();
		assertFalse("Login should not exist", md.loginIdExists(login));
	}

	@Test
	public void logoutRoot(){
		LoginUserService service = new LoginUserService(md.getSuperUser().getName(), "***");
		service.execute();
		login = service.result();
		assertTrue("Login exists", md.loginIdExists(login));
		LogoutUserService service2 = new LogoutUserService(login);
		service2.execute();
		assertFalse("Login should not exist", md.loginIdExists(login));
	}
}
