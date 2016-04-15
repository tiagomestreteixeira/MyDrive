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
		PlainFile testPlain2 = new PlainFile("testPlainFile2", userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS, "contentOf:\n\nPlainFile2");
		
		ListDirectoryService service = new ListDirectoryService(loginId);
		service.execute();
		List<FileDto> result = service.result();
		assertEquals("Dir type should be:", "Dir", result.get(2).getType());
		
		assertEquals("First file name should be:", "testPlainFile", result.get(1).getFilename());
		assertEquals("First file permissions should be:", USER_DEFAULT_PERMISSIONS, result.get(1).getPerimissions());
		assertEquals("First file content should be:", "contentOf:\n\nPlainFile", result.get(1).getContent());
		assertEquals("First file type should be:", "PlainFile", result.get(1).getType());

		assertEquals("Second file name should be:", "testPlainFile2", result.get(0).getFilename());
		assertEquals("Second file permissions should be:", USER_DEFAULT_PERMISSIONS, result.get(0).getPerimissions());
		assertEquals("Second file content should be:", "contentOf:\n\nPlainFile2", result.get(0).getContent());
		assertEquals("Second file type should be:", "PlainFile", result.get(0).getType());
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
