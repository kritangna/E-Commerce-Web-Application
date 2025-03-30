# E-Commerce-Web-Application
Building an E-Commerce Web Application using Java, Spring Boot, Hibernate, Spring Security, PostgreSQL

**Requirements for User Management**
1. User registration, authentication, and profile management.
2. Password encryption and secure authentication mechanisms (e.g., JWT).
3. Role-based access control for different user types (admin, customer).


**Solution**
1. Developed a User Registration API to register new users while ensuring they are not already present in the database.
2. Implemented authentication and profile management using Spring Security and JWT for secure access.
3. Encrypted passwords using BCrypt to enhance security.
4. Integrated role-based access control (RBAC) to differentiate permissions for admins and customers.
5. Designed a JWT-based authentication mechanism to securely manage user sessions and authorization.

**Role-Based-Access-Control for ADMIN and CUSTOMER**

**1. ROLE_ADMIN**

  a. Built REST API to login -> POST Method
    
    - A user with ADMIN privelleges can login if the user details are present in the DB.
  
  b. Built REST API to Get All Users -> GET Method
    
    - A user with ADMIN privelleges after logging in can fetch the list of users present in the DB

 c. Built REST API to Get a user based on ID present in the DB -> GET Method
    
    - A user with ADMIN privelleges after logging in can get the individual details of the users present in the DB.
    - A user with ADMIN privelleges after logging in tries to fetch user details with the given ID which is not present in the DB, handled this with a Custom     
      Exception 

 d. Built REST API to update the user based on the ID -> PUT Method
    
    - A user with ADMIN privelleges after logging in can update the user details of itself.
    - A user with ADMIN privelleges after logging in tries to update the details of some other user with different ID which is either present or not present in the DB,             handled with Custom Exception.

 e. Built REST API to Delete a user based on ID -> DELETE Method
    
    - A user with ADMIN privelleges after logging in can delete its account from the DB.
    - A user with ADMIN privelleges after logging in tries to delete an account with some other ID which is either present or not present in the DB, handled with Custom             Exception.
    - A user with ADMIN privelleges after logging in tries to delete without passing any ID, handled with Custom Exception.


**1. ROLE_CUSTOMER**

  a. Built REST API to login -> POST Method
    
    - A user with CUSTOMER privelleges can login if the user details are present in the DB.
    
 b. Built REST API to Get All Users -> GET Method
    
    - A user with CUSTOMER privelleges after logging in is not allowed to fetch the details of all the users, handled with Custom Exception.

 c. Built REST API to Get a user based on ID present in the DB -> GET Method
    
    - A user with CUSTOMER privelleges after logging in can get the details of its own.
    - A user with CUSTOMER privelleges after logging in tries to fetch user details with the given ID which is either present or not present in the DB and is not of its own,       is not allowed. This is handled this with a Custom Exception.

 d. Built REST API to update the user based on the ID -> PUT Method
   
    - A user with CUSTOMER privelleges after logging in can update the user details of itself.
    - A user with CUSTOMER privelleges after logging in tries to update the details of some other user with different ID which is either present or not present in the DB,          handled with Custom Exception as the user is not allowed to do this.

 e. Built REST API to Delete a user based on ID -> DELETE Method
   
    - A user with CUSTOMER privelleges after logging in can delete its account from the DB.
    - A user with CUSTOMER privelleges after logging in tries to delete an account with some other ID which is either present or not present in the DB, handled with Custom         Exception as no CUSTOMER is allowed to delete anyone else's details.
    - A user with CUSTOMER privelleges after logging in tries to delete without passing any ID, handled with Custom Exception.
    - A user with CUSTOMER privelleges after logging in when deletes itself and tries to delete again is also handled with a Custom Exception.


