#S-BPM Modelling and Execution Platform#

This is a modelling and execution platform for S-BPM processes, based on microservices powered by Spring Boot. The modelling platform is based on Angular 1, the frontend for th execution platform, however, is based on Angular 2.
Basically, the platform consists of the following modules:

 - **ProcessModelStorage:** This is the process repository.
 - **ProcessEngine:** Responsible for the execution of processes, based on Akka. 
 - **Gateway:** Authentication and authorization service and handles the request of the process execution frontend (GUI).
 - **Persistence:** Hibernate mapping of the database tables.
 - **GUI-Dev:** Development project for the Angular 2 execution platform frontend (With some dev tools, with node server backend).
 - **GUI:** Production project for the Angular 2 execution platform frontend (minified, uglified, with Spring Boot backend).
 - **sbpm-modeler:** Project for the Angular 1 modelling platform frontend.

##Setup##
###Prerequisites###

 - Java Platform (JDK) 8
 - MySQL
 - npm for Angular development

###Database Settings###
Please execute following statements to create the schema and the db user in your MySQL database:
[DB_setup.sql](Setup/DB_setup.sql)

##Startup##
###Execution Platform###
 1. Start the MySQL Service
 2. Go to ProcessModelStorage and run in cmd: 
 ```gradlew bootRun```
 3. Go to ProcessEngine and run in cmd: 
 ```gradlew bootRun```
 4. Go to Gateway and run in cmd: 
 ```gradlew bootRun```
 5. Go to GUI and run in cmd: 
 ```gradlew bootRun```
 6. Go to ```http://localhost:3000```
 
####Ports####
Basically, the following ports are used:

|  Service  |  Port  |
|  -------  |  ----- |
|  Gateway  |  10000 |
|  ProcessModelStorage  |  11000  |
|  ProcessEngine  |  14000  |
|  GUI  |  3000  |

To change the port configuration, change the server port in this file e.g. Gateway: [application.properties] (Gateway/src/main/resources/application.properties)
Make sure to change the ipconfig in the Gateway, if you change the ports of ProcessModelStorage or ProcessEngine

####User Configuration####
In general, the authentication concept ist based on RBAC. Each user can be assigned to one or more roles. Each role can be assigned to one more rules.

Currently, there *.csv files for the user configuration:
- [users.csv](Gateway/src/main/resources/users.csv)
- [roles.csv](Gateway/src/main/resources/roles.csv)
- [rules.csv](Gateway/src/main/resources/rules.csv)

###GUI-Dev###
1. run in cmd: ```npm install```
2. Go to GUI-Dev and run in cmd: ```npm start```
3. in case of any errors when starting the first time, please stick to the installation guide of [ng2-admin](https://github.com/akveo/ng2-admin/)
