package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class SuperUser extends SuperUser_Base {
    public SuperUser(MyDrive md) {
        setUsername("root");
        setName("root");
        setPasswordInternal("***");
        setUmask("rwxdr-x-");
        md.addUser(this);
    }

    @Override
    public boolean checkPermission(File file, Character c) {
        return true;
    }

    public boolean setPermissions(File file, String newPermissions) {
        file.setPermissions(newPermissions);
        return true;
    }

	@Override
    public void remove() throws MyDriveException {
        throw new NoPermissionException("SuperUser.remove()");
    }

    @Override
    public boolean isLoginValid(DateTime loginDate) {
        return loginDate.plusMinutes(10).isAfterNow();
    }
}



