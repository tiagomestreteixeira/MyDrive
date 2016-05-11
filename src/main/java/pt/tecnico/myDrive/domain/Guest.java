package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class Guest extends Guest_Base {

	public Guest(MyDrive md) {
		init(md, "nobody", "Guest", "rwxdr-x-", null);
	}

	@Override
	protected void init(MyDrive md, String username, String name, String umask, String password) {
		md.addUser(this);
		super.setUsername(username);
		super.setName(name);
		super.setUmask(umask);
		setHomeDir(md.getSuperUser().makeDir("/home/" + username));
		getHomeDir().setOwner(md.getSuperUser(), this);
		getHomeDir().setPermissions(umask);
	}

	@Override
	public boolean checkPassword(String attempt) {
		return true;
	}

	@Override
	public void setPassword(String pass) throws MyDriveException {
		throw new NoPermissionException("Guest.setPassword()");
	}

	@Override
	public void setUsername(String username) {
		throw new NoPermissionException("Guest.setUsername()");
	}

	@Override
	public void setName(String name) {
		throw new NoPermissionException("Guest.setName()");
	}

	@Override
	public void setUmask(String umask) {
		throw new NoPermissionException("Guest.setUmask()");
	}

	@Override
	public void remove() throws MyDriveException {
		throw new NoPermissionException("Guest.remove()");
	}

	@Override
	public boolean checkPermission(File file, Character c){
		if (file.isOwner(this)){
			switch (c){
				case 'r':
					return file.getPermissions().matches("r.......");
				case 'w':
					return false;
				case 'x':
					return file.getPermissions().matches("..x.....");
				case 'd':
					return false;
			}
		}
		switch (c){
			case 'r':
				return file.getPermissions().matches("....r...");
			case 'w':
				return false;
			case 'x':
				return file.getPermissions().matches("......x.");
			case 'd':
				return false;
		}
		return false;
	}

	@Override
	public boolean isLoginValid(DateTime loginDate) {
		return true;
	}
}


