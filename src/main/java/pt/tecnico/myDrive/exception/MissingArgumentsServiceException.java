package pt.tecnico.myDrive.exception;


public class MissingArgumentsServiceException extends MyDriveException {

    String serviceName;
    String msg;

    public MissingArgumentsServiceException(String serviceName, String msg){
        super("Error in arguments of " + serviceName +". " + msg);
        this.serviceName = serviceName;
        this.msg = msg;
    }

    public MissingArgumentsServiceException(String serviceName){
        super(serviceName);
    }

}
