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
            for (String s : args) {
                log.info("command line argument: " + s);
                File fileToImport = resourceFile(s);
                if(fileToImport == null)
                    throw new ImportDocumentException(s,"File cannot be loaded. Please make sure that it exists.");
                importXML(fileToImport);
            }
            xmlPrint();
            end();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FenixFramework.shutdown();
        }
    }

    @Atomic
    public static void cleanup() {
        MyDrive.getInstance().cleanup();
    }

    @Atomic
    private static void end() {
        SuperUser root = SuperUser.getInstance();
        Dir homeDir = (Dir) root.getFileByName("home");

        homeDir.getFileByName(root, "README").remove();
        homeDir.listFileSet();
    }

    @Atomic
    public static void setup() {
        MyDrive md = MyDrive.getInstance();

        SuperUser root = SuperUser.getInstance();

        // TODO: Change to use User File creation
        Dir rootDir = Dir.getRootDir();

        // TODO: Change to use User File creation
        //Dir homeDir = new Dir("home", root, rootDir, "rwxdr-x-");

        // Create README file
        PlainFile readme = new PlainFile();
        readme.setName("README");
        readme.setOwner(root);
        readme.setId(md.getNewId());
        readme.setPermissions("rwxdr-x-");
        readme.setDir(rootDir);
        readme.setContent("lista de utilizadores");

        Dir binDir = root.makeDir("/usr/local/bin");

        System.out.println(readme.getContent());


        binDir.remove();
        //new User(md,"");
    }

    @Atomic
    public static void importXML(File file) {
        MyDrive md = MyDrive.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build(file);
            md.xmlImport(document.getRootElement());

        } catch (ImportDocumentException | JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    @Atomic
    public static void xmlPrint() {
        Document doc = MyDrive.getInstance().xmlExport();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Atomic
    public static File resourceFile(String filename) {
        ClassLoader classLoader = MyDrive.getInstance().getClass().getClassLoader();
        if (classLoader.getResource(filename) == null) return null;
        return new java.io.File(classLoader.getResource(filename).getFile());
    }

}
