package pt.tecnico.myDrive.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;


import pt.tecnico.myDrive.Main;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.service.*;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

    private MyDrive md;
    private SuperUser su;

    private User user_tiago;
    private User user_andre;

    private static final String username_jtb = "jtb", username_mja = "mja";
    private static final String password_jtb = "Fernandes", password_mja = "Peyrelongue";
    private Long token_jtb, token_mja;

    private static final String importXMLFilename = "users.xml";

    private class UserInfo{
        public String username, password;
        public Long token;
        UserInfo(){};
    }

    private static final List<UserInfo> users = new ArrayList<UserInfo>();

    private void UsersXMLtoList() {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build(Main.resourceFile(importXMLFilename));
            for (Element node : document.getRootElement().getChildren("user")) {
                UserInfo ui = new UserInfo();
                ui.username = node.getAttribute("username").getValue();;
                ui.password = node.getChild("password").getValue();
                ui.token = null;
                users.add(ui);
            }

        }catch(ImportDocumentException | JDOMException | IOException e){
            e.printStackTrace();
        }
    }



    protected void populate() {

        md = MyDrive.getInstance();
        su = md.getSuperUser();
        UsersXMLtoList();



        //user_tiago = new User(md, username_tiago);
        //user_andre = new User(md, username_andre);
        //users.add(user_tiago);
        //users.add(user_andre);

        Main.importXML(Main.resourceFile(importXMLFilename));
    }

    @Test
    public void success() throws Exception {
       //token_jtb = new LoginUserService();
        for(UserInfo ui : users){
            log.info("User:");
            log.info("username: " + ui.username);
            log.info("password: " + ui.password);
            log.info("token: " + ui.token);
        }

    }

}