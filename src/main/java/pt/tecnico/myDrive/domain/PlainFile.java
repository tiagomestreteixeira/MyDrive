package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.ImportDocumentException;
public class PlainFile extends PlainFile_Base {
    
    public PlainFile() {
    	super();
    }
    
    public String getContent(){
    	return super.getContent();
    }

    public PlainFile(Element xml){
        super();
        xmlImport(xml,"plain","contents");
    }

    public void xmlImport(Element plainFileElement, String elementDomain, String elementDomainValue) throws ImportDocumentException {

        String  path,
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

        User owner = MyDrive.getInstance().getUserByUsername(ownerUsername);;
        if(defaultPermissions==null){
            if(owner == null) {
                owner = MyDrive.getInstance().getUserByUsername("root");
            }
        }

        defaultPermissions = owner.getUmask();

        //setPath(path);
        setName(name);
        setPermissions(defaultPermissions);
        setOwner(owner);
        setContent(contents);

        //    throw new ImportDocumentException("plain","<perm> node cannot be read properly.");
    }
}
