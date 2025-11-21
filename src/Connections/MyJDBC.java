package Connections;

import CommonComponents.Components;
import Modules.User;

import java.sql.*;

public class MyJDBC {

    // METHOD TO CREATE CONNECTION TO THE DATABASE
    public static Connection connection() {
        try {
            return  DriverManager.getConnection(Components.DB_URL,Components.DB_USERNAME,Components.DB_PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    // METHOD TO REGISTER USERS
    public static boolean register(User user){

        String sql = "INSERT INTO "+Components.USER_TABLE+ "(username, password, role) VALUES(?,?,?)";

        try (Connection connection = connection(); PreparedStatement preSt = connection.prepareStatement(sql)){


            preSt.setString(1, user.getUsername());
            preSt.setString(2, user.getPassword());
            preSt.setString(3, user.getRole());

            return preSt.executeUpdate() > 0;


        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

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
            e.printStackTrace();
        }

        return null;
    }

}