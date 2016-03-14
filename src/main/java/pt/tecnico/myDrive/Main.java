package pt.tecnico.myDrive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.ImportDocumentException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {

        try {
            setup();
            for (String s : args){
                log.info("command line argument: " + s);
                importXML(new File(s));
            }
            xmlPrint();
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
        Dir rootDir = Dir.getRootDir();

        // TODO: Change to use User File creation
        Dir homeDir = new Dir("home", root, rootDir, "rwxdr-x-");

        // Create README file
        PlainFile readme = new PlainFile();
        readme.setName("README");
        readme.setOwner(root);
        readme.setId(md.getNewId());
        readme.addDir(homeDir);
        readme.setContent("lista de utilizadores");

        Dir usrDir = new Dir("usr", root, rootDir, "rwxdr-x-");

        Dir localDir = new Dir("local", root, usrDir, "rwxdr-x-");

        Dir binDir = new Dir("bin", root, localDir, "rwxdr-x-");

        binDir.remove();

        homeDir.listFileSet();

        System.out.println(readme.getContent());
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

    @Atomic
    public static void xmlPrint() {
        log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc = MyDrive.getInstance().xmlExport();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }



}
