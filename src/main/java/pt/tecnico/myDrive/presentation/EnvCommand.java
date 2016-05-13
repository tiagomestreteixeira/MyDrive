package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.AddEnvVariableService;
import pt.tecnico.myDrive.service.dto.EnvVarDto;

import java.util.ArrayList;
import java.util.List;

public class EnvCommand extends MdCommand {

	private static final String USAGE_MSG = "USAGE: env [name [value]]\n";
	private MdShell mdShell;

	public EnvCommand(MdShell sh) {
		super(sh, "env", "Create or modify the value of an environment variable if variable 'name' already exists.");
		mdShell = sh;
	}

	@Override
	public void execute(String[] args) throws Exception {
		long token = mdShell.getCurrentToken();
		String name;
		String value;

		if (args.length > 2)
			throw new RuntimeException(USAGE_MSG);

		if (args.length == 1) {
			name = args[0];
			value = null;
		} else {
			if (args.length == 2) {
				name = args[0];
				value = args[1];
			} else {
				name = null;
				value = null;
			}
		}
		AddEnvVariableService service = new AddEnvVariableService(token, name, value);
		service.execute();
		List<EnvVarDto> result = service.result();
		for (EnvVarDto dto : result)
			System.out.println(dto.getName() + " = " + dto.getValue());
	}
}