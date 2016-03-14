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
}
