package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;

public class LogoutUserTest extends AbstractServiceTest {

	private long login;
	private MyDrive md;
	private static final String USER =  "testUser";
	private static final String USERPW = "testUserPW";
	private static final long WRONGLOGIN = 123456789;

	protected void populate() {

		LoginUserService service = new LoginUserService(USER, USERPW);
		service.execute();
		md = MyDriveService.getMyDrive();
		login = service.result();
	}

	@Test
	public void success(){
		//TODO
	}
}
