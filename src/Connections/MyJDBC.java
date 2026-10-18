package Connections;

import CommonComponents.Components;
import Modules.Customer;
import Modules.Food;
import Modules.User;

import java.sql.*;
import java.util.ArrayList;

public class MyJDBC {

    // METHOD TO CREATE CONNECTION TO THE DATABASE
    public static Connection connection() {
        try {
            return  DriverManager.getConnection(Components.DB_URL,Components.DB_USERNAME,Components.DB_PASSWORD);
        } catch (SQLException e){
            System.out.print("Connection to the database failed!");
            return null;
        }
    }

    // METHOD TO REGISTER A NEW CUSTOMER
    public static boolean register(User user){
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return false;
        }

        if (usernameExists(user.getUsername())) {
            return false;
        }

        String sql = "INSERT INTO "+Components.USER_TABLE+ "(username, password, role) VALUES(?,?,?)";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)){


            preSt.setString(1, user.getUsername());
            preSt.setString(2, user.getPassword());
            preSt.setString(3, user.getRole());

            return preSt.executeUpdate() > 0;


        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }
    }

    public static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM "+Components.USER_TABLE+" WHERE username = ?";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)) {
            preSt.setString(1, username);
            try (ResultSet rs = preSt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }
    }

    // METHOD TO LOGIN
    public static String login(String username, String password){

        String sql = "SELECT role FROM "+Components.USER_TABLE + " WHERE username = ? AND password = ?";
        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)){
            preSt.setString(1, username);
            preSt.setString(2, password);

            try(ResultSet rs = preSt.executeQuery()){

                if (rs.next()){
                    return rs.getString("role");
                }
            }

        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
        }

        return null;
    }


    // METHOD TO ADD FOOD IN THE TABLE

    public static boolean addFood(Food food) {

        String sql = "INSERT INTO "+Components.FOOD_TABLE+" (foodName, price, quantity) VALUES(?, ?, ?)";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)) {

            preSt.setString(1,food.getFoodName());
            preSt.setDouble(2, food.getPrice());
            preSt.setInt(3, food.getQuantity());

            return preSt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }
    }

    // METHOD TO VIEW FOOD AND PRICE
    public static ArrayList<Food> viewAllFood(){

        ArrayList<Food> foods = new ArrayList<>();

        String sql = "SELECT * FROM "+Components.FOOD_TABLE;

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql); ResultSet rs = preSt.executeQuery()){

            while (rs.next()){
                foods.add(new Food(
                        rs.getInt("foodId"),
                        rs.getString("foodName"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
        }

        return foods;
    }

    public static Food getFoodById(int foodId) {
        String sql = "SELECT * FROM "+Components.FOOD_TABLE+" WHERE foodId = ?";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)) {
            preSt.setInt(1, foodId);
            try (ResultSet rs = preSt.executeQuery()) {
                if (rs.next()) {
                    return new Food(
                            rs.getInt("foodId"),
                            rs.getString("foodName"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong! RESTART APPLICATION");
        }

        return null;
    }

    public static boolean buyFood(int foodId, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        String selectSql = "SELECT quantity FROM "+Components.FOOD_TABLE+" WHERE foodId = ?";
        String updateSql = "UPDATE "+Components.FOOD_TABLE+" SET quantity = quantity - ? WHERE foodId = ? AND quantity >= ?";

        try (Connection connection = connection()) {
            if (connection == null) {
                return false;
            }

            try (PreparedStatement selectSt = connection.prepareStatement(selectSql)) {
                selectSt.setInt(1, foodId);
                try (ResultSet rs = selectSt.executeQuery()) {
                    if (!rs.next() || rs.getInt("quantity") < quantity) {
                        return false;
                    }
                }
            }

            try (PreparedStatement updateSt = connection.prepareStatement(updateSql)) {
                updateSt.setInt(1, quantity);
                updateSt.setInt(2, foodId);
                updateSt.setInt(3, quantity);
                return updateSt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }
    }

    // METHOD TO UPDATE FOOD NAME
    public static boolean updateFood(Food food) {

        String sql = "UPDATE "+Components.FOOD_TABLE+" SET foodName = ?, price = ? , quantity = ? WHERE foodId = ? ";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql); ){

            preSt.setString(1, food.getFoodName());
            preSt.setDouble(2, food.getPrice());
            preSt.setInt(3, food.getQuantity());
            preSt.setInt(4, food.getFoodId());
            return preSt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }



    }

    // METHOD TO DELETE FOOD
    public static boolean deleteFood(int id) {

        String sql = "DELETE FROM "+Components.FOOD_TABLE+ " WHERE foodId = ? ";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)) {

            preSt.setInt(1, id);

            return preSt.executeUpdate() > 0 ;

        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }
    }

    // METHOD TO VIEW ALL USERS
    public static ArrayList<Customer> viewAllCustomers(){

        ArrayList<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM "+Components.USER_TABLE;

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql); ResultSet rs = preSt.executeQuery();){

            while (rs.next()){
                customers.add(new Customer(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }

        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");

        }

        return customers;
    }

    // METHOD TO ADD A NEW CUSTOMER
    public static boolean addCustomer(Customer customer){
        if (customer == null || customer.getUsername() == null || customer.getPassword() == null) {
            return false;
        }

        if (usernameExists(customer.getUsername())) {
            return false;
        }

        String sql = "INSERT INTO "+Components.USER_TABLE+ " (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)){

            preSt.setString(1, customer.getUsername());
            preSt.setString(2, customer.getPassword());
            preSt.setString(3, customer.getRole());

            return preSt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }
    }

    // METHOD TO UPDATE CUSTOMER DETAILS
    public static boolean updateCustomerDetails(Customer customer){

        String sql = "UPDATE "+Components.USER_TABLE+ " SET username = ? , password = ? WHERE userId = ?";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)) {

            preSt.setString(1, customer.getUsername());
            preSt.setString(2, customer.getPassword());
            preSt.setInt(3, customer.getUserId());

            return preSt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }
    }

    // METHOD TO DELETE A CUSTOMER
    public static boolean deleteCustomer(int userId){

        String sql = "DELETE FROM "+Components.USER_TABLE+ " WHERE userId = ?";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)){

            preSt.setInt(1, userId);

            return preSt.executeUpdate() > 0;
        } catch (SQLException e){
            System.out.println("Something went wrong! RESTART APPLICATION");
            return false;
        }

    }


}