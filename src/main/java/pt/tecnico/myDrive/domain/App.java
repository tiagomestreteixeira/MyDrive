package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.MyDriveException;

public class App extends App_Base {

    public App() {
        super();
    }

    public App(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
    }

    public App(Element node){
        super();
        xmlImport(node,"app","method");
    }

    public String getContent(){
        return super.getContent();
    }

    public Element xmlExport(){
        Element appElement =  new Element("app");
        appElement = xmlExportHelper(appElement);

        Element valueElement = new Element("method");
        valueElement.addContent(getContent());
        appElement.addContent(valueElement);
        return appElement;
    }
}
