// USE MySQL AS THE DATABASE TO STORE ALL YOUR APPLICATION RECORDS

import Modules.Customer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Customer customer = new Customer("Starboy","comegetyourthings");

        try {

            while (true){
                System.out.println("============ Welcome to Quick Bite ============");
                System.out.println("1. Login\n2. Register\n3. Exit");
                int operation = input.nextInt();

                if (operation == 3) {
                    System.out.println("Thank you for using Quick Bite!");

                    break;
                }

                if (operation == 1) {
                    System.out.print("Enter username : ");
                    String username = input.next();
                    input.nextLine();
                    System.out.print("Enter password : ");
                    String password = input.nextLine();

                    String userType =  customer.login(username,password);

                    if ("Admin".equals(userType)) {
                        System.out.println("Login successful as admin!");
                        //  ENTER ADMIN MODULE LATER
                    }  else if ("Customer".equals(userType)){
                        System.out.println("Logged in successful as Customer!");

                        // ADD CUSTOMER MODULES LATER
                    } else {


                    }


                }




            }


        } catch (Exception e){
            System.out.println("Something went wrong");

        }
    }
}