package pt.tecnico.myDrive.service;


import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.DirHaveNoContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeCreateFileServiceException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;


public class CreateFileTest extends  AbstractServiceTest {

    private long login;
    private String name = "joao";
    private User userObject;
    private MyDrive md;
    private SuperUser root;
    private String testPlainFileName = "testPlainFile";

    @Override
    protected void populate() {

        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        userObject = new User(md, name);
        login = md.createLogin(name,name);

    }

    @Test(expected = DirHaveNoContentException.class)
    public void createFileDirWithContent() throws Exception {
        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir","ContentOfADir");
        service.execute();
    }


    @Test(expected = InvalidFileTypeCreateFileServiceException.class)
    public void createFileInputFileType() throws Exception{

        CreateFileService service = new CreateFileService(login,"MyDirectory","INVALIDFILETYPE","Content");
        service.execute();

    }

    @Test
    public void createFileCreateDir() {

        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir");
        service.execute();

        assertNotNull("Dir File Not Created", userObject.lookup("/home/joao/MyDirectory"));

    }

    @Test
    public void createFileCreateLinkWithContentProvided() {

        CreateFileService service = new CreateFileService(login,"MyLinkFile","Link","contentlink");
        service.execute();

        Link linkFile = (Link) userObject.lookup("/home/joao/MyLinkFile");

        assertNotNull("Link File Not Created", linkFile);
        assertEquals("Content link should be", "contentlink", linkFile.getContent());
    }

    @Test(expected = )
    public void createFileCreateLinkNoContentProvided() {

        CreateFileService service = new CreateFileService(login,"MyLinkFile","Link");
        service.execute();

    }

    @Test
    public void createFileCreateAppNoContentProvided() {

        CreateFileService service = new CreateFileService(login,"MyAppFile","App");
        service.execute();

        App appFile = (App) userObject.lookup("/home/joao/MyAppFile");

        assertNotNull("App File Not Created", appFile);
        // TODO: What is the app default content in domain?
        assertEquals("Content App should be", "", appFile.getContent());
    }


    @Test
    public void createFileCreateDirR() {

        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir");
        service.execute();

        assertNotNull("Dir File Not Created", userObject.lookup("/home/joao/MyDirectory"));

    }


    @Test(expected = InvalidFileTypeCreateFileServiceException.class)
    public void writeDir() {
        new Dir("DirSetContent",userObject,userObject.getHomeDir(),);
        WriteFileService service = new WriteFileService(login, "DirSetContent","Content to Write on a Dir-File");
        service.execute();

    }

    @Test(expected = NoPermissionException.class)
    public void createFileNotOwnerDirectoryPermission() throws Exception {
        //PlainFile rootPlainFile = new PlainFile("plainfile", root, userObject.getHomeDir(), "rwxd----");
        //rootPlainFile.setContent("CreatedFile");
        //WriteFileService service = new WriteFileService(login, "plainfile","Anything");
        //service.execute();

        CreateFileService service = new CreateFileService(login,"MyPlainFile","Dir");
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
