package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.DirHaveNoContentException;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;

import static junit.framework.TestCase.assertEquals;

public class WriteFileTest extends AbstractServiceTest {

    private long login;
    private String name = "joao";
    private User userObject;
    private MyDrive md;

    private String testPlainFileName = "testPlainFile";

    private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

    protected void populate() {

        md = MyDriveService.getMyDrive();
        userObject = new User(md, name);

        new Dir("DirSetContent",userObject,userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS);

        new PlainFile("testEmptyPlainFile", userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS,"");
        new PlainFile(testPlainFileName, userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS,"contentOf:\n\nPlainFile");

        new Link("testLinkFile", userObject, userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS,"contentOfLink");
        new App("testAppFile", userObject, userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS).setContent("contentOfApp");

        LoginUserService loginUserService = new LoginUserService(name, name);
        login = loginUserService.result();

    }

    @Test(expected = FileDoesNotExistException.class)
    public void writeNonExistingFile() throws Exception {

        WriteFileService service = new WriteFileService(login, "filenameUnexisting","Content to Write on Non-Existing File");
        service.execute();

    }


    @Test
    public void basicWrite() {

        WriteFileService service = new WriteFileService(login, testPlainFileName,"ReplaceText");
        service.execute();

        PlainFile pf = (PlainFile) userObject.lookup(userObject.getHomeDir().getPath()+testPlainFileName);
        assertEquals("Content was not written to file", "ReplaceTex", pf.getContent());
    }


    @Test
    public void doubleWrite() {

        WriteFileService service = new WriteFileService(login, testPlainFileName,"ReplaceTextOne");
        service.execute();

        service = new WriteFileService(login, testPlainFileName,"ReplaceTextTwo");
        service.execute();

        PlainFile pf = (PlainFile) userObject.lookup(userObject.getHomeDir().getPath()+testPlainFileName);
        assertEquals("Content was not written to file", "ReplaceTextTwo", pf.getContent());
    }


    @Test(expected = DirHaveNoContentException.class)
    public void dirWrite() {

        WriteFileService service = new WriteFileService(login, "DirSetContent","Content to Write on a Dir-File");
        service.execute();

    }

    //@Test
    //public void ReadBlankContent() {
        //final String filename = "testFile";
        //ReadFileService service = new ReadFileService(login, "test");
        //service.execute();
        //String result = service.result();

        // check basic read
        //assertEquals("Content does not match", "", result);
    //}

	/*@Test
	public void ReadNoPermissions() {
		final String filename = "testFile";
		ReadFileService service = new ReadFileService(login, "test");
		service.execute();
		String result = service.result();

		// check basic read
		assertEquals("Content does not match", "", result);
	}*/


}
