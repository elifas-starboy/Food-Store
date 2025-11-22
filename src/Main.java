// USE MySQL AS THE DATABASE TO STORE ALL YOUR APPLICATION RECORDS

import Modules.Customer;
import Modules.Food;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Customer customer = new Customer("","");

        try {

            while (true){
                System.out.println("============ Welcome to Quick Bite ============");
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
                        System.out.println("Logged in Successfully!\n");

                        //  ENTER ADMIN MODULE LATER
                        System.out.print("1.View All Food\n2.Add New Food\n3.Update Food price\n4.Update Food Name\n5.Remove Food\n6.Logout");
                        int adminInput = input.nextInt();

                        Food food = new Food("",0.00);


                        if (adminInput == 1) {

                        } else if (adminInput == 2) {
                            System.out.print("Enter Food Name : ");
                            String foodName = input.next().trim();
                            input.nextLine();
                            System.out.print("Enter Food price : ");
                            double price = input.nextDouble();

                        }




                    }  else if ("Customer".equals(userType)){
                        System.out.println("Logged in successfully!\n");


                        // ADD CUSTOMER MODULES LATER
                    } else {

                        System.out.println("Invalid username or password! Try again!\n");

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
                        Customer newCustomer = new Customer(username,password);
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
            System.out.println("Something went wrong");

        }
    }
}