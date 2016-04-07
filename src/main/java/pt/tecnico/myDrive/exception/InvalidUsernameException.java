package pt.tecnico.myDrive.exception;

public class InvalidUsernameException extends MyDriveException {

   private static final long serialVersionUID = 1L;

    private String username;
    private String errorMessage;

    public InvalidUsernameException(String username, String errorMessage) {
        this.errorMessage = errorMessage;
        this.username = username;
    }



    @Override
    public String getMessage() {
        return "Usernames cannot be empty or contain " +
                "non-alphanumeric characters." + "Invalid username: " + username + errorMessage;
    }
}