package Modules;

import Connections.MyJDBC;

public class Customer extends User{

    private int userId;


    public Customer(int userId,String username, String password){
        super(username, password,"Customer");
        this.userId = userId;

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public boolean addNewCustomer(){
        return MyJDBC.addCustomer(this);
    }

    public boolean updateCustomerDetails(){
        return MyJDBC.updateCustomerDetails(this);
    }
    public boolean deleteCustomer(int userId){
        return MyJDBC.deleteCustomer(userId);
    }

    @Override
    public String toString(){
        return "userId : "+userId+" | Username : "+getUsername()+" | Password : "+ getPassword();
    }
}
