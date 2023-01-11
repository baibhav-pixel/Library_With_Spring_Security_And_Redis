# Library_With_Spring_Security_And_Redis
# Java-Backend
Self Projects on Java Backend Development (Count=3)

#Library-With-Sping-Security-And-Redis-Caching-Added

-> The Library project is about Library Management System where a User can issue a Book or Return. 

-> If the user issues the book, the Admin takes the request to process. 

-> Once the Admin approves, a Transaction is created and the Book is assigned to the student.

-> Upon returning the Book the system checks for fine if any.

-> Necessary APIs are secured to avoid unnecessary interventions from unauthorized users (Be ready to see 401 and 403 error codes now!!!)

-> Enabled Caching for Student retrieval and saving

**What's New**
All the necessary APIs are made secured and requires authentication for using. Its recommended to use POSTMAN for the API testing.

Caching is enabled to get the data from Redis Database

**Skills:** 
MySQL, Java 8, SpringBoot, Hibernate, JPA, Maven, OOP, Spring Security, Redis
Tools/Technologies: IntelliJ Idea IDE, Postman, Git, MySQL Command Line Client

**Some Tips:**
You need to add first admin manually and then it allows other admins to be created.
If you get an error like 'Error creating bean with name 'entityManagerFactory'" then check:
1. Application.properties file
2. Check whether MySQL is running via cmd prompt
3. Getting LazyInitializationException means that a part of the object couldn't fetched and it was returned. Wrt to Student under model List<Request> can get this exception which returns 500 Internal Server Error
