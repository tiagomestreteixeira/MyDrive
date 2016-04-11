package pt.tecnico.myDrive.domain;

import java.util.Set;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.domain.LoginToken;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;

public class Session extends Session_Base {
    
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
    
    
    public boolean isRootToken(String token){
    	for(User u : this.getMydrive().getUserSet()){
    		if(u.getUsername().equals("root") && u.getLogintoken().getIdentifier().equals(token)){
    				return true;
    			}
    		}
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
    			}
    		}
    	}
    	return null;
    }
    
}
