package pt.tecnico.myDrive.service;


import junit.framework.Assert;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

import static junit.framework.TestCase.*;


public class CreateFileTest extends  AbstractServiceTest {

    private long login;
    private String name = "joao";
    private User userObject;
    private MyDrive md;
    private SuperUser root;

    @Override
    protected void populate() {
        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        userObject = new User(md, name);
        login = md.createLogin(name,name);

    }

    @Test(expected = FileAlreadyExistsException.class)
    public void createFileAlreadyExists() throws Exception{
        new PlainFile("testCreateAlreadyExistingPlain", userObject, userObject.getHomeDir(), "rwxd----" ,"contentOflainFile");

        CreateFileService service = new CreateFileService(login,"testCreateAlreadyExistingPlain","Plain","Content");
        service.execute();

    }

    @Test(expected = InvalidFileTypeCreateFileServiceException.class)
    public void createFileInputFileType() throws Exception{

        CreateFileService service = new CreateFileService(login,"MyDirectory","INVALIDFILETYPE","Content");
        service.execute();

    }

    @Test(expected = DirHaveNoContentException.class)
    public void createFileDirWithContent() throws Exception {

        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir","ContentOfADir");
        service.execute();

    }

    @Test
    public void createFileCreateDir() {

        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir");
        service.execute();

        try {
            assertNotNull("Dir File Not Created", userObject.getHomeDir().getFileByName(userObject, "MyDirectory"));
        }catch(MyDriveException e){
            fail(e.getMessage());
        }
    }


    public void checkFileCreationAndMatchingContentCreated(PlainFile plainFile,
                                                           String contentExpected,
                                                           String fileType){
        assertNotNull(fileType + " File Not Created", plainFile);
        assertEquals("Content of " + fileType + " file should be ", contentExpected, plainFile.getContent());
    }

    @Test
    public void createFileCreateLinkWithContentProvided() {

        String expectedContent = "contentlink";
        CreateFileService service = new CreateFileService(login,"MyLinkFile","Link",expectedContent);
        service.execute();

        try {
            checkFileCreationAndMatchingContentCreated(
                    (Link) userObject.getHomeDir().getFileByName(userObject, "MyLinkFile"), expectedContent, "Link");
        }
        catch(MyDriveException e){
            fail(e.getMessage());
        }
    }

    @Test(expected = MissingArgumentsServiceException.class)
    public void createFileCreateLinkNoContentProvided() {

        CreateFileService service = new CreateFileService(login,"MyLinkFile","Link");
        service.execute();

    }

    @Test
    public void createFileCreateAppWithContentProvided() {

        String expectedContent = "contentapp";
        CreateFileService service = new CreateFileService(login,"MyAppFile","App",expectedContent);
        service.execute();

        try {
            checkFileCreationAndMatchingContentCreated(
                    (App) userObject.getHomeDir().getFileByName(userObject, "MyAppFile"), expectedContent, "App");

        }
        catch(MyDriveException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void createFileCreateAppNoContentProvided() {

        String expectedContent = "";
        CreateFileService service = new CreateFileService(login,"MyAppFile","App");
        service.execute();

        try{
            checkFileCreationAndMatchingContentCreated(
                (App) userObject.getHomeDir().getFileByName(userObject,"MyAppFile"), expectedContent, "App");
        }
        catch(MyDriveException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void createFileCreatePlainWithContentProvided() {

        String expectedContent = "contentplain";
        CreateFileService service = new CreateFileService(login,"MyPlainFile","MyPlainFile",expectedContent);
        service.execute();

        try{
            checkFileCreationAndMatchingContentCreated(
                (PlainFile) userObject.getHomeDir().getFileByName(userObject,"MyPlainFile"), expectedContent, "Plain");
        }
        catch(MyDriveException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void createFileCreatePlainWithNoContentProvided() {

        String expectedContent = "";
        CreateFileService service = new CreateFileService(login,"MyPlainFile","MyPlainFile");
        service.execute();

        try {
            checkFileCreationAndMatchingContentCreated(
                    (PlainFile) userObject.getHomeDir().getFileByName(userObject, "MyPlainFile"), expectedContent, "Plain");
        }
        catch(MyDriveException e){
            fail(e.getMessage());
        }
    }


    @Test(expected = NoPermissionException.class)
    public void createFileOwnerDirectoryNoPermission() {
        userObject.getHomeDir().setPermissions("r-xdr-xd");
        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir");
        service.execute();
    }

}
