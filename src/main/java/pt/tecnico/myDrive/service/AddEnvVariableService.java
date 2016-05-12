package pt.tecnico.myDrive.service;

import java.util.HashMap;

import pt.tecnico.myDrive.domain.EnvVariables;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NotValidEnvVariableException;

public class AddEnvVariableService extends MyDriveService{
	
		private String name;
		private String value;
		private MyDrive md;
		private long token;
		private HashMap<String, String> list;

		public AddEnvVariableService(long token, String name, String value) {
			this.token = token;
			this.name = name;
			this.value = value;
		}

		@Override
		protected void dispatch() throws MyDriveException {
			md = MyDrive.getInstance();
			Login login = md.getLoginFromId(token);
			
			if( !(name instanceof String) || !(value instanceof String)) throw new NotValidEnvVariableException();
			
			EnvVariables variable = new EnvVariables(login, name, value);
			
			//TODO Environment Variable DTO
			list = login.getEnvVarSet();
			
			for(EnvVariables env : login.getEnvVarSet())
				if(env.getName().equals(variable.getName())){
					env.setValue(variable.getName());
				}
			login.addEnvVar(variable);
		}

		public HashMap<String, String> result() {
			return list;
		}
}
