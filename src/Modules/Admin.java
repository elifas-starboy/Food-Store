package Modules;

import Connections.MyJDBC;

public class Admin extends User{

    public String role;

    public Admin(String username, String password){
        super(username, password, "Admin");
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean register(){
        return false;
    }

    @Override
    public String login(String username, String password ){
        return MyJDBC.login(username, password);
    }


}
