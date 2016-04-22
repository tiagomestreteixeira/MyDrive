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
		setUsername(username);
		setName(name);
		setUmask(umask);
		setHomeDir(md.getSuperUser().makeDir("/home/" + username));
		getHomeDir().setOwner(md.getSuperUser(), this);
		getHomeDir().setPermissions(umask);
	}

	@Override
	public boolean checkPassword(String attempt) {
		return true;
	}

	@Override
	public DateTime getLoginExpiration() {
		return new DateTime(9999, 0, 0, 0, 0, 0, 0);
	}

	@Override
	public void setPassword(String attempt) throws MyDriveException {
		throw new NoPermissionException("Guest.setPassword()");
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
}


