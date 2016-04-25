package pt.tecnico.myDrive.presentation;

import java.util.HashMap;

public class UsersManager {

    private HashMap<String,Long> users;
    private String currentUsername;
    private Long currentToken;

    private static final String GET_TOKEN_MSG = "Presentation : addUser - UserManager ";
    private static final String UPDATE_TOKEN_MSG = "Presentation : updateToken - UserManager ";
    private static final String SET_CURRENT_USERNAME_MSG  = "Presentation : setCurrentUsername - User ";
    private static final String SET_CURRENT_TOKEN_MSG  = "Presentation : setCurrentToken - User \"Illegal usage. Use updateToken(username,token) instead.\\n\"";

    private static final String NOT_LOGGED_SYSTEM_MSG = " has not been logged into the system.\n\n";

    private static final String GET_TOKEN_HELP_MSG = "\t\t- First add username and its token : addUser(";
    private static final String UPDATE_TOKEN_HELP_MSG = GET_TOKEN_HELP_MSG;

    public UsersManager(){
        users = new HashMap<String, Long>();
    }

    private void checkExistence(String username, String exMsg) throws Exception
    {
        if (!users.containsKey(username))
            throw new Exception(exMsg);
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
    public void addUser(String username,Long token) throws Exception{
        users.put(username,token);
    }


    public Long getToken(String username) throws Exception{
        checkExistence(username,GET_TOKEN_MSG + username + NOT_LOGGED_SYSTEM_MSG + GET_TOKEN_HELP_MSG
                + username + ",token[Long])\n");

        return users.get(username);
    }

    public void updateToken(String username,Long token) throws Exception{
        checkExistence(username,UPDATE_TOKEN_MSG + username
                + NOT_LOGGED_SYSTEM_MSG + UPDATE_TOKEN_HELP_MSG + username + ",token)\n");

        users.put(username,token);
    }
    
    public String getCurrentUsername(){
        return currentUsername;
    }

    public Long getCurrentToken(){
        return currentToken;
    }

}
