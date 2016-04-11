package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import java.util.Random;

import pt.tecnico.myDrive.domain.User;

public class LoginToken extends LoginToken_Base {
    
    public LoginToken(User user) {
        super();
        this.setUser(user);
        this.setLoginDate(new DateTime());
        if (user.getUsername().equals("root")){
     	   this.setIdentifier(user.getUsername());
        }
        else{
     	   int r = (new Random().nextInt(10));
     	   this.setIdentifier(user.getUsername()+r);
        }
    }
    
    public LoginToken(User user, int old) {
        super();
        this.setUser(user);
        this.setLoginDate(new DateTime());
        if (user.getUsername().equals("root")){
     	   this.setIdentifier(user.getUsername());
        }
        else{
        	while(true){
        		int r = (new Random().nextInt(10));
        		if (r != old){
        			this.setIdentifier(user.getUsername()+r);
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
	    	int r = (new Random().nextInt(9));
	    	if (this.getIdentifier().equals(this.getUser().getUsername()+r)){ //gerou mesmo random
	    		this.refreshToken();
	    	}
	    	else{
	    		this.setIdentifier(this.getUser().getUsername()+r);
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
