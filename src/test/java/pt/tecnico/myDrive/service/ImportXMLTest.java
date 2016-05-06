package pt.tecnico.myDrive.service;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;

import java.io.StringReader;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ImportXMLTest extends AbstractServiceTest {


	private MyDrive md;
	private User user;
	private File file;
	private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<myDrive>"
			+ "  <user username=\"jtb\">"
			+ "    <password>Fernandes</password>"
			+ "    <name>Joaquim Teófilo Braga</name>"
			+ "    <home>/home/jtb</home>"
			+ "    <mask>rwxd----</mask>"
			+ "  </user>"
			+ "  <user username=\"jms\">"
			+ "    <password>marquesSilva</password>"
			+ "    <name>João Marques Silva</name>"
			+ "    <home>/home/jms</home>"
			+ "    <mask>rwxdrwxd</mask>"
			+ "  </user>"
			+ "  <user username=\"mja\">"
			+ "   <name>Manuel José de Arriaga</name>"
			+ "   <password>Peyrelongue</password>"
			+ "  </user>"
			+ "  <plain id=\"3\">"
			+ "    <path>/home/jtb/ui/iu/uiy</path>"
			+ "    <name>profile</name>"
			+ "    <owner>jtb</owner>"
			+ "    <perm>rw-dr---</perm>"
			+ "    <contents>Primeiro chefe de Estado do regime republicano (acumulando com a chefia do governo), numa capacidade provisória até à eleição do primeiro presidente da República.</contents>"
			+ "  </plain>"
			+ "  <dir id=\"4\">"
			+ "    <path>/home/jtb</path>"
			+ "    <name>documents</name>"
			+ "    <owner>jtb</owner>"
			+ "    <perm>rwxdr-x-</perm>"
			+ "  </dir>"
			+ "  <link id=\"5\">"
			+ "    <path>/home/jtb</path>"
			+ "    <name>doc</name>"
			+ "    <owner>jtb</owner>"
			+ "    <perm>r-xdr-x-</perm>"
			+ "    <value>/home/jtb/documents</value>"
			+ "  </link>"
			+ "  <dir id=\"7\">"
			+ "    <path>/home/jtb</path>"
			+ "    <owner>jtb</owner>"
			+ "    <name>bin</name>"
			+ "    <perm>rwxd--x-</perm>"
			+ "  </dir>"
			+ "  <dir id=\"666\">"
			+ "    <path>/home/1/2/3/4/5/6</path>"
			+ "    <owner>jtb</owner>"
			+ "    <name>uuid</name>"
			+ "    <perm>rwxd--x-</perm>"
			+ "  </dir>"
			+ "  <app id=\"9\">"
			+ "    <path>/home/jtb/bin</path>"
			+ "    <name>hello</name>"
			+ "    <owner>jtb</owner>"
			+ "    <perm>rwxd--x-</perm>"
			+ "    <method>pt.tecnico.myDrive.app.Hello</method>"
			+ "  </app>"
			+ "  <link id=\"51\">"
			+ "    <path>/a/b/c/d/e/f/g/h/i</path>"
			+ "    <name>docweq</name>"
			+ "    <owner>jtb</owner>"
			+ "    <perm>r-xdr-x-</perm>"
			+ "    <value>/home/jtb/documents</value>"
			+ "  </link>"
			+ "</myDrive>";


	protected void populate() {
		md = MyDriveService.getMyDrive();
	}

	@Test
	public void testUserImport() throws Exception {
		Document doc = new SAXBuilder().build(new StringReader(xml));
		ImportXMLService service = new ImportXMLService(doc);
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
		Document doc = new SAXBuilder().build(new StringReader(xml));
		ImportXMLService service = new ImportXMLService(doc);
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
