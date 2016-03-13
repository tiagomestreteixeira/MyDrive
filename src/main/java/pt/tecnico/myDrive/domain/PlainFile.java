package pt.tecnico.mydrive;

import pt.tecnico.myDrive.File;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.FileNotFoundException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile() {
    	super();
    }
    
    public String getContent(){
    	return super.getContent();
    }
}
