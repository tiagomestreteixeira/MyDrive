package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;

import static junit.framework.TestCase.assertNull;

public class DeleteFileTest extends AbstractServiceTest {
	long login;
	User u;

	@Override
	protected void populate() {
		u = new User(MyDriveService.getMyDrive(), "Ivan");
		Dir home = u.getHomeDir();
		new PlainFile("test", u, home, u.getUmask());
		LoginUserService service = new LoginUserService("Ivan", "Ivan");
		service.execute();
		login = service.result();
	}

	@Test
	public void success() throws Exception {
		DeleteFileService service = new DeleteFileService(login, "test");
		service.execute();
		assertNull("File should not exist", u.lookup("/home/Ivan/test"));
	}
}
