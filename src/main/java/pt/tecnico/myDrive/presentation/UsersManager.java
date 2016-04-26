package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.exception.UserManagerPresentationException;

import java.util.HashMap;

public class UsersManager {

    private HashMap<String,Long> users;
    private String currentUsername;
    private Long currentToken;

    private static final String GET_TOKEN_MSG = "Presentation : getTokenByUsername - ";
    private static final String UPDATE_TOKEN_MSG = "Presentation : updateToken - ";
    private static final String SET_CURRENT_USERNAME_MSG  = "Presentation : setCurrentUsername - ";
    private static final String SET_CURRENT_TOKEN_MSG  = "Presentation : setCurrentToken - \"Illegal usage. Use updateToken(username,token) instead.\n";

    private static final String NOT_LOGGED_SYSTEM_MSG = " - has not been previously logged into the system.\n\n";

    private static final String GET_TOKEN_HELP_MSG = "\t\t- First add username and its token : addUser(";
    private static final String UPDATE_TOKEN_HELP_MSG = GET_TOKEN_HELP_MSG;

    public UsersManager(){
        users = new HashMap<String, Long>();
    }

    private void checkUserExistence(String username, String exMsg) throws UserManagerPresentationException {
        if (!users.containsKey(username))
            throw new UserManagerPresentationException(exMsg);
    }


    // TODO: sequence use case
    // - login a pass
    // - login a pass -> Updates the token or throws Exception?
    // throws exception Implementation:
    //public void addUser(String username,Long token) throws Exception{
    //    if(users.containsKey(username))
    //        throw new Exception(ADD_USER_MSG + username
    //                + HAS_LOGGED_SYSTEM_MSG  + ADD_USER_HELP_MSG + username + ",token[Long])\n");
    //    users.put(username,token);
    //}
    // Updates the token Implementation:
    public void addUser(String username,Long token) {
        users.put(username,token);
        currentUsername = username;
        currentToken = token;
    }


    public Long getTokenByUsername(String username) throws UserManagerPresentationException {
        checkUserExistence(username,GET_TOKEN_MSG + username + NOT_LOGGED_SYSTEM_MSG + GET_TOKEN_HELP_MSG
                + username + ",token[Long])\n");

        return users.get(username);
    }

    public void updateTokenByUsername(String username,Long token) throws UserManagerPresentationException{
        checkUserExistence(username,UPDATE_TOKEN_MSG + username
                + NOT_LOGGED_SYSTEM_MSG + UPDATE_TOKEN_HELP_MSG + username + ",token[Long])\n");

        users.put(username,token);
    }

    public void setCurrentUsername(String username) throws UserManagerPresentationException{
        checkUserExistence(username,SET_CURRENT_USERNAME_MSG + username + NOT_LOGGED_SYSTEM_MSG + "\n");

        currentUsername = username;
        currentToken = users.get(username);
    }

    public void setCurrentToken(Long token) throws UserManagerPresentationException {
        throw new UserManagerPresentationException(SET_CURRENT_TOKEN_MSG);
    }

    public String getCurrentUsername(){
        return currentUsername;
    }

    public Long getCurrentToken(){
        return currentToken;
    }

}
