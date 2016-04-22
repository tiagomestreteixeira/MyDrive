package pt.tecnico.myDrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileCanNotBeRemovedException;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;
import pt.tecnico.myDrive.exception.InvalidLoginTokenException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.fail;

public class DeleteFileTest extends AbstractServiceTest {
	static Logger log = LogManager.getLogger();
	long login;
	Dir subDir;
	User u;

	@Override
	protected void populate() {
		MyDrive md = MyDriveService.getMyDrive();
		u = new User(md, "Ivan");
		Dir home = u.getHomeDir();

		new PlainFile("test", u, home, u.getUmask());
		new Link("link", u, home, u.getUmask(), home.getPath() + "/" + "test");
		Dir dir = new Dir("testDir", u, home, u.getUmask());
		subDir = new Dir("subDir", u, dir, u.getUmask());
		new PlainFile("test", u, subDir, u.getUmask());

		login = md.createLogin(u.getUsername(), u.getPassword());
	}

	@Test (expected = FileDoesNotExistException.class)
	public void success() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "test");
		service.execute();
		u.lookup("/home/Ivan/test");
		fail("Lookup should throw a FileDoesNotExistException.");
	}

	@Test (expected = FileDoesNotExistException.class)
	public void deleteLinkFile() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "link");
		service.execute();
		assertNotNull("File 'test' should exist", u.lookup("/home/Ivan/test"));
		u.lookup("/home/Ivan/link");
		fail("Lookup should throw a FileDoesNotExistException.");
	}

	@Test (expected = FileDoesNotExistException.class)
	public void deleteDirAndSubDirs() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "testDir");
		service.execute();
		u.lookup("/home/Ivan/testDir");
		fail("Lookup should throw a FileDoesNotExistException.");
	}

	@Test(expected = NoPermissionException.class)
	public void deleteFileNoOwnerNoPermission() throws Exception {
		SuperUser root = MyDriveService.getMyDrive().getSuperUser();
		new PlainFile("test1", root, u.getHomeDir(), "rwxd----");
		DeleteFileService service = new DeleteFileService(login, "test1");
		service.execute();
	}

	@Test(expected = NoPermissionException.class)
	public void deleteFileNoPermission() throws Exception {
		new PlainFile("test1", u, u.getHomeDir(), "----rwxd");
		DeleteFileService service = new DeleteFileService(login, "test1");
		service.execute();
	}

	@Test(expected = FileDoesNotExistException.class)
	public void deleteFileNoOwnerWithPermission() throws Exception {
		SuperUser root = MyDriveService.getMyDrive().getSuperUser();
		new PlainFile("test1", root, u.getHomeDir(), "rwxdrwxd");
		DeleteFileService service = new DeleteFileService(login, "test1");
		service.execute();
		root.lookup("/home/Ivan/test1");
		fail("Lookup should throw a FileDoesNotExistException.");
	}

	@Test (expected = NoPermissionException.class)
	public void deleteSubdirWithoutPermission() throws Exception {
		subDir.setPermissions("rwx-rwx-");
		DeleteFileService service = new DeleteFileService(login, "testDir");
		service.execute();
	}

	@Test(expected = FileDoesNotExistException.class)
	public void deleteNonExistingFile() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "test1");
		service.execute();
	}

	@Test(expected = InvalidLoginTokenException.class)
	public void deleteWithInvalidLogin() throws Exception {
		Login l = MyDrive.getInstance().getLoginFromId(login);
		l.setExpirationDate(new DateTime(0));
		DeleteFileService service = new DeleteFileService(login, "test");
		service.execute();
	}

	@Test(expected = FileCanNotBeRemovedException.class)
	public void deleteHomeDirectory() throws Exception {
		Login l = MyDrive.getInstance().getLoginFromId(login);
		l.setCurrentDir(MyDrive.getInstance().getRootDir());
		DeleteFileService service = new DeleteFileService(login, "home");
		service.execute();
	}
}
