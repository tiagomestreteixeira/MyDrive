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
import pt.tecnico.myDrive.service.ImportXMLService;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    static final Logger log = LogManager.getRootLogger();

    // TODO: Check for additional needed initializations/cleanup's
    public static void main(String[] args) throws Exception{
        try {
            for (String s : args){
                log.info("command line argument: " + s);
                importXML(resourceFile(s));
            }
            MdShell.main();
            xmlPrint();
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

    public static void importXML(File file) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(file);
            new ImportXMLService(document).execute();
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
