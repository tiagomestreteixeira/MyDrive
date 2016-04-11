package pt.tecnico.myDrive.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;

public class ReadFileTest extends AbstractServiceTest {

	private long token;
	private String user;
	private User userClass;
	private MyDrive md;

	protected void populate() {

		user = "Joao";
		LoginUserService loginUserService = new LoginUserService(user, user);
		token = loginUserService.result();
		md = MyDriveService.getMyDrive();
		userClass = new User(md, user);
		new PlainFile("testFile", userClass, userClass.getHomeDir(), "rwxd----");

	}
	@Test
	public void basicRead() {
		final String filename = "testFile";
		userClass.getFileByName(filename).write(userClass,"abc");

		ReadFileService service = new ReadFileService(token, "testFile");
		service.execute();
		String result = service.result();

		// check basic read
		assertEquals("Content does not match", "abc", result);
	}

	@Test
	public void linkRead() {
		String linkContent = userClass.getHomeDir().getFileByName(userClass,"testfile").getPath();
		String filename = "testLink";

		new Link(filename, userClass, userClass.getHomeDir(),"rwxd----", linkContent);

		ReadFileService service = new ReadFileService(token, "testLink");
		service.execute();
		String result = service.result();

		// check link read
		assertEquals("Content does not match", userClass.getHomeDir().getFileByName(userClass,"testfile").read(userClass), result);
	}

	@Test
	public void ReadBlankContent() {
		final String filename = "testFile";
		ReadFileService service = new ReadFileService(token, "test");
		service.execute();
		String result = service.result();

		// check basic read
		assertEquals("Content does not match", "", result);
	}

	/*@Test
	public void ReadNoPermissions() {
		final String filename = "testFile";
		ReadFileService service = new ReadFileService(token, "test");
		service.execute();
		String result = service.result();

		// check basic read
		assertEquals("Content does not match", "", result);
	}*/


}


