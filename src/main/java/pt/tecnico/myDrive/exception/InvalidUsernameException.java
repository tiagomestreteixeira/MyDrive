package pt.tecnico.myDrive.exception;

public class InvalidUsernameException extends MyDriveException {

   private static final long serialVersionUID = 1L;

    private String username;

    public InvalidUsernameException(String username) {
        username = username;
    }

    public String getInvalidUsernameException() {
        return username;
    }

    @Override
    public String getMessage() {
        return "Usernames cannot be empty and cannot contain " +
                "non-alphanumeric characters." + "Invalid username: " + username;
    }
}