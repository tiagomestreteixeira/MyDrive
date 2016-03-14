package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
public class App extends App_Base {

    public App() {
        super();
    }

    public String getContent(){
        return super.getContent();
    }

    public App(Element node){
        super();
        xmlImport(node,"app","method");
    };


    public Element xmlExport(){
        Element appElement =  new Element("app");
        appElement = xmlExportHelper(appElement);

        Element valueElement = new Element("method");
        valueElement.addContent(getContent());
        appElement.addContent(valueElement);
        return valueElement;
    }
}
