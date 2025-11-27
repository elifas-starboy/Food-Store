# Command Line Food Store
## Requirements 
MySQL and Jave
## MySQL command to run
Create database foot_store; <br> <br>
CREATE TABLE users( <br>
userId INT NOT NULL AUTO_INCREMENT, <br>
username VARCHAR(50) NOT NULL, <br>
password VARCHAR(50) NOT NULL, <br>
role VARCHAR(20) NOT NULL, <br>
PRIMARY KEY (userId)
<br>);
<br>

CREATE TABLE food (<br>
foodId INT NOT NULL AUTO_INCREMENT, <br>
foodName VARCHAR(50) NOT NULL, <br>
price DECIMAL(10,2) NOT NULL, <br>
quantity INT NOT NULL, <br>
PRIMARY KEY (foodId)
<br>);


