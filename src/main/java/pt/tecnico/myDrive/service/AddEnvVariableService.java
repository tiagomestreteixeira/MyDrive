package pt.tecnico.myDrive.service;

import java.util.HashMap;
import java.util.List;

import pt.tecnico.myDrive.domain.EnvVariables;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NotValidEnvVariableException;
import pt.tecnico.myDrive.service.dto.EnvVarDto;

public class AddEnvVariableService extends MyDriveService{
	
		private String name;
		private String value;
		private MyDrive md;
		private long token;
		private List<EnvVarDto> list;

		public AddEnvVariableService(long token, String name, String value) {
			this.token = token;
			this.name = name;
			this.value = value;
		}

		@Override
		protected void dispatch() throws MyDriveException {
			md = MyDrive.getInstance();
			Login login = md.getLoginFromId(token);
			
			EnvVariables variable = new EnvVariables(login, name, value);
			
			for(EnvVariables env : login.getEnvVarSet()){
				list.add(new EnvVarDto(env.getName(), env.getValue()));
				if(env.getName().equals(variable.getName())){
					env.setValue(variable.getName());
				}
			}
			login.addEnvVar(variable);
		}

		public List<EnvVarDto> result() {
			return list;
		}
}
