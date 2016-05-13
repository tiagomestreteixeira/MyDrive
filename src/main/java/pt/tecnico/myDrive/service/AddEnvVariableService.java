package pt.tecnico.myDrive.service;

import java.util.ArrayList;

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
	private ArrayList<EnvVarDto> list;

	public AddEnvVariableService(long token, String name, String value) {
		this.token = token;
		this.name = name;
		this.value = value;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		list = new ArrayList<EnvVarDto>();

		md = MyDrive.getInstance();
		Login login = md.getLoginFromId(token);

		switch(value){
		case "null":
			if(name == "null"){
				for(EnvVariables env : login.getEnvVarSet()){
					list.add(new EnvVarDto(env.getName(), env.getValue()));
				}
				return;
			}else{
				for(EnvVariables env : login.getEnvVarSet()){
					if(env.getName().equals(name)){
						list.add(new EnvVarDto(env.getName(), env.getValue()));
					}
				}
				return;
			}
		default:
			boolean exists = false;
			for(EnvVariables env : login.getEnvVarSet()){
				if(env.getName().equals(name)){
					env.setValue(value);
					exists = true;
				}
				list.add(new EnvVarDto(env.getName(), env.getValue()));
			}
			if(exists) return;
				new EnvVariables(login, name, value);
				for(EnvVariables env : login.getEnvVarSet())
					list.add(new EnvVarDto(env.getName(), env.getValue()));
		}
	}

	public ArrayList<EnvVarDto> result() {
		return list;
	}
}