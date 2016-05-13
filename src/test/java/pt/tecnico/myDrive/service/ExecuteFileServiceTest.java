package pt.tecnico.myDrive.service;

import org.junit.Ignore;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.NoPermissionException;


public class ExecuteFileServiceTest extends AbstractServiceTest {

	private static final String DEFAULT_APP = "pt.tecnico.myDrive.presentation.Hello";

	private long loginTest;
	private long loginTestFail;
    private User user;
    private User userNoExe;
    private MyDrive md;
    private SuperUser root;
    private String[] args;
    
	@Override
	protected void populate() {
		md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        user = new User(md, "test", "test", "rwxd----", "testpw12");
        userNoExe = new User(md, "testfail", "testfail", "rw-d----", "testfailpw");
        loginTest = md.createLogin("test", "testpw12");
        loginTestFail = md.createLogin("testfail", "testfailpw");
	}

	@Test
	public void executePlainFile() throws Exception {
		App app = new App("testApp", user, user.getHomeDir(), user.getUmask(), DEFAULT_APP);
		new PlainFile("testExecutePlainFile", user, user.getHomeDir(), "rwxd----", app.getPath());

		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/testExecutePlainFile", args);
		efs.execute();
	}

	@Ignore // TODO: Fix test to set content to App
	@Test
	public void executeAppFile() throws Exception{
		new App("testExecuteApp", user, user.getHomeDir(), "rwxd----");
		
		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/testExecuteApp", args);
		efs.execute();
	}

	@Ignore // TODO: Fix test to set content to App
	@Test
	public void executeLinkFile() throws Exception{
		new App("testExecuteApp", user, user.getHomeDir(), "rwxd----");
		new Link("linkExecuteFile", user, user.getHomeDir(), "rwxd----", "/home/test/testExecuteApp");
		
		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/linkExecuteFile", args);
		efs.execute();
	}
	
	@Test (expected = NoPermissionException.class)
	public void executeDirFile() throws Exception{
		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test", args);
		efs.execute();
	}
	
	@Test (expected = NoPermissionException.class)
	public void executeLinkNoPermission() throws Exception{
		new App("testExecuteApp", userNoExe, userNoExe.getHomeDir(), "rwxd----");
		new Link("linkExecuteNoPermission", userNoExe, userNoExe.getHomeDir(), "rwxd----", "/home/test/testExecuteApp");
		
		ExecuteFileService efs = new ExecuteFileService(loginTestFail, "/home/testfail/linkExecuteNoPermission", args);
		efs.execute();
	}
	
	@Test (expected = NoPermissionException.class)
	public void executeAppNoPermission() throws Exception{
		new App("appExecuteNoPermission", userNoExe, userNoExe.getHomeDir(), "rwxd----", "");
		
		ExecuteFileService efs = new ExecuteFileService(loginTestFail, "/home/testfail/appExecuteNoPermission", args);
		efs.execute();
	}

	@Ignore // TODO: Fix test to set content to App
	@Test
	public void executeLinkToAnotherFile() throws Exception{
		new App("testExecuteApp", user, user.getHomeDir(), "rwxd----");
		new Link("testLinkToAnotherFile1", user, user.getHomeDir(), "rwxd----", "/home/test/testExecuteApp");
		new Link("testLinkToAnotherFile2", user, user.getHomeDir(), "rwxd----", "/home/test/testLinkToAnotherFile1");
		
		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/testLinkToAnotherFile2", args);
		efs.execute();
	}
}
