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

import java.io.File;
import java.io.IOException;

public class Main {
    static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {

        try {
            setup();
            for (String s : args){
                log.info("command line argument: " + s);
                importXML(new File(s));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FenixFramework.shutdown();
        }
    }

    @Atomic
    public static void setup() {
        // TODO: Change to use MyDrive Singleton
        MyDrive md = new MyDrive.getInstance();
        md.setRoot(FenixFramework.getDomainRoot());
        md.setIdCounter(1);

        // TODO: Change later use SuperUser Singleton
        SuperUser root = new SuperUser();
        root.setUsername("root");
        root.setName("root");
        root.setPassword("***");
        root.setUmask("rwxdr-x-");

        md.addUser(root);

        // TODO: Change to use User File creation
        int id = md.getIdCounter();

        Dir rootDir = new Dir();
        rootDir.setName("/");
        rootDir.addUser(root);
        rootDir.setId(id);
        rootDir.addFile(rootDir);
        md.setIdCounter(id + 1);

        // TODO: Change to use User File creation
        id = md.getIdCounter();
        Dir homeDir = new Dir();
        homeDir.setName("home");
        homeDir.addUser(root);
        homeDir.setId(id);
        md.setIdCounter(id + 1);
        rootDir.addFile(homeDir);
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

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }



}