package pt.tecnico.myDrive.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class ChangeDirectoryTest extends AbstractServiceTest {

	static Logger log = LogManager.getLogger();
	private long login;
	private String user;
	private User userObject;
	private SuperUser root;
	private MyDrive md;
	private Dir l3;

	protected void populate() {
		user = "Andre";
		LoginUserService loginUserService = new LoginUserService(user, user);
		login = loginUserService.result();
		md = MyDriveService.getMyDrive();
		userObject = new User(md, user);
		root = MyDriveService.getMyDrive().getSuperUser();
		Dir l1 = new Dir("level1", userObject, userObject.getHomeDir(), userObject.getUmask());
		Dir l2 = new Dir("level2", userObject, l1, userObject.getUmask());
		l3 = new Dir("level3", userObject, l2, userObject.getUmask());
		log.info(l3.getPath());
	}

	@Test
	public void success() {
		final String pathname = "/home/Andre/level1/level2/level3";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		String result = service.result();
		//Check that current Pathname is updated in domain in the current login session.
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test
	public void changeDirCurrentPath() {
		final String pathname = "/home/Andre/level1/level2";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, ".");
		service.execute();
		String result = service.result();
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test
	public void changeDirCurrentPathWithOther() {
		final String pathname = "/home/Andre/level1/level2";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "./level3");
		service.execute();
		String result = service.result();
		//Check that current Pathname is updated in domain in the current login session.
		assertEquals("Returned path does not match", "/home/Andre/level1/level2/level3", result);
	}

	@Test
	public void changeDirParentPath() {
		final String pathname = "/home/Andre/level1/level2";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "..");
		service.execute();
		String result = service.result();
		//Check that current Pathname is updated in domain in the current login session.
		assertEquals("Returned path does not match", "/home/Andre/level1", result);
	}

	@Test
	public void changeDirRelativePath() {
		final String pathname = "/home/Andre/level1/level2";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "level3");
		service.execute();
		String result = service.result();
		//Check that current Pathname is updated in domain in the current login session.
		assertEquals("Returned path does not match", "/home/Andre/level1/level2/level3", result);
	}

	@Test
	public void changeDirParentPathMultiple() {
		final String pathname = "/home/Andre/level1/level2/level3";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "../../..");
		service.execute();
		String result = service.result();
		//Check that current Pathname is updated in domain in the current login session.
		assertEquals("Returned path does not match", "/home/Andre", result);
	}

	@Test(expected = FileDoesNotExistException.class)
	public void changeDirNonExistingFile() {
		final String pathname = "/home/Andre/level1/level2/null";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
	}

	@Test(expected = NoPermissionException.class)
	public void changeDirOwnerNoPermission() {
		final String pathname = "/home/Andre/level1/level2/level3/level4";
		new Dir("level4", userObject, l3, "----rwxd");
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
	}

	@Test
	public void changeDirNoOwnerPermission() {
		final String pathname = "/home/Andre/level1/level2/level3/level4";
		new Dir("level4", root, l3, "----rwxd");
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		String result = service.result();
		//Check that current Pathname is updated in domain in the current login session.
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test (expected = NoPermissionException.class)
	public void changeDirNoOwnerNoPermission() {
		final String pathname = "/home/Andre/level1/level2/level3/level4";
		new Dir("level4", root, l3, "--------");
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
	}
}


