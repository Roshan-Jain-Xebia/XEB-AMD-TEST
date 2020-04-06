<h1>CRUD Web Application</h1>
Maven project is implemented a Spring based web application with basic security and its
exposing the following APIs

<h3>Swagger</h3>
http://localhost:9090/swagger-ui.html#/user-controller

• GET\
    To get all user details http://localhost:9090/user \
    To get specific user details http://localhost:9090/user/{id}
    
• POST\
    To add new user http://localhost:9090/user/ with request data\
    {\
      "username": "testUser",\
      "password": "password",\
      "status": "Activated"\
    }
    
• PUT\
    To update user details with request data http://localhost:9090/user/{id} \
    {\
       "username": "testUser",\
       "password": "password",\
       "status": "Deactivated"\
    }

• DELETE\
    To delete user details http://localhost:9090/user/{id}


<h3>Tech and Tool Stack</h3>
Maven\
Java\
Spring\
Spring Boot\
Spring Security\
Hibernate\
H2\
Spring Test\
Jacoco\
Swagger\
Intellij\

<h3>Build and Deployment</h3>
build  war  >> mvn install\
run the application >> mvn spring-boot:run\

<h3>Application Access</h3>
http://localhost:9090/

<h3>H2 DB Access</h3>
http://localhost:9090/h2
*****
Saved Settings=Generic H2 (Embedded)\
Driver Class=org.h2.Driver\
JDBC URL=jdbc:h2:mem:testuserdb\
User Name=sa
Password=root
*****

<h3>Code Coverage</h3>
http://localhost:63342/assignment/target/site/jacoco/index.html

<h3>Security Details</h3>
Two users are configured in memory with following credentials and role\

| Username  | Password | Role |
| ------------- | ------------- |------------- |
| user | User@1234 | USER |
| admin  | Admin@1234  | ADMIN |


