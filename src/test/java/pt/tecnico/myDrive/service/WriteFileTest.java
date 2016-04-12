package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.DirHaveNoContentException;
import pt.tecnico.myDrive.exception.FileDoesNotExistException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import static junit.framework.TestCase.assertEquals;

public class WriteFileTest extends AbstractServiceTest {

    private long login;
    private String name = "joao";
    private User userObject;
    private MyDrive md;
    private SuperUser root;
    private String testPlainFileName = "testPlainFile";

    private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

    protected void populate() {

        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();

        userObject = new User(md, name);

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
    public void writeEmptyFile() {
        new PlainFile("testEmptyPlainFile", userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS,"");
        WriteFileService service = new WriteFileService(login, "testEmptyPlainFile","ReplaceText");
        service.execute();
        PlainFile pf = (PlainFile) userObject.getHomeDir().getFileByName(userObject,testPlainFileName);
        assertEquals("Content was not written to file", "ReplaceTex", pf.getContent());
    }

    @Test
    public void writeBasic() {
        WriteFileService service = new WriteFileService(login, testPlainFileName,"ReplaceText");
        service.execute();
        PlainFile pf = (PlainFile) userObject.getHomeDir().getFileByName(userObject,testPlainFileName);
        assertEquals("Content was not written to file", "ReplaceTex", pf.getContent());
    }


    @Test
    public void writeDouble() {
        WriteFileService service = new WriteFileService(login, testPlainFileName,"ReplaceTextOne");
        service.execute();
        service = new WriteFileService(login, testPlainFileName,"ReplaceTextTwo");
        service.execute();
        PlainFile pf = (PlainFile) userObject.getHomeDir().getFileByName(userObject,testPlainFileName);
        assertEquals("Content was not written to file", "ReplaceTextTwo", pf.getContent());
    }


    @Test(expected = DirHaveNoContentException.class)
    public void writeDir() {
        new Dir("DirSetContent",userObject,userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS);
        WriteFileService service = new WriteFileService(login, "DirSetContent","Content to Write on a Dir-File");
        service.execute();

    }

    @Test(expected = NoPermissionException.class)
    public void writeNotOwnerNoPermission() throws Exception {
        PlainFile rootPlainFile = new PlainFile("plainfile", root, userObject.getHomeDir(), "rwxd----");
        rootPlainFile.setContent("CreatedFile");
        WriteFileService service = new WriteFileService(login, "plainfile","Anything");
        service.execute();
    }

    @Test(expected = NoPermissionException.class)
    public void writeIsOwnerNoPermission() throws Exception {
        new PlainFile("plainfile", userObject, userObject.getHomeDir(), "----rwxd");
        WriteFileService service = new WriteFileService(login, "plainfile","Anything");
        service.execute();
    }

    @Test
    public void writeNotOwnerHavePermission() {

        PlainFile pf = new PlainFile("testFileNotOwner", root, userObject.getHomeDir(), "rwxdrwxd");
        pf.setContent("Content Of File\n NotBeing The Owner");
        WriteFileService service = new WriteFileService(login, "testFileNotOwner","I\nCan\nChange");
        service.execute();
        assertEquals("Content was not written to file", "I\nCan\nChange", pf.getContent());
    }

}
