package pt.tecnico.myDrive.service;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.MyDriveException;

import java.io.File;
import java.io.IOException;

public class ImportXMLService extends MyDriveService {

	private Document doc;
	private String filename;
	private File file;

	public ImportXMLService(String filePath) {
		this.filename = filePath;

	}

	@Override
	protected void dispatch() throws MyDriveException {
		SAXBuilder builder = new SAXBuilder();
		ClassLoader classLoader = MyDrive.getInstance().getClass().getClassLoader();
		if (classLoader.getResource(filename) == null) return;
		file = new java.io.File(classLoader.getResource(filename).getFile());
		try {
			doc = builder.build(file);
			getMyDrive().xmlImport(doc.getRootElement());
		} catch (ImportDocumentException | JDOMException | IOException e) {
			e.printStackTrace();
		}
	}
}
