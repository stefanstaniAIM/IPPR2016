#S-BPM Modelling and Execution Platform#

This is a modelling and execution platform for S-BPM processes, based on microservices powered by Spring Boot. The modelling platform is based on Angular 1, the frontend for the execution platform, however, is based on Angular 2.
Basically, the platform consists of the following modules:

 - **ProcessModelStorage:** This is the process repository.
 - **ProcessEngine:** Responsible for the execution of processes, based on Akka. 
 - **Gateway:** Authentication and authorization service and handles the requests of the process execution frontend (GUI).
 - **Persistence:** Hibernate mapping of the database tables.
 - **GUI-Dev:** Development project for the Angular 2 execution platform frontend (With some dev tools, with node server backend).
 - **GUI:** Production project for the Angular 2 execution platform frontend (minified, uglified, with Spring Boot backend).
 - **ModellingPlatform-Dev:** Development project for the Angular 1 modelling platform frontend (with node server backend).
 - **ModellingPlatform:** Production project for the Angular 1 modelling platform frontend (with Spring Boot backend).

##Tutorial Videos##
[ModellingPlatform](https://youtu.be/3gJXmBRKWNo)

[ExecutionPlatform](https://youtu.be/LkoJVZ__f1k)

##Functionalities##

###Execution###
| Functionality | Implemented | Comment |
| ------------ | ------------ | ------- |
| Internal Subjects | Yes | - |
| Multisubjects | No | - |
| External Subjects | No | - |
| Function States | Yes | - |
| Send States | Yes | - |
| Receive States | Yes | - |
| Timeouts | No | - |
| Refinements | Yes | partially (Engine) |
| Business Objects | Yes | - |
| Start Process | Yes | - |
| Stop Process | Yes | - |
| Nested Business Objects | Yes | partially (Engine) |
| Formbuilder | Yes | - |
| OWL-Import | Yes | standard-pass-ont |
| Connected Processes | No | - |
| Multiprocesses | No | - |

###Modelling###
| Functionality | Implemented | Comment |
| ------------ | ------------ | ------- |
|SID View|yes|-|
|SBD View|yes|-|
|Subjects|yes|-|
|Message Connector|yes|-|
|Message Types (Business Objects)|yes|-|
|Send State|yes|-|
|Receive State|yes|-|
|Function State|yes|-|
|OWL-Import|no|standard-pass-ont|
|OWL-Export|yes|-|
|ZoomIn/Out|no|-|
|Customizability|no|-|

##Setup##
###Prerequisites###

 - Java Platform (JDK) 8
 - MySQL
 
**Development only:**
 - NodeJS for Angular development in GUI-Dev and ModellingPlatform-Dev
 - Gulp for Angular development in ModellingPlatform-Dev

###Database Settings###
Please execute following statements to create the schema and the db user in your MySQL database:
[DB_setup.sql](Setup/DB_setup.sql)

##Startup##
###Start everything (Windows)###
 1. Start the MySQL Service
 2. Go to [Setup/start_windows](Setup/start_windows)
 3. Open [start.bat](Setup/start_windows/start.bat)

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
 
####Alternative####
If you prefer to run the jar files, without using gradlew:
 1. Start the MySQL Service
 2. Go to [builds](builds)
 3. run in cmd: ```java -jar Gateway-0.0.1-SNAPSHOT.jar```
 4. run in cmd: ```java -jar pmstorage-0.0.1-SNAPSHOT.jar```
 5. run in cmd: ```java -jar processengine-0.0.1-SNAPSHOT.jar```
 6. run in cmd: ```java -jar gui-0.0.1-SNAPSHOT.jar```
 7. Go to ```http://localhost:3000```

**Note:** If you do any changes to a module, you have to rebuild it with ```gradlew build``` and use the *.jar that is generated in ```(name of module)/build/libs/```
 
###Modelling Platform###
 1. Go to ModellingPlatform and run in cmd: 
 ```gradlew bootRun```
 2. Go to ```http://localhost:4000```
 
####Alternative####
If you prefer to run the jar files, without using gradlew:
 1. Go to [builds](builds)
 2. run in cmd: ```java -jar ModellingPlatform-0.0.1-SNAPSHOT.jar```
 3. Go to ```http://localhost:4000```
 


###GUI-Dev###
Just for development purposes, not for production!
 1. run in cmd: ```npm install```
 2. Go to GUI-Dev and run in cmd: ```npm start```
 3. in case of any errors when starting the first time, please stick to the installation guide of [ng2-admin](https://github.com/akveo/ng2-admin/)
 4. Go to ```http://localhost:3000```

###ModellingPlatform-Dev###
Just for development purposes, not for production!
 1. Run ```npm install -g yo bower grunt-cli gulp && npm install && bower install``` to install required dependencies.
 2. Go to ModellingPlatform-Dev and run in cmd: ```npm install && bower install```
 3. Run in cmd: ```gulp serve```
 4. Go to ```http://localhost:3000```
 
##Configuration##

###Ports###
Basically, the following ports are used:

|  Service  |  Port  |
|  -------  |  ----- |
|  Gateway  |  10000 |
|  ProcessModelStorage  |  11000  |
|  ProcessEngine  |  14000  |
|  GUI  |  3000  |
|  ModellingPlatform  |  4000  |

To change the port configuration, change the server port in this file e.g. Gateway: [application.properties] (Gateway/src/main/resources/application.properties)

**Note:** Make sure to change the ipconfig in the Gateway, if you change the ports of ProcessModelStorage or ProcessEngine.

**Note:** If you change the port of the Gateway, make sure to change the restApi configuration in [processes.service.ts](GUI-Dev/src/app/processes.service.ts) and to rebuild the GUI-Dev and GUI project according to the guide provide [below](#gui).

###User Configuration###
In general, the authentication concept ist based on RBAC. Each user can be assigned to one or more roles. Each role can be assigned to one more rules.

The current *.csv files for the user configuration:
- [users.csv](Gateway/src/main/resources/memoryusers/users.csv)
- [roles.csv](Gateway/src/main/resources/memoryusers/roles.csv)
- [rules.csv](Gateway/src/main/resources/memoryusers/rules.csv)

#####Standard configuration:#####

|  User  |  Roles  |  Password  |
|  ----  |  -----  |  --------  |
|  robert  |  BOSS  |  1234  |
|  matthias  |  ADMIN, EMPLOYEE  |  1234  |
|  stefan  |  EMPLOYEE  |  1234  |
|  maksym  |  EMPLOYEE  |  1234  |

##Development##
###Spring Boot Modules###
Any further development concerning the Gateway, the ProcessModelStorage or the ProcessEngine can be done directly in java. For further information please use the [Spring Boot Documentation](https://projects.spring.io/spring-boot/)

**Note:** If you do any changes to a module, you have to rebuild it with ```gradlew build```

###GUI###
Any further development concerning the GUI has to be done in the GUI-Dev module by using typescript and Angular 2. For further information please use the [Angular Documentation](https://angular.io/) and the [ng2-admin Documentation](https://akveo.github.io/ng2-admin/articles/001-getting-started/).

**Note:** If you do any changes to the GUI-Dev project, you have to update the GUI project afterwards:
 1. Go to GUI-Dev and run in cmd: ```npm run prebuild:prod && npm run build:prod```
 2. Go to [GUI/src/main/resources/public/](GUI/src/main/resources/public/) and remove of all of its content.
 3. Go to [GUI-Dev/dist](GUI-Dev/dist) and move all of its content to [GUI/src/main/resources/public/](GUI/src/main/resources/public/).
 4. Go to [GUI](GUI) and run ```gradlew build``` or ```gradlew run```
 
###ModellingPlatform###
Any further development concerning the ModellingPlatform has to be done in the ModellingPlatform-Dev module by using javascript and Angular 1.

**Note:** If you do any changes to the ModellingPlatform-Dev project, you have to update the ModellingPlatform project afterwards:
 1. Go to ModellingPlatform-Dev and run in cmd: ```gulp build```
 2. Go to [ModellingPlatform/src/main/resources/public/](ModellingPlatform/src/main/resources/public/) and remove of all of its content.
 3. Go to [ModellingPlatform-Dev/dist](ModellingPlatform-Dev/dist) and move all of its content to [ModellingPlatform/src/main/resources/public/](ModellingPlatform/src/main/resources/public/).
 4. Go to [ModellingPlatform](ModellingPlatform) and run ```gradlew build``` or ```gradlew run```
 
**Note:** You can change the appearance of the elements by modify the following file: [ui-joint-shape.template.scss](ModellingPlatform-dev/src/app/components/services/ui-joint/ui-joint-shape/ui-joint-shape.template.scss)
 
##License##
[MIT License](LICENSE)
