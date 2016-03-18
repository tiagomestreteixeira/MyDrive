package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import pt.ist.fenixframework.Atomic;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.MyDriveException;

import java.util.Stack;

public class PlainFile extends PlainFile_Base {

    public PlainFile() {
        super();
    }

    public PlainFile(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
    }

    public PlainFile(Element xml) {
        super();
        xmlImport(xml, "plain", "contents");
    }

    public String getContent() {
        return super.getContent();
    }

    private Stack<String> toStack (String pathname) {
        String[] params = pathname.split("/");
        Stack<String> st = new Stack<>();
        for (int i = params.length - 1; i > 0; i--) {
            log.debug(params[i]);
            st.push(params[i]);
        }
        return st;
    }
    @Atomic
    public void xmlImport(Element plainFileElement, String elementDomain, String elementDomainValue) throws ImportDocumentException {

        String path,
                name,
                ownerUsername,
                defaultPermissions,
                contents;

        path = name = ownerUsername = defaultPermissions = null;
        contents = "";

        for (Element child : plainFileElement.getChildren()) {

            if (child.getName().equals("path"))
                path = child.getText();
            if (child.getName().equals("name"))
                name = child.getText();
            if (child.getName().equals("owner"))
                ownerUsername = child.getText();
            if (child.getName().equals("perm"))
                defaultPermissions = child.getText();
            if (child.getName().equals(elementDomainValue))
                contents = child.getText();

            log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");

        }

        if (path == null)
            throw new ImportDocumentException(elementDomain, "<path> node cannot be read properly.");
        if (name == null)
            throw new ImportDocumentException(elementDomain, "<name> node cannot be read properly.");
        if (ownerUsername == null)
            ownerUsername = "root";

        User owner = MyDrive.getInstance().getUserByUsername(ownerUsername);

        if (defaultPermissions == null) {
            if (owner == null) {
                owner = MyDrive.getInstance().getUserByUsername("root");
            }
        }

        defaultPermissions = owner.getUmask();

        setId(MyDrive.getInstance().getNewId());
        setName(name);
        setPermissions(defaultPermissions);
        setOwner(owner);
        setContent(contents);
        addDir((Dir) SuperUser.getInstance().makeDir(path));
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
