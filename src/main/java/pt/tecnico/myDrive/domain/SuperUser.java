package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

public class SuperUser extends SuperUser_Base {
    public SuperUser(MyDrive md) {
        super.setUsername("root");
        super.setName("root");
        super.setUmask("rwxdr-x-");
        super.setPasswordInternal("***");
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
    public void setPassword(String pass) throws MyDriveException {
        throw new NoPermissionException("Guest.setPassword()");
    }

    @Override
    public void setUsername(String username) {
        throw new NoPermissionException("SuperUser.setUsername()");
    }

    @Override
    public void setName(String name) {
        throw new NoPermissionException("SuperUser.setName()");
    }

    @Override
    public void setUmask(String umask) {
        throw new NoPermissionException("SuperUser.setUmask()");
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



