package pt.tecnico.myDrive.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class ReadFileTest extends AbstractServiceTest {

	private long login;
	private String user, rootUser;
	private User userObject;
	private SuperUser root;

	private MyDrive md;

	protected void populate() {

		user = "Joao";
		rootUser = "root";
		LoginUserService loginUserService = new LoginUserService(user, user);
		login = loginUserService.result();
		md = MyDriveService.getMyDrive();
		userObject = new User(md, user);
		root = MyDriveService.getMyDrive().getSuperUser();
		new PlainFile("testFile", userObject, userObject.getHomeDir(), "rwxd----", "abc");

	}
	@Test
	public void basicRead() {

		final String filename = "testFile";

		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
		String result = service.result();

		assertEquals("Content does not match", "abc", result);
	}

	@Test
	public void linkRead() {

		final String filename = "testLink";
		final String linkContent = userObject.getHomeDir().getFileByName(userObject,"testFile").getPath();

		new Link(filename, userObject, userObject.getHomeDir(),"rwxd----", linkContent);

		ReadFileService service = new ReadFileService(login, "testLink");
		service.execute();
		String result = service.result();

		assertEquals("Content does not match", userObject.getHomeDir().getFileByName(userObject,"testFile").read(userObject), result);
	}

	@Test
	public void ReadBlankContent() {

		final String filename = "testFileBlank";

		new PlainFile(filename, userObject, userObject.getHomeDir(), "rwxd----");

		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
		String result = service.result();

		assertEquals("Content does not match", "", result);
	}

	@Test
	public void ReadNotOwner() {

		final String filename = "testFileNoPermissions";

		new PlainFile("testFileNoPermissions", root, userObject.getHomeDir(), "----rwxd");

		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
		String result = service.result();

		assertEquals("Content does not match", "abc", result);
	}

	@Test (expected = NoPermissionException.class)
	public void ReadNoPermissionsOwner() {

		new PlainFile("testFileNoPermissions", userObject, userObject.getHomeDir(), "----rwxd");

		final String filename = "testFileNoPermissions";
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
	}

	@Test (expected = NoPermissionException.class)
	public void ReadNoPermissionsNotOwner() {

		new PlainFile("testFileNoPermissions", root, userObject.getHomeDir(), "rwxd----");

		final String filename = "testFileNoPermissions";
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
	}

	@Test
	public void ReadOverWrittenContent() {

		final String filename = "testFile";
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
		String result = service.result();

		assertEquals("Content does not match", "abc", result);
		userObject.getHomeDir().getFileByName(userObject, filename).write(userObject, "qqq");

		service = new ReadFileService(login, filename);
		service.execute();
		result = service.result();

		assertEquals("Content does not match", "qqq", result);
	}

	@Test(expected = FileDoesNotExistException.class)
	public void ReadNonExistentFile() {
		final String filename = "nullFile";
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
	}

}


