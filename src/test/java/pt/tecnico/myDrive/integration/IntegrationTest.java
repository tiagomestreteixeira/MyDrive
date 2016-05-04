package pt.tecnico.myDrive.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.integration.junit4.JMockit;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;


import pt.tecnico.myDrive.Main;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.FileDto;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

    private MyDrive md;
    private SuperUser su;

    private int jtb_index;
    private int mja_index;
    private int blm_index;
    private int sbp_index;
    private int jcc_index;
    private int aja_index;
    private int mtg_index;

    private static final String IMPORT_XML_FILENAME = "users.xml";

    private class UserInfo{
        public String username, password;
        public Long token;
        public int numberFilesHomeDir;
        UserInfo(){};
    }

    private int indexOfByUsername(String username) {
        int i = 0;
        for (UserInfo ui : users){
            if (ui.username.equals(username))
                return i;
            i++;
        }
        return -1;
    }

    private static final List<UserInfo> users = new ArrayList<UserInfo>();

    private void usersXMLtoList() {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build(Main.resourceFile(IMPORT_XML_FILENAME));
            for (Element node : document.getRootElement().getChildren("user")) {
                UserInfo ui = new UserInfo();
                ui.username = node.getAttribute("username").getValue();;
                ui.password = node.getChild("password").getValue();
                ui.token = null;
                ui.numberFilesHomeDir = 0;
                users.add(ui);
            }

        }catch(ImportDocumentException | JDOMException | IOException e){
            e.printStackTrace();
        }
        //log.debug("INDEX:" + indexOfByUsername("jtb"));
        //log.info("TJB" + users.get(indexOfByUsername("jtb")).numberFilesHomeDir);
        users.get(indexOfByUsername("jtb")).numberFilesHomeDir = 4;
    }

    protected void populate() {

        md = MyDrive.getInstance();
        su = md.getSuperUser();
        usersXMLtoList();

        Main.importXML(Main.resourceFile(IMPORT_XML_FILENAME));
    }

    @Test
    public void success() throws Exception {

        log.debug("USERS:");
        for(UserInfo ui : users){
            LoginUserService us = new LoginUserService(ui.username,ui.password);
            us.execute();
            assertNotNull(us.result());
            ui.token = us.result();
            log.debug("username: " + ui.username);
            log.debug("password: " + ui.password);
            log.debug("token: " + ui.token);
            log.debug("Number of Files in Home dir: " + ui.numberFilesHomeDir);

        }

        UserInfo infoJtb = users.get(indexOfByUsername("jtb"));
        System.out.println("List current non-empty HomeDir Files of : User " + infoJtb.username);

        ListDirectoryService lds = new ListDirectoryService(infoJtb.token);
        lds.execute();

        for (FileDto dto : lds.result())
            System.out.println("\t" + dto.getType() + " -> " + dto.getFilename());
        assertEquals(lds.result().size(), infoJtb.numberFilesHomeDir);


    }

}

