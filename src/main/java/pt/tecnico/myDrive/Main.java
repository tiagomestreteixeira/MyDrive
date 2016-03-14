package pt.tecnico.myDrive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.ImportDocumentException;

import java.io.File;
import java.io.IOException;

public class Main {
    static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {

        try {
            setup();
            for (String s : args){
                //log.info("command line argument: " + s);
                //importXML(new File(s));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FenixFramework.shutdown();
        }
    }

    @Atomic
    public static void setup() {
        MyDrive md = MyDrive.getInstance();

        SuperUser root = SuperUser.getInstance();

        // TODO: Change to use User File creation
        Dir rootDir = new Dir();
        rootDir.setName("/");
        rootDir.setOwner(root);
        rootDir.setId(md.getNewId());
        rootDir.addDir(rootDir);

        // TODO: Change to use User File creation
        Dir homeDir = new Dir();
        homeDir.setName("home");
        homeDir.setOwner(root);
        homeDir.setId(md.getNewId());
        homeDir.addDir(rootDir);

        pt.tecnico.myDrive.domain.File testFile = new pt.tecnico.myDrive.domain.File("test",root,homeDir,"rwxdrwxd");
        log.debug(testFile.getName());
        log.debug(testFile.getPath());
        log.debug(testFile.isOwner(root));
        log.debug(testFile.getFileOwner());
        log.debug(testFile.getLastModification().toString());




    }

    @Atomic
    public static void importXML(File file) {
        log.trace("importXML: " + FenixFramework.getDomainRoot());

        // MyDrive Singleton must be implemented
        MyDrive md = MyDrive.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(file);
            md.xmlImport(document.getRootElement());

        } catch (ImportDocumentException | JDOMException | IOException e) {
            e.printStackTrace();
        }
    }



}
