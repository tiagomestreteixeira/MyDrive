package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.AddEnvVariableService;
import pt.tecnico.myDrive.service.dto.EnvVarDto;

public class EnvCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: env [name [value]]\n";

	public EnvCommand(Shell sh) {
		super(sh, "env", "Create or modify the value of an environment variable if variable 'name' already exists.");
	}

	public void executeEnv(long token, String name, String value) throws Exception {


		AddEnvVariableService aev = new AddEnvVariableService(token, name, value);
		aev.execute();

		if(name != "null" && value == "null"){
			for(EnvVarDto env : aev.result()){
				if(env.getName().equals(name))
					shell().println(env.getValue());
			}
		}

		if(name == "null"){
			for (EnvVarDto env : aev.result()) {
				shell().println(env.getName() + "=" + env.getValue());
			}
		}


	}

	@Override
	public void execute(String[] args) throws Exception {
		long token = shell().getCurrentToken();
		String name = "";
		String value = "";

		if(args.length > 2)
			throw new RuntimeException(USAGE_MSG);

		if(args.length == 1){
			name = args[0];
			value = "null";
			executeEnv(token, name, value);
		}

		if(args.length == 2){
			name = args[0];
			value = args[1];
			executeEnv(token, name, value);
		}

		if(args.length == 0){
			name = "null";
			value = "null";
			executeEnv(token, name, value);
		}

	}
}