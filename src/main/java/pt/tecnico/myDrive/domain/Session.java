package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.domain.LoginToken;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;

public class Session extends Session_Base {
    
	static final Logger log = LogManager.getRootLogger();
	
    public Session() {
        super();
    }
    
    public boolean hasOnlineUsers(){
    	if(this.getTokensSet().isEmpty()){
    		return false;
    	}
    	else{
    		return true;
    	}
    }
    
    public void maintenance(){
    	if (this.hasOnlineUsers()){
    		for (LoginToken token : getTokensSet()){
    			if (!token.isDateValid(new DateTime())){
    				this.removeUserFromSession(token.getIdentifier());
    			}
    				
    		}
    	}
    }
    
    
    public boolean isRootToken(Long token){
    	for(User user : this.getMydrive().getUserSet()){
    		if(user.getUsername().equals("root") && user.getLogintoken().getIdentifier().equals(token)){
    				return true;
    			}
    		}
    	log.warn("Given token does not match existing root token.");
    	return false;
    }
    
    public boolean hasUsernameInSession(String username){
    	if(this.hasOnlineUsers()){
    		for(LoginToken token: this.getTokensSet()){
    			if(token.getUser().getUsername().equals(username)){
    				return true;
    			}
    		}
    		return false;
    	}
    	return false;
    }
    
    public boolean isOnline(Long token){
    	if(this.hasOnlineUsers()){
    		for(LoginToken t : this.getTokensSet()){
    			if(t.getIdentifier().equals(token)){
    					return true;
    			}
    		}
    	}
    	log.warn("Given token does not match existing tokens.");
		return false;
    }
    
    public LoginToken getTokenFromUser(String username){
    	Set<LoginToken> users = this.getTokensSet();
    	if (users.isEmpty()){
    		return null;
    	}
    	for (LoginToken t: users){
    		if (t.getUser().getUsername().equals(username))
    			return t;
    	}
    	return null;
    }
    
    public Long addUsertoSession(String username) throws UserDoesNotExistException{
    	User user = this.getMydrive().getUserByUsername(username);
    	if(user != null){
    		LoginToken token = new LoginToken(user);
    		this.addTokens(token);
    		return token.getIdentifier();
    	}
    	else{
    		throw new UserDoesNotExistException(username);
    	}
    }
    
    public Long addUsertoSession(String username, Long oldToken) throws UserDoesNotExistException{
    	User user = this.getMydrive().getUserByUsername(username);
    	if(user != null){
    		//Integer oldRandom = Integer.parseInt(oldToken.substring(oldToken.length()-1)); 
    		LoginToken token = new LoginToken(user, oldToken);
    		this.addTokens(token);
    		return token.getIdentifier();
    	}
    	else{
    		throw new UserDoesNotExistException(username);
    	}
    }
    
    public void removeUserFromSession(Long userToken){
    	if(this.hasOnlineUsers()){
    		if(this.isOnline(userToken)){
    			for(LoginToken token : this.getTokensSet()){
    				if(token.getIdentifier().equals(userToken)){
    					this.removeTokens(token);
    				}else{
    					log.warn("Given token does not match any existing token.");
    				}
    			}
    		}
    	}	
    }
    
    public User getUserFromSession(String userToken){
    	if(this.hasOnlineUsers()){
    		for(LoginToken token: this.getTokensSet()){
    			if(token.getIdentifier().equals(userToken)){
    				return token.getUser();
    			}else{
    				log.warn("Given token does not match any existing token.");
    			}
    		}
    	}
    	return null;
    }
    
}
