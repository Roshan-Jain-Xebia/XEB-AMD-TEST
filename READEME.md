#CRUD Web Application 
Maven project is implemented a Spring based web application with basic security and its
exposing the following APIs 
#####Swagger 
http://localhost:9090/swagger-ui.html#/user-controller

• GET\
    To get all user details http://localhost:9090/user\
    To get specific user details http://localhost:9090/user/{id}\
    
• POST\
    To add new user http://localhost:9090/user/ with request data\
    {\
      "username": "testUser",\
      "password": "*********",\
      "status": "Activated"\
    }\
    
• PUT\
    To update user details with request data http://localhost:9090/user/{id}\
    {\
       "username": "testUser",\
       "password": "*********",\
       "status": "Deactivated"\
    }\

• DELETE\
    To delete user details http://localhost:9090/user/{id}\


###Tech and Tool Stack
Maven\
Java\
Spring\
Spring Boot\
Spring Security\
Hibernate\
H2\
Spring Test\
Jacoco
Swagger
Intellij

###Build and Deployment 
build  war  >> mvn install\
run the application >> mvn spring-boot:run\

###Application Access
http://localhost:9090/

###H2 DB Access
http://localhost:9090/h2
*****
Saved Settings=Generic H2 (Embedded)\
Driver Class=org.h2.Driver\
JDBC URL=jdbc:h2:mem:testuserdb\
User Name=sa
Password=root
*****

###Code Coverage
http://localhost:63342/assignment/target/site/jacoco/index.html\

###Security Details
Two users are configured in memory with following credentials and role\

| Username  | Password | Role |
| ------------- | ------------- |------------- |
| user | User@1234 | USER |
| admin  | Admin@1234  | ADMIN |

