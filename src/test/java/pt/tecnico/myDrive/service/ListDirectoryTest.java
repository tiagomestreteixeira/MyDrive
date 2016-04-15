package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;
import pt.tecnico.myDrive.service.dto.FileDto;

public class ListDirectoryTest extends AbstractServiceTest{

	static Logger log = LogManager.getLogger();
	private long loginId;
	private User userObject;
	private MyDrive md;
	private Login login;
	private ArrayList<FileDto> testFileList = new ArrayList<FileDto>();
	private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";


	@Override
	protected void populate() {
		md = MyDriveService.getMyDrive();
		userObject = new User(md, "Andre");
		loginId = md.createLogin("Andre", "Andre");
		login = MyDriveService.getMyDrive().getLoginFromId(loginId);
	}

	@Test
	public void success(){
		PlainFile testPlain = new PlainFile("testPlainFile", userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS, "contentOf:\n\nPlainFile");
		Link testLink = new Link("testLink", userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS, testPlain.getPath());
		Dir testDir = new Dir("testDir", userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS);
		App testApp = new App("testApp", userObject, userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS);

		
		ListDirectoryService service = new ListDirectoryService(loginId);
		service.execute();
		List<FileDto> result = service.result();
		log.info(result.size());

		assertTrue("Size should be 4", result.size() == 4);
		assertEquals("First file name should be:", "testApp", result.get(0).getFilename());
		assertEquals("Second file name should be:", "testDir", result.get(1).getFilename());
		assertEquals("Third file name should be:", "testLink", result.get(2).getFilename());
		assertEquals("Fourth file name should be:", "testPlainFile", result.get(3).getFilename());

		assertEquals("First file permissions should be:", USER_DEFAULT_PERMISSIONS, result.get(0).getPerimissions());
		assertEquals("Second file permissions should be:", USER_DEFAULT_PERMISSIONS, result.get(1).getPerimissions());
		assertEquals("Third file permissions should be:", USER_DEFAULT_PERMISSIONS, result.get(2).getPerimissions());
		assertEquals("Fourth file permissions should be:", USER_DEFAULT_PERMISSIONS, result.get(3).getPerimissions());

		assertEquals("First file type should be:", "App", result.get(0).getType());
		assertEquals("Second file type should be:", "Dir", result.get(1).getType());
		assertEquals("Third file type should be:", "Link", result.get(2).getType());
		assertEquals("Fourth file type should be:", "PlainFile", result.get(3).getType());

		assertEquals("Third file content should be:", testPlain.getPath(), result.get(2).getContent());
		assertEquals("Fourth file content should be:", "contentOf:\n\nPlainFile", result.get(3).getContent());
	}

	
	@Test(expected=DirectoryHasNoFilesException.class)
	public void listEmptyDirectory(){
		final String pathname = "/home/Andre/Empty";
		new Dir("Empty", userObject, userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS );
		changeDir(loginId, pathname);
		ListDirectoryService service = new ListDirectoryService(loginId);
		service.execute();		
	}

	@Test(expected=NoPermissionException.class)
	public void noPermissionDirectoryList(){
		final String pathname = "/home/Andre/NoPerm";
		new Dir("NoPerm", userObject, userObject.getHomeDir(), "--------");
		changeDir(loginId, pathname);
		ListDirectoryService service = new ListDirectoryService(loginId);
		service.execute();
	}

	@Test(expected=FileDoesNotExistException.class)
	public void NonExistantDirectoryList(){
		final String pathname = "/home/Andre/NA";
		changeDir(loginId, pathname);
		ListDirectoryService service = new ListDirectoryService(loginId);
		service.execute();
	}

	public void changeDir(long loginId, String pathname){
		
		login.refreshToken();
		User user = login.getUser();
		Dir currentDir = login.getCurrentDir();

		if (!pathname.startsWith("/"))
			pathname = currentDir.getPath() + "/" + pathname;

		Dir d = (Dir) user.lookup(pathname);
		login.setCurrentDir(d);
	}
}
