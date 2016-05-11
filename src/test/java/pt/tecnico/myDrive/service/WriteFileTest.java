package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;

public class WriteFileTest extends AbstractServiceTest {

    private long login;
    private String name = "joao";
    private String pass = "joaojoao";
    private User userObject;
    private MyDrive md;
    private SuperUser root;
    private String testPlainFileName = "testPlainFile";
    private App myApp;

    private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

    protected void populate() {

        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();

        userObject = new User(md, name, name, "rwxd----", pass);

        new PlainFile(testPlainFileName, userObject, userObject.getHomeDir(), USER_DEFAULT_PERMISSIONS,"contentOf:\n\nPlainFile");

        myApp = new App("MyAppFile", userObject, userObject.getHomeDir(),USER_DEFAULT_PERMISSIONS);
        myApp.setContent("pt.mydrive.myapp");

        login = md.createLogin(name,pass);

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
        PlainFile pf = (PlainFile) userObject.getHomeDir().getFileByName(userObject,"testEmptyPlainFile");
        assertEquals("Content was not written to file", "ReplaceText", pf.getContent());
    }

    @Test
    public void writeBasic() {
        WriteFileService service = new WriteFileService(login, testPlainFileName,"ReplaceText");
        service.execute();
        PlainFile pf = (PlainFile) userObject.getHomeDir().getFileByName(userObject,testPlainFileName);
        assertEquals("Content was not written to file", "ReplaceText", pf.getContent());
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


    @Test(expected = NoPermissionException.class)
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

    @Test(expected = InvalidLoginTokenException.class)
    public void writeWithInvalidLogin() throws Exception {

        Login session = md.getLoginFromId(login);
        session.setLoginDate(new DateTime(1));

        PlainFile pf = new PlainFile("testFile", userObject, userObject.getHomeDir(), "rwxdrwxd","OriginalContent");
        WriteFileService service = new WriteFileService(login, "testFile", "ChangeContent");
        service.execute();
    }

    @Test
    public void writeOnLink() {
        PlainFile pf = new PlainFile("plaindestinationfile", userObject, userObject.getHomeDir(), userObject.getUmask(), "aa");
        new Link("testFile", userObject, userObject.getHomeDir(), userObject.getUmask(), pf.getPath());
        WriteFileService service = new WriteFileService(login, "testFile","bb");
        service.execute();

        assertEquals("Content was not written to file", "bb", pf.getContent());
    }

    @Test(expected = MethodNotValidException.class)
    public void writeAppInvalidContent() {

        String appContent = "ala%@34901ç~~~º´´º´?!§_-._-------´´?";

        WriteFileService service = new WriteFileService(login, "MyAppFile", appContent);
        service.execute();
    }

    @Test
    public void writeAppValidContent() {
        WriteFileService service = new WriteFileService(login, "MyAppFile", "edu.mit.mydrive");
        service.execute();

        assertEquals("Content was not written to App", "edu.mit.mydrive", myApp.getContent());
    }

}
