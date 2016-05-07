package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;

public class AddVariableTest extends AbstractServiceTest {

    private long login;
    private String name = "joao";
    private String pass = "joaojoao";
    private User userObject;
    private MyDrive md;
    private SuperUser root;
    private String testPlainFileName = "testPlainFile";
    private App myApp;

    private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

    protected void populate() {

        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        userObject = new User(md, name, name, "rwxd----", pass);

    }
}