package pt.tecnico.myDrive.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;

import pt.tecnico.myDrive.domain.MyDrive; // Mockup
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.*;
import pt.tecnico.myDrive.exception.*;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

    private static final List<String> names = new ArrayList<String>();
    private static final String p1 = "Tiago", p2 = "Miguel", p3 = "Xana";
    private static final String p4 = "António", p5 = "Ana";
    private static final String importFile = "other.xml";
    private static final int phone2 = 345678900, phone5 = 935667654;

    protected void populate() { // populate mockup
        names.add(p2);
        names.add(p4);
    }

    @Test
    public void success() throws Exception {


    }
}