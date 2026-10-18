# Command Line Food Store
## Requirements
- Java JDK
- MySQL server

## Database setup
Create the database and required tables:

```sql
CREATE DATABASE food_store;

USE food_store;

CREATE TABLE users (
    userId INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (userId)
);

CREATE TABLE food (
    foodId INT NOT NULL AUTO_INCREMENT,
    foodName VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (foodId)
);
```

## Features
- Customer registration and login
- Admin panel to view, add, update and delete food items
- Customer panel to view the menu and place orders
- Inventory auto-updates after each successful purchase
- Swing GUI for login, registration, and management screens

## Running the project
Compile the Java files and run the main class. The app opens a Java Swing GUI by default:

```bash
javac -cp lib/mysql-connector-j-9.3.0.jar -d out/production/"Online Food Store" src/**/*.java
java -cp "out/production/Online Food Store;lib/mysql-connector-j-9.3.0.jar" Main
```

If you are using a different MySQL username/password, update the values in `src/CommonComponents/Components.java` before running the app.


