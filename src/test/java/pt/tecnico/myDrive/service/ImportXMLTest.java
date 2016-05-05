package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;

public class ImportXMLTest extends AbstractServiceTest {


	private MyDrive md;
	private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	protected void populate() {
		md = MyDriveService.getMyDrive();
	}

	@Test
	public void success() throws Exception {

	}

}
