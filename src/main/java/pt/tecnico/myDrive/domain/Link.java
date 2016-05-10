package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class Link extends Link_Base {

    public Link(String name, User user, Dir directory, String permissions, String content) throws MyDriveException {
        init(name, user, directory, permissions);
        user.lookup(content);
        this.setContent(content);
    }

    public Link(Element node){
        xmlImport(node,"link","value");
    }

    @Override
    public File getFileByName(User user, String file){
        if (user.checkPermission(this, 'r')) {
            return user.lookup(this.getContent()).getFileByName(user,file);
        } else {
            throw new NoPermissionException("link.read()");
        }
    }

    @Override
    public void execute(User user){
        if (user.checkPermission(this, 'r')) {
            user.lookup(this.getContent()).execute(user);
        } else {
            throw new NoPermissionException("link.read()");
        }
    }

    @Override
    public String read(User user){
        if (user.checkPermission(this, 'r')) {
            return user.lookup(this.getContent()).read(user);
        } else {
            throw new NoPermissionException("link.read()");
        }
    }

    @Override
    public void write(User user, String content){
        if (user.checkPermission(this, 'r')) {
            user.lookup(this.getContent()).write(user,content);
        } else {
            throw new NoPermissionException("link.read()");
        }
    }

    @Override
    public File lookup(User user, String path) throws MyDriveException {
        File target = user.lookup(getContent());
        return target.lookup(user, path);
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

    @Override
    public String getType(){
        return "Link";
    }
}
