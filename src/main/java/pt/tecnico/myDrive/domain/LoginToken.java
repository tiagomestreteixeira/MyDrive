package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.joda.time.DateTime;

import java.math.BigInteger;

import java.util.Random;

import pt.tecnico.myDrive.domain.User;

public class LoginToken extends LoginToken_Base {
    
	static final Logger log = LogManager.getRootLogger();
	
    public LoginToken(User user) {
        super();
        
        Long tokenId = new BigInteger(64, new Random()).longValue();
        
        this.setUser(user);
        this.setLoginDate(new DateTime());
        if (user.getUsername().equals("root")){
     	   this.setIdentifier(tokenId);
        }
        else{
     	   this.setIdentifier(tokenId);
        }
    }
    
    public LoginToken(User user, long oldToken) {
        super();
        
        Long tokenId = new BigInteger(64, new Random()).longValue();
        
        this.setUser(user);
        this.setLoginDate(new DateTime());
        if (user.getUsername().equals("root")){
     	   this.setIdentifier(tokenId);
        }
        else{
        	while(true){
        		if (tokenId != oldToken){
        			this.setIdentifier(tokenId);
        			break;
        		}
        	}
        }
     }
    
    public void refreshToken(){
    	if(this.getIdentifier().equals("root")){
    		this.setLoginDate(new DateTime());
    	}
    	else{	
    		Long tokenId = new BigInteger(64, new Random()).longValue();
    		
	    	if (this.getIdentifier().equals(tokenId)){ //gerou mesmo random
	    		this.refreshToken();
	    	}
	    	else{
	    		this.setIdentifier(tokenId);
	        	this.setLoginDate(new DateTime());
	    	}
    	}	
    }
    
    public boolean isDateValid(DateTime data){
    	double hours = (data.getMillis() - this.getLoginDate().getMillis()) /1000/60/60;
		if(hours>2){
			return false;
		}
		return true;
    }
    
    public void delete(){
    	this.setUser(null);
    	this.setSession(null);
    	deleteDomainObject();
    }
}
