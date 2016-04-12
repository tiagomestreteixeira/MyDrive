package pt.tecnico.myDrive.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class ReadFileTest extends AbstractServiceTest {

	private long login;
	private String user;
	private User userObject;
	private SuperUser root;
	private MyDrive md;

	protected void populate() {
		md = MyDriveService.getMyDrive();
		user = "Joao";
		userObject = new User(md, user);
		root = MyDriveService.getMyDrive().getSuperUser();
		LoginUserService loginUserService = new LoginUserService(user, user);
		login = loginUserService.result();
		new PlainFile("testFile", userObject, userObject.getHomeDir(), userObject.getUmask(), "abc");

	}

	@Test
	public void success() {
		final String filename = "testFile";
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
		String result = service.result();
		assertEquals("Content does not match", "abc", result);
	}

	@Test
	public void ReadLink() {
		final String filename = "testLink";
		final String linkContent = userObject.getHomeDir().getFileByName(userObject, "testFile").getPath();
		new Link(filename, userObject, userObject.getHomeDir(), userObject.getUmask(), linkContent);
		ReadFileService service = new ReadFileService(login, "testLink");
		service.execute();
		String result = service.result();
		assertEquals("Content does not match", userObject.getHomeDir().getFileByName(userObject, "testFile").read(userObject), result);
	}

	@Test
	public void ReadBlankContent() {
		final String filename = "testFileBlank";
		new PlainFile(filename, userObject, userObject.getHomeDir(), userObject.getUmask());
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

	@Test(expected = NoPermissionException.class)
	public void ReadNoPermissionsOwner() {
		new PlainFile("testFileNoPermissions", userObject, userObject.getHomeDir(), "----rwxd");
		final String filename = "testFileNoPermissions";
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
	}

	@Test(expected = NoPermissionException.class)
	public void ReadNoPermissionsNotOwner() {
		new PlainFile("testFileNoPermissions", root, userObject.getHomeDir(), "rwxd----");
		final String filename = "testFileNoPermissions";
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
	}

	@Test
	public void ReadOverWrittenContent() {
		final String filename = "testFile";
		final String content = "Test 123 /n Test 321";
		userObject.getHomeDir().getFileByName(userObject, filename).write(userObject, content);
		ReadFileService service = new ReadFileService(login, filename);
		service.execute();
		String result = service.result();
		assertEquals("Content does not match", content, result);
	}

}


