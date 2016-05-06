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

        try {

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


            if(ui.username.equals("jtb")) {
                log.debug("[System Integration Test] List current non-empty HomeDir Files of : User " + ui.username
                        + " - uses ListDirectoryService");

                ListDirectoryService lds = new ListDirectoryService(ui.token, ".");
                lds.execute();

                for (FileDto dto : lds.result())
                    log.debug("\t" + dto.getType() + " -> " + dto.getFilename());

                assertEquals("[System Integration Test] ListDirectoryService. " +
                        "User jtb should have" + ui.numberFilesHomeDir, lds.result().size(), ui.numberFilesHomeDir);
            }


            log.debug("[System Integration Test] Each user create a plain file (with name plainExample in its home dir" +
                    " - uses CreateFileService");
            String plainFilename = "plainExample";
            String fileType = "Plain";
            String plainContent = "This\nIs\nA\nPlain File\nContent!";
            CreateFileService cft = new CreateFileService(ui.token, plainFilename, fileType, plainContent);
            cft.execute();
            assertNotNull("[System Integration Test] CreateFileService. The " + fileType + " file with name "
                       + plainFilename + ", owner " + ui.username + " and content " + plainContent
                       + "should have been created", su.lookup("/home/" + ui.username + "/" + plainFilename));


            log.debug("[System Integration Test] Each user write the content of the plain file created previously" +
                        " with their username - uses WriteFileService");

            WriteFileService wft = new WriteFileService(ui.token, plainFilename, ui.username);
            wft.execute();
            String assertWriteServiceMsg = "[System Integration Test] WriteFileService. The " + fileType +
                        " file with name " + plainFilename + ", owner " + ui.username + " and content " + plainContent
                        + "should have been written successful with content " + ui.username;

            PlainFile pf =  (PlainFile)su.lookup("/home/" + ui.username + "/" + plainFilename);
            assertNotNull(assertWriteServiceMsg, pf);
            assertEquals(ui.username,pf.getContent());
            ui.numberFilesHomeDir++;


            log.debug("[System Integration Test] Each user reads the plain file created previously " +
                    "- uses ReadFileService");
            ReadFileService rft = new ReadFileService(ui.token, plainFilename);
            rft.execute();
            assertNotNull("[System Integration Test] ReadFileService. The " + fileType + " file with name "
                        + plainFilename + ", owner " + ui.username + " and content " + plainContent
                        + "should have been read successful", rft.result());
            assertEquals(ui.username,rft.result());


            log.debug("[System Integration Test] List current non-empty HomeDir Files By User - uses ListDirectoryService");
            ListDirectoryService ldsAfterCreated = new ListDirectoryService(ui.token, ".");
            ldsAfterCreated.execute();

            for (FileDto dto : ldsAfterCreated.result()) {
                log.debug("dasd","\t" + dto.getType() + " -> " + dto.getFilename());
                assertEquals("[System Integration Test] ListDirectoryService. User " + ui.username + " should have "
                            + ui.numberFilesHomeDir + " files.", ui.numberFilesHomeDir,ldsAfterCreated.result().size());
            }


            log.debug("[System Integration Test] Each user create a new directory, with name corresponding to " +
                "dir[Username] in its home dir - uses CreateFileService");
            fileType = "Dir";

            String dirFilename = "dir"+ui.username;
            String pathNewDir = "/home/" + ui.username + "/" + dirFilename;
            log.info("New dir filename:" + dirFilename);
            log.info("Path of the New dir filename:" + pathNewDir);
            cft = new CreateFileService(ui.token, dirFilename, fileType);
            cft.execute();
            assertNotNull(
                        "[System Integration Test] CreateFileService. The " + fileType + " file with name "
                                + dirFilename + ", owner " + ui.username + "should have been created",
                        su.lookup("/home/" + ui.username + "/" + dirFilename));


            log.debug("[System Integration Test] Each user changes current dir to the dir created previously " +
                        "- uses ChangeDirectoryService");
            ChangeDirectoryService cds = new ChangeDirectoryService(ui.token,pathNewDir);
            cds.execute();
            assertEquals("Changed to a wrong pathname",cds.result(),pathNewDir);


            log.debug("[System Integration Test] Each user creates 10 plainfiles - uses ChangeDirectoryService");
            int numberPlainsToCreate = 10;
            fileType = "Plain";
            for(int idFile = 0; idFile < numberPlainsToCreate; idFile++) {
                plainFilename = "plaintestfile"+idFile;
                plainContent = Integer.toString(idFile);
                    cft = new CreateFileService(ui.token, plainFilename, fileType,plainContent);
                    cft.execute();
                    assertNotNull(
                            "[System Integration Test] CreateFileService. The " + fileType + " file with name "
                                    + plainFilename + ", owner " + ui.username + "should have been created",
                            su.lookup(pathNewDir+"/"+plainFilename));
            }
        }

    }catch (Exception e){
        fail(e.getMessage());
    }

    }
}

