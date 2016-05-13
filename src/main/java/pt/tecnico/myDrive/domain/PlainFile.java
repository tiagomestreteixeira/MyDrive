package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import java.util.Arrays;


public class PlainFile extends PlainFile_Base {

    protected PlainFile() {
        super();
    }

    public PlainFile(Element xml) {
        super();
        xmlImport(xml, "plain", "contents");
    }

    public PlainFile(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
        setContent("");
    }

    @Override
    public int getSize() {
        return getContent().length();
    }

    public PlainFile(String name, User user, Dir directory, String permissions, String content) throws MyDriveException {
        init(name, user, directory, permissions);
        this.setContent(content);
    }

    public String getContent() {
        return super.getContent();
    }

    @Override
    public String read(User user) {
        if (user.checkPermission(this, 'r')) {
            return this.getContent();
        } else {
            throw new NoPermissionException("PlainFile.read()");
        }
    }

    @Override
    public void write(User user, String string) {
        if (user.checkPermission(this, 'w')) {
            this.setContent(string);
            this.setLastModification(new DateTime());
        } else {
            throw new NoPermissionException("PlainFile.write()");
        }
    }

    @Override
    public void execute(User user, String[] args) {
		if (user.checkPermission(this, 'x')) {
			Dir rootDir = user.getMyDrive().getRootDir();
			Dir relativeDir = getDir();
			String content = getContent();
			String[] lines = content.split("\n");

			for (String line : lines) {
				String[] lineSplit = line.split(" ");
				String appName = lineSplit[0];
				String[] appArgs = new String[0];
				App app;

				if(lineSplit.length > 1)
					appArgs = Arrays.copyOfRange(lineSplit, 1, args.length);

				if (appName.startsWith("/")) {
					app = (App) rootDir.lookup(user, appName.substring(1));
				} else {
					app = (App) relativeDir.lookup(user, appName);
				}
				app.execute(user, appArgs);
			}
		} else {
			user.executeAssociation(getPath());
		}
	}

    public void xmlImport(Element plainFileElement, String elementDomain, String elementDomainValue) throws ImportDocumentException {

        String contents = "";

        xmlImport(plainFileElement,elementDomain);
        for (Element child : plainFileElement.getChildren()) {

            if (child.getName().equals(elementDomainValue))
                contents = child.getText();
        }

        setContent(contents);
    }

    @Override
    public Element xmlExport(){
        Element plainElement =  new Element("plain");
        plainElement = xmlExportHelper(plainElement);

        Element contentsElement = new Element("contents");
        contentsElement.addContent(getContent());
        plainElement.addContent(contentsElement);
        return plainElement;
    }

}
