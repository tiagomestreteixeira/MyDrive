package pt.tecnico.myDrive.service;

import mockit.Mock;
import mockit.MockUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;
import pt.tecnico.myDrive.exception.InvalidLoginTokenException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import static junit.framework.TestCase.assertEquals;


public class ChangeDirectoryTest extends AbstractServiceTest {

	static Logger log = LogManager.getLogger();
	private long login;
	private String user;
	private String pass;
	private User userObject;
	private SuperUser root;
	private MyDrive md;
	private Dir l3;

	protected void populate() {
		user = "Andre";
		pass = "andreandre";
		md = MyDriveService.getMyDrive();
		userObject = new User(md, user, user, "rwxd----", pass);
		login = md.createLogin(user,pass);
		root = MyDriveService.getMyDrive().getSuperUser();
		Dir l1 = new Dir("level1", userObject, userObject.getHomeDir(), userObject.getUmask());
		Dir l2 = new Dir("level2", userObject, l1, userObject.getUmask());
		l3 = new Dir("level3", userObject, l2, userObject.getUmask());
	}

	@Test
	public void TestEnvironmentLink() {
		final String pathname = "/home/Andre/level1/level2/level3";
		final String pathnameEnvVar = "/home/Andre/level1/$HOME/level3";
		final User u = md.getLoginFromId(login).getUser();

		new MockUp<User>(){
			@Mock
			public File lookup(String pathname) throws MyDriveException {
				return l3;
			}
		};

		ChangeDirectoryService service = new ChangeDirectoryService(login, pathnameEnvVar);
		service.execute();

		String result = service.result();
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test
	public void success() {
		final String pathname = "/home/Andre/level1/level2/level3";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		String result = service.result();
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
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
		String pathname = "/home/Andre/level1/level2";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "././././level3");
		service.execute();
		String result = service.result();
		pathname = "/home/Andre/level1/level2/level3";
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test
	public void changeDirParentPath() {
		String pathname = "/home/Andre/level1/level2";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "..");
		service.execute();
		String result = service.result();
		pathname = "/home/Andre/level1";
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
		assertEquals("Returned path does not match", pathname, result);
	}
	
	@Test
	public void changeDirThroughLink() {
		String pathname = "/home/Andre/level1/level2/level3/link/level1";
		new Link("link", userObject, l3, userObject.getUmask(),"/home/Andre");
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		String result = service.result();
		pathname = "/home/Andre/level1";
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test
	public void changeDirToLink() {
		String pathname = "/home/Andre/level1/level2/level3/link";
		new Link("link", userObject, l3, userObject.getUmask(), "/home/Andre");
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		String result = service.result();
		pathname = "/home/Andre";
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test
	public void changeDirRelativePath() {
		String pathname = "/home/Andre/level1/level2";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "level3");
		service.execute();
		String result = service.result();
		pathname = "/home/Andre/level1/level2/level3";
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test
	public void changeDirParentPathMultiple() {
		String pathname = "/home/Andre/level1/level2/level3";
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		service = new ChangeDirectoryService(login, "../../../../../../../..");
		service.execute();
		String result = service.result();
		pathname = "/";
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
		assertEquals("Returned path does not match", pathname, result);
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
		l3.setPermissions("----rwxd");
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
		assertEquals("Login CurrentDir does not match", pathname, md.getLoginFromId(login).getCurrentDir().getPath());
		assertEquals("Returned path does not match", pathname, result);
	}

	@Test (expected = NoPermissionException.class)
	public void changeDirNoOwnerNoPermission() {
		final String pathname = "/home/Andre/level1/level2/level3/level4";
		l3.setPermissions("--------");
		new Dir("level4", root, l3, "--------");
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
	}

	@Test(expected = InvalidLoginTokenException.class)
	public void changeDirWithInvalidLogin() throws Exception {
		final String pathname = "/home/Andre/level1/level2/level3";
		Login session = md.getLoginFromId(login);
		session.setLoginDate(new DateTime(1));
		ChangeDirectoryService service = new ChangeDirectoryService(login, pathname);
		service.execute();
		assertEquals("Login CurrentDir does not match", userObject.getHomeDir().getPath(), md.getLoginFromId(login).getCurrentDir().getPath());
	}
}


