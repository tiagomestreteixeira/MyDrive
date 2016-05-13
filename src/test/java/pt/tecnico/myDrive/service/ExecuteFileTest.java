package pt.tecnico.myDrive.service;

import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidLoginTokenException;
import pt.tecnico.myDrive.exception.NoPermissionException;

@RunWith(JMockit.class)
public class ExecuteFileTest extends AbstractServiceTest {

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
		args = new String[0];
		md = MyDriveService.getMyDrive();
		root = md.getSuperUser();
		user = new User(md, "test", "test", "rwxd----", "testpw12");
		userNoExe = new User(md, "testfail", "testfail", "rw-d----", "testfailpw");
		loginTest = md.createLogin("test", "testpw12");
		loginTestFail = md.createLogin("testfail", "testfailpw");
	}

	@Test
	public void executePlainFile(final @Mocked pt.tecnico.myDrive.presentation.Hello mock) throws Exception {
		App app = new App("testApp", user, user.getHomeDir(), user.getUmask(), DEFAULT_APP);
		new PlainFile("testExecutePlainFile", user, user.getHomeDir(), "rwxd----", app.getPath());

		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/testExecutePlainFile", args);
		efs.execute();
		new Verifications() {{
			mock.main(args);
			times = 1;
		}};
	}

	@Test
	public void executeAppFile(final @Mocked pt.tecnico.myDrive.presentation.Hello mock) throws Exception {
		new App("testExecuteApp", user, user.getHomeDir(), "rwxd----", DEFAULT_APP);

		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/testExecuteApp", args);
		efs.execute();
		new Verifications() {{
			mock.main(args);
			times = 1;
		}};
	}

	@Test
	public void executeLinkFile(final @Mocked pt.tecnico.myDrive.presentation.Hello mock) throws Exception {
		new App("testExecuteApp", user, user.getHomeDir(), "rwxd----", DEFAULT_APP);
		new Link("linkExecuteFile", user, user.getHomeDir(), "rwxd----", "/home/test/testExecuteApp");

		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/linkExecuteFile", args);
		efs.execute();
		new Verifications() {{
			mock.main(args);
			times = 1;
		}};
	}

	@Test(expected = NoPermissionException.class)
	public void executeDirFile() throws Exception {
		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test", args);
		efs.execute();
	}

	@Test(expected = NoPermissionException.class)
	public void executeLinkNoPermission() throws Exception {
		new App("testExecuteApp", userNoExe, userNoExe.getHomeDir(), "rwxd----", DEFAULT_APP);
		new Link("linkExecuteNoPermission", userNoExe, userNoExe.getHomeDir(), "rwxd----", "/home/test/testExecuteApp");

		ExecuteFileService efs = new ExecuteFileService(loginTestFail, "/home/testfail/linkExecuteNoPermission", args);
		efs.execute();
	}

	@Test(expected = NoPermissionException.class)
	public void executeAppNoPermission() throws Exception {
		new App("appExecuteNoPermission", userNoExe, userNoExe.getHomeDir(), "rwxd----", "");

		ExecuteFileService efs = new ExecuteFileService(loginTestFail, "/home/testfail/appExecuteNoPermission", args);
		efs.execute();
	}

	@Test
	public void executeLinkToAnotherFile(final @Mocked pt.tecnico.myDrive.presentation.Hello mock) throws Exception {
		new App("testExecuteApp", user, user.getHomeDir(), "rwxd----", DEFAULT_APP);
		new Link("testLinkToAnotherFile1", user, user.getHomeDir(), "rwxd----", "/home/test/testExecuteApp");
		new Link("testLinkToAnotherFile2", user, user.getHomeDir(), "rwxd----", "/home/test/testLinkToAnotherFile1");

		ExecuteFileService efs = new ExecuteFileService(loginTest, "/home/test/testLinkToAnotherFile2", args);
		efs.execute();
		new Verifications() {{
			mock.main(args);
			times = 1;
		}};
	}

	@Test(expected = InvalidLoginTokenException.class)
	public void executeInvalidLogin() throws Exception {
		new App("testExecuteApp", user, user.getHomeDir(), "rwxd----", DEFAULT_APP);

		Login login = md.getLoginFromId(loginTest);
		login.setLoginDate(new DateTime(0));

		new ExecuteFileService(loginTest, "/home/test/testExecuteApp", new String[0]).execute();
	}

	@Test
	public void executeNoOwnerPermission(final @Mocked pt.tecnico.myDrive.presentation.Hello mock) {
		new App("testExecuteApp", root, user.getHomeDir(), "------x-", DEFAULT_APP);

		new ExecuteFileService(loginTest, "/home/test/testExecuteApp", new String[0]).execute();
		new Verifications() {{
			mock.main(args);
			times = 1;
		}};
	}

	@Test(expected = NoPermissionException.class)
	public void executeNoOwnerNoPermission() {
		new App("testExecuteApp", root, user.getHomeDir(), "--------", DEFAULT_APP);

		new ExecuteFileService(loginTest, "/home/test/testExecuteApp", new String[0]).execute();
	}

	@Test(expected = NoPermissionException.class)
	public void executeOwnerNoPermission() {
		new App("testExecuteApp", user, user.getHomeDir(), "--------", DEFAULT_APP);

		new ExecuteFileService(loginTest, "/home/test/testExecuteApp", new String[0]).execute();
	}

	@Test
	public void executeOwnerPermission(final @Mocked pt.tecnico.myDrive.presentation.Hello mock) {
		new App("testExecuteApp", user, user.getHomeDir(), "--x-----", DEFAULT_APP);
		new ExecuteFileService(loginTest, "/home/test/testExecuteApp", new String[0]).execute();
		new Verifications() {{
			mock.main(args);
			times = 1;
		}};
	}
}
