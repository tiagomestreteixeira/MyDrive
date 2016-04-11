package pt.tecnico.myDrive.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;

public class ReadFileTest extends AbstractServiceTest {

	long token;
	String user;
	User userClass;
	MyDrive md;

	protected void populate() {

		user = "Joao";
		LoginUserService loginUserService = new LoginUserService(user, user);
		token = loginUserService.result();

		md = MyDriveService.getMyDrive();
		userClass = new User(md, user);
		new PlainFile("testFile", userClass, userClass.getHomeDir(), "rwxd----");

	}
	@Test
	public void success() {
		final String filename = "testFile";
		userClass.getFileByName(filename).write(userClass,"abc");

		ReadFileService service = new ReadFileService(token, "test");
		service.execute();
		String result = service.result();

		// check basic read
		assertEquals("Content does not match", "abc", result);
	}

}


