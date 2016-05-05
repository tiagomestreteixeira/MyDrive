package pt.tecnico.myDrive.system;

import org.junit.Test;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.presentation.*;
import pt.tecnico.myDrive.service.AbstractServiceTest;

public class SystemTest extends AbstractServiceTest {
	private final static String USERNAME1 = "ivan";
	private final static String NAME1 = "Ivan";
	private final static String PASSWORD1 = "12345678";

	private final static String USERNAME2 = "tiago";
	private final static String NAME2 = "Tiago";
	private final static String PASSWORD2 = "12345678";

	private final static String PERMISSION = "rwxdr-x-";

	private final static String PLAINFILENAME = "Teste";
	private final static String DIRECTORYNAME = "Dir";


	private Shell sh;

	@Override
	protected void populate() {
		sh = new MdShell();
		MyDrive md = MyDrive.getInstance();

		User u1 = new User(md, USERNAME1, NAME1, PERMISSION, PASSWORD1);
		new PlainFile(PLAINFILENAME, u1, u1.getHomeDir(), u1.getUmask());
		new Dir(DIRECTORYNAME, u1, u1.getHomeDir(), u1.getUmask());

		User u2 = new User(md, USERNAME2, NAME2, PERMISSION, PASSWORD2);
		new PlainFile(PLAINFILENAME, u2, u2.getHomeDir(), u1.getUmask());
		new Dir(DIRECTORYNAME, u, u1.getHomeDir(), u1.getUmask());
	}

	@Test
	public void success() throws Exception {
		new LoginCommand(sh).execute(new String[]{USERNAME1, PASSWORD1});
		new DoCommand(sh).execute(new String[]{PLAINFILENAME});
		new CWDCommand(sh).execute(new String[]{DIRECTORYNAME});

		new LoginCommand(sh).execute(new String[]{USERNAME2, PASSWORD2});
		new DoCommand(sh).execute(new String[]{PLAINFILENAME});
		new CWDCommand(sh).execute(new String[]{DIRECTORYNAME});

		new TokenCommand(sh).execute(new String[]{});

	}
}
