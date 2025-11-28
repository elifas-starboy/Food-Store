// USE MySQL AS THE DATABASE TO STORE ALL YOUR APPLICATION RECORDS

import Connections.MyJDBC;
import Modules.Admin;
import Modules.Customer;
import Modules.Food;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Customer customer = new Customer(0,"","");

        try {

            while (true){
                System.out.println("\n============ Welcome to Quick Bite ============");
                System.out.print("1. Login\n2. Register\n3. Exit\n: ");
                int operation = input.nextInt();

                if (operation == 3) {
                    System.out.println("Thank you for using Quick Bite!");

                    break;
                }


                if (operation == 1) {
                    // Login
                    System.out.print("Enter username : ");
                    String username = input.next().trim();
                    input.nextLine();
                    System.out.print("Enter password : ");
                    String password = input.nextLine().trim();


                    String userType =  customer.login(username,password);

                    if ("Admin".equals(userType)) {
                        System.out.print("\nLogged in Successfully!\n");

                        //  ENTER ADMIN MODULE LATER

                        while (true) {

                            System.out.println("\n============ Welcome To The Admin Panel ============");
                            System.out.print("1.Manage Food\n2.Manage Customers\n3.Logout\n: ");
                            int adminChoice = input.nextInt();

                            if (adminChoice == 1 ) {

                                try {
                                    while (true) {
                                        System.out.println("\n============ Welcome To The Food Panel ============");
                                        System.out.print("1.View All Food\n2.Add New Food\n3.Update Food\n4.Remove Food\n5.Logout this panel\n: ");
                                        int adminInput = input.nextInt();

                                        if (adminInput == 1) {
                                            System.out.println("\nMenu");
                                            for(Food viewFood: MyJDBC.viewAllFood()){
                                                System.out.println(viewFood);
                                            }
                                            System.out.println();

                                        } else if (adminInput == 2) {
                                            System.out.print("Enter Food Name : ");
                                            String foodName = input.next().trim();
                                            input.nextLine();
                                            System.out.print("Enter Food price : ");
                                            double price = input.nextDouble();
                                            input.nextLine();
                                            System.out.print("Enter quantity : ");
                                            int quantity = input.nextInt();

                                            Food newFood = new Food(0,foodName,price, quantity);

                                            if (newFood.addFood()){

                                                System.out.print("\nFood added successfully!\n");
                                            } else {
                                                System.out.print("\nSomething went wrong try again!\n");
                                            }

                                        } else if (adminInput == 3){
                                            // ADD UPDATE FOOD LATER
                                            System.out.print("Enter food ID : ");
                                            int foodId = input.nextInt();
                                            input.nextLine();
                                            System.out.print("Enter new food name : ");
                                            String foodName = input.next();
                                            input.nextLine();
                                            System.out.print("Enter new price : ");
                                            double price = input.nextDouble();
                                            input.nextLine();
                                            System.out.print("Enter new quantity : ");
                                            int quantity = input.nextInt();
                                            input.nextLine();

                                            Food updatedFood = new Food(foodId,foodName,price,quantity);

                                            if (updatedFood.updateFood()){
                                                System.out.print("\nUpdated food successful!\n");
                                            } else {
                                                System.out.print("\nSomething went wrong try again!\n");
                                            }


                                        } else if (adminInput == 4) {
                                            System.out.print("\nEnter Food Id To Be Deleted : ");
                                            int id = input.nextInt();

                                            Food deleteFood = new Food(0,"",0.0,0);

                                            if(deleteFood.deleteFood(id)){
                                                System.out.print("\nFood of id = "+id+" Deleted successfully\n");
                                            } else {
                                                System.out.print("\nSomething went try again\n");
                                            }

                                        } else if (adminInput == 5){
                                            System.out.print("\nLogged out the food panel!\n");
                                            break;
                                        }

                                    }
                                } catch (Exception e){
                                    System.out.println("Something went wrong try again!");
                                }

                            } else if ( adminChoice == 2 ){
                                // Add Admin customer manage module later
                                try {
                                    while (true) {
                                        System.out.println("\n============ Welcome To The Customer Panel ============");
                                        System.out.print("1.View all customers\n2.Add new customer\n3.Update customer details\n4.Delete a customer\n5.Logout this panel\n: ");
                                        int adminCustomer = input.nextInt();

                                        if (adminCustomer == 1){
                                            System.out.println("\nUsers");
                                            for(Customer customer1: MyJDBC.viewAllCustomers()){
                                                System.out.println(customer1);
                                            }
                                        } else if (adminCustomer == 2) {
                                            System.out.print("\nEnter new username : ");
                                            String newUsername = input.next().trim();
                                            input.nextLine();
                                            System.out.print("\nEnter password : ");
                                            String newPassword = input.next().trim();

                                            Customer newCustomer = new Customer(0,newUsername,newPassword);

                                            if (newCustomer.addNewCustomer()){
                                                System.out.println("\nCustomer added successfully!");
                                            } else {
                                                System.out.println("\nSomething went wrong try again!");
                                            }

                                        } else if (adminCustomer == 3) {
                                            // ADD MODULE TO UPDATE CUSTOMER DETAILS

                                        } else if (adminCustomer == 4) {
                                            // ADD A MODULE TO DELETE A CUSTOMER
                                        } else if (adminChoice == 5) {
                                            System.out.println("\nLogged out the customer panel!");
                                            break;
                                        } else {
                                            System.out.println("\nInvalid Input Try Again!");
                                        }
                                    }
                                } catch (Exception e){
                                    System.out.println("Something went wrong try again!");
                                }

                            } else if (adminChoice == 3){
                                System.out.println("\nLogged out the admin panel!");
                                break;
                            } else {
                                System.out.println("\nInvalid Input Try Again!");
                            }

                        }


                    }  else if ("Customer".equals(userType)){
                        System.out.print("\nLogged in successfully!\n");


                        // ADD CUSTOMER MODULES LATER
                    } else {

                        System.out.print("\nInvalid username or password! Try again!\n");

                    }


                } else if (operation == 2) {
                    // Register new customer
                    System.out.print("Enter new username : ");
                    String username = input.next().trim();
                    input.nextLine();
                    System.out.print("Enter new password : ");
                    String password = input.next().trim();
                    input.nextLine();
                    System.out.print("Verify password : ");
                    String verifyPassword = input.next().trim();


                    // checking if the password match
                    if (password.equals(verifyPassword)) {
                        Customer newCustomer = new Customer(0,username,password);
                        if (newCustomer.register()){
                            System.out.println("Registered Successfully !");
                        } else {
                            System.out.println("Registration Failed! ");
                        }
                    } else {
                        System.out.println("Password does not match! Try again!");
                    }

                }

            }

        } catch (Exception e){
            System.out.println("Something went wrong! RESTART APPLICATION");

        }
    }
}