package pt.tecnico.myDrive.integration;

import mockit.Mock;
import mockit.MockUp;
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

import static org.junit.Assert.*;

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

        UserInfoTest() {
        }
    }

    private int indexOfByUsername(String username) {
        int idx = 0;
        for (UserInfoTest ui : users) {
            if (ui.username.equals(username))
                return idx;
            idx++;
        }
        return -1;
    }

    void specificUserInitialization() {
        UserInfoTest jtb = users.get(indexOfByUsername("jtb"));
        jtb.numberFilesHomeDir = INITIAL_NUMBER_FILES + 4;
        for (UserInfoTest ui : users) {
            ui.numberPlainsToCreate = 10;
            ui.numberLinksToCreate = 10;
            ui.numberAppsToCreate = 10;
            ui.numberDirsToCreate = 10;
        }
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

    private void loginUser(UserInfoTest uit) {
        log.debug("[System Integration Test] Login Service of user " + uit.username + " - uses LoginUserService");

        LoginUserService us = new LoginUserService(uit.username, uit.password);
        us.execute();

        assertNotNull(us.result());

        uit.token = us.result();
        log.debug("username: " + uit.username);
        log.debug("password: " + uit.password);
        log.debug("token: " + uit.token);
        log.debug("Number of Files in Home dir: " + uit.numberFilesHomeDir);
    }

    private void listDirectoryUser(UserInfoTest uif, int expectedNumberFiles) {
        log.debug("[System Integration Test] List current Dir Files of User: " + uif.username
                + ", Current Dir : " + uif.currentDir + " - uses ListDirectoryService");

        ListDirectoryService lds = new ListDirectoryService(uif.token);
        lds.execute();

        for (FileDto dto : lds.result())
            log.debug("\t" + dto.getType() + " -> " + dto.getFilename());

        assertEquals("[System Integration Test] ListDirectoryService. User " + uif.username + " should have the correct "
                + "number of files in " + uif.currentDir + " : ", expectedNumberFiles, lds.result().size());
    }

    private void changeDirUser(UserInfoTest uif, String pathNewDir) {
        log.debug("[System Integration Test] ChangeDirectoryService . User " + uif.username + "changes current dir from "
                + uif.currentDir + " to " + pathNewDir + " - uses ChangeDirectoryService");

        ChangeDirectoryService cds = new ChangeDirectoryService(uif.token, pathNewDir);
        cds.execute();

        assertEquals("Changed to a wrong pathname", pathNewDir, cds.result());
    }

    private void writeFileServiceUser(UserInfoTest uit, String fileName, String content) {
        log.debug("[System Integration Test] WriteFileService. User: " + uit.username + ", write the content:" + content
                + " to the file : " + fileName + " - uses WriteFileService");

        WriteFileService wft = new WriteFileService(uit.token, fileName, content);
        wft.execute();

        String assertWriteServiceMsg = "[System Integration Test] WriteFileService. The  file "
                + uit.currentDir + "/" + fileName + " of user " + uit.username + " should exist";
        PlainFile pf = (PlainFile) su.lookup(uit.currentDir + "/" + fileName);

        assertNotNull(assertWriteServiceMsg, pf);
        assertEquals("Content Written don't match.", uit.username, pf.getContent());
    }

    private void readFileServiceUser(UserInfoTest uit, String fileName) {
        log.debug("[System Integration Test] ReadFileService. User: " + uit.username + ", reads the content of file: " +
                fileName + " -  uses ReadFileService");

        ReadFileService rft = new ReadFileService(uit.token, fileName);
        rft.execute();

        assertNotNull("[System Integration Test] ReadFileService. The file " + fileName + " should exists", rft.result());
        assertEquals("Content Read from don't match.", uit.username, rft.result());
    }

    private void createFileServiceBatchUser(UserInfoTest uit, String filename, String fileType, String content, int maxNumber) {
        for (int idFile = 0; idFile < maxNumber; idFile++) {
            createFileServiceUser(uit, filename + idFile, fileType, content);
        }
    }

    private void createFileServiceUser(UserInfoTest uit, String filename, String fileType, String content) {
        log.debug("[System Integration Test] CreateFileService. User: " + uit.username + ", will create the file "
                + filename + ", of type : " + fileType + ", in the directory: " + uit.currentDir + " - uses CreateFileService");

        if (fileType.equals("Dir")) {
            new CreateFileService(uit.token, filename, fileType).execute();
        } else {
            new CreateFileService(uit.token, filename, fileType, content).execute();
        }

        String assertWriteServiceMsg = "[System Integration Test] CreateFileService. The  file "
                + uit.currentDir + "/" + filename + " of user " + uit.username + " should have been created";

        assertNotNull(assertWriteServiceMsg, su.lookup(uit.currentDir + "/" + filename));
    }

    private void deleteFileServiceUser(UserInfoTest uit, String fileName) {
        log.debug("[System Integration Test] DeleteFileService. User: " + uit.username + ", delete the file "
                + fileName + " - uses DeleteFileService");

        DeleteFileService dft = new DeleteFileService(uit.token, fileName);
        dft.execute();

        assertNull("DeleteFileService. The file " + fileName + " should not exists. ", su.lookup(uit.currentDir + "/" + fileName));
    }

    @Test
    public void success() throws Exception {
        try {

            log.debug("[System Integration Test] - ImportXMLService");
            new ImportXMLService(doc).execute();


            for (UserInfoTest ui : users) {
                loginUser(ui);
                listDirectoryUser(ui, ui.numberFilesHomeDir);

                String filename = "plainExample";
                String fileType = "Plain";
                String plainContent = "This\nIs\nA\nPlain File\nContent!";
                createFileServiceUser(ui, filename, fileType, plainContent);
                ui.numberFilesHomeDir++;

                writeFileServiceUser(ui, filename, ui.username);
                readFileServiceUser(ui, filename);
                listDirectoryUser(ui, ui.numberFilesHomeDir);

                fileType = "Dir";
                filename = "dir" + ui.username;
                createFileServiceUser(ui, filename, fileType, "");
                ui.numberFilesHomeDir++;

                String pathNewDir = "/home/" + ui.username + "/" + filename;
                changeDirUser(ui, pathNewDir);
                ui.currentDir = pathNewDir;

                createFileServiceBatchUser(ui, fileType, fileType, "", ui.numberDirsToCreate);
                listDirectoryUser(ui, ui.numberDirsToCreate);

                fileType = "Plain";
                createFileServiceBatchUser(ui, fileType, fileType, plainContent, ui.numberPlainsToCreate);
                listDirectoryUser(ui, ui.numberPlainsToCreate + ui.numberDirsToCreate);

                fileType = "Link";
                String linkContent = "/home/" + ui.username;
                createFileServiceBatchUser(ui, fileType, fileType, linkContent, ui.numberLinksToCreate);
                listDirectoryUser(ui, ui.numberLinksToCreate + ui.numberPlainsToCreate + ui.numberDirsToCreate);

                fileType = "App";
                String appContent = "pt.tecnico.myDrive.presentation.Hello.sum.pdf";
                createFileServiceBatchUser(ui, fileType, fileType, appContent, ui.numberAppsToCreate);

                int expectedNumberfiles = ui.numberLinksToCreate + ui.numberPlainsToCreate + ui.numberDirsToCreate
                        + ui.numberAppsToCreate;
                listDirectoryUser(ui, expectedNumberfiles);

                filename = "Plain.pdf";
                createFileServiceUser(ui, filename, "Plain", "");

                final String  filenameToExecute = filename;
                new MockUp<ExecuteFileAssociationService>(){
                    @Mock
                    public final String result() {
                        return appContent;
                    }
                    @Mock
                    public final void dispatch(){
                        su.lookup("/home/" + ui.username + "/" + filenameToExecute +"/"+"App0").execute(md.getUserByUsername(ui.username));
                    }
                };

                ExecuteFileAssociationService efas = new ExecuteFileAssociationService(ui.token, filename);
                efas.execute();
                assertEquals(appContent,efas.result());


                pathNewDir = "/home/" + ui.username;
                changeDirUser(ui, pathNewDir);
                ui.currentDir = pathNewDir;

                listDirectoryUser(ui, ui.numberFilesHomeDir);
                // TODO: Check the correctness
                //deleteFileServiceUser(ui, pathNewDir+"/dir"+ui.username);




            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

}

