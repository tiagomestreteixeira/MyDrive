package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
public class Link extends Link_Base {
    
    public Link() {
    	super();
    }
    
    public String getContent(){
    	return super.getContent();
    }

    public Link(Element node){
        super();
        xmlImport(node,"link","value");
    };


    public Element xmlExport(){
        Element linkElement =  new Element("link");
        linkElement = xmlExportHelper(linkElement);

        Element valueElement = new Element("value");
        valueElement.addContent(getContent());
        linkElement.addContent(valueElement);
        return valueElement;
    }
}
