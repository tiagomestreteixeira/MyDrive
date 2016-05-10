package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.MethodNotValidException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class App extends App_Base {

    public App(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
	    super.setContent("");
    }

    public App(String name, User user, Dir directory, String permissions, String content) throws MyDriveException {
        init(name, user, directory, permissions);
        this.setContent(content);
    }

    public App(Element node){
        xmlImport(node,"app","method");
    }

    @Override
    public void setContent(String method) throws MyDriveException {

        if (method.matches("(([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*)|^$")) {
            super.setContent(method);
            return;
        }
	    throw new MethodNotValidException(method);
    }


    @Override
    public void execute(User user){
        if (user.checkPermission(this, 'x')) {
            // TODO Execute Apps.
        } else {
            throw new NoPermissionException("App.execute()");
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

    @Override
    public String getType(){
        return "App";
    }
}
