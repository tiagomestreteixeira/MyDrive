package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;

import static junit.framework.TestCase.assertEquals;

public class WriteFileTest extends AbstractServiceTest {

    private long token;
    private String name;
    private User userClass;
    private MyDrive md;

    private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

    protected void populate() {

        name = "Joana";
        LoginUserService loginUserService = new LoginUserService(name, name);
        token = loginUserService.result();
        md = MyDriveService.getMyDrive();
        userClass = new User(md, name);

        new PlainFile("testEmptyPlainFile", userClass, userClass.getHomeDir(), USER_DEFAULT_PERMISSIONS,"");
        new PlainFile("testPlainFileWithOneLineContent",
                userClass, userClass.getHomeDir(), USER_DEFAULT_PERMISSIONS,"contentTestFileOfOneLine");
        new PlainFile("testPlainFileWithMultiLineContent",
                userClass, userClass.getHomeDir(), USER_DEFAULT_PERMISSIONS,
                "contentTestFileOfMultiLine\nLine1\nLine2\nLine4");

        new App("testApp",userClass,userClass.getHomeDir(),USER_DEFAULT_PERMISSIONS);



        MyDriveService.getUser("");
    }


    @Test
    public void basicWrite() {
        String filename = "testFile";
        userClass.getFileByName(filename).write(userClass,"abc");

        WriteFileTest service = new WriteFileTest(token, "testFile");
        service.execute();
        String result = service.result();

        // check basic read
        assertEquals("Content does not match", "abc", result);
    }

    /*@Test
    public void linkRead() {


        String linkContent = userClass.getHomeDir().getFileByName(userClass,"testfile").getPath();
        String filename = "testLink";



        new Link(filename, userClass, userClass.getHomeDir(),"rwxd----", linkContent);


        ReadFileService service = new ReadFileService(token, "testLink");
        service.execute();
        String result = service.result();

        // check link read
        assertEquals("Content does not match", userClass.getHomeDir().getFileByName(userClass,"testfile").read(userClass), result);

    }*/

    //@Test
    //public void ReadBlankContent() {
        //final String filename = "testFile";
        //ReadFileService service = new ReadFileService(token, "test");
        //service.execute();
        //String result = service.result();

        // check basic read
        //assertEquals("Content does not match", "", result);
    //}

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
