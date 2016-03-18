package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.MyDriveException;

public class Link extends Link_Base {
    
    public Link() {
    	super();
    }

    public Link(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
    }

    public Link(Element node){
        super();
        xmlImport(node,"link","value");
    }

    public String getContent(){
    	return super.getContent();
    }

    @Override
    public Element xmlExport(){
        Element linkElement =  new Element("link");
        linkElement = xmlExportHelper(linkElement);

        Element valueElement = new Element("value");
        valueElement.addContent(getContent());
        linkElement.addContent(valueElement);
        return linkElement;
    }
}
