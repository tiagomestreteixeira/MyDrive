package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import org.omg.CORBA.StringHolder;
import pt.tecnico.myDrive.exception.MethodNotValidException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import java.lang.reflect.Method;

public class App extends App_Base {

    protected App() {
        super();
    }

    public App(String name, User user, Dir directory, String permissions) throws MyDriveException {
        init(name, user, directory, permissions);
    }

    public App(String name, User user, Dir directory, String permissions, String content) throws MyDriveException {
        init(name, user, directory, permissions);
        this.setContent(content);
    }

    public App(Element node){
        super();
        xmlImport(node,"app","method");
    }

    @Override
    public void setContent(String method) {
        if (method == null || method.equals("")) {
            super.setContent("");
            return;
        }

        if (method.matches("([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*"))
            super.setContent(method);
        else
            throw new MethodNotValidException(method);
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
