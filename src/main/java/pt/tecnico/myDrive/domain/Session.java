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
    		for (LoginToken tok : getTokensSet()){
    			if (!tok.isDateValid(new DateTime())){
    				this.removeUserFromSession(tok.getIdentifier());
    			}
    				
    		}
    	}
    }
    
    
    public boolean isRootToken(String tok){
    	for(User u : this.getMydrive().getUserSet()){
    		if(u.getUsername().equals("root") && u.getLogintoken().getIdentifier().equals(tok)){
    				return true;
    			}
    		}
    	return false;
    }
    
    public boolean hasUsernameInSession(String username){
    	if(this.hasOnlineUsers()){
    		for(LoginToken tok: this.getTokensSet()){
    			if(tok.getUser().getUsername().equals(username)){
    				return true;
    			}
    		}
    		return false;
    	}
    	return false;
    }
    
    public boolean isOnline(String token){
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
    	for (LoginToken tk: users){
    		if (tk.getUser().getUsername().equals(username))
    			return tk;
    	}
    	return null;
    }
    
    public String addUsertoSession(String username) throws UserDoesNotExistException{
    	User u = this.getMydrive().getUserByUsername(username);
    	if(u!=null){
    		LoginToken tok = new LoginToken(u);
    		this.addTokens(tok);
    		return tok.getIdentifier();
    	}
    	else{
    		throw new UserDoesNotExistException(username);
    	}
    }
    
    public String addUsertoSession(String username,String old) throws UserDoesNotExistException{
    	User u = this.getMydrive().getUserByUsername(username);
    	if(u!=null){
    		Integer randomOld = Integer.parseInt(old.substring(old.length()-1)); 
    		LoginToken tok = new LoginToken(u,randomOld);
    		this.addTokens(tok);
    		return tok.getIdentifier();
    	}
    	else{
    		throw new UserDoesNotExistException(username);
    	}
    }
    
    public void removeUserFromSession(String usertoken){
    	if(this.hasOnlineUsers()){
    		if(this.isOnline(usertoken)){
    			for(LoginToken tok : this.getTokensSet()){
    				if(tok.getIdentifier().equals(usertoken)){
    					this.removeTokens(tok);
    				}
    			}
    		}
    	}	
    }
    
    public User getUserFromSession(String usertoken){
    	if(this.hasOnlineUsers()){
    		for(LoginToken tok: this.getTokensSet()){
    			if(tok.getIdentifier().equals(usertoken)){
    				return tok.getUser();
    			}
    		}
    	}
    	return null;
    }
    
}
