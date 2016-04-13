package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.domain.*;



public class LoginUserService extends MyDriveService {
	 private String username;
	 private String password;
	 private MyDrive md;
	 private long token;
	
	public LoginUserService(String username, String Password) {
	    this.username=username;
	    this.password=password;
	    this.md=MyDrive.getInstance();
	}
	    @Override
		protected void dispatch() throws MyDriveException {
	    
	    md.getUserByUsername(username);
	    User user = md.getUserByUsername(username);
	    
	    String pass= user.getPassword();
	    
	    if(pass == null || !(pass.equals(this.password))){
	      	user.setPassword(password);
	    }
	    token = md.createLogin(this.username, this.password);
			
		}
	
	  
		protected void validateAndAuthorize(String token) {
			return;
		}
	  
	
		protected void checkAccess() {
			return;
		}


	public long result() {
		return 0;
	}
}

