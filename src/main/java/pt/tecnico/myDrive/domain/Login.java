package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.NoPermissionException;
import pt.tecnico.myDrive.exception.VariableNotFoundException;

import java.math.BigInteger;
import java.util.Random;

public class Login extends Login_Base {

    public Login(User user) {
        super();

        Long tokenId = new BigInteger(64, new Random()).longValue();

        super.setUser(user);
        super.setLoginDate(new DateTime());
     	super.setIdentifier(tokenId);
     	super.setCurrentDir(user.getHomeDir());
    }

	@Override
	public void setIdentifier(long identifier) {
		throw new NoPermissionException("Login.setIdentifier()");
	}

	@Override
	public DateTime getLoginDate() {
		// TODO: Lock access to Login.setLoginDate
		return super.getLoginDate();
	}

	@Override
	public void setLoginDate(DateTime loginDate) {
		// TODO: Lock access to Login.setLoginDate
		super.setLoginDate(loginDate);
	}

	@Override
	public void setUser(User user) {
		throw new NoPermissionException("Login.setUser()");
	}

	@Override
	public void removeEnvVar(EnvVariables envVar) {
		throw new NoPermissionException("Login.removeEnvVar()");
	}

	@Override
	public void setMydrive(MyDrive mydrive) {
		throw new NoPermissionException("Login.setMyDrive()");
	}

	@Override
	public void addEnvVar(EnvVariables variable) {
		for (EnvVariables env : getEnvVarSet()) {
			if (env.getName().equals(variable.getName())) {
				System.out.println(variable.getName() + " " + variable.getValue());
				if (variable.getValue() == null) {
					variable.setValue(env.getValue());
				}
				env.setValue(variable.getValue());
				return;
			}
		}
		super.addEnvVar(variable);
	}

	public EnvVariables getEnviromentVariable(Login login, String name, String value) {
		for (EnvVariables env : getEnvVarSet()) {
			if (env.getName().equals(name)) {
				if (value != null) {
					env.setValue(value);
				}
				return env;
			}
		}

		if (value == null)
			throw new VariableNotFoundException(name);

		EnvVariables var = new EnvVariables(login, name, value);
		super.addEnvVar(var);
		return var;
	}

	public void refreshToken() {
		this.setLoginDate(new DateTime());
	}

    public void delete(){
    	super.setUser(null);
    	super.setCurrentDir(null);

    	for(EnvVariables env : super.getEnvVarSet()){
	    	super.removeEnvVar(env);
	    }

    	deleteDomainObject();
    }

	public boolean isValid() {
		User user = getUser();
		return user.isLoginValid(getLoginDate());
	}
}


