package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.ImportDocumentException;

public class MyDrive extends MyDrive_Base {

    static final Logger log = LogManager.getRootLogger();




    ///// Eliminar
    private MyDrive() {
        setRoot(FenixFramework.getDomainRoot());
    }

    public static MyDrive getInstance() {
        MyDrive md = FenixFramework.getDomainRoot().getMyDrive();
        if (md == null)
            return new MyDrive();

        return md;
    }
    ///





    public User getUserByUsername(String username) {
        for (User user : getUserSet()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void xmlImport(Element element) {


        // import users
        for (Element node: element.getChildren("user")) {

            String username = node.getAttribute("username").getValue();

            if(username == null)
                throw new ImportDocumentException("User", "attribute username cannot be read properly");

            log.info("Node Element : " + node.getName());
            log.info("Attribute : " + username);

            User user = getUserByUsername(username);
            //if(user != null)
            //    throw new UserAlreadyExistsException(username);

            String defaultHome = "/home/" + username;
            String defaultMask = "rwxd----";
            String defaultName = username;
            String defaultPassword = username;

            for (Element child : node.getChildren()) {
                if (child.getName().equals("home"))
                    defaultHome = child.getText();
                if (child.getName().equals("mask"))
                    defaultMask = child.getText();
                if (child.getName().equals("name"))
                    defaultName = child.getText();
                if (child.getName().equals("password"))
                    defaultPassword = child.getText();
                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }
            // User implementation needed
           // new User(defaultName);
        }

        // import dirs
        for (Element node: element.getChildren("dir")) {
            String name = node.getAttribute("id").getValue();
            log.info("Node Element : " + node.getName());
            log.info("Attribute : " + name);
            String  directoryPath,
                    directoryName,
                    directoryOwnerUsername,
                    directoryPermissions;

            directoryPath = directoryName = directoryOwnerUsername = directoryPermissions = null;
            for (Element child : node.getChildren()) {
                if (child.getName().equals("path"))
                    directoryPath = child.getText();
                if (child.getName().equals("name"))
                    directoryName = child.getText();
                if (child.getName().equals("owner"))
                    directoryOwnerUsername = child.getText();
                if (child.getName().equals("perm"))
                    directoryPermissions = child.getText();
                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }

            // Directory implementation needed
            // new dir(directoryPath,directoryName,directoryOwnerUsername,directoryPermissions)


        }





    }
}
