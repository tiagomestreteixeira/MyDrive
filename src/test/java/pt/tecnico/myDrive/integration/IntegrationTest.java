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
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.FileDto;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

    private MyDrive md;
    private SuperUser su;

    private Document doc;
    private static final String IMPORT_XML_FILENAME = "users.xml";
    private static final int INITIAL_NUMBER_FILES = 0;

    private static final List<UserInfoTest> users = new ArrayList<UserInfoTest>();

    private class UserInfoTest {
        public String username, password;
        public Long token;

        public String currentDir;
        public int numberFilesHomeDir;

        public int numberDirsToCreate;
        public int numberPlainsToCreate;
        public int numberLinksToCreate;
        public int numberAppsToCreate;

        UserInfoTest(){}
    }

    private int indexOfByUsername(String username) {
        int idx = 0;
        for (UserInfoTest ui : users){
            if (ui.username.equals(username))
                return idx;
            idx++;
        }
        return -1;
    }

    void specificUserInitialization(){
        users.get(indexOfByUsername("jtb")).numberFilesHomeDir = INITIAL_NUMBER_FILES + 4;
    }

    private Document usersXMLtoList() {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(Main.resourceFile(IMPORT_XML_FILENAME));
            for (Element node : doc.getRootElement().getChildren("user")) {
                UserInfoTest ui = new UserInfoTest();
                ui.username = node.getAttribute("username").getValue();
                ui.password = node.getChild("password").getValue();
                ui.currentDir = node.getChild("home").getValue();
                ui.token = null;
                ui.numberFilesHomeDir = INITIAL_NUMBER_FILES;
                users.add(ui);
                specificUserInitialization();
            }
        } catch (ImportDocumentException | JDOMException | IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    protected void populate() {
        md = MyDrive.getInstance();
        su = md.getSuperUser();
        doc = usersXMLtoList();
    }

    private void loginUser(UserInfoTest uit){
        log.debug("[System Integration Test] Login Service of user " + uit.username + " - uses LoginUserService");

        LoginUserService us = new LoginUserService(uit.username,uit.password);
        us.execute();

        assertNotNull(us.result());

        uit.token = us.result();
        log.debug("username: " + uit.username);
        log.debug("password: " + uit.password);
        log.debug("token: " + uit.token);
        log.debug("Number of Files in Home dir: " + uit.numberFilesHomeDir);
    }

    private void listDirectoryUser(UserInfoTest uif, int expectedNumberFiles){
        log.debug("[System Integration Test] List current Dir Files of User: " + uif.username
                + ", Current Dir : " + uif.currentDir + " - uses ListDirectoryService");

        ListDirectoryService lds = new ListDirectoryService(uif.token);
        lds.execute();

        for (FileDto dto : lds.result())
            log.debug("\t" + dto.getType() + " -> " + dto.getFilename());

        assertEquals("[System Integration Test] ListDirectoryService. User " + uif.username + " should have the correct "
                + "number of files in " + uif.currentDir + " : ", expectedNumberFiles, lds.result().size());
    }

    private void changeDirUser(UserInfoTest uif, String pathNewDir){
        log.debug("[System Integration Test] ChangeDirectoryService . User " + uif.username + "changes current dir from "
                + uif.currentDir + " to " + pathNewDir + " - uses ChangeDirectoryService");

        ChangeDirectoryService cds = new ChangeDirectoryService(uif.token,pathNewDir);
        cds.execute();

        assertEquals("Changed to a wrong pathname",pathNewDir,cds.result());
    }

    private void writeFileServiceUser(UserInfoTest uit, String fileName, String content){
        log.debug("[System Integration Test] WriteFileService. User: " + uit.username + ", write the content:" + content
                + " to the file : " + fileName + " - uses WriteFileService");

        WriteFileService wft = new WriteFileService(uit.token, fileName, content);
        wft.execute();

        String assertWriteServiceMsg = "[System Integration Test] WriteFileService. The  file "
            + uit.currentDir + "/" + fileName + " of user " + uit.username + " should exist";
        PlainFile pf =  (PlainFile)su.lookup(uit.currentDir + "/" + fileName);

        assertNotNull(assertWriteServiceMsg, pf);
        assertEquals("Content Written not match.", uit.username,pf.getContent());
    }

    private void readFileServiceUser(UserInfoTest uit, String fileName){
        log.debug("[System Integration Test] ReadFileService. User: " + uit.username + ", reads the content of file: " +
                fileName + " -  uses ReadFileService");

        ReadFileService rft = new ReadFileService(uit.token, fileName);
        rft.execute();

        assertNotNull("[System Integration Test] ReadFileService. The file " + fileName + " should exists", rft.result());
        assertEquals("Content Read from file is wrong.",uit.username,rft.result());
    }

    private void createFileServiceUser(UserInfoTest uit, String filename, String fileType, String content) {
        log.debug("[System Integration Test] CreateFileService. User: " + uit.username + ", will create the file "
                + filename + ", of type : " + fileType + ", in the directory: " + uit.currentDir + " - uses CreateFileService");

        if(fileType.equals("Dir")) {
           new CreateFileService(uit.token, filename, fileType).execute();
        }else{
            new CreateFileService(uit.token, filename, fileType, content).execute();
        }

        String assertWriteServiceMsg = "[System Integration Test] CreateFileService. The  file "
                + uit.currentDir + "/" + filename + " of user " + uit.username + " should have been created";

        assertNotNull(assertWriteServiceMsg, su.lookup(uit.currentDir + "/" + filename));
    }


    @Test
    public void success() throws Exception {
        try {

            log.debug("[System Integration Test] - ImportXMLService");
            new ImportXMLService(doc).execute();


            for(UserInfoTest ui : users){
                loginUser(ui);
                listDirectoryUser(ui,ui.numberFilesHomeDir);

                String filename = "plainExample";
                String fileType = "Plain";
                String plainContent = "This\nIs\nA\nPlain File\nContent!";

                createFileServiceUser(ui,filename,fileType,plainContent);
                ui.numberFilesHomeDir++;

                writeFileServiceUser(ui,filename,ui.username);
                readFileServiceUser(ui,filename);
                listDirectoryUser(ui,ui.numberFilesHomeDir);

                fileType = "Dir";
                filename = "dir"+ui.username;

                createFileServiceUser(ui,filename,fileType,plainContent);
                
                String pathNewDir = "/home/" + ui.username + "/" + filename;
                changeDirUser(ui,pathNewDir);


            /*log.debug("[System Integration Test] Each user creates 10 plainfiles - uses ChangeDirectoryService");
            int numberPlainsToCreate = 10;
            fileType = "Plain";
            for(int idFile = 0; idFile < numberPlainsToCreate; idFile++) {
                plainFilename = "plaintestfile"+idFile;
                plainContent = Integer.toString(idFile);
                    cft = new CreateFileService(ui.token, plainFilename, fileType,plainContent);
                    cft.execute();
                    assertNotNull(su.lookup(pathNewDir+"/"+plainFilename));
            }*/



            /*log.debug("[System Integration Test] Listing of the "+ pathNewDir + " dir created previously " +
                      "- uses ChangeDirectoryService");
            ldsAfterCreated = new ListDirectoryService(ui.token);
            ldsAfterCreated.execute();

            for (FileDto dto : ldsAfterCreated.result()) {
                log.debug("\t" + dto.getType() + " -> " + dto.getFilename());
                assertEquals("[System Integration Test] ListDirectoryService. User " + ui.username + " should have "
                            + numberPlainsToCreate + " files.", numberPlainsToCreate,ldsAfterCreated.result().size());
            }

              */
            // chamar execução de App
            //for()
            }
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }

}

