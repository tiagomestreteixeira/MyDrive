package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

import java.math.BigInteger;
import java.util.Random;

public class Login extends Login_Base {

    public Login(User user) {
        super();

        Long tokenId = new BigInteger(64, new Random()).longValue();

        this.setUser(user);
        this.setLoginDate(new DateTime());
     	this.setIdentifier(tokenId);
     	this.setCurrentDir(user.getHomeDir());
    }

	public void refreshToken() {
		this.setLoginDate(new DateTime());
	}

    public void delete(){
    	this.setUser(null);
    	this.setCurrentDir(null);

    	for(EnvVariables env : super.getEnvVarSet()){
	    	super.removeEnvVar(env);
	    }

    	deleteDomainObject();
    }

	public boolean isValid() {
		return getLoginDate().plusHours(2).isAfterNow();
	}
}


