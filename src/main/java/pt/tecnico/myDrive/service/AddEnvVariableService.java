package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.EnvVariables;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.EnvVarDto;

import java.util.ArrayList;
import java.util.List;

public class AddEnvVariableService extends MyDriveService{

	private String name;
	private String value;
	private long token;
	private List<EnvVarDto> list;

	public AddEnvVariableService(long token, String name, String value) {
		this.token = token;
		this.name = name;
		this.value = value;
		list = new ArrayList<>();
	}

	@Override
	protected void dispatch() throws MyDriveException {
		Login login = getMyDrive().getLoginFromId(token);

		if (name == null) {
			login.getEnvVarSet().forEach(e -> list.add(new EnvVarDto(e.getName(), e.getValue())));
			return;
		}

		EnvVariables var = login.getEnviromentVariable(login, name, value);
		list.add(new EnvVarDto(var.getName(), var.getValue()));
	}

	public List<EnvVarDto> result() {
		return list;
	}
}