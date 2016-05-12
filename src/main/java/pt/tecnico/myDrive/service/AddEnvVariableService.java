package pt.tecnico.myDrive.service;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.MyDriveException;

import java.util.HashMap;

public class AddEnvVariableService extends MyDriveService {
    private Long token;
    private String name;
    private String value;

    public AddEnvVariableService(Long token, String name, String value){
        this.token = token;
        this.name = name;
        this.value = value;
    }

    @Override
    protected void dispatch() throws MyDriveException {
    // TODO: Mockup to AddEnvVariableTest
    }

    public HashMap<String,String> result() {
        return new HashMap<>();
    }
}
