package pt.tecnico.myDrive.service;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Ignore;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;

import java.io.StringReader;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ImportXMLTest extends AbstractServiceTest {


	private MyDrive md;
	private User user;

	protected void populate() {
		md = MyDriveService.getMyDrive();
	}

	@Test
	public void testUserImport() throws Exception {
		ImportXMLService service = new ImportXMLService("test.xml");
		service.execute();

		user = md.getUserByUsername("jms");

		// check users have been created
		assertEquals("create 3 users", 5, md.getUserSet().size());
		assertTrue("create jtb", md.hasUser("jtb"));
		assertTrue("create jms", md.hasUser("jms"));
		assertTrue("create mja", md.hasUser("mja"));

		//check user properties are correct
		assertTrue("user Password ", user.checkPassword("marquesSilva"));
		assertTrue("user Name     ", user.getName().equals("João Marques Silva"));
		assertTrue("user Home Dir ", user.getHomeDir().getPath().equals("/home/jms"));
		assertTrue("user Umask    ", user.getUmask().equals("rwxdrwxd"));
	}


	@Test
	public void testFilesImport() throws Exception {
		ImportXMLService service = new ImportXMLService("test.xml");
		service.execute();

		assertTrue("created jtb", md.hasUser("jtb"));
		user = md.getUserByUsername("jtb");

		//check files have been created
		assertTrue("create Plain    ", user.hasFile("/home/jtb/ui/iu/uiy/profile"));
		assertTrue("create Dir      ", user.hasFile("/home/jtb/documents"));
		assertTrue("create Link     ", user.hasFile("/home/jtb/doc"));
		assertTrue("create SubDir   ", user.hasFile("/home/jtb/bin"));
		assertTrue("create SubSubDir", user.hasFile("/home/1/2/3/4/5/6/uuid"));
		assertTrue("create App      ", user.hasFile("/home/jtb/bin/hello"));
		assertTrue("create Link     ", user.hasFile("/a/b/c/d/e/f/g/h/i/docweq"));

		//check file properties
		assertTrue("file name       ", user.getFileByName("/home/jtb/ui/iu/uiy/profile").getName().equals("profile"));
		assertTrue("file permissions", user.getFileByName("/home/jtb/ui/iu/uiy/profile").getPermissions().equals("rw-dr---"));
		assertTrue("file content    ", ((PlainFile) user.getFileByName("/home/jtb/ui/iu/uiy/profile")).getContent().equals("Primeiro chefe de Estado do regime republicano (acumulando com a chefia do governo), numa capacidade provisória até à eleição do primeiro presidente da República."));
		assertTrue("App  content    ", ((App) user.getFileByName("/home/jtb/bin/hello")).getContent().equals("pt.tecnico.myDrive.app.Hello"));
		assertTrue("Link content    ", ((Link) user.getFileByName("/a/b/c/d/e/f/g/h/i/docweq")).getContent().equals("/home/jtb/documents"));
	}

}
