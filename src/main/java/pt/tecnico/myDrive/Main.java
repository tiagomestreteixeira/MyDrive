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
import pt.tecnico.myDrive.presentation.MdShell;
import pt.tecnico.myDrive.presentation.Shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws Exception{
        // TODO: Import XML call
        // TODO: Add XML files to resources
        // @see first-sprint
        MdShell.main(args);
    }

    @Atomic
    public static void cleanup() {
    	MyDrive.getInstance().cleanup();
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
