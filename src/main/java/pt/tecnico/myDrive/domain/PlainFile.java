package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;


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
