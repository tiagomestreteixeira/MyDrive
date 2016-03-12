package pt.tecnico.myDrive.domain;

import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.NoPermissionException;

import java.util.Set;

public class MyDrive extends MyDrive_Base {
    
    private MyDrive() {
        setRoot(FenixFramework.getDomainRoot());
    }

    public static MyDrive getInstance() {
        MyDrive md = FenixFramework.getDomainRoot().getMyDrive();
        if (md == null)
            return new MyDrive();

        return md;
    }

    public User getUserByUsername(String username) {
        for (User user : getUserSet()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    @Override
    public void addUser(User user) {
        // TODO: Make necessary checks
        super.addUser(user);
    }

    @Override
    public void removeUser(User user) {
        // TODO: User deletes itself
        super.removeUser(user);
    }

    @Override
    public Set<User> getUserSet() {
        // TODO: Check if access should be allowed
        return super.getUserSet();
    }

    @Override
    public Integer getIdCounter() {
        // TODO: Check if access should be allowed
        return super.getIdCounter();
    }

    @Override
    public void setIdCounter(Integer idCounter) {
        // TODO: Check if access should be allowed
        super.setIdCounter(idCounter);
    }

    public int getNewId() {
        int id = super.getIdCounter();
        id++;
        super.setIdCounter(id);
        return id;
    }

    @Override
    public void setRoot(DomainRoot root) throws MyDriveException {
        //throw new NoPermissionException("setRoot");
    }

}
