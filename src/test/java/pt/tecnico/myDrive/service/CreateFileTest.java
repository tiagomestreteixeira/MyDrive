package pt.tecnico.myDrive.service;


import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

import static junit.framework.TestCase.*;


public class CreateFileTest extends  AbstractServiceTest {

    private long login;
    private String name = "joao";
    private String pass = "joaojoao";
    private User userObject;
    private MyDrive md;
    private SuperUser root;

    @Override
    protected void populate() {
        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        userObject = new User(md, name, name, "rwxd----", pass);
        //userObject = new User(md, name, name, "rwxd----", pass);
        login = md.createLogin(name,pass);
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void createFileAlreadyExists() throws Exception{
        new PlainFile("testCreateAlreadyExistingPlain", userObject, userObject.getHomeDir(), "rwxd----" ,"contentOflainFile");

        CreateFileService service = new CreateFileService(login,"testCreateAlreadyExistingPlain","Plain","Content");
        service.execute();

    }

    @Test(expected = InvalidFileTypeException.class)
    public void createFileInputFileType() throws Exception{

        CreateFileService service = new CreateFileService(login,"MyDirectory","INVALIDFILETYPE","Content");
        service.execute();

    }

    @Test(expected = DirCanNotHaveContentException.class)
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

        String expectedContent = userObject.getHomeDir().getPath();
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

    @Test(expected = FileDoesNotExistException.class)
    public void createFileCreateLinkNoContentProvided() {

        CreateFileService service = new CreateFileService(login,"MyLinkFile","Link");
        service.execute();

    }

    @Test
    public void createFileCreateAppWithContentProvided() {

        String expectedContent = "content.app.main";
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
        CreateFileService service = new CreateFileService(login,"MyPlainFile","Plain",expectedContent);
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
        CreateFileService service = new CreateFileService(login,"MyPlainFile","Plain");
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
        md.getSuperUser().setPermissions(userObject.getHomeDir(),"r-xdr-xd");
        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir");
        service.execute();
    }


    @Test(expected = InvalidLoginTokenException.class)
    public void createWithInvalidLogin() throws Exception {
        Login session = md.getLoginFromId(login);
        session.setLoginDate(new DateTime(1));

        CreateFileService service = new CreateFileService(login,"MyPlainFile","Plain");
        service.execute();
    }


    @Test(expected = InvalidFileNameException.class)
    public void createInvalidFileName() throws Exception {
        final String fileName="example\0";
        CreateFileService service = new CreateFileService(login,fileName,"Dir");
        service.execute();
    }

    @Test(expected = FilePathTooLongException.class)
    public void createPathTooLong() throws Exception {
        final String pathTooLong =
                        "aaaaIIIIaaaaaaaaaIIIaaaaaIIIaaaaaaIIIaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaIIaaaaaIIIIIaaaaaIIIaaaaaaIIIaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaIIaaIIaaIIIaaaaaIIIaaaaaaIIIaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaaIIIaaaaIIIaaaaaIIIIIIIIIIIIaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaaaaaaaaaIIIaaaaaaaaaIIIaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaaaaaaaaaIIIaaaaaaaaaIIIaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaaaaaaaaaIIIaaaaaaaaaIIIaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIIIIaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaIIIaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaIIIaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaIIIaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaIIIaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaIIIaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";


        CreateFileService service = new CreateFileService(login,pathTooLong,"Dir");
        service.execute();

    }


    @Test(expected = MethodNotValidException.class)
    public void createAppWithInvalidContent() {

        String expectedContent = "ala%@34901ç~~~º´´º´?!§_-._-------´´?";
        CreateFileService service = new CreateFileService(login,"MyAppFile","App",expectedContent);
        service.execute();

        try {
            checkFileCreationAndMatchingContentCreated(
                    (App) userObject.getHomeDir().getFileByName(userObject, "MyAppFile"), expectedContent, "App");

        }
        catch (MethodNotValidException e){
            throw e;
        }
        catch(MyDriveException e){
            fail(e.getMessage());
        }
    }
}
