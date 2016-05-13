package pt.tecnico.myDrive.system;

import org.junit.Ignore;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.presentation.*;
import pt.tecnico.myDrive.service.AbstractServiceTest;

public class SystemTest extends AbstractServiceTest {
	private final static String USERNAME1 = "ivan";
	private final static String NAME1 = "Ivan";
	private final static String PASSWORD1 = "12345678";

	private final static String USERNAME2 = "tiagoteixeira";
	private final static String NAME2 = "Tiago";
	private final static String PASSWORD2 = USERNAME2;

	private final static String PERMISSION = "rwxdr-x-";

	private final static String PLAINFILENAME = "PlainFile";
	private final static String PLAINFILECONTENT = "PlainFile";
	private final static String APPNAME = "App";
	private final static String APPMETHOD = "pt.tecnico.myDrive.presentation.Hello";
	private final static String APPARGUMENT = "arg";

	private final static String DIRECTORYNAME = "Dir";

	private final static String ENVNAME = "envar";
	private final static String ENVVALUE = "envvalue";

	private Shell sh;

	@Override
	protected void populate() {
		sh = new MdShell();
		MyDrive md = MyDrive.getInstance();

		User u1 = new User(md, USERNAME1, NAME1, PERMISSION, PASSWORD1);
		App app = new App(APPNAME, u1, u1.getHomeDir(), u1.getUmask(), APPMETHOD);
		new PlainFile(PLAINFILENAME, u1, u1.getHomeDir(), u1.getUmask(), app.getPath());
		new Dir(DIRECTORYNAME, u1, u1.getHomeDir(), u1.getUmask());

		User u2 = new User(md, USERNAME2, NAME2, PERMISSION, PASSWORD2);
		new PlainFile(PLAINFILENAME, u2, u2.getHomeDir(), u2.getUmask(), app.getPath());
		new Dir(DIRECTORYNAME, u2, u2.getHomeDir(), u2.getUmask());
	}

	@Test
	public void success() throws Exception {
		new LoginCommand(sh).execute(new String[]{USERNAME1, PASSWORD1});
		new DoCommand(sh).execute(new String[]{PLAINFILENAME});
		new DoCommand(sh).execute(new String[]{APPNAME});
		new DoCommand(sh).execute(new String[]{APPNAME, APPARGUMENT});

		new UpdateCommand(sh).execute(new String[]{PLAINFILENAME, PLAINFILECONTENT});

		new LsCommand(sh).execute(new String[]{});
		new LsCommand(sh).execute(new String[]{DIRECTORYNAME});

		new EnvCommand(sh).execute(new String[]{});
		new EnvCommand(sh).execute(new String[]{ENVNAME, ENVVALUE});
		new EnvCommand(sh).execute(new String[]{ENVNAME});

		new CWDCommand(sh).execute(new String[]{DIRECTORYNAME});
		new CWDCommand(sh).execute(new String[]{".."});
		new CWDCommand(sh).execute(new String[]{});

		new LoginCommand(sh).execute(new String[]{USERNAME2, PASSWORD2});
		new DoCommand(sh).execute(new String[]{PLAINFILENAME});
		new CWDCommand(sh).execute(new String[]{DIRECTORYNAME});

		new TokenCommand(sh).execute(new String[]{});
		new TokenCommand(sh).execute(new String[]{USERNAME1});

	}
}
