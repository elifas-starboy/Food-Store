package Modules;

import Connections.MyJDBC;

public class Customer extends User{


    public Customer(String username, String password){
        super(username, password,"Customer");

    }

    // ADD METHODS TO LOGIN AND REGISTER LATER

    @Override
    public boolean register(){
        return MyJDBC.register(this);
    }

    @Override
    public String login(String username, String password ){
        return MyJDBC.login(username,password);
    }
}
