package pt.tecnico.myDrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

public class DeleteFileTest extends AbstractServiceTest {
	static Logger log = LogManager.getLogger();
	long login;
	User u;

	@Override
	protected void populate() {
		MyDrive md = MyDriveService.getMyDrive();
		u = new User(md, "Ivan");
		Dir home = u.getHomeDir();

		new PlainFile("test", u, home, u.getUmask());
		new Link("link", u, home, u.getUmask(), home.getPath() + "/" + "test");
		Dir dir = new Dir("testDir", u, home, u.getUmask());
		Dir subDir = new Dir("subDir", u, dir, u.getUmask());
		new PlainFile("test", u, subDir, u.getUmask());

		login = md.createLogin(u.getUsername(), u.getPassword());
	}

	@Test
	public void success() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "test");
		service.execute();
		assertNull("File should not exist", u.lookup("/home/Ivan/test"));
	}

	@Test
	public void deleteLinkFile() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "link");
		service.execute();
		assertNull("File 'link' should not exist", u.lookup("/home/Ivan/link"));
		assertNotNull("File 'test' should exist", u.lookup("/home/Ivan/test"));
	}

	@Test
	public void deleteDirAndSubDirs() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "testDir");
		service.execute();
		assertNull("testDir should not exist", u.lookup("/home/Ivan/testDir"));
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

	@Test
	public void deleteFileNoOwnerWithPermission() throws Exception {
		SuperUser root = MyDriveService.getMyDrive().getSuperUser();
		new PlainFile("test1", root, u.getHomeDir(), "rwxdrwxd");
		DeleteFileService service = new DeleteFileService(login, "test1");
		service.execute();
		assertNull("File 'test1' should not exist", root.lookup("/home/Ivan/test1"));
	}

	@Test(expected = FileDoesNotExistException.class)
	public void deleteNonExistingFile() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "test1");
		service.execute();
	}
}
