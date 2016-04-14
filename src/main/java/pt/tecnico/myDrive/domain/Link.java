package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class Link extends Link_Base {
    
    protected Link() {
    	super();
    }

    public Link(String name, User user, Dir directory, String permissions, String content) throws MyDriveException {
        init(name, user, directory, permissions);
        user.lookup(content);
        this.setContent(content);
    }

    public Link(Element node){
        super();
        xmlImport(node,"link","value");
    }

    public String getContent(){
    	return super.getContent();
    }

    /*@Override
    public String getName(){
        return super.getName() + "->" + getContent();
    }*/

    @Override
    public String getFormatedName() {
        return "Link " + getPermissions()
                + " " + getFileOwner().getName()
                + " " + getId()
                + " " + super.getName()
                + "->" + getContent();
    }


    @Override
    public File getFileByName(User user, String file){
        if (user.checkPermission(this, 'x')) {
            return user.lookup(this.getContent()).getFileByName(user,file);
        } else {
            throw new NoPermissionException("delete");
        }
    }

    @Override
    public void execute(User user){
        if (user.checkPermission(this, 'x')) {
            user.lookup(this.getContent()).execute(user);
        } else {
            throw new NoPermissionException("delete");
        }
    }

    @Override
    public String read(User user){
        if (user.checkPermission(this, 'r')) {
            return user.lookup(this.getContent()).read(user);
        } else {
            throw new NoPermissionException("read");
        }
    }

    @Override
    public void write(User user, String content){
        if (user.checkPermission(this, 'r')) {
            user.lookup(this.getContent()).write(user,content);
        } else {
            throw new NoPermissionException("write");
        }
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
