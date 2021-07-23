/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.controller;

import com.example.models.BCrypt;
import com.example.models.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import java.sql.Time;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.lang.Integer;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }


  @RequestMapping("/")
  String index() {
    return "redirect:/tee-rific/login";
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

//************************************
// TEE-RIFIC CODE STARTS HERE
//************************************

  String[] priorities = {"GOLFER", "OWNER", "ADMIN"};

//**********************
// LOGIN
//**********************

  boolean failedLogin = false;

  @GetMapping(
          path = "/tee-rific/login"
  )
  public String getLoginPage(Map<String, Object> model){

    User user = new User();
    model.put("loginUser", user);

    if (failedLogin == true){
      String error = "Error: Username/Password Doesn't match/exist";
      model.put("failedLogin", error);
      failedLogin = false;
    }
    return "Login&Signup/login";
  }

  @PostMapping(
          path = "/tee-rific/login/check",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String checkLoginInfo(Map<String, Object> model, User user, HttpServletRequest request) throws Exception {

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))");

      // creates and check if admin account created
      int checkIfAdminExists = 0;
      ResultSet checkAdmin = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
      while (checkAdmin.next()){
        checkIfAdminExists++;
      }
      if (checkIfAdminExists == 0){
        String adminPassword = "cmpt276";
        String encryptedAdminPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String insert = "INSERT INTO users (priority, username, password) VALUES ('ADMIN','admin','"+encryptedAdminPassword+"')";
        stmt.executeUpdate(insert);
      }

      String sql = "SELECT * FROM users WHERE username = '" + user.getUsername() + "'";
      ResultSet rs = stmt.executeQuery(sql);

      int checkIfUserExists = 0;
      String checkPassword = "";
      String priority = "";
      while (rs.next()){
        checkIfUserExists++;
        checkPassword = rs.getString("password");
        priority = rs.getString("priority");

        String encryptedPassword = BCrypt.hashpw(checkPassword, BCrypt.gensalt());

      }
      System.out.println(checkPassword);
      System.out.println(user.getPassword());

      if (checkIfUserExists > 0 && (BCrypt.checkpw(user.getPassword(), checkPassword))){

        request.getSession().setAttribute("username", user.getUsername());
        String userid = (String) request.getSession().getAttribute("username");

        return "redirect:/tee-rific/home/" + userid;
      }
      failedLogin = true;
      return "redirect:/tee-rific/login";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


//**********************
// SIGN-UP
//**********************

  boolean usernameError = false;

  @GetMapping(
          path = "/tee-rific/signup"
  )
  public String getSignupPage(Map<String, Object> model) {
    User user = new User();
    model.put("newUser", user);
    if (usernameError == true){
      String error = "Error: Username already Exists.";
      model.put("usernameError", error);
      usernameError = false;
    }
    return "Login&Signup/signup";
  }


  @PostMapping(
          path = "/tee-rific/signup"
  )
  public String handleBrowserNewUserSubmit(Map<String, Object> model, User user) throws Exception {
    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

      user.setPriority(priorities[0]);      //sets the priority of the user to 'GOLFER'

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))");

      // creates and check if admin account created
      int checkIfAdminExists = 0;
      ResultSet checkAdmin = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
      while (checkAdmin.next()){
        checkIfAdminExists++;
      }
      if (checkIfAdminExists == 0){
        String adminPassword = "cmpt276";
        String encryptedAdminPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String insert = "INSERT INTO users (priority, username, password) VALUES ('ADMIN','admin','"+encryptedAdminPassword+"')";
        stmt.executeUpdate(insert);
      }

      stmt.executeUpdate("INSERT INTO users (priority, username, password, fname, lname, email, gender) VALUES ('" + user.getPriority() + "','" + user.getUsername() + "','" + encryptedPassword + "','" + user.getFname() + "','" + user.getLname() + "','" + user.getEmail() + "','" + user.getGender() + "')");

      String sql = "SELECT username FROM users WHERE username ='"+user.getUsername()+"'";
      ResultSet rs = stmt.executeQuery(sql);
      int checkCount = 0;
      while (rs.next()){
        checkCount++;
      }

      if (checkCount > 1){
        stmt.executeUpdate("DELETE FROM users WHERE priority='" + user.getPriority() + "' and username='" + user.getUsername() + "' and password='"+ encryptedPassword + "' and fname='"+user.getFname() + "' and lname='"+user.getLname() + "' and email='"+user.getEmail() + "' and gender='"+user.getGender()+"'");
        usernameError = true;
        return "redirect:/tee-rific/signup";
      } else {
        model.put("username", user.getUsername());
        return "LandingPages/success";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


//**********************
// OWNER SIGN-UP
//**********************

  boolean courseNameError = false;

  @GetMapping(
          path = "/tee-rific/signup/Owner"
  )
  public String getOwnerSignUpPage(Map<String, Object> model){
    CourseOwner owner = new CourseOwner();
    model.put("newOwner", owner);

    if (usernameError == true){
      String error = "Error: Username already Exists.";
      model.put("usernameError", error);
      usernameError = false;
    } else if (courseNameError == true){
      String error = "Error: Course Name already Exists.";
      model.put("courseNameError", error);
      courseNameError = false;
    }
    return "Owner/ownerSignUp";
  }


  @PostMapping(
          path = "/tee-rific/signup/Owner",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleBrowserOwnerSubmit(Map<String, Object> model, CourseOwner owner){
    //create a new user, owner, golf course, then add to database

    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String encryptedPassword = BCrypt.hashpw(owner.getPassword(), BCrypt.gensalt());

      //TODO: will need to figure out way to store image into sql later - Mike

      owner.setNumHoles(18);
      String ownerInfo = getSQLNewTableOwner();
      String insertOwners = getSQLInsertOwner(owner, encryptedPassword);

      //add user to database
      stmt.executeUpdate(ownerInfo);
      stmt.executeUpdate(insertOwners);

      //snake case the course name
    /* 'THIS IS BETTER THAN CAMEL CASE' for this situation:
        - cannot have spaces in name or else it breaks the SQL query
        - snake case works best as it makes for easy conversion back to original format
        - camel case is disregarded by the SQL, implying no way of knowing where to split the words to convert back to original format */
      String updatedCourseName = convertToSnakeCase(owner.getCourseName());
      String courseInfo = "CREATE TABLE IF NOT EXISTS " + updatedCourseName + " (holeNumber integer, yardage integer, par integer, handicap integer)";
      stmt.executeUpdate(courseInfo);

      //initializes a table to keep track of the course hole details
      for(int i = 0; i < owner.getNumHoles(); i++){
        String insertHole = "INSERT INTO " + updatedCourseName + "(" + "holeNumber, yardage, par, handicap) VALUES (' " + (i + 1) + "', '0', '0', '0')";
        stmt.executeUpdate(insertHole);
      }

      //add to user table
      String userInfo = getSQLNewTableUsers();

      // creates and check if admin account created
      int checkIfAdminExists = 0;
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))");
      ResultSet checkAdmin = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
      while (checkAdmin.next()){
        checkIfAdminExists++;
      }
      if (checkIfAdminExists == 0){
        String adminPassword = "cmpt276";
        String encryptedAdminPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String insert = "INSERT INTO users (priority, username, password) VALUES ('ADMIN','admin','"+encryptedAdminPassword+"')";
        stmt.executeUpdate(insert);
      }

      //create a user based on owner fields
      User user = new User();
      user.setPriority(priorities[1]);
      user.setUsername(owner.getUsername());
      user.setPassword(owner.getPassword());
      user.setFname(owner.getFname());
      user.setLname(owner.getLname());
      user.setEmail(owner.getEmail());
      user.setGender(owner.getGender());

      String insertUser = getSQLInsertUser(user, encryptedPassword);

      stmt.executeUpdate(userInfo);
      stmt.executeUpdate(insertUser);

      // Initialize rental inventory and bookings of golf course - Chino
      ownerCreateInventory(connection, updatedCourseName);
      ownerCreateBookingsTable(connection, updatedCourseName);

      // check if username or course name exists for already existing user
      String sql = "SELECT username FROM owners WHERE username ='"+user.getUsername()+"'";
      ResultSet rs = stmt.executeQuery(sql);
      int checkUserCount = 0;
      while (rs.next()){
        checkUserCount++;
      }

      String sqlCH = "SELECT courseName FROM owners WHERE courseName ='"+updatedCourseName+"'";
      ResultSet rsCH = stmt.executeQuery(sqlCH);
      int checkCNCount = 0;
      while (rsCH.next()){
        checkCNCount++;
      }

      if (checkUserCount > 1){
        // delete from user and owner database
        stmt.executeUpdate("DELETE FROM users WHERE priority='" + user.getPriority() + "' and username='" + user.getUsername() + "' and password='"+ encryptedPassword + "' and fname='"+ user.getFname() + "' and lname='"+user.getLname() + "' and email='"+user.getEmail() + "' and gender='"+user.getGender()+"'");
        String deleteOwner = getSQLDeleteOwner(owner, encryptedPassword);
        stmt.executeUpdate(deleteOwner);

        usernameError = true;
        return "redirect:/tee-rific/signup/Owner";
      } else if (checkCNCount > 1){
        stmt.executeUpdate("DELETE FROM users WHERE priority='" + user.getPriority() + "' and username='" + user.getUsername() + "' and password='"+ encryptedPassword + "' and fname='"+ user.getFname() + "' and lname='"+user.getLname() + "' and email='"+user.getEmail() + "' and gender='"+user.getGender()+"'");
        String deleteOwner = getSQLDeleteOwner(owner, encryptedPassword);
        stmt.executeUpdate(deleteOwner);

        courseNameError = true;
        return "redirect:/tee-rific/signup/Owner";
      } else
        model.put("username", user.getUsername());
      return "LandingPages/success";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  String getSQLNewTableOwner() {
    return  "CREATE TABLE IF NOT EXISTS owners (" +
            "courseName varchar(100), address varchar(100), city varchar(100), country varchar(100), website varchar(150), phoneNumber varchar(100), " +
            "courseLogo varchar(150), " +               //TODO: will need to fix this one image storage is figured out - MIKE
            "directionsToCourse varchar(500), description varchar(500), weekdayRates varchar(100), weekendRates varchar(100), numHoles integer, timeOpen varchar(10)," +
            "timeClose varchar(10), userName varchar(100), password varchar(100),firstName varchar(100),lastName varchar(100),email varchar(100),yardage varchar(100),gender varchar(100))";
  }


  private String getSQLInsertOwner(CourseOwner owner, String secretPW){
    return "INSERT INTO owners ( " +
            "courseName, address, city, country, website, phoneNumber, courseLogo, " +
            "directionsToCourse, description, weekdayRates, weekendRates, numHoles, timeOpen," +
            "timeClose, userName, password, firstName, lastName, email, yardage, gender) VALUES ('" +
            owner.getCourseName() + "','" + owner.getAddress() + "','" + owner.getCity() + "','" +
            owner.getCountry() + "','" + owner.getWebsite() + "','" + owner.getPhoneNumber() + "','" +
            owner.getCourseLogo() + "','" + owner.getDirectionsToCourse() + "','" + owner.getDescription() + "','" +
            owner.getWeekdayRates() + "','" +  owner.getWeekendRates() + "','" + owner.getNumHoles() + "','" + owner.getTimeOpen() + "','" +
            owner.getTimeClose() + "','" + owner.getUsername() + "','" + secretPW + "','" + owner.getFname() + "','" + owner.getLname() + "','" +
            owner.getEmail() + "','" + owner.getYardage() + "', '" + owner.getGender() + "')";
  }


  private String getSQLDeleteOwner(CourseOwner owner, String secretPW){
    return "DELETE FROM owners WHERE courseName='" + owner.getCourseName() + "' and address='" + owner.getAddress() +
            "' and city='" + owner.getCity() +  "' and country='" + owner.getCountry() + "' and website='"  +
            owner.getWebsite() + "' and phoneNumber='" + owner.getPhoneNumber() + "' and courseLogo='" +
            owner.getCourseLogo() + "' and directionsToCourse='" + owner.getDirectionsToCourse() + "' and description='" +
            owner.getDescription() + "' and weekdayRates='" + owner.getWeekdayRates() + "' and weekendRates='" +
            owner.getWeekendRates() + "' and numHoles='" + owner.getNumHoles() + "' and userName='" + owner.getUsername() +
            "' and password='" + secretPW + "' and firstName='" + owner.getFname() + "' and lastName='" + owner.getLname() +
            "' and email='" + owner.getEmail() + "' and yardage='" + owner.getYardage() + "' and gender='" + owner.getGender() + "'";
  }


  private String getSQLNewTableUsers(){
    return "CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))";
  }


  private String getSQLInsertUser(User user, String secretPW){
    return "INSERT INTO users (priority, username, password, fname, lname, email, gender) VALUES ('" + user.getPriority() + "','" + user.getUsername() + "','" + secretPW + "','" + user.getFname() + "','" + user.getLname() + "','" + user.getEmail() + "','" + user.getGender() + "')";
  }


  private String convertToSnakeCase(String toConvert){
    String updated = "";
    for(int i = 0; i < toConvert.length(); i++){
      if(toConvert.charAt(i) == ' '){
        updated += '_';
      }else{
        updated += toConvert.charAt(i);
      }
    }
    return updated;
  }


  private String convertFromSnakeCase(String toConvert){
    String updated = "";
    for(int i = 0; i < toConvert.length(); i++){
      if(toConvert.charAt(i) == '_'){
        updated += ' ';
      }else{
        updated += toConvert.charAt(i);
      }
    }
    return updated;
  }


//**********************
// HOME PAGE
//**********************

  @GetMapping(
          path = "/tee-rific/home/{username}"
  )
  public String getHomePage(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) throws Exception{

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String getUserPriority= "SELECT priority FROM users WHERE username='" + user +"'";
      ResultSet rs = stmt.executeQuery(getUserPriority);

      String userPriority = "";
      while(rs.next()){
        userPriority = rs.getString("priority");
      }

      if(userPriority.equals(priorities[0])){         // returns golfer homepage
        return "LandingPages/home";
      }else if(userPriority.equals(priorities[1])){   //returns owner homepage
        return "Owner/ownerHome";
      }else{                                          //returns admin homepage
        return "Admin/adminHome";
      }
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


//**********************
// COURSES
//**********************

//TODO: get all the courses, display ratings, allow user to rate courses

//**********************
// MODIFY ACCOUNT
//**********************

  @GetMapping(
          path = "/tee-rific/account/{username}"
  )
  public String getAccountPage(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request)
  {
    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/account/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    model.put("username", user);
    return "AccountInfo/account";
  }


  @GetMapping(
          path = "/tee-rific/editAccount/{username}"
  )

  public String getEditAccountPage(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request){

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/editAccount/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String getUserPriority= "SELECT priority FROM users WHERE username='" + user +"'";
      ResultSet rs = stmt.executeQuery(getUserPriority);

      String userPriority = "";
      while(rs.next()){
        userPriority = rs.getString("priority");
      }

      if(userPriority.equals(priorities[0])){         // returns golfer edit account
        //TODO: get the user object and pass the info into the model
        String getUserDetails= "SELECT * FROM users WHERE username='" + user +"'";
        ResultSet details = stmt.executeQuery(getUserDetails);
        details.next();

        User output = new User();

        output.setUsername(details.getString("username"));
        output.setPassword(details.getString("password"));
        output.setFname(details.getString("fname"));
        output.setLname(details.getString("lname"));
        output.setEmail(details.getString("email"));
        output.setGender(details.getString("gender"));
        
        model.put("userInfo", output);
        return "AccountInfo/editAccount";
      }else{                                          //returns owner edit account
        //TODO: get the owner object and pass the info into the model
        String getUserDetails= "SELECT * FROM owners WHERE username='" + user +"'";
        ResultSet details = stmt.executeQuery(getUserDetails);
        details.next();

        CourseOwner output = new CourseOwner();

        output.setUsername(details.getString("username"));      //Account Info
        output.setPassword(details.getString("password"));
        output.setFname(details.getString("firstname"));
        output.setLname(details.getString("lastname"));
        output.setEmail(details.getString("email"));
        output.setGender(details.getString("gender"));  
        
        output.setCourseName(details.getString("coursename"));  //Course Basic Info
        output.setAddress(details.getString("address"));
        output.setCity(details.getString("city"));
        output.setCountry(details.getString("country"));
        output.setPhoneNumber(details.getString("phonenumber"));  
        output.setWebsite(details.getString("website"));
        // output.setCourseLogo(details.getString("courselogo"));  
        // output.setYardage(details.getString("yardage"));  
        
        output.setTimeOpen(details.getString("timeopen"));   
        output.setTimeClose(details.getString("timeclose")); 
        output.setWeekdayRates(details.getString("weekdayrates"));  
        output.setWeekendRates(details.getString("weekendrates"));
        output.setDirectionsToCourse(details.getString("directionstocourse"));  
        output.setDescription(details.getString("description")); 
        
        model.put("ownerInfo", output);
        return "AccountInfo/editAccountOwner";
      }
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
    path = "/tee-rific/editAccount/{editColumn}/{username}"
  )
  public String updateAccountInformation(@PathVariable("username")String user, @PathVariable("editColumn") String column, Map<String, Object> model, HttpServletRequest request){
    
    if(!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }
    
    // CourseOwner newValue = new CourseOwner();
    // model.put("value", newValue);

    model.put("column", column);
    model.put("user", user);

    // if (changeUsernameError == true){
    //   changeUsernameError = false;
    //   String error = "Username is already taken.";
    //   model.put("errorMessage", error);
    // } else if (changeValueError == true){
    //   changeUsernameError = false;
    //   String error = "Error Updating value, Retry again.";
    //   model.put("errorMessage", error);
    // }

    // if (column == "timeopen" || column == "timeclose")                   // Since theres different input fields the owner can update,
    //   return "editTimeValues";                                           // it should redirect to the appropriate html with the right
    // else if (column == "description" || column == "directionstocourse")  // input field to fill out.     - Justin
    //   return "editTextAreas";
    // else if (column == "gender")
    //   return "editGender";
    // else
    System.out.println("test");
    return "AccountInfo/editShortStringValues";
  }

  // @PostMapping(
  //   path = "/tee-rific/editAccount/{editColumn}/{username}",  
  // consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  // )
  // public String changeAccountInformation(@PathVariable("username")String user, @PathVariable("editColumn") String column, Map<String, Object> model, CourseOwner newValue){
  //   try (Connection connection = dataSource.getConnection())
  //   {
  //     Statement stmt = connection.createStatement();
  //     String selectUser= "SELECT priority FROM users WHERE username='" + user +"'";
  //     ResultSet userData = stmt.executeQuery(selectUser);

  //     String value = "";
  //     if (column == "username"){         // To get value, depending on which column
  //       // check if username is taken        
  //       ResultSet check = stmt.executeQuery("SELECT username FROM users WHERE username ='"+newValue.getUsername()+"'");
  //       int checkCount = 0;
  //       while (check.next()){
  //         checkCount++;
  //       }
  //       if (checkCount > 0){
  //         changeUsernameError = true;
  //         return "redirect:/tee-rific/editAccount/username/"+user;  //
  //       }

  //       value = newValue.getUsername();
  //     } else if (column == "password")
  //       // encrypt password
  //       value = BCrypt.hashpw(newValue.getPassword(), BCrypt.gensalt());
  //     else if (column == "fname" || column == "firstname")
  //       value = newValue.getFname();
  //     else if (column == "lname" || column == "lastname")
  //       value = newValue.getLname();
  //     else if (column == "email")
  //       value = newValue.getEmail();
  //     else if (column == "gender")
  //       value = newValue.getGender();
  //     else if (column == "address")
  //       value = newValue.getAddress();
  //     else if (column == "city")
  //       value = newValue.getCity();
  //     else if (column == "country")
  //       value = newValue.getCountry();
  //     else if (column == "phonenumber")
  //       value = newValue.getPhoneNumber();
  //     else if (column == "website")
  //       value = newValue.getWebsite();
  //     else if (column == "timeopen")
  //       value = newValue.getTimeOpen();
  //     else if (column == "timeclose")
  //       value = newValue.getTimeClose();
  //     else if (column == "weekdayrates")
  //       value = newValue.getWeekdayRates();
  //     else if (column == "weekendrates")
  //       value = newValue.getWeekendRates();
  //     else if (column == "directionstocourse")
  //       value = newValue.getDirectionsToCourse();
  //     else if (column == "description")
  //       value = newValue.getDescription();
  //     else {
  //       changeValueError = true;
  //       return "redirect:/tee-rific/editAccount/"+column+"/"+user;  //
  //     }
      
  //     String userPriority = "";
  //     while(userData.next()){
  //       userPriority = userData.getString("priority");
  //     }

  //     if(userPriority.equals(priorities[0])){     //If user/golfer
  //       String updateColumnValue = "UPDATE users SET " + column + "='" + value + "' WHERE username='" + user;
  //       stmt.executeUpdate(updateColumnValue);
  //     } else {
  //       // Update user part of account
  //       if (column == "username" || column == "password" || column == "firstname" || column == "lastname" || column == "email" || column == "gender"){
  //         if (column == "firstname"){         // This if and else if are for first name and last name, since its different name on owner db
  //           String updateUserInfo = "UPDATE users SET fname='" + value + "' WHERE username='" + user;
  //           stmt.executeUpdate(updateUserInfo);
  //         } else if (column == "lastname"){
  //           String updateUserInfo = "UPDATE users SET lname='" + value + "' WHERE username='" + user;
  //           stmt.executeUpdate(updateUserInfo);
  //         } else {
  //           String updateUserInfo = "UPDATE users SET " + column + "='" + value + "' WHERE username='" + user;
  //           stmt.executeUpdate(updateUserInfo);
  //         }
  //       }
  //       // Update owner part of account
  //       String updateOwnerInfo = "UPDATE owners SET " + column + "='" + value + "' WHERE username='" + user;
  //       stmt.executeUpdate(updateOwnerInfo);
  //     }
    
  //     model.put("username", user);
  //     return "redirect:/tee-rific/editAccount/accountUpdatedSuccessfully";
  //   } catch (Exception e) {
  //     model.put("message", e.getMessage());
  //     return "error";
  //   } 
  // }



  @GetMapping(
    path = "/tee-rific/delete/{username}"
  )
  public String accountDeleted(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request)
  {
    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/delete/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    model.put("username", user);
    return "AccountInfo/AccountDeleted";
  }


  @PostMapping(
          path = "/tee-rific/delete/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String deleteUser(@PathVariable("username")String user, Map<String, Object> model) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement stmt = connection.createStatement();

      //if the user Account is an owner, user login information from user table and owner table is deleted, leave course details so scorecards wont be NULL for columns that need them, needs to be tested
      String remove = "DELETE FROM users WHERE username ='" + user + "'";
      stmt.execute(remove);
      remove = "DELETE FROM owners WHERE username ='" + user + "'";
      stmt.execute(remove);

      model.put("username", user);
      return "AccountInfo/AccountDeleted";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


//********************************
// MODIFY COURSE DETAILS -- OWNER
//********************************

  @GetMapping(
          path = "/tee-rific/golfCourseDetails/{username}"
  )
  public String getCourseDetails(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request){

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/golfCourseDetails/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    //TODO: get the course details here and insert into the model

    model.put("username", user);
    return "Booking&ViewingCourses/golfCourseDetails";
  }


//TODO: add a post-method to modify the golf course details


  //**********************
  // BOOKING
  //**********************
  // 1. User first selects which specific course to play at (to specify open / close times)
  // 2. User picks date and time for teetime (using table of buttons)
  // 3. Show options for renting equipment and such and enter into bookings table
  // 4. Create a scorecard for the game with the generated Game ID
//TODO: can someone do invalid inputs :3


  @GetMapping(
    path = "/tee-rific/booking/{username}"
  )
  public String displayCourses(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) {
    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/booking/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    ArrayList<CourseOwner> coursesList = new ArrayList<CourseOwner>();

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM owners");

      while (rs.next()) {
        CourseOwner course = new CourseOwner();
        course.setCourseName(rs.getString("courseName"));
        coursesList.add(course);
      }

      CourseOwner selectedCourse = new CourseOwner();

      model.put("username", user);
      model.put("selectedCourse", selectedCourse);
      model.put("coursesList", coursesList);
      return "Booking&ViewingCourses/bookingCourse";

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }// displayCourses()

  @PostMapping(
    path = "/tee-rific/booking/{username}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleSelectCourse(@PathVariable("username")String user, CourseOwner selectedCourse, Map<String, Object> model) {
    // Enter into bookings db to create a ID for the game
    try (Connection connection = dataSource.getConnection()) {
      String courseName = convertToSnakeCase(selectedCourse.getCourseName());
      String id = createNewBooking(connection, user, courseName);

      return "redirect:/tee-rific/booking/"+courseName+"/"+id+"/"+user+"";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }// handleSelectCourse()

  @GetMapping(
    path = "/tee-rific/booking/{courseName}/{gameID}/{username}"
  )
  public String displayCourseTimes(@PathVariable Map<String, String> pathVars, Map<String, Object> model, HttpServletRequest request) throws Exception {
    // Kyle's shit
    String user = pathVars.get("username");

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/booking/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    String courseName = convertFromSnakeCase(pathVars.get("courseName"));
    String gameIDStr = pathVars.get("gameID");
    Integer gameID = Integer.parseInt(gameIDStr);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      // ResultSet rs = stmt.executeQuery("SELECT * FROM bookings_"+courseName+" where gameID='"+gameID+"'");
      ResultSet courseInfo = stmt.executeQuery("SELECT * FROM owners WHERE courseName='"+courseName+"'");
      courseInfo.next();

      // Convert DB data into ints for comparison
      String timeOpenStr = courseInfo.getString("timeOpen");
      // timeOpenStr = timeOpenStr + ":00";
      String timeOpenSegments[] = timeOpenStr.split(":");
      String timeOpenHrStr = timeOpenSegments[0];
      String timeOpenMinStr = timeOpenSegments[1];

      String timeCloseStr = courseInfo.getString("timeClose");
      // timeCloseStr = timeCloseStr + ":00";
      String timeCloseSegments[] = timeCloseStr.split(":");
      String timeCloseHrStr = timeCloseSegments[0];
      String timeCloseMinStr = timeCloseSegments[1];

      Integer timeOpenHr = Integer.parseInt(timeOpenHrStr);
      Integer timeOpenMin = Integer.parseInt(timeOpenMinStr);
      Integer timeCloseHr = Integer.parseInt(timeCloseHrStr);
      Integer timeCloseMin = Integer.parseInt(timeCloseMinStr);

      // Time timeOpen = Time.valueOf(timeOpenStr);
      // Time timeClose = Time.valueOf(timeCloseStr);

      // Create array of valid teetimes based on open / closing times of specified course
      // TODO: (if we have time) extend so to consider gold course capacity
      ArrayList<Timeslot> validTimeSlots = new ArrayList<Timeslot>();
      Integer hour = 0;
      Integer min = 0;

      for (int i = 0; i < 48; i++) {
        if (hour >= timeOpenHr && hour < timeCloseHr) {
          if (hour == timeOpenHr && min < timeOpenMin) {
            min = 30;
          }
          if (hour == timeOpenHr && min < timeOpenMin) {
            min = 0;
            hour++;
          }

          String time = singleDigitToDoubleDigitString(hour) + ":" + singleDigitToDoubleDigitString(min);
          Timeslot ts = new Timeslot();
          ts.setTime(time);

          validTimeSlots.add(ts);
        }

        if (min == 0) {
          min = 30;
        } else {
          hour++;
          min = 0;
        }
      }

      TeeTimeBooking booking = new TeeTimeBooking();
      booking.setUsername(user);
      courseName = convertToSnakeCase(courseName);

      model.put("timeSlots", validTimeSlots);
      model.put("courseName", courseName);
      model.put("gameID", gameID);
      model.put("username", user);
      model.put("booking", booking);
      return "Booking&ViewingCourses/bookingTimes"; 

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }// displayCourseTimes()

  @PostMapping(
    path = "/tee-rific/booking/{courseName}/{gameID}/{username}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleNewBooking(@PathVariable Map<String, String> pathVars, Map<String, Object> model, TeeTimeBooking booking) throws Exception {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    String gameIDStr = pathVars.get("gameID");
    String courseName = convertFromSnakeCase(courseNameSC);

    try (Connection connection = dataSource.getConnection()) {
      String teetime = booking.getTime();
      teetime = teetime + ":00";
      booking.setTime(teetime);

      updateBookingsTable(connection, booking, courseNameSC, gameIDStr);

      // Create a new scorecard
      Scorecard scorecard = new Scorecard();
      scorecard.setGameID(gameIDStr);
      scorecard.setDatePlayed(booking.getDate());
      scorecard.setCoursePlayed(courseName);
      scorecard.setAttestor("");
      scorecard.setFormatPlayed("");
      scorecard.setHolesPlayed("");
      scorecard.setTeesPlayed("");

      userCreateScorecardsTable(connection, user);
      userInsertScorecard(connection, user, scorecard);
      courseName = convertToSnakeCase(courseNameSC);

      return "redirect:/tee-rific/booking/{courseName}/{gameID}/{username}/success";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }
  

  @GetMapping(
    path = "/tee-rific/booking/{courseName}/{gameID}/{username}/success"
  )
  public String bookingSuccessful(@PathVariable Map<String, String> pathVars, Map<String, Object> model, HttpServletRequest request) throws Exception {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    String gameIDStr = pathVars.get("gameID");
    String courseName = convertFromSnakeCase(courseNameSC);

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/booking/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    // Confirmation / Success page, display booking info
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM bookings_"+courseNameSC+" WHERE gameID='"+gameIDStr+"'");
      rs.next();

      TeeTimeBooking toDisplay = new TeeTimeBooking();
      toDisplay.setDate(rs.getString("date"));
      toDisplay.setNumPlayers(rs.getInt("numplayers"));
      toDisplay.setRentalID(rs.getString("rentalID"));
      Time teetime = rs.getTime("teetime");
      toDisplay.setTime(teetime.toString());
      toDisplay.setUsername(rs.getString("username"));

      model.put("toDisplay", toDisplay);
      model.put("gameID", gameIDStr);
      model.put("courseName", courseName);
      model.put("username", user);

      return "Booking&ViewingCourses/bookingSuccessful";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  } // bookingSuccessful()


  // HELPER BOIS
  public void ownerCreateBookingsTable(Connection connection, String courseName) throws Exception {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings_"+courseName+" (gameID serial, username varchar(100), date date, teetime time, numplayers integer, rentalID varchar(20))");
  } //ownerCreateBookingsTable

  public String createNewBooking(Connection connection, String user, String courseName) throws Exception {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("INSERT INTO bookings_"+courseName+" (username) VALUES ('"+user+"')");

    // Go to very bottom of table since we just inserted there
    ResultSet rs = stmt.executeQuery("SELECT gameID FROM bookings_"+courseName+"");
    String id = "";
    while (rs.next()) {
      id = rs.getString("gameID");
    }

    return id;
  }// createNewBooking()

  public String singleDigitToDoubleDigitString(Integer n) {
    // Converts single digit ints to double digit strings
    // ie 0 -> "00"
    String ret = Integer.toString(n);
    if (n.intValue() < 10) {
      ret = "0" + ret;
    }
    return ret;
  } // singleDigitToDoubleDigitString()

  public void updateBookingsTable(Connection connection, TeeTimeBooking booking, String courseName, String gameID) throws Exception {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("UPDATE bookings_"+courseName+" SET (date, teetime, numplayers) = ('"+booking.getDate()+"', '"+booking.getTime()+"', '"+booking.getNumPlayers()+"') WHERE gameID='"+gameID+"'");
  } //updateBookingsTable()


//**********************
// RENT EQUIPMENT
//**********************

  //TODO: Add extra style
//TODO: Corner cases (out of stock case, quantity in cart > stock case, negative number case)
  @GetMapping(
          path = "/tee-rific/rentEquipment/{courseName}/{gameID}/{username}"
  )
  public String rentEquipment(@PathVariable Map<String, String> pathVars, Map<String, Object> model, HttpServletRequest request) {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    String gameIDStr = pathVars.get("gameID");

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/rentEquipment/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    EquipmentCart cart = new EquipmentCart();

    model.put("gameID", gameIDStr);
    model.put("courseName", courseNameSC);
    model.put("username", user);
    model.put("cart", cart);
    return "Rentals/rentEquipment";
  }


  @PostMapping(
          path = "/tee-rific/rentEquipment/{courseName}/{gameID}/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleShop(@PathVariable Map<String, String> pathVars, Map<String, Object> model, EquipmentCart cart) throws Exception {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    String gameIDStr = pathVars.get("gameID");
    
    try (Connection connection = dataSource.getConnection()) {
      // Create a table with our cart data inside to display on checkout
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cart_"+user+" (numBalls integer, numCarts integer, numClubs integer)");
      stmt.executeUpdate("INSERT INTO cart_"+user+" VALUES ('"+cart.getNumBalls()+"', '"+cart.getNumCarts()+"', '"+cart.getNumClubs()+"')");
      model.put("username", user);

      return "redirect:/tee-rific/rentEquipment/checkout/" + courseNameSC +"/"+ gameIDStr +"/"+ user;

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }



  @GetMapping(
          path="/tee-rific/rentEquipment/checkout/{courseName}/{gameID}/{username}"
  )
  public String handleViewCart(@PathVariable Map<String, String> pathVars, Map<String, Object> model, HttpServletRequest request) throws Exception {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    String gameIDStr = pathVars.get("gameID");

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/rentEquipment/checkout/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      // Create a table with our cart data inside to display on checkout
      EquipmentCart toView = getUserCartContentsFromDB(connection, user);
      toView.printfields();

      model.put("gameID", gameIDStr);
      model.put("courseName", courseNameSC);
      model.put("username", user);
      model.put("userCart", toView);
      return "Rentals/rentEquipmentCheckout";

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @PostMapping(
          path = "/tee-rific/rentEquipment/checkout/{courseName}/{gameID}/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleCheckout(@PathVariable Map<String, String> pathVars, Map<String, Object> model) throws Exception {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    String gameIDStr = pathVars.get("gameID");

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      EquipmentCart cart = getUserCartContentsFromDB(connection, user);
      updateInventory(connection, cart, courseNameSC);
      stmt.executeUpdate("DROP TABLE cart_"+user+"");

      // Create table of rentals so employees can keep track
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rentals_"+courseNameSC+" (id serial, username varchar(100), dateCheckout timestamp DEFAULT now(), numBalls integer, numCarts integer, numClubs integer)");
      stmt.executeUpdate("INSERT INTO rentals_"+courseNameSC+" (username, numBalls, numCarts, numClubs) VALUES ('temp', '"+cart.getNumBalls()+"', '"+cart.getNumCarts()+"', '"+cart.getNumClubs()+"')");

      // Link rental to booking
      ResultSet rs = stmt.executeQuery("SELECT * FROM rentals_"+courseNameSC+"");
      String rentalID = "";
      while (rs.next()) {
        rentalID = rs.getString("id");
      }

      stmt.executeUpdate("UPDATE bookings_"+courseNameSC+" SET (rentalID) = ('"+rentalID+"')");
      
      model.put("username", user);

      return "redirect:/tee-rific/rentEquipment/checkout/success/";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
          path="/tee-rific/rentEquipment/checkout/success/{username}"
  )
  public String rentSuccessPage(@PathVariable("username")String user, HttpServletRequest request) {

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/rentEquipment/checkout/success/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    return "Rentals/rentEquipmentSuccess";
  }


  // ------ OWNER'S PAGE ------ //
// TODO: ensure paths are correct
// TODO: no way to secure IF LOGGED IN ALREADY - kyle
// TODO: Add style to table in viewInventory page
  @GetMapping(
          path="/tee-rific/golfCourseDetails/inventory/{username}"
  )
  public String viewInventory(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) throws Exception {
    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String courseName = ownerGetCourseName(connection, user);
      String courseNameSC = convertToSnakeCase(courseName);

      ResultSet rs = stmt.executeQuery("SELECT * FROM inventory_"+courseNameSC+"");
      ArrayList<Equipment> eqs = new ArrayList<Equipment>();
      
      while (rs.next()) {
        Equipment eq = new Equipment();
        eq.setItemName(rs.getString("name"));
        eq.setStock(rs.getInt("stock"));

        eqs.add(eq);
      }

      model.put("username", user);
      model.put("eqsArray", eqs);
      return "Rentals/inventory";

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  // TODO: no way to secure - kyle
// TODO: ensure paths are correct
  @GetMapping(
          path="/tee-rific/golfCourseDetails/inventory/update/{username}"
  )
  public String invUpdate(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) {

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    EquipmentCart cart = new EquipmentCart();

    model.put("username", user);
    model.put("ownerCart", cart);
    return "Rentals/inventoryUpdate";
  }


  // TODO: ensure paths are correct
  @PostMapping(
          path="/tee-rific/golfCourseDetails/inventory/update/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleInvUpdate(@PathVariable("username")String user, Map<String, Object> model, EquipmentCart cart) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      String courseName = ownerGetCourseName(connection, user);
      String courseNameSC = convertToSnakeCase(courseName);
      ownerUpdateInventory(connection, cart, courseNameSC);

      return "redirect:/tee-rific/golfCourseDetails/inventory/" + user;

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  private void updateInventory(Connection connection, EquipmentCart cart, String courseName) throws Exception {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM inventory_"+courseName+"");

    // Calculate updated values for stock
    rs.next();
    int ballStock = rs.getInt("stock");
    int updatedBallStock = ballStock - cart.getNumBalls();
    rs.next();
    int golfCartStock = rs.getInt("stock");
    int updatedGolfCartStock = golfCartStock - cart.getNumCarts();
    rs.next();
    int clubStock = rs.getInt("stock");
    int updatedClubStock = clubStock - cart.getNumClubs();

    // Update inventory table
    stmt.executeUpdate("UPDATE inventory_"+courseName+" SET stock ='"+updatedBallStock+"' WHERE name = 'balls'");
    stmt.executeUpdate("UPDATE inventory_"+courseName+" SET stock ='"+updatedGolfCartStock+"' WHERE name = 'carts'");
    stmt.executeUpdate("UPDATE inventory_"+courseName+" SET stock ='"+updatedClubStock+"' WHERE name = 'clubs'");
  }


  private EquipmentCart getUserCartContentsFromDB(Connection connection, String username) throws Exception {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM cart_"+username+"");
    rs.next();

    EquipmentCart ret = new EquipmentCart();
    ret.setNumBalls(rs.getInt("numballs"));
    ret.setNumCarts(rs.getInt("numcarts"));
    ret.setNumClubs(rs.getInt("numclubs"));

    return ret;
  }


  private void ownerCreateInventory(Connection connection, String courseName) throws Exception {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS inventory_"+courseName+" (name varchar(100), stock integer DEFAULT 0)");
    stmt.executeUpdate("INSERT INTO inventory_"+courseName+" (name) VALUES ('balls')");
    stmt.executeUpdate("INSERT INTO inventory_"+courseName+" (name) VALUES ('carts')");
    stmt.executeUpdate("INSERT INTO inventory_"+courseName+" (name) VALUES ('clubs')");
  }


  // private void ownerInsertNewItem(Connection connection, String nameOfItem) throws Exception {
  //   Statement stmt = connection.createStatement();
  //   stmt.executeUpdate("INSERT INTO inventory_"+courseName+" (name) VALUES ('"+nameOfItem+"')");
  // }


  // private void ownerDeleteItem(Connection connection, String nameOfItem) throws Exception {
  //   Statement stmt = connection.createStatement();
  //   stmt.executeUpdate("DELETE FROM inventory_"+courseName+" WHERE name='"+nameOfItem+"'");
  // }


  private void ownerUpdateInventory(Connection connection, EquipmentCart cart, String courseName) throws Exception {
    Statement stmt = connection.createStatement();
    // ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");

    // rs.next();
    // int ballStock = rs.getInt("stock");
    // int updatedBallStock = ballStock + cart.getNumBalls();
    // rs.next();
    // int golfCartStock = rs.getInt("stock");
    // int updatedGolfCartStock = golfCartStock + cart.getNumCarts();
    // rs.next();
    // int clubStock = rs.getInt("stock");
    // int updatedClubStock = clubStock + cart.getNumClubs();

    // Update inventory table
    stmt.executeUpdate("UPDATE inventory_"+courseName+" SET stock ='"+cart.getNumBalls()+"' WHERE name = 'balls'");
    stmt.executeUpdate("UPDATE inventory_"+courseName+" SET stock ='"+cart.getNumCarts()+"' WHERE name = 'carts'");
    stmt.executeUpdate("UPDATE inventory_"+courseName+" SET stock ='"+cart.getNumClubs()+"' WHERE name = 'clubs'");
  }
  
  private String ownerGetCourseName(Connection connection, String username) throws Exception {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM owners WHERE username='"+username+"'");
    rs.next();
    String courseName = rs.getString("courseName");

    return courseName;
  }

//**********************
// BROWSE COURSES
//**********************

  @GetMapping(
          path = "/tee-rific/courses/{username}"
  )
  public String viewAllCourses(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) throws Exception {
    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/courses/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(getSQLNewTableOwner());
      ResultSet rs = stmt.executeQuery("SELECT * FROM owners");
      ArrayList<CourseOwner> output = new ArrayList<CourseOwner>();
      while(rs.next()) {
        CourseOwner course = new CourseOwner();
        course.setCourseName(rs.getString("courseName"));
        course.setAddress(rs.getString("address"));
        course.setCity(rs.getString("city"));
        course.setCountry(rs.getString("country"));
        course.setWebsite(rs.getString("website"));
        course.setPhoneNumber(rs.getString("phoneNumber"));
        course.setCourseLogo(rs.getString("courseLogo"));
        course.setDirectionsToCourse(rs.getString("directionsToCourse"));
        course.setDescription(rs.getString("description"));
        course.setWeekdayRates(rs.getString("weekdayRates"));
        course.setWeekendRates(rs.getString("weekendRates"));
        course.setNumHoles(rs.getInt("numHoles"));
        course.setUsername(rs.getString("userName"));
        course.setPassword(rs.getString("password"));
        course.setFname(rs.getString("firstName"));
        course.setLname(rs.getString("lastName"));
        course.setEmail(rs.getString("email"));
        course.setYardage(rs.getString("yardage"));
        course.setGender(rs.getString("gender"));

        output.add(course);
      }
      model.put("courses", output);
      model.put("username", user);
      return "Booking&ViewingCourses/listCourses";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
          path="/tee-rific/courses/{courseID}/{username}"
  )
  public String getCourseInfo(@PathVariable("courseID")String courseID, @PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) throws Exception {

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if(!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      //get the course info by searching for snakeCase name in DB
      String convertedName = convertToSnakeCase(courseID);

      String getCourseInfo = "SELECT * FROM " + convertedName;
      ResultSet courseInfo = stmt.executeQuery(getCourseInfo);
      ArrayList<Hole> courseHoles = new ArrayList<Hole>();
      while(courseInfo.next()){
        Hole hole = new Hole();
        hole.setHoleNumber(Integer.parseInt(courseInfo.getString("holeNumber")));
        hole.setYardage(Integer.parseInt(courseInfo.getString("yardage")));
        hole.setPar(Integer.parseInt(courseInfo.getString("par")));
        hole.setHandicap(Integer.parseInt(courseInfo.getString("handicap")));

        courseHoles.add(hole);
      }

      model.put("courseName", courseID);
      model.put("username", user);
      model.put("course", courseHoles);

      return "Booking&ViewingCourses/courseInformation";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


//**********************
// SCORECARD
//**********************

  @GetMapping(
          path = "/tee-rific/scorecards/{username}"
  )
  public String getScorecards(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) throws Exception {

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/scorecards/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      //TODO: Scorecards -- how to store an arrayList/array in SQL for the users
      String sqlScorecardsInit = "CREATE TABLE IF NOT EXISTS scorecards_"+user+" (id varchar(100), date varchar(100), course varchar(100), teesPlayed varchar(100), holesPlayed varchar(100), formatPlayed varchar(100), attestor varchar(100))";
      stmt.executeUpdate(sqlScorecardsInit);

      ResultSet rs = stmt.executeQuery("SELECT * FROM scorecards_"+user+"");
      ArrayList<Scorecard> output = new ArrayList<Scorecard>();
      while(rs.next()) {
        Scorecard scorecard = new Scorecard();
        scorecard.setGameID(rs.getString("id"));
        scorecard.setDatePlayed(rs.getString("date"));
        scorecard.setCoursePlayed(rs.getString("course"));
        scorecard.setTeesPlayed(rs.getString("teesPlayed"));
        scorecard.setHolesPlayed(rs.getString("holesPlayed"));
        scorecard.setFormatPlayed(rs.getString("formatPlayed"));
        scorecard.setAttestor(rs.getString("attestor"));
        //TODO: Scorecards -- store an arrayList/array in SQL for the users

        output.add(scorecard);
      }

      model.put("username", user);
      model.put("scorecards", output);
      return "Scorecard/scorecard";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
          path = "/tee-rific/scorecards/{username}/{gameID}"
  )
  public String getSpecificScorecard(@PathVariable("username")String user, @PathVariable("gameID")String gameID, Map<String, Object> model, HttpServletRequest request) throws Exception {

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if(!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      //get the scorecard info
      String getScorecardInfo = "SELECT * FROM scorecards_" +user+" WHERE id='" + gameID + "'";
      ResultSet scoreCardInfo = stmt.executeQuery(getScorecardInfo);

      Scorecard scorecard = new Scorecard();
      while(scoreCardInfo.next()){
        scorecard.setGameID(scoreCardInfo.getString("id"));
        scorecard.setDatePlayed(scoreCardInfo.getString("date"));
        scorecard.setCoursePlayed(scoreCardInfo.getString("course"));
        scorecard.setTeesPlayed(scoreCardInfo.getString("teesPlayed"));
        scorecard.setHolesPlayed(scoreCardInfo.getString("holesPlayed"));
        scorecard.setFormatPlayed(scoreCardInfo.getString("formatPlayed"));
        scorecard.setAttestor(scoreCardInfo.getString("attestor"));
        //TODO: Scorecards -- store an arrayList/array in SQL for the users
      }

      //get the course info by searching for snakeCase name in DB
      String courseName = scorecard.getCoursePlayed();
      String convertedName = convertToSnakeCase(courseName);

      String getCourseInfo = "SELECT * FROM " + convertedName;
      ResultSet courseInfo = stmt.executeQuery(getCourseInfo);
      ArrayList<Hole> courseHoles = new ArrayList<Hole>();
      while(courseInfo.next()){
        Hole hole = new Hole();
        hole.setHoleNumber(Integer.parseInt(courseInfo.getString("holeNumber")));
        hole.setYardage(Integer.parseInt(courseInfo.getString("yardage")));
        hole.setPar(Integer.parseInt(courseInfo.getString("par")));
        hole.setHandicap(Integer.parseInt(courseInfo.getString("handicap")));

        courseHoles.add(hole);
      }

      model.put("username", user);
      model.put("scorecard", scorecard);
      model.put("course", courseHoles);

      return "Scorecard/game";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


//TODO: postMapping for scorecard updating
// @PostMapping(
//   path = "/tee-rific/scorecards/{gameID}",
//   consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
// )
// public String updateScorecard(@PathVariable("gameID")String gameID, Map<String, Object> model){
//   return "game";
// }//updateScorecard()

// HELPER BOIS (for booking) - Chino
public void userCreateScorecardsTable(Connection connection, String username) throws Exception{
  Statement stmt = connection.createStatement();
  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS scorecards_"+username+" (id varchar(100), date varchar(100), course varchar(100), teesPlayed varchar(100), holesPlayed varchar(100), formatPlayed varchar(100), attestor varchar(100))");
}

public void userInsertScorecard(Connection connection, String username, Scorecard scorecard) throws Exception {
  Statement stmt = connection.createStatement();
  stmt.executeUpdate("INSERT INTO scorecards_"+username+" (id, date, course, teesPlayed, holesPlayed, formatPlayed, attestor) VALUES (" +
                      "'" + scorecard.getGameID() + "', '" + scorecard.getDatePlayed() + "', '" + scorecard.getCoursePlayed() +
                      "', '" + scorecard.getTeesPlayed() + "', '" + scorecard.getHolesPlayed() + "', '" + scorecard.getFormatPlayed() +
                      "', '" + scorecard.getAttestor() + "')");
}


//**********************
// TOURNAMENT
//**********************

  @GetMapping(
          path = "/tee-rific/tournament/{username}"
  )
  public String tournament(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request){

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/tournament/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    model.put("username", user);
    return "Tournaments/tournament";
  }

  // TODO: page crashes on the live version, local host works fine for some reason
// TODO: breaks if no tournaments have been created yet
  @GetMapping(
          path = "/tee-rific/availableTournaments/{username}"
  )
  public String availableTournaments(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request){

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/availableTournaments/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try(Connection connection = dataSource.getConnection())
    {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM tournaments");
      ArrayList<Tournament> output = new ArrayList<Tournament>();
      while (rs.next())
      {
        Tournament tournament = new Tournament();
        tournament.setId(rs.getInt("id"));
        tournament.setName(rs.getString("name"));
        tournament.setDate(rs.getString("date"));
        tournament.setTime(rs.getString("time"));
        tournament.setParticipantSlots(rs.getInt("participant_slots"));
        tournament.setBuyIn(rs.getInt("buy_in"));
        tournament.setFirstPrize(rs.getString("first_prize"));
        tournament.setSecondPrize(rs.getString("second_prize"));
        tournament.setThirdPrize(rs.getString("third_prize"));
        tournament.setAgeRequirement(rs.getString("age_requirement"));
        tournament.setGameMode(rs.getString("game_mode"));
        tournament.setClubName(rs.getString("club_name"));

        output.add(tournament);
      }

      model.put("tournaments", output);
      Tournament tournament = new Tournament();
      model.put("tournament", tournament);

      model.put("username", user);
      return "Tournaments/availableTournaments";
    } catch (Exception e){
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
          path = "/tee-rific/createTournament/{username}"
  )
  public String createTournament(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request)
  {

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/createTournament/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    Tournament tournament = new Tournament();

    model.put("newTournament", tournament);
    model.put("username", user);
    return "Tournaments/createTournament";
  }


  @PostMapping(
          path = "/tee-rific/createTournament/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleTournamentCreation(@PathVariable("username")String user, Map<String, Object> model, Tournament tournament) throws Exception
  {
    try (Connection connection = dataSource.getConnection())
    {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tournaments (id serial, name varchar(100), date varchar(10), time varchar(50), participant_slots integer, buy_in integer, first_prize varchar(100), second_prize varchar(100), third_prize varchar(100), age_requirement varchar(20), game_mode varchar(100), club_name varchar(100))");
      Integer buyIn = tournament.getBuyIn();
      if (buyIn == null)
      {
        buyIn = 0;
      }
      String ageReq = tournament.getAgeRequirement();
      if (ageReq == null)
      {
        ageReq = "all ages";
      }
      stmt.executeUpdate("INSERT INTO tournaments (name, date, time, participant_slots, buy_in, first_prize, second_prize, third_prize, age_requirement, game_mode, club_name) VALUES ('" + tournament.getName() + "','" + tournament.getDate() + "','" + tournament.getTime() + "','" + tournament.getParticipantSlots() + "','" + buyIn + "','" + tournament.getFirstPrize() + "','" + tournament.getSecondPrize() + "','" + tournament.getThirdPrize() + "','" + ageReq + "','" + tournament.getGameMode() + "','" + tournament.getClubName() + "')");
      
      return "redirect:/tee-rific/availableTournaments/" + user;
    } catch (Exception e)
    {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
          path = "/tee-rific/viewTournament/{tid}/{username}"
  )
  public String viewSelectedTournament(@PathVariable("username")String user, Map<String, Object> model, @PathVariable("tournamentId") String tournamentId, HttpServletRequest request)
  {
    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if(!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try(Connection connection = dataSource.getConnection())
    {
      Statement stmt = connection.createStatement();
      model.put("id", tournamentId);
      ResultSet rs = stmt.executeQuery("SELECT * FROM tournaments WHERE id =" + tournamentId);
      ArrayList<Tournament> output = new ArrayList<Tournament>();
      while (rs.next())
      {
        Tournament tournament = new Tournament();
        tournament.setId(rs.getInt("id"));
        tournament.setName(rs.getString("name"));
        tournament.setDate(rs.getString("date"));
        tournament.setTime(rs.getString("time"));
        tournament.setParticipantSlots(rs.getInt("participant_slots"));
        tournament.setBuyIn(rs.getInt("buy_in"));
        tournament.setFirstPrize(rs.getString("first_prize"));
        tournament.setSecondPrize(rs.getString("second_prize"));
        tournament.setThirdPrize(rs.getString("third_prize"));
        tournament.setAgeRequirement(rs.getString("age_requirement"));
        tournament.setGameMode(rs.getString("game_mode"));
        tournament.setClubName(rs.getString("club_name"));

        output.add(tournament);
      }

      model.put("tournaments", output);
      Tournament tournament = new Tournament();
      model.put("tournament", tournament);
      model.put("username", user);
      model.put("tournamentId", tournamentId);
      return "Tournaments/viewTournament";

    } catch (Exception e)
    {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
    path = "/tee-rific/tournamentDelete/{tournamentId}/{username}"
  )
  public String displayDeleteTournamentPage(@PathVariable("username")String user, @PathVariable("tournamentId") String tournamentId, Map<String, Object> model, HttpServletRequest request)
  {

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/tournamentDelete/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    model.put("username", user);
    model.put("tournamentId", tournamentId);
    return "Tournaments/tournamentDelete";
  }//displayDeleteTournamentPage()


  @PostMapping(
          path = "/tee-rific/tournamentDelete/{tournamentId}/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String deleteTournament(@PathVariable("username")String user, @PathVariable("tournamentId") String tournamentId, Map<String, Object> model, Tournament tournament)
  {
    try (Connection connection = dataSource.getConnection())
    {
      Statement stmt = connection.createStatement();
      stmt.execute("DELETE FROM tournaments WHERE id = " + tournamentId);
      return "redirect:/tee-rific/availableTournaments/" + user;
    } catch (Exception e)
    {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
    path = "/tee-rific/tournamentSignUp/{tournamentId}/{username}"
    )
  public String tournamentSignUp(@PathVariable("username")String user,  @PathVariable("tournamentId") String tournamentId, Map<String, Object> model, Tournament tournament, HttpServletRequest request)
    {

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/tournamentSignUp/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection())
    {
      Statement stmt = connection.createStatement();
      //add user to tournament.participants
      model.put("username", user);
      model.put("tournamentId", tournamentId);
      // ArrayList<User> old_partitipant_list = tournament.getParticipants();
      // //search participant list to see if use is already registered
      // for (User participant : old_partitipant_list)
      // {
      //   if (participant == user)
      //   {
      //     //pop up displays to show that the user is already signed up in the tournament
      //     return "tournamentSignUp";
      //   }
      // }
      // old_partitipant_list.add(user);
      // tournament.setParticipants(old_partitipant_list);
      // //display sign up success
      return "Tournaments/tournamentSignUp";
    } catch (Exception e)
    {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


// @PostMapping(
//   path ="tee-rific/tournamentSignUp",
//   consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
// )
// public String handleTournamentSignUp(Map<String, Object> model, Tournament tournament)
// {
//   try (Connection connection = dataSource.getConnection())
//   {
//     //sign the user up, add them to the participant list
//   } catch (Exception e)
//   {
//     model.put("message", e.getMessage());
//     return "error";
//   }
// }


//**********************
// ADMIN BUTTONS
//**********************

  // LIST OF USERS
//--------------------------------
  @GetMapping(
          path = "/tee-rific/admin/users"
  )
  public String listUsers(Map<String, Object> model, HttpServletRequest request)
  {

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    if(!"admin".equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(30), username varchar(30), password varchar(100), fname varchar(30), lname varchar(30), email varchar(30), gender varchar(30))");

      // creates and check if admin account created
      int checkIfAdminExists = 0;
      ResultSet checkAdmin = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
      while (checkAdmin.next()){
        checkIfAdminExists++;
      }
      if (checkIfAdminExists == 0){
        String adminPassword = "cmpt276";
        String encryptedAdminPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String insert = "INSERT INTO users (priority, username, password) VALUES ('ADMIN','admin','"+encryptedAdminPassword+"')";
        stmt.executeUpdate(insert);
      }

      ResultSet listU = stmt.executeQuery("SELECT * FROM users");
      ArrayList<User> output = new ArrayList<User>();
      while (listU.next()) {
        User temp = new User();

        temp.setPriority(listU.getString("priority"));
        temp.setUsername(listU.getString("username"));
        temp.setPassword(listU.getString("password"));
        temp.setFname(listU.getString("fname"));
        temp.setLname(listU.getString("lname"));
        temp.setEmail(listU.getString("email"));
        temp.setGender(listU.getString("gender"));

        output.add(temp);
      }

      model.put("userList",output);
      return "Admin/listOfUsers";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
          path = "/tee-rific/admin/users/clear",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String clearUserDB(Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()){
      Statement stmtUser = connection.createStatement();
      Statement stmtOwner = connection.createStatement();

      ResultSet deleteOwners = stmtUser.executeQuery("SELECT * FROM users");          // Delete owner accounts as well
      while(deleteOwners.next()){
        String checkPrioirty = deleteOwners.getString("priority");
        if (checkPrioirty.equals(priorities[1])){
          String userName = deleteOwners.getString("username");
          stmtOwner.executeUpdate("DELETE FROM owners WHERE username='"+userName+"'");
        }
      }

      stmtUser.executeUpdate("DROP TABLE users");


      return "redirect:/tee-rific/admin/users";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @PostMapping(
          path = "/tee-rific/admin/users/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String deleteUser(Map<String, Object> model, @PathVariable("username") String name){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String sql = "DELETE FROM users WHERE username='"+name+"'";
      stmt.executeUpdate(sql);

      return "LandingPages/deleteSuccess";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  // LIST OF TOURNAMENTS
//--------------------------------
  @GetMapping(
          path = "/tee-rific/admin/tournaments"
  )
  public String listTournaments(Map<String, Object> model, HttpServletRequest request)
  {

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    if(!"admin".equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tournaments (id serial, name varchar(50), participant_slots integer, buy_in integer, first_prize varchar(30), second_prize varchar(30), third_prize varchar(30), age_requirement integer, game_mode varchar(30), club_name varchar(50))");
      ResultSet listT = stmt.executeQuery("SELECT * FROM tournaments IF ");
      ArrayList<Tournament> output = new ArrayList<Tournament>();
      while (listT.next()) {
        Tournament temp = new Tournament();

        temp.setId(listT.getInt("id"));
        temp.setName(listT.getString("name"));
        temp.setDate(listT.getString("date"));
        temp.setTime(listT.getString("time"));
        temp.setParticipantSlots(listT.getInt("participant_slots"));
        temp.setBuyIn(listT.getInt("buy_in"));
        temp.setFirstPrize(listT.getString("first_prize"));
        temp.setSecondPrize(listT.getString("second_prize"));
        temp.setThirdPrize(listT.getString("third_prize"));
        temp.setAgeRequirement(listT.getString("age_requirement"));
        temp.setGameMode(listT.getString("game_mode"));
        temp.setClubName(listT.getString("club_name"));

        output.add(temp);
      }

      model.put("tournamentList",output);
      return "Admin/listOfTournaments";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
          path = "/tee-rific/admin/tournaments/clear",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String clearTournamentDB(Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String sql = "DROP TABLE tournaments";
      stmt.executeUpdate(sql);

      return "redirect:/tee-rific/admin/tournaments";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
          path = "/tee-rific/admin/tournaments/{id}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String deleteTournament(Map<String, Object> model, @PathVariable("id") long id){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String sql = "DELETE FROM tournaments WHERE id="+id;
      stmt.executeUpdate(sql);

      return "LandingPages/deleteSuccess";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  // LIST OF OWNERS AND GOLF COURSES
//--------------------------------
  @GetMapping(
          path = "/tee-rific/admin/owners"
  )
  public String listOwners(Map<String, Object> model, HttpServletRequest request)
  {

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    if(!"admin".equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String checkIfOwnerTExists = getSQLNewTableOwner();
      stmt.executeUpdate(checkIfOwnerTExists);
      ResultSet listO = stmt.executeQuery("SELECT * FROM owners");
      ArrayList<CourseOwner> output = new ArrayList<CourseOwner>();
      while (listO.next()) {
        CourseOwner temp = new CourseOwner();

        temp.setCourseName(listO.getString("coursename"));
        temp.setAddress(listO.getString("address"));
        temp.setCity(listO.getString("city"));
        temp.setCountry(listO.getString("country"));

        temp.setUsername(listO.getString("username"));
        temp.setPassword(listO.getString("password"));
        temp.setFname(listO.getString("firstname"));
        temp.setLname(listO.getString("lastname"));
        temp.setEmail(listO.getString("email"));
        temp.setGender(listO.getString("gender"));

        output.add(temp);
      }

      model.put("ownerList",output);
      return "Admin/listOfOwners";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
          path = "/tee-rific/admin/owners/clear",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String clearOwnerDB(Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()){
      Statement stmtOwner = connection.createStatement();
      Statement stmtUser = connection.createStatement();
      ResultSet deleteOwners = stmtOwner.executeQuery("SELECT * FROM owners");
      while(deleteOwners.next()){
        String ownerName = deleteOwners.getString("username");
        stmtUser.executeUpdate("DELETE FROM users WHERE username='"+ownerName+"'");
      }

      stmtOwner.executeUpdate("DROP TABLE owners");


      return "redirect:/tee-rific/admin/owners";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
          path = "/tee-rific/admin/owner/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String deleteOwner(Map<String, Object> model, @PathVariable("username") String name){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String sql = "DELETE FROM owners WHERE username='"+name+"'";
      stmt.executeUpdate(sql);
      String userSql = "DELETE FROM users WHERE username='"+name+"'";
      stmt.executeUpdate(userSql);

      return "LandingPages/deleteSuccess";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @GetMapping(
          path = "/tee-rific/admin/owners/golfCourse/{courseName}"
  )
  public String viewGolfCourse(Map<String, Object> model, @PathVariable("courseName") String course, HttpServletRequest request){

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    if(!"admin".equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()){
      String searchCourse = convertToSnakeCase(course);

      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM "+searchCourse;
      ResultSet courseDetails = stmt.executeQuery(sql);

      ArrayList<Hole> output = new ArrayList<Hole>();
      while (courseDetails.next()) {
        Hole temp = new Hole();

        //(holeNumber integer, yardage integer, par integer, handicap integer)
        temp.setHoleNumber(courseDetails.getInt("holeNumber"));
        temp.setYardage(courseDetails.getInt("yardage"));
        temp.setPar(courseDetails.getInt("par"));
        temp.setHandicap(courseDetails.getInt("handicap"));

        output.add(temp);
      }

      model.put("details",output);
      return "Admin/courseDetails";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

//**********************
// ABOUT-US
//**********************

  @GetMapping(
          path = "/tee-rific/aboutUs/{username}"
  )
  public String aboutDevelopers(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request){

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/aboutUs/" + request.getSession().getAttribute("username");
    }

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    //this is optional, if you guys feel comfortable doing so, we can upload 'selfies' of our team and maybe talk about our development process
    model.put("username", user);
    return "LandingPages/aboutUs";
  }


//**********************
// LOGOUT
//**********************

  @GetMapping(
          path = "/tee-rific/logout"
  )
  public String Logout(HttpServletRequest request){

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    //log the user out
    return "LandingPages/logout";
  }

  @PostMapping(
          path = "tee-rific/logout"
  )
  public String confirmLogout(HttpServletRequest request) {
    request.getSession().invalidate();
    return "redirect:/";
  }

  // try 
  // {
  //   URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=vancouver&units=metric&appid=96bf787bdb96400f9a642360f1e901d7");
  //   HttpURLConnection conn = (HttpURLConnection)url.openConnection();
  //   conn.setRquestMethod("GET");
  //   conn.connect();
  
  //   int response_code = conn.getResponseCode();
  //   if (response_code != 200)
  //   {
  //     //throw exception
  //   }
  //   else
  //   {
  //     String inline = "";
  //     Scanner scanner = new Scanner(url.openStream());
  //     while (scanner.hasNext())
  //     {
  //       inline += scanner.nextLine();
  //     }
  //     scanner.close();
  
  //   JSONParser parse = new JSONParser();
  //   JSONObject data_obj = (JSONObject) parse.parse(inline);
  //   }
  // }



  //read the weather data I want

  // JSONObject object = (JSONObject) data_obj.get("data I want here");



}
