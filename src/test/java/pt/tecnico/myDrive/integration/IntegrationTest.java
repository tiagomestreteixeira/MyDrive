package pt.tecnico.myDrive.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.FileDto;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

    private MyDrive md;
    private SuperUser su;

    private static final String IMPORT_XML_FILENAME = "users.xml";

    private class UserInfo{
        public String username, password;
        public Long token;
        public int numberFilesHomeDir;
        UserInfo(){};
    }

    private int indexOfByUsername(String username) {
        int idx = 0;
        for (UserInfo ui : users){
            if (ui.username.equals(username))
                return idx;
            idx++;
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
        } catch(ImportDocumentException | JDOMException | IOException e){
            e.printStackTrace();
        }

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

        log.debug("==|USERS|==");
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
        log.debug("[System Integration Test] List current non-empty HomeDir Files of : User " + infoJtb.username
                 + " - uses ListDirectoryService");

        ListDirectoryService lds = new ListDirectoryService(infoJtb.token);
        lds.execute();

        for (FileDto dto : lds.result())
            log.debug("\t" + dto.getType() + " -> " + dto.getFilename());
        assertEquals("[System Integration Test] ListDirectoryService. " +
                "User jtb should have" + infoJtb.numberFilesHomeDir,lds.result().size(), infoJtb.numberFilesHomeDir);


        log.debug("[System Integration Test] Each user create a plain file in its home dir - uses CreateFileService");
        String plainFilename = "plainExample";
        String fileType = "Plain";
        String plainContent = "This\nIs\nA\nPlain File\nContent!";
        try {
           for (UserInfo ui : users) {
               CreateFileService cft = new CreateFileService(ui.token, plainFilename, fileType, plainContent);
               cft.execute();
               assertNotNull(
                       "[System Integration Test] CreateFileService. The " + fileType + " file with name "
                       + plainFilename + ", owner " + ui.username + " and content " + plainContent
                       + "should have been created", su.lookup("/home/" + ui.username + "/" + plainFilename));
           }
        }catch (Exception e){
            fail(e.getMessage());
        }


        log.debug("[System Integration Test] Each user reads the plain file created previously - uses ReadFileService");
        try {
            for (UserInfo ui : users) {
                ReadFileService rft = new ReadFileService(ui.token, plainFilename);
                rft.execute();
                assertNotNull(
                        "[System Integration Test] ReadFileService. The " + fileType + " file with name "
                        + plainFilename + ", owner " + ui.username + " and content " + plainContent
                        + "should have been read successful", rft.result());
                assertEquals(plainContent,rft.result());
            }
        }catch (Exception e){
            fail(e.getMessage());
        }

        log.debug("[System Integration Test] Each user write the content of the plain file created previously" +
                " with their username - uses WriteFileService");
        try {
            for (UserInfo ui : users) {
                WriteFileService wft = new WriteFileService(ui.token, plainFilename, ui.username);
                wft.execute();

                String assertWriteServiceMsg = "[System Integration Test] WriteFileService. The " + fileType +
                        " file with name " + plainFilename + ", owner " + ui.username + " and content " + plainContent
                        + "should have been written successful with content " + ui.username;

                PlainFile pf =  (PlainFile)su.lookup("/home/" + ui.username + "/" + plainFilename);
                assertNotNull(assertWriteServiceMsg, pf);
                assertEquals(ui.username,pf.getContent());
                ui.numberFilesHomeDir++;
            }
        }catch (Exception e){
            fail(e.getMessage());
        }


        log.debug("[System Integration Test] List current non-empty HomeDir Files By User - uses ListDirectoryService");
        try {
            for (UserInfo ui : users) {
                ListDirectoryService ldsAfterCreated = new ListDirectoryService(ui.token);
                ldsAfterCreated.execute();

                for (FileDto dto : ldsAfterCreated.result()) {
                    log.debug("\t" + dto.getType() + " -> " + dto.getFilename());
                    assertEquals("[System Integration Test] ListDirectoryService. User " + ui.username + " should have "
                            + ui.numberFilesHomeDir + " files.", ui.numberFilesHomeDir,ldsAfterCreated.result().size());
                }

            }
        }catch (Exception e){
            fail(e.getMessage());
        }

        // TODO: Change log msg
        log.debug("[System Integration Test] Each user create a plain file in its home dir - uses CreateFileService");
        fileType = "Dir";
        try {
            for (UserInfo ui : users) {
                String dirFilename = "dir"+ui.username;
                String pathNewDir = "/home/" + ui.username + "/" + dirFilename;
                CreateFileService cft = new CreateFileService(ui.token, dirFilename, fileType);
                cft.execute();
                assertNotNull(
                        "[System Integration Test] CreateFileService. The " + fileType + " file with name "
                                + dirFilename + ", owner " + ui.username + "should have been created",
                        su.lookup("/home/" + ui.username + "/" + dirFilename));

                log.debug("[System Integration Test] Each user changes current dir to the dir created " +
                        "- uses ChangeDirectoryService");
                ChangeDirectoryService cds = new ChangeDirectoryService(ui.token,pathNewDir);
                cds.execute();
                assertEquals("Changed to a wrong pathname",cds.result(),pathNewDir);
            }
        }catch (Exception e){
            fail(e.getMessage());
        }

        /*try {
            for (UserInfo ui : users) {
                String dirFilename = "dir"+ui.username;
                CreateFileService cft = new CreateFileService(ui.token, dirFilename, fileType);
                cft.execute();
                assertNotNull(
                        "[System Integration Test] CreateFileService. The " + fileType + " file with name "
                                + dirFilename + ", owner " + ui.username + "should have been created",
                        su.lookup("/home/" + ui.username + "/" + dirFilename));
            }
        }catch (Exception e){
            fail(e.getMessage());
        }*/

    }
}

