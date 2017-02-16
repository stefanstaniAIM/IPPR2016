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
