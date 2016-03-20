package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

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

    @Override
    public void write(User user, String methodName) {
            // TODO  Check for correct formating of method.
    }

    @Override
    public String getFormatedName() {
        return "App " + getPermissions() + " " + getFileOwner().getName() +  " " + getId() + " " + getName();
    }

    @Override
    public void execute(User user){
        if (user.checkPermission(this, 'x')) {
            // TODO Execute Apps.
        } else {
            throw new NoPermissionException("delete");
        }
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
