package pt.tecnico.myDrive.service;


import org.junit.Test;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.DirHaveNoContentException;


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
    public void createFileDirWithContent throws Exception {
        CreateFileService service = new CreateFileService(login,"MyDirectory","Dir","ContentOfADir");
        service.execute();
    }

    @Test(expected = DirHaveNoContentException.class)
    public void writeDir() {

        WriteFileService service = new WriteFileService(login, "DirSetContent","Content to Write on a Dir-File");
        service.execute();

    }

}
