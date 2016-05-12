package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

import java.util.HashMap;

public class AddVariableTest extends AbstractServiceTest {

    private long login;
    private String name = "andre";
    private String pass = "andreandre";
    private User userObject;
    private MyDrive md;
    private SuperUser root;
    private App myApp;
    private int notValidName;
    private int notValidValue;
    private String variableName;
    private String variableName2;
    private String variableValue;
    private String variableValue2;
    private HashMap<String, String> result;

    private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

    protected void populate() {

        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        userObject = new User(md, name, name, USER_DEFAULT_PERMISSIONS, pass);
        login = md.createLogin(name, pass);
        variableName = "envVar1";
        variableName2 = "envVar2";
        variableValue = "envValue1";
        variableValue2 = "envValue2";
    }
    
    @Test
    public void addEnvVariable() throws Exception{
    	AddEnvVariableService aev = new AddEnvVariableService(login, variableName, variableValue);
		aev.execute();
		
		result = aev.result();
		assertNotNull(result.get(variableName));
		assertEquals(variableValue, result.get(variableName));
    }
    
    @Test
    public void changeExistingVariable() throws Exception{
    	AddEnvVariableService aev = new AddEnvVariableService(login, variableName, variableValue);
		aev.execute();
		result = aev.result();
		assertEquals(variableValue, result.get(variableName));
		
		AddEnvVariableService aev = new AddEnvVariableService(login, variableName, variableValue2);
		aev.execute();
		result = aev.result();
		assertEquals(variableValue2, result.get(variableName));
    }
    
    @Test(expected = NotValidEnvVariableException.class)
    public void addVariableWithNonStringName() throws Exception{
    	AddEnvVariableService aev = new AddEnvVariableService(login, notValidName, variableValue);
		aev.execute();
    }
    
    @Test(expected = NotValidEnvVariableException.class)
    public void addVariableWithNonStringValue() throws Exception{
    	AddEnvVariableService aev = new AddEnvVariableService(login, variableName, notValidValue);
		aev.execute();
    }
    
    
    
}