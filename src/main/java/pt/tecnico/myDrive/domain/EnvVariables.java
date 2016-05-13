package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.NoPermissionException;

public class EnvVariables extends EnvVariables_Base {
    
    public EnvVariables(Login login, String name, String value) {
        super();
        super.setLogin(login);
        super.setName(name);
        super.setValue(value);
    }
    
    
    @Override
    public void setName(String name){
    	throw new NoPermissionException("EnvVariables.setName()");
    }
    
    @Override
    public void setValue(String value){
    	super.setValue(value);
    }
    
    @Override
    public void setLogin(Login login){
    	login.addEnvVar(this);
    }
    
    @Override
    public String getName(){
		return super.getName();    	
    }
    
    @Override
    public String getValue(){
    	return super.getValue();
    }
    
    @Override
    public Login getLogin(){
    	return super.getLogin();
    }

}
