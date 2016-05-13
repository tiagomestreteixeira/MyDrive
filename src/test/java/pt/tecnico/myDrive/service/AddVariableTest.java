package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;
import pt.tecnico.myDrive.service.dto.EnvVarDto;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class AddVariableTest extends AbstractServiceTest {

    private long login;
    private String name = "andre";
    private String pass = "andreandre";
    private User userObject;
    private MyDrive md;
    private SuperUser root;
    private App myApp;
    private String variableName;
    private String variableName2;
    private String variableValue;
    private String variableValue2;
    private ArrayList<EnvVarDto> result = new ArrayList<EnvVarDto>();

    private static final String USER_DEFAULT_PERMISSIONS = "rwxd----";

    protected void populate() {

        md = MyDriveService.getMyDrive();
        root = md.getSuperUser();
        userObject = new User(md, name, name, USER_DEFAULT_PERMISSIONS, pass);
        login = md.createLogin(name, pass);
        variableName = "envName1";
        variableName2 = "envName2";
        variableValue = "envValue1";
        variableValue2 = "envValue2";
    }
    
    @Test
    public void addEnvVariable() throws Exception{
    	AddEnvVariableService aev = new AddEnvVariableService(login, variableName, variableValue);
		aev.execute();
		
		List<EnvVarDto> result = aev.result();
		assertEquals(variableName, result.get(0).getName());
		assertEquals(variableValue, result.get(0).getValue());
    }
    
    @Test
    public void changeExistingVariable() throws Exception{
    	AddEnvVariableService aev = new AddEnvVariableService(login, variableName, variableValue);
		aev.execute();
		List<EnvVarDto> result = aev.result();
		assertEquals(variableName, result.get(0).getName());
		assertEquals(variableValue, result.get(0).getValue());
		
		AddEnvVariableService aev2 = new AddEnvVariableService(login, variableName, variableValue2);
		aev2.execute();
		result = aev2.result();
		assertEquals(variableName, result.get(0).getName());
		assertEquals(variableValue2, result.get(0).getValue());
    }
    
    @Test
    public void addEnvVariables() throws Exception{
    	AddEnvVariableService service = new AddEnvVariableService(login, variableName, variableValue);
		service.execute();

		service = new AddEnvVariableService(login, variableName2, variableValue2);
		service.execute();

		service = new AddEnvVariableService(login, null, null);
		service.execute();
		List<EnvVarDto> result = service.result();

		assertEquals("Result size should be 2.", 2, result.size());
    }

}