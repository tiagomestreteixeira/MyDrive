package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;

public class MyDrive extends MyDrive_Base {

    static final Logger log = LogManager.getRootLogger();

    public MyDrive() {
        super();
    }

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
            log.info("Node Element : " + node.getName());
            log.info("Attribute : " + username);

            User person = getUserByUsername(username);

            for (Element child : node.getChildren()) {

                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }
        }


        // import dirs
        for (Element node: element.getChildren("dir")) {
            String name = node.getAttribute("username").getValue();
            log.info("Node Element : " + node.getName());
            log.info("Attribute : " + name);
            for (Element child : node.getChildren()) {

                log.info("<" + child.getName() + ">" + child.getText() + " </" + child.getName() + ">");
            }
        }

    }
}
