package pt.tecnico.myDrive.presentation;

import java.util.HashMap;

public class Users {

    private HashMap<String,Long> users;

    private static final String ADD_USER_MSG  = "Presentation : addUser - User ";
    private static final String GET_TOKEN_MSG = "Presentation : addUser - User ";
    private static final String UPDATE_TOKEN_MSG = "Presentation : updateToken - User ";

    private static final String HAS_LOGGED_SYSTEM_MSG = " has already benn logged into the system.\n\n";
    private static final String NOT_LOGGED_SYSTEM_MSG = " has not been logged into the system.\n\n";

    private static final String ADD_USER_HELP_MSG = "\t\t- Just want to update it's token? : updateToken(";
    private static final String GET_TOKEN_HELP_MSG = "\t\t- First add username and its token : addUser(";
    private static final String UPDATE_TOKEN_HELP_MSG = GET_TOKEN_HELP_MSG;

    public Users(){
        users = new HashMap<String, Long>();
    }

    public void addUser(String username,Long token) throws Exception{
        if(users.containsKey(username))
            throw new Exception(ADD_USER_MSG + username
                    + HAS_LOGGED_SYSTEM_MSG  + ADD_USER_HELP_MSG + username + ",token[Long])\n");
        users.put(username,token);
    }

    public Long getToken(String username) throws Exception{
        if(!users.containsKey(username))
            throw new Exception(GET_TOKEN_MSG + username
                    + NOT_LOGGED_SYSTEM_MSG + GET_TOKEN_HELP_MSG + username + ",token[Long])\n");

        return users.get(username);
    }

    public void updateToken(String username,Long token) throws Exception{
        if(!users.containsKey(username))
            throw new Exception(UPDATE_TOKEN_MSG + username
                    + NOT_LOGGED_SYSTEM_MSG + UPDATE_TOKEN_HELP_MSG + username + ",token)\n");

        users.put(username,token);
    }

}
