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

    public void xmlImport(Element element) throws ImportDocumentException{


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
            String id = node.getAttribute("id").getValue();

            if(id == null)
                throw new ImportDocumentException("Directory", "attribute id cannot be read properly");

            log.info("Node Element : " + node.getName());
            log.info("Attribute Id: " + id);
            String  directoryPath,
                    directoryName,
                    directoryOwnerUsername,
                    directoryDefaultPermissions;

            directoryPath = directoryName = directoryOwnerUsername = directoryDefaultPermissions = null;

            for (Element child : node.getChildren()) {
                if (child.getName().equals("path"))
                    directoryPath = child.getText();

                if (child.getName().equals("name"))
                    directoryName = child.getText();

                if (child.getName().equals("owner"))
                    directoryOwnerUsername = child.getText();

                if (child.getName().equals("perm"))
                    directoryDefaultPermissions = child.getText();

                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }


            if(directoryPath == null)
                throw new ImportDocumentException("Directory","<path> node cannot be read properly.");
            if(directoryName == null)
                throw new ImportDocumentException("Directory","<name> node cannot be read properly.");
            if(directoryOwnerUsername == null)
                throw new ImportDocumentException("Directory","<owner> node cannot be read properly.");
            // TODO: Check correctness of defaultPermissions
            if(directoryDefaultPermissions == null)
                directoryDefaultPermissions = "rwxd----";
            //    throw new ImportDocumentException("Directory","<perm> node cannot be read properly.");


            // Directory implementation needed
            // new dir(directoryPath,directoryName,directoryOwnerUsername,directoryPermissions)
        }


        // import Plain Files
        for (Element node: element.getChildren("plain")) {
            String name = node.getAttribute("id").getValue();
            log.info("Node Element : " + node.getName());
            log.info("Attribute : " + name);
            String  plainPath,
                    plainName,
                    plainOwnerUsername,
                    plainDefaultPermissions,
                    plainContents;

            plainPath = plainName = plainOwnerUsername = plainDefaultPermissions = null;
            plainContents = "";

            for (Element child : node.getChildren()) {
                if (child.getName().equals("path"))
                    plainPath = child.getText();
                if (child.getName().equals("name"))
                    plainName = child.getText();
                if (child.getName().equals("owner"))
                    plainOwnerUsername = child.getText();
                if (child.getName().equals("perm"))
                    plainDefaultPermissions = child.getText();
                if (child.getName().equals("contents"))
                    plainContents = child.getText();

                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }


            if(plainPath == null)
                throw new ImportDocumentException("Plain File", "<path> node cannot be read properly.");
            if(plainName == null)
                throw new ImportDocumentException("Plain File","<name> node cannot be read properly.");
            if(plainOwnerUsername == null)
                throw new ImportDocumentException("Plain File","<owner> node cannot be read properly.");

            // TODO: Check correctness of plainDefaultPermissions
            if(plainDefaultPermissions == null)
                plainDefaultPermissions = "rwxd----";
            //    throw new ImportDocumentException("plain","<perm> node cannot be read properly.");


            // Directory implementation needed
            // new plainFile(plainPath,plainName,plainOwnerUsername,plainDefaultPermissions,plainContents);
        }


        // import Link Files
        for (Element node: element.getChildren("link")) {
            String name = node.getAttribute("id").getValue();
            log.info("Node Element : " + node.getName());
            log.info("Attribute : " + name);
            String  linkPath,
                    linkName,
                    linkOwnerUsername,
                    linkDefaultPermissions,
                    linkValue;

            linkPath = linkName = linkOwnerUsername = linkDefaultPermissions = null;
            linkValue = "";

            for (Element child : node.getChildren()) {
                if (child.getName().equals("path"))
                    linkPath = child.getText();
                if (child.getName().equals("name"))
                    linkName = child.getText();
                if (child.getName().equals("owner"))
                    linkOwnerUsername = child.getText();
                if (child.getName().equals("perm"))
                    linkDefaultPermissions = child.getText();
                if (child.getName().equals("value"))
                    linkValue = child.getText();

                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }


            if(linkPath == null)
                throw new ImportDocumentException("Link", "<path> node cannot be read properly.");
            if(linkName == null)
                throw new ImportDocumentException("Link","<name> node cannot be read properly.");
            if(linkOwnerUsername == null)
                throw new ImportDocumentException("Link","<owner> node cannot be read properly.");

            // TODO: Check correctness of plainDefaultPermissions
            if(linkDefaultPermissions == null)
                linkDefaultPermissions = "rwxd----";
            //    throw new ImportDocumentException("Link","<perm> node cannot be read properly.");


            // Directory implementation needed
            // new Link(linkPath,linkName,linkOwnerUsername,linkDefaultPermissions,linkValue);
        }

        // import App's
        for (Element node: element.getChildren("app")) {
            String name = node.getAttribute("id").getValue();
            log.info("Node Element : " + node.getName());
            log.info("Attribute : " + name);
            String  appPath,
                    appName,
                    appOwnerUsername,
                    appDefaultPermissions,
                    appMethod;

            appPath = appName = appOwnerUsername = appDefaultPermissions = null;
            appMethod = "";

            for (Element child : node.getChildren()) {
                if (child.getName().equals("path"))
                    appPath = child.getText();
                if (child.getName().equals("name"))
                    appName = child.getText();
                if (child.getName().equals("owner"))
                    appOwnerUsername = child.getText();
                if (child.getName().equals("perm"))
                    appDefaultPermissions = child.getText();
                if (child.getName().equals("method"))
                    appMethod = child.getText();

                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }


            if(appPath == null)
                throw new ImportDocumentException("app", "<path> node cannot be read properly.");
            if(appName == null)
                throw new ImportDocumentException("app","<name> node cannot be read properly.");
            if(appOwnerUsername == null)
                throw new ImportDocumentException("app","<owner> node cannot be read properly.");

            // TODO: Check correctness of plainDefaultPermissions
            if(appDefaultPermissions == null)
                appDefaultPermissions = "rwxd----";
            //    throw new ImportDocumentException("app","<perm> node cannot be read properly.");


            // App implementation needed
            // new App(appPath,appName,appOwnerUsername,appDefaultPermissions,appValue);
        }

    }
}
