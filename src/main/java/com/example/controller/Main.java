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

import java.time.LocalDate;

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
  public String getLoginPage(Map<String, Object> model) {

    User user = new User();
    model.put("loginUser", user);

    if (failedLogin == true) {
      String error = "Error: Username/Password Doesn't match/exist";
      model.put("failedLogin", error);
      failedLogin = false;
    } else if (changedUsername == true) {
      String redirectText = "Successfully changed Username!\nSince you changed username, Please relog in with new username.";
      model.put("failedLogin", redirectText);
      changedUsername = false;
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
      while (checkAdmin.next()) {
        checkIfAdminExists++;
      }
      if (checkIfAdminExists == 0) {
        String adminPassword = "cmpt276";
        String encryptedAdminPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String insert = "INSERT INTO users (priority, username, password) VALUES ('ADMIN','admin','" + encryptedAdminPassword + "')";
        stmt.executeUpdate(insert);
      }

      String sql = "SELECT * FROM users WHERE username = '" + user.getUsername() + "'";
      ResultSet rs = stmt.executeQuery(sql);

      int checkIfUserExists = 0;
      String checkPassword = "";
      String priority = "";
      while (rs.next()) {
        checkIfUserExists++;
        checkPassword = rs.getString("password");
        priority = rs.getString("priority");

        String encryptedPassword = BCrypt.hashpw(checkPassword, BCrypt.gensalt());

      }
      System.out.println(checkPassword);
      System.out.println(user.getPassword());

      if (checkIfUserExists > 0 && (BCrypt.checkpw(user.getPassword(), checkPassword))) {

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
    if (usernameError == true) {
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
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

      user.setPriority(priorities[0]);      //sets the priority of the user to 'GOLFER'

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))");

      // creates and check if admin account created
      int checkIfAdminExists = 0;
      ResultSet checkAdmin = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
      while (checkAdmin.next()) {
        checkIfAdminExists++;
      }
      if (checkIfAdminExists == 0) {
        String adminPassword = "cmpt276";
        String encryptedAdminPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String insert = "INSERT INTO users (priority, username, password) VALUES ('ADMIN','admin','" + encryptedAdminPassword + "')";
        stmt.executeUpdate(insert);
      }

      stmt.executeUpdate("INSERT INTO users (priority, username, password, fname, lname, email, gender) VALUES ('" + user.getPriority() + "','" + user.getUsername() + "','" + encryptedPassword + "','" + user.getFname() + "','" + user.getLname() + "','" + user.getEmail() + "','" + user.getGender() + "')");

      String sql = "SELECT username FROM users WHERE username ='" + user.getUsername() + "'";
      ResultSet rs = stmt.executeQuery(sql);
      int checkCount = 0;
      while (rs.next()) {
        checkCount++;
      }

      if (checkCount > 1) {
        stmt.executeUpdate("DELETE FROM users WHERE priority='" + user.getPriority() + "' and username='" + user.getUsername() + "' and password='" + encryptedPassword + "' and fname='" + user.getFname() + "' and lname='" + user.getLname() + "' and email='" + user.getEmail() + "' and gender='" + user.getGender() + "'");
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
  public String getOwnerSignUpPage(Map<String, Object> model) {
    CourseOwner owner = new CourseOwner();
    model.put("newOwner", owner);

    if (usernameError == true) {
      String error = "Error: Username already Exists.";
      model.put("usernameError", error);
      usernameError = false;
    } else if (courseNameError == true) {
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
  public String handleBrowserOwnerSubmit(Map<String, Object> model, CourseOwner owner) {
    //create a new user, owner, golf course, then add to database

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String encryptedPassword = BCrypt.hashpw(owner.getPassword(), BCrypt.gensalt());

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
      for (int i = 0; i < owner.getNumHoles(); i++) {
        String insertHole = "INSERT INTO " + updatedCourseName + "(" + "holeNumber, yardage, par, handicap) VALUES (' " + (i + 1) + "', '0', '0', '0')";
        stmt.executeUpdate(insertHole);
      }

      //add to user table
      String userInfo = getSQLNewTableUsers();

      // creates and check if admin account created
      int checkIfAdminExists = 0;
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))");
      ResultSet checkAdmin = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
      while (checkAdmin.next()) {
        checkIfAdminExists++;
      }
      if (checkIfAdminExists == 0) {
        String adminPassword = "cmpt276";
        String encryptedAdminPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String insert = "INSERT INTO users (priority, username, password) VALUES ('ADMIN','admin','" + encryptedAdminPassword + "')";
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
      ownerCreateInventory(connection, owner.getCourseName());
      ownerCreateBookingsTable(connection);

      // check if username or course name exists for already existing user
      String sql = "SELECT username FROM owners WHERE username ='" + user.getUsername() + "'";
      ResultSet rs = stmt.executeQuery(sql);
      int checkUserCount = 0;
      while (rs.next()) {
        checkUserCount++;
      }

      String sqlCH = "SELECT courseName FROM owners WHERE courseName ='" + updatedCourseName + "'";
      ResultSet rsCH = stmt.executeQuery(sqlCH);
      int checkCNCount = 0;
      while (rsCH.next()) {
        checkCNCount++;
      }

      if (checkUserCount > 1) {
        // delete from user and owner database
        stmt.executeUpdate("DELETE FROM users WHERE priority='" + user.getPriority() + "' and username='" + user.getUsername() + "' and password='" + encryptedPassword + "' and fname='" + user.getFname() + "' and lname='" + user.getLname() + "' and email='" + user.getEmail() + "' and gender='" + user.getGender() + "'");
        String deleteOwner = getSQLDeleteOwner(owner, encryptedPassword);
        stmt.executeUpdate(deleteOwner);

        usernameError = true;
        return "redirect:/tee-rific/signup/Owner";
      } else if (checkCNCount > 1) {
        stmt.executeUpdate("DELETE FROM users WHERE priority='" + user.getPriority() + "' and username='" + user.getUsername() + "' and password='" + encryptedPassword + "' and fname='" + user.getFname() + "' and lname='" + user.getLname() + "' and email='" + user.getEmail() + "' and gender='" + user.getGender() + "'");
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
    return "CREATE TABLE IF NOT EXISTS owners (" +
            "courseName varchar(100), address varchar(100), city varchar(100), country varchar(100), website varchar(150), phoneNumber varchar(100), " +
            "courseLogo varchar(800), directionsToCourse varchar(1000), description varchar(1000), weekdayRates varchar(150), weekendRates varchar(150), numHoles integer, timeOpen varchar(10)," +
            "timeClose varchar(10), bookingInterval varchar(10), userName varchar(100), password varchar(100),firstName varchar(100),lastName varchar(100),email varchar(100),yardage varchar(100)," +
            "gender varchar(100), rating double precision, numberRatings double precision)";
  }


  private String getSQLInsertOwner(CourseOwner owner, String secretPW) {
    return "INSERT INTO owners ( " +
            "courseName, address, city, country, website, phoneNumber, courseLogo, " +
            "directionsToCourse, description, weekdayRates, weekendRates, numHoles, timeOpen," +
            "timeClose, bookingInterval, userName, password, firstName, lastName, email, yardage, gender, rating, numberRatings) VALUES ('" +
            owner.getCourseName() + "','" + owner.getAddress() + "','" + owner.getCity() + "','" +
            owner.getCountry() + "','" + owner.getWebsite() + "','" + owner.getPhoneNumber() + "','" +
            owner.getCourseLogo() + "','" + owner.getDirectionsToCourse() + "','" + owner.getDescription() + "','" +
            owner.getWeekdayRates() + "','" + owner.getWeekendRates() + "','" + owner.getNumHoles() + "','" + owner.getTimeOpen() + "','" +
            owner.getTimeClose() + "','" + owner.getBookingInterval() + "','" + owner.getUsername() + "','" + secretPW + "','" + owner.getFname() + "','" + owner.getLname() + "','" +
            owner.getEmail() + "','" + owner.getYardage() + "', '" + owner.getGender() + "', '" + owner.getRating() + "', '" + owner.getNumberRatings() + "')";
  }


  private String getSQLDeleteOwner(CourseOwner owner, String secretPW) {
    return "DELETE FROM owners WHERE courseName='" + owner.getCourseName() + "' and address='" + owner.getAddress() +
            "' and city='" + owner.getCity() + "' and country='" + owner.getCountry() + "' and website='" +
            owner.getWebsite() + "' and phoneNumber='" + owner.getPhoneNumber() + "' and courseLogo='" +
            owner.getCourseLogo() + "' and directionsToCourse='" + owner.getDirectionsToCourse() + "' and description='" +
            owner.getDescription() + "' and weekdayRates='" + owner.getWeekdayRates() + "' and weekendRates='" +
            owner.getWeekendRates() + "' and numHoles='" + owner.getNumHoles() + "' and timeOpen='" + owner.getTimeOpen() +
            "' and timeClose='" + owner.getTimeClose() + "' and bookingInterval='" + owner.getBookingInterval() + "' and userName='" + owner.getUsername() +
            "' and password='" + secretPW + "' and firstName='" + owner.getFname() + "' and lastName='" + owner.getLname() +
            "' and email='" + owner.getEmail() + "' and yardage='" + owner.getYardage() + "' and gender='" + owner.getGender() +
            "' and rating='" + owner.getRating() + "'and numberRatings='" + owner.getNumberRatings() + "'";
  }


  private String getSQLNewTableUsers() {
    return "CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))";
  }


  private String getSQLInsertUser(User user, String secretPW) {
    return "INSERT INTO users (priority, username, password, fname, lname, email, gender) VALUES ('" + user.getPriority() + "','" + user.getUsername() + "','" + secretPW + "','" + user.getFname() + "','" + user.getLname() + "','" + user.getEmail() + "','" + user.getGender() + "')";
  }


  private String convertToSnakeCase(String toConvert) {
    String updated = "";
    for (int i = 0; i < toConvert.length(); i++) {
      if (toConvert.charAt(i) == ' ') {
        updated += '_';
      } else {
        updated += toConvert.charAt(i);
      }
    }
    return updated;
  }


  private String convertFromSnakeCase(String toConvert) {
    String updated = "";
    for (int i = 0; i < toConvert.length(); i++) {
      if (toConvert.charAt(i) == '_') {
        updated += ' ';
      } else {
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

  public String getHomePage(@PathVariable("username") String user, Map<String, Object> model, HttpServletRequest request) throws Exception {

    if (!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    if (null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      //create owners table if not exists, prevents bookings button from breaking
      String newOwnersTable = getSQLNewTableOwner();
      stmt.executeUpdate(newOwnersTable);

      String getUserPriority= "SELECT priority FROM users WHERE username='" + user +"'";
      ResultSet rs = stmt.executeQuery(getUserPriority);

      String userPriority = "";
      while (rs.next()) {
        userPriority = rs.getString("priority");
      }

      if (userPriority.equals(priorities[0])) {         // returns golfer homepage
        return "LandingPages/home";
      } else if (userPriority.equals(priorities[1])) {   //returns owner homepage
          stmt.executeUpdate(getSQLNewTableOwner());
          ResultSet rs1 = stmt.executeQuery("SELECT * FROM owners WHERE username='" + user + "'");
          CourseOwner course1 = new CourseOwner();
          while (rs1.next()) {
            course1.setCourseName(rs1.getString("courseName"));
          }
          String coursename = course1.getCourseName();
          model.put("courseName", coursename);
        return "Owner/ownerHome";
      } else {                                          //returns admin homepage
        return "Admin/adminHome";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


//**********************
// COURSES
//**********************


//**********************
// MODIFY ACCOUNT
//**********************

  @GetMapping(
          path = "/tee-rific/account/{username}"
  )
  public String getAccountPage(@PathVariable("username") String user, Map<String, Object> model, HttpServletRequest request) {
    if (!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/account/" + request.getSession().getAttribute("username");
    }

    if (null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    model.put("username", user);
    return "AccountInfo/account";
  }


  @GetMapping(
          path = "/tee-rific/editAccount/{username}"
  )

  public String getEditAccountPage(@PathVariable("username") String user, Map<String, Object> model, HttpServletRequest request) {

    if (!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/editAccount/" + request.getSession().getAttribute("username");
    }

    if (null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String getUserPriority = "SELECT priority FROM users WHERE username='" + user + "'";
      ResultSet rs = stmt.executeQuery(getUserPriority);

      String userPriority = "";
      while (rs.next()) {
        userPriority = rs.getString("priority");
      }

      if (userPriority.equals(priorities[0])) {                   // returns golfer edit account
        String getUserDetails = "SELECT * FROM users WHERE username='" + user + "'";
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
      } else {                                                    //returns owner edit account
        String getUserDetails = "SELECT * FROM owners WHERE username='" + user + "'";
        ResultSet details = stmt.executeQuery(getUserDetails);
        details.next();

        CourseOwner output = new CourseOwner();

        output.setUsername(details.getString("username"));      //Account Info
        output.setPassword(details.getString("password"));
        output.setFname(details.getString("firstname"));
        output.setLname(details.getString("lastname"));
        output.setEmail(details.getString("email"));
        output.setGender(details.getString("gender"));

        output.setCourseName(details.getString("coursename"));  //Golf Course Info
        output.setAddress(details.getString("address"));
        output.setCity(details.getString("city"));
        output.setCountry(details.getString("country"));
        output.setPhoneNumber(details.getString("phonenumber"));
        output.setWebsite(details.getString("website"));
        output.setCourseLogo(details.getString("courselogo"));
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
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  boolean changeUsernameError = false;
  boolean changeValueError = false;

  @GetMapping(
          path = "/tee-rific/editAccount/passwordSecurity/{username}"
  )
  public String changePasswordSecurity(@PathVariable("username") String user, Map<String, Object> model, HttpServletRequest request) {
    if (!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/editAccount/passwordSecurity/" + request.getSession().getAttribute("username");
    }

    if (null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }
      User output = new User();

      model.put("userInfo", output);

    return "AccountInfo/changePasswordSecurity";
}


@PostMapping (
        "/tee-rific/editAccount/passwordSecurity/{username}/check"
)
public String checkPasswordVerification(@PathVariable("username") String user, User output, Map<String, Object> model, HttpServletRequest request) {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    System.out.println(user);
    String sql = "SELECT password FROM users WHERE username='" + user + "'";
    ResultSet rs = stmt.executeQuery(sql);
    String checkPassword = "";
    while (rs.next()) {
      checkPassword = rs.getString("password");
    }
      System.out.println(output.getPassword());
      System.out.println(checkPassword);

      if (BCrypt.checkpw(output.getPassword(), checkPassword)) {
        return "redirect:/tee-rific/editAccount/password/" + user;
      } else {
        String error = "Password is incorrect.";
        model.put("errorMessage", error);
        model.put("userInfo", output);
        return "AccountInfo/changePasswordSecurity";
      }
  } catch (Exception e) {
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
    
    CourseOwner newValue = new CourseOwner();
    model.put("value", newValue);
    model.put("column", column);
    model.put("user", user);

    //A cleaner version of text for title and to show which value the user is editing
    String cleanerColumn = column.replaceAll("(.)([A-Z])", "$1 $2");
    cleanerColumn = cleanerColumn.substring(0,1).toUpperCase() + cleanerColumn.substring(1);
    model.put("columnName", cleanerColumn);   

    if (changeUsernameError == true){         //refreshes page with error message
      changeUsernameError = false;
      String error = "Username is already taken.";
      model.put("errorMessage", error);
    } else if (changeValueError == true){     //if any other error
      changeUsernameError = false;
      String error = "Error Updating value, Go back and retry again.";
      model.put("errorMessage", error);
    }

    switch (column){     
      case "timeOpen":
      case "timeClose":                               // Since theres different input fields the owner can update,
        return "AccountInfo/changeTimeValues";        // it should redirect to the appropriate html with the right
      case "description":
      case "directionsToCourse":                      // input field to fill out.     - Justin
        return "AccountInfo/changeTextAreas";
      case "gender":
        return "AccountInfo/changeGender";
      case "courseLogo":
        return "AccountInfo/changeLogo";
      default:
        return "AccountInfo/changeShortStringValues";
    }
  }

  boolean changedUsername = false; 

  @PostMapping(
    path = "/tee-rific/editAccount/changing/{editColumn}/{username}",  
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String changeAccountInformation(@PathVariable("username")String user, @PathVariable("editColumn") String column, Map<String, Object> model, CourseOwner newValue){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String selectUser= "SELECT priority FROM users WHERE username='" + user +"'";
      ResultSet userData = stmt.executeQuery(selectUser);

      String value = "";
      switch(column){
        case "username":         // To get value, depending on which column
          // check if username is taken  
          Statement stmtCheck = connection.createStatement();  
          ResultSet check = stmtCheck.executeQuery("SELECT username FROM users WHERE username ='"+newValue.getUsername()+"'");
          int checkCount = 0;
          while (check.next()){
            checkCount++;
          }
          if (checkCount > 0){
            changeUsernameError = true;
            return "redirect:/tee-rific/editAccount/"+column+"/"+user;  //
          }          
          value = newValue.getUsername();
          break;
        case "password":
          // encrypt password
          value = BCrypt.hashpw(newValue.getPassword(), BCrypt.gensalt());
          break;
        case "fname":
          value = newValue.getFname();
          break;
        case "lname":
          value = newValue.getLname();
          break;
        case "email":
          value = newValue.getEmail();
          break;
        case "gender":
          value = newValue.getGender();
          break;
        case "address":
          value = newValue.getAddress();
          break;
        case "city":
          value = newValue.getCity();
          break;
        case "country":
          value = newValue.getCountry();
          break;
        case "phoneNumber":
          value = newValue.getPhoneNumber();
          break;
        case "website":
          value = newValue.getWebsite();
          break;
        case "timeOpen":
          value = newValue.getTimeOpen();
          break;
        case "timeClose":
          value = newValue.getTimeClose();
          break;
        case "weekdayRates":
          value = newValue.getWeekdayRates();
          break;
        case "weekendRates":
          value = newValue.getWeekendRates();
          break;
        case "courseLogo":
          value = newValue.getCourseLogo();
          break;
        case "directionsToCourse":
          value = newValue.getDirectionsToCourse();
          break;
        case "description":
          value = newValue.getDescription();
          break;
        default:
          changeValueError = true;        
          return "redirect:/tee-rific/editAccount/"+column+"/"+user+"";
      }

      column = column.toLowerCase();    //lowercase since columns are all lowercase letters

      String userPriority = "";
      while(userData.next()){
        userPriority = userData.getString("priority");
      }

      Statement stmtOwner = connection.createStatement();
      Statement stmtUser = connection.createStatement();

      if(userPriority.equals(priorities[0])){     //If user/golfer
        String updateColumnValue = "UPDATE users SET " + column + "='" + value + "' WHERE username='" + user + "'";
        stmtUser.executeUpdate(updateColumnValue);
      } else {
        // Update user part of account
        if (column.equals("username") || column.equals("password") || column.equals("fname") || column.equals("lname") || column.equals("email") || column.equals("gender")){
          
          if (column.equals("fname")){         // This if and else if are for first name and last name, since its different label on owner db
            String updateUserInfo = "UPDATE owners SET firstname='" + value + "' WHERE username='" + user + "'";
            stmtOwner.executeUpdate(updateUserInfo);
          } else if (column.equals("lname")){
            String updateUserInfo = "UPDATE owners SET lastname='" + value + "' WHERE username='" + user + "'";
            stmtOwner.executeUpdate(updateUserInfo);
          } else {
            String updateUserInfo = "UPDATE owners SET " + column + "='" + value + "' WHERE username='" + user + "'";
            stmtOwner.executeUpdate(updateUserInfo);      
          }
          String updateUserInfo = "UPDATE users SET " + column + "='" + value + "' WHERE username='" + user + "'";
          stmtUser.executeUpdate(updateUserInfo);                
        } else {
          // Update owner part of account
          String updateOwnerInfo = "UPDATE owners SET " + column + "='" + value + "' WHERE username='" + user + "'";
          stmtOwner.executeUpdate(updateOwnerInfo);
        }
      }

      // Update scorecards
      Statement updateScoreCards = connection.createStatement();
      String changeScorecard = "UPDATE scorecards SET username='" + value + "' WHERE username='" + user + "'";
      updateScoreCards.executeUpdate(changeScorecard);  

      if(column.equals("username")){
        changedUsername = true;
      }

      return "redirect:/tee-rific/editAccount/accountUpdatedSuccessfully/"+user+"";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    } 
  }

  @GetMapping(
    path = "/tee-rific/editAccount/accountUpdatedSuccessfully/{username}"
  )
  public String editSuccessfull(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request)
  {
    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/tee-rific/editAccount/accountUpdatedSuccessfully/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if (changedUsername == true){
      return "redirect:/tee-rific/login";
    }
    model.put("userName", user);
    return "AccountInfo/accountUpdatedSuccess";
  }

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
// BROWSE BOOKING SCHEDULE -- OWNER
//********************************

  @GetMapping(
      path = "/tee-rific/courseSchedule/{username}"
  )
  public String getCourseSchedule(@PathVariable("username")String user, Map<String, Object> model, WrapperDate newDate, HttpServletRequest request) throws Exception {
    
    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/golfCourseDetails/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet courseInfo = stmt.executeQuery("SELECT * FROM owners WHERE username='"+user+"'");
      courseInfo.next();

      String course = convertToSnakeCase(courseInfo.getString("courseName"));
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

      Integer increments = Integer.parseInt(courseInfo.getString("bookingInterval"));

      ArrayList<StatusTeeTime> courseSchedule = new ArrayList<StatusTeeTime>();
      Integer hour = 0;
      Integer min = 0;

      Integer numIntervals = 0;

      String dates = "";
      if (newDate.getDate() == null){
        Statement getDate = connection.createStatement();
        ResultSet date = getDate.executeQuery("SELECT * FROM bookings WHERE coursename='"+course+"' ORDER BY date asc");
        
        int checkIfEmpty = 0;
        while (date.next()){
          checkIfEmpty++;
        }
        
        if (checkIfEmpty > 0){
          date = getDate.executeQuery("SELECT * FROM bookings WHERE coursename='"+course+"' ORDER BY date asc");
          date.next();
          dates = date.getString("date");
        } else {
          LocalDate currentDate = LocalDate.now();
          dates = currentDate.toString();
        }

        if (dates == null || dates.isEmpty()){
          LocalDate currentDate = LocalDate.now();
          dates = currentDate.toString();
        }
      } else {
        dates = newDate.getDate();
      }

      while (hour < 25){
        if (hour >= timeOpenHr && hour < timeCloseHr) {   
          
          // starting value when close to timeOpen (ex: at 12:00 when TO is 12:45 w/ 5 min incrementals)
          if (hour == timeOpenHr && min < timeOpenMin) {      //ex: 12:15 - Start at 12:30
            while (min < timeOpenMin){
              if (min >= (60-increments)){
                min = 0;
                hour++;
                break;
              }
              min += increments;
            }
          }

          String time = singleDigitToDoubleDigitString(hour) + ":" + singleDigitToDoubleDigitString(min);
          StatusTeeTime ts = new StatusTeeTime();
          ts.setTeeTime(time);

          String teeTime = time + ":00";

          ts.setStatus("EMPTY");
          Statement checkOccupency = connection.createStatement();
          ResultSet checkTeeTime = checkOccupency.executeQuery("SELECT * FROM bookings WHERE coursename='"+course+"' AND teetime='"+teeTime+"' AND date='"+dates+"'");        
          
          int numPlayersTotal = 0;
          int countBookings = 0;
          while(checkTeeTime.next()){
            countBookings++;
            numPlayersTotal += checkTeeTime.getInt("numplayers");
          }
          ts.setTotalPlayers(numPlayersTotal);

          if (countBookings > 0){
            String newStatus = countBookings + " Bookings";
            ts.setStatus(newStatus);
          }
          courseSchedule.add(ts);
        }

        if (min < (60-increments)) {         //to set up to TimeOpen value/hour
          min += increments;
        } else {
          hour++;
          min = 0;
        }

        numIntervals++;
      }

      WrapperDate changeDate = new WrapperDate();
      model.put("newDate", changeDate);
      model.put("currentDate", dates);
      model.put("teeTimes", courseSchedule);
      return "OwnerSchedule/courseSchedule";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  // HELPERS
  public Integer getDateToCompare(String date){
    String splitDate[] = date.split("-");
    String combineDate = splitDate[0] + splitDate[1] + splitDate[2];
    Integer intDate = Integer.parseInt(combineDate);
    return intDate;
  }

  @GetMapping(
        path = "/tee-rific/courseSchedule/{date}/{time}/{username}"
  )

  public String viewBookingsForTime(@PathVariable("username")String user, @PathVariable("date")String date, @PathVariable("time")String teeTime, Map<String, Object> model, HttpServletRequest request) throws Exception {
    
    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/golfCourseDetails/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet courseInfo = stmt.executeQuery("SELECT * FROM owners WHERE username='"+user+"'");
      courseInfo.next();
      String course = convertToSnakeCase(courseInfo.getString("courseName"));
      
      String bookingsCommand = "SELECT * FROM bookings WHERE coursename='" + course + "' AND date='" + date +"' AND teetime='" + teeTime + "'";
      ResultSet getBookings = stmt.executeQuery(bookingsCommand);
      ArrayList<TeeTimeBooking> output = new ArrayList<TeeTimeBooking>();

      while(getBookings.next()){
        TeeTimeBooking temp = new TeeTimeBooking();

        temp.setUsername(getBookings.getString("username"));
        temp.setNumPlayers(getBookings.getInt("numplayers"));
        temp.setGameID(getBookings.getInt("gameid"));
        temp.setRentalID(getBookings.getString("rentalid"));

        output.add(temp);
      }

      model.put("date", date);
      model.put("time", teeTime);
      model.put("course", course);
      model.put("bookings", output);
      return "OwnerSchedule/timeSchedule";

    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
      path = "/tee-rific/courseSchedule/{date}/{time}/{gameID}/{username}",
      consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String deleteBookingOwnerSide(@PathVariable("username")String user, @PathVariable("date")String date, @PathVariable("time")String teeTime, @PathVariable("gameID")int gameID, Map<String, Object> model, HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String getCourseCommand = "SELECT * FROM owners WHERE username='" + user + "'";
      ResultSet getCourseName = stmt.executeQuery(getCourseCommand);
      getCourseName.next();
      String courseName = convertFromSnakeCase(getCourseName.getString("coursename"));
      
      EquipmentCart updateInv = new EquipmentCart();

      //get the rental quantities
      int balls = 0;
      int clubs = 0;
      int carts = 0;
      ResultSet removeStock = stmt.executeQuery("SELECT * FROM rentals WHERE id=" + gameID);
      while(removeStock.next()){
        balls = removeStock.getInt("numballs");
        carts = removeStock.getInt("numcarts");
        clubs = removeStock.getInt("numclubs");
      }

      //negate cart values
      balls = 0 - balls;
      carts = 0 - carts;
      clubs = 0 - clubs;

      updateInv.setNumBalls(balls);
      updateInv.setNumCarts(carts);
      updateInv.setNumClubs(clubs);

      //subtract from inventory
      updateInventory(connection, updateInv, courseName);

      stmt.executeUpdate("DELETE FROM bookings WHERE gameID='"+gameID+"'");
      stmt.executeUpdate("DELETE FROM scorecards WHERE id='"+gameID+"'");
      stmt.executeUpdate("DELETE FROM rentals WHERE id='"+gameID+"'");

      return "redirect:/tee-rific/courseSchedule/"+user+"";
    }catch (Exception e) {
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
  public String getCourseDetails(@PathVariable("username")String user, Map<String, Object> model, HttpServletRequest request) throws Exception {

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/golfCourseDetails/" + request.getSession().getAttribute("username");
    }

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      //get the course name from user
      String getCourseName = "SELECT courseName FROM owners WHERE username='" + user + "'";
      ResultSet rs = stmt.executeQuery(getCourseName);

      while(rs.next()){
        getCourseName = rs.getString("courseName");
      }

      String convertedName = convertToSnakeCase(getCourseName);
      ArrayList<Hole> courseHoles = new ArrayList<Hole>();

      String getCourseHoles = "SELECT * FROM " + convertedName + " ORDER BY holenumber asc";
      rs = stmt.executeQuery(getCourseHoles);

      //get the current hole information from DB
      while(rs.next()){
        Hole hole = new Hole();
        hole.setHoleNumber(rs.getInt("holeNumber"));
        hole.setYardage(rs.getInt("yardage"));
        hole.setPar(rs.getInt("par"));
        hole.setHandicap(rs.getInt("handicap"));

        courseHoles.add(hole);
      }

      //wrap the arrayList in an object so that it can be binded in 'view'
      WrapperHoles wrapper = new WrapperHoles();
      wrapper.setHoles(courseHoles);
    
      model.put("WrapperHoles", wrapper);
      model.put("username", user);
      return "Booking&ViewingCourses/golfCourseDetails";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }//getCourseDetails()


  @PostMapping(
    path = "/tee-rific/golfCourseDetails/{username}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String updateCourseDetails(@PathVariable("username")String user, Map<String, Object> model, WrapperHoles wrapper) throws Exception{
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      //get the course name from user
      String getCourseName = "SELECT courseName FROM owners WHERE username='" + user + "'";
      ResultSet rs = stmt.executeQuery(getCourseName);

      while(rs.next()){
        getCourseName = rs.getString("courseName");
      }

      String convertedName = convertToSnakeCase(getCourseName);
      int totalYardage = 0;

      //loop through wrapper object, gets the hole, updates the DB
      for(int i = 0; i < wrapper.getHoles().size(); i++){
        Hole hole = wrapper.getHoles().get(i);
        totalYardage += hole.getYardage();

        String yardage = "UPDATE " + convertedName + " SET yardage='" + hole.getYardage() + "' WHERE holeNumber='" + hole.getHoleNumber() + "'"; 
        String par = "UPDATE " + convertedName + " SET par='" + hole.getPar() + "' WHERE holeNumber='" + hole.getHoleNumber() + "'";
        String handicap = "UPDATE " + convertedName + " SET handicap='" + hole.getHandicap() + "' WHERE holeNumber='" + hole.getHoleNumber() + "'";

        stmt.executeUpdate(yardage);
        stmt.executeUpdate(par);
        stmt.executeUpdate(handicap);
      }

      //update totalYardage Field in owners table
      String ownerTotalYardage = "UPDATE owners SET yardage='" + totalYardage + "' WHERE username='" + user + "'";
      stmt.executeUpdate(ownerTotalYardage); 

      model.put("username", user);
      return "redirect:/tee-rific/golfCourseDetails/" + user;
  }catch (Exception e) {
    model.put("message", e.getMessage());
    return "LandingPages/error";
  }
}//updateCourseDetails()



  //**********************
  // BOOKING
  //**********************
  // 1. User first selects which specific course to play at (to specify open / close times)
  // 2. User picks date and time for teetime (using table of buttons)
  // 3. Show options for renting equipment and such and enter into bookings table
  // 4. Create a scorecard for the game with the generated Game ID

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
      ArrayList<Timeslot> validTimeSlots = new ArrayList<Timeslot>();
      Integer hour = 0;
      Integer min = 0;

      Integer increments = Integer.parseInt(courseInfo.getString("bookingInterval"));

      while (hour < 25){
        if (hour >= timeOpenHr && hour < timeCloseHr) {   
          
          // starting value when close to timeOpen (ex: at 12:00 when TO is 12:45 w/ 5 min incrementals)
          if (hour == timeOpenHr && min < timeOpenMin) {      //ex: 12:15 - Start at 12:30
            // min += increments;
            while (min < timeOpenMin){
              if (min >= (60-increments)){
                min = 0;
                hour++;
                break;
              }
              min += increments;
            }
          }
          // if (hour == timeOpenHr && min < timeOpenMin) {      //ex: 12:45 - start at 1:00
          //   min = 0;
          //   hour++;
          // }
  
          String time = singleDigitToDoubleDigitString(hour) + ":" + singleDigitToDoubleDigitString(min);
          Timeslot ts = new Timeslot();
          ts.setTime(time);
  
          validTimeSlots.add(ts);
        }
  
        if (min < (60-increments)) {         //to set up before it 
          min += increments;
        } else {
          hour++;
          min = 0;
        }
      }
      // for (int i = 0; i < 48; i++) {
      //   if (hour >= timeOpenHr && hour < timeCloseHr) {
      //     if (hour == timeOpenHr && min < timeOpenMin) {
      //       min = 30;
      //     }
      //     if (hour == timeOpenHr && min < timeOpenMin) {
      //       min = 0;
      //       hour++;
      //     }

      //     String time = singleDigitToDoubleDigitString(hour) + ":" + singleDigitToDoubleDigitString(min);
      //     Timeslot ts = new Timeslot();
      //     ts.setTime(time);

      //     validTimeSlots.add(ts);
      //   }

      //   if (min == 0) {
      //     min = 30;
      //   } else {
      //     hour++;
      //     min = 0;
      //   }
      // }

      String city = courseInfo.getString("city");
      String country = courseInfo.getString("country");

      TeeTimeBooking booking = new TeeTimeBooking();
      booking.setUsername(user);
      courseName = convertToSnakeCase(courseName);
      model.put("timeSlots", validTimeSlots);
      model.put("courseName", courseName);
      model.put("gameID", gameID);
      model.put("username", user);
      model.put("booking", booking);
      model.put("city", city);
      model.put("country", country);
      model.put("OpenTime", timeOpenHr);
      model.put("CloseTime", timeCloseHr);

      model.put("notSnakeCasedCourseName", convertFromSnakeCase(courseName));
      
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

      updateBookingsTable(connection, booking, courseName, gameIDStr);

      // Create a new scorecard
      Scorecard scorecard = new Scorecard();
      scorecard.setActive(true);
      scorecard.setGameID(gameIDStr);
      scorecard.setDatePlayed(booking.getDate());
      scorecard.setCoursePlayed(courseName);
      scorecard.setAttestor("");
      scorecard.setFormatPlayed("");
      scorecard.setHolesPlayed("");
      scorecard.setTeesPlayed("");

      userCreateScorecardsTable(connection);
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
      ResultSet rs = stmt.executeQuery("SELECT * FROM bookings WHERE gameID='"+gameIDStr+"'");
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
      model.put("courseNameSC", courseNameSC);
      model.put("username", user);

      return "Booking&ViewingCourses/bookingSuccessful";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  } // bookingSuccessful()


  // HELPER BOIS
  public void ownerCreateBookingsTable(Connection connection) throws Exception {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (gameID serial, courseName varchar(100), username varchar(100), date date, teetime time, numplayers integer, rentalID varchar(20))");
  } //ownerCreateBookingsTable

  public String createNewBooking(Connection connection, String user, String courseName) throws Exception {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("INSERT INTO bookings (username, courseName) VALUES ('" + user + "' , '" + courseName + "')");

    // Go to very bottom of table since we just inserted there
    ResultSet rs = stmt.executeQuery("SELECT gameID FROM bookings");
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
    stmt.executeUpdate("UPDATE bookings SET (date, teetime, numplayers) = ('"+booking.getDate()+"', '"+booking.getTime()+"', '"+booking.getNumPlayers()+"') WHERE gameID='"+gameID+"'");
  } //updateBookingsTable()


//**********************
// RENT EQUIPMENT
//**********************

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
  } // rentEquipment()


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
  } // handleShop()



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
  } // handleViewCart()


  @PostMapping(
          path = "/tee-rific/rentEquipment/checkout/{courseName}/{gameID}/{username}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleCheckout(@PathVariable Map<String, String> pathVars, Map<String, Object> model) throws Exception {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    String courseName = convertFromSnakeCase(courseNameSC);

    String gameIDStr = pathVars.get("gameID");

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      EquipmentCart cart = getUserCartContentsFromDB(connection, user);
      updateInventory(connection, cart, courseName);
      stmt.executeUpdate("DROP TABLE cart_"+user+"");

      // Create table of rentals so employees can keep track
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rentals (id integer, courseName varchar(100), username varchar(100), dateCheckout timestamp DEFAULT now(), numBalls integer, numCarts integer, numClubs integer)");
      stmt.executeUpdate("INSERT INTO rentals (id, courseName, username, numBalls, numCarts, numClubs) VALUES ('" + gameIDStr + "','" + courseName + "' , '" +user+"', '"+cart.getNumBalls()+"', '"+cart.getNumCarts()+"', '"+cart.getNumClubs()+"')");

      // Link rental to booking
      stmt.executeUpdate("UPDATE bookings SET rentalID = '"+gameIDStr+"' WHERE gameID='"+gameIDStr+"'");
      
      model.put("username", user);

      return "redirect:/tee-rific/rentEquipment/checkout/success/ + username";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  } // handleCheckout()


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

// *********************
    //    OWNER'S PAGE
// *********************
 
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

      ResultSet rs = stmt.executeQuery("SELECT * FROM inventory WHERE courseName = '"+courseName+"'");
      ArrayList<Equipment> eqs = new ArrayList<Equipment>();
      
      while (rs.next()) {
        Equipment eq = new Equipment();
        eq.setItemName(rs.getString("itemName"));
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
      ownerUpdateInventory(connection, cart, courseName);

      return "redirect:/tee-rific/golfCourseDetails/inventory/" + user;

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  @GetMapping(
    path = "/tee-rific/scorecards/{username}/{courseName}/{gameID}/cancel"
  )
  public String cancelBooking(@PathVariable Map<String, String> pathVars, Map<String, Object> model, HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      String user = pathVars.get("username");
      String courseNameSC = pathVars.get("courseName");
      String gameID = pathVars.get("gameID");
      String courseName = convertFromSnakeCase(courseNameSC);

      if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
        return "redirect:/tee-rific/rentEquipment/" + request.getSession().getAttribute("username");
      }
  
      if(null == (request.getSession().getAttribute("username"))) {
        return "redirect:/";
      }

      TeeTimeBooking toCancel = new TeeTimeBooking();

      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM bookings WHERE gameID='"+gameID+"'");
      rs.next();

      toCancel.setDate(rs.getString("date"));
      toCancel.setTime(rs.getString("teetime"));
      toCancel.setGameID(rs.getInt("gameID"));

      model.put("username", user);
      model.put("courseName", courseName);
      model.put("courseNameSC", courseNameSC);
      model.put("gameID", gameID);
      model.put("toCancel", toCancel);

      return "Booking&ViewingCourses/bookingCancel";

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  } 

  @PostMapping(
    path = "/tee-rific/scorecards/{username}/{courseName}/{gameID}/cancel", 
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleCancelBooking(@PathVariable Map<String, String> pathVars, Map<String, Object> model, TeeTimeBooking toCancel) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      String user = pathVars.get("username");
      String courseNameSC = pathVars.get("courseName");
      String gameID = pathVars.get("gameID");
      String courseName = convertFromSnakeCase(courseNameSC);

      Statement stmt = connection.createStatement();
      EquipmentCart updateInv = new EquipmentCart();

      //get the rental quantities
      int balls = 0;
      int clubs = 0;
      int carts = 0;
      ResultSet removeStock = stmt.executeQuery("SELECT * FROM rentals WHERE id=" + gameID);
      while(removeStock.next()){
        balls = removeStock.getInt("numballs");
        carts = removeStock.getInt("numcarts");
        clubs = removeStock.getInt("numclubs");
      }

      //negate cart values
      balls = 0 - balls;
      carts = 0 - carts;
      clubs = 0 - clubs;

      updateInv.setNumBalls(balls);
      updateInv.setNumCarts(carts);
      updateInv.setNumClubs(clubs);

      //subtract from inventory
      updateInventory(connection, updateInv, courseName);

      stmt.executeUpdate("DELETE FROM bookings WHERE gameID='"+gameID+"'");
      stmt.executeUpdate("DELETE FROM scorecards WHERE id='"+gameID+"'");
      stmt.executeUpdate("DELETE FROM rentals WHERE id='"+gameID+"'");

      return "redirect:/tee-rific/scorecards/" + user;
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }


  private void updateInventory(Connection connection, EquipmentCart cart, String courseName) throws Exception {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM inventory WHERE courseName = '"+courseName+"'");

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
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedBallStock+"' WHERE itemName = 'balls' AND courseName = '"+courseName+"'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedGolfCartStock+"' WHERE itemName = 'carts' AND courseName = '"+courseName+"'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedClubStock+"' WHERE itemName = 'clubs' AND courseName = '"+courseName+"'");
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
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS inventory (courseName varchar(100), itemName varchar(100), stock integer DEFAULT 0)");
    stmt.executeUpdate("INSERT INTO inventory (courseName, itemName) VALUES ('"+courseName+"', 'balls')");
    stmt.executeUpdate("INSERT INTO inventory (courseName, itemName) VALUES ('"+courseName+"', 'carts')");
    stmt.executeUpdate("INSERT INTO inventory (courseName, itemName) VALUES ('"+courseName+"', 'clubs')");
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
    stmt.executeUpdate("UPDATE inventory SET stock ='"+cart.getNumBalls()+"' WHERE itemName = 'balls' AND courseName = '"+courseName+"'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+cart.getNumCarts()+"' WHERE itemName = 'carts' AND courseName = '"+courseName+"'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+cart.getNumClubs()+"' WHERE itemName = 'clubs' AND courseName = '"+courseName+"'");
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
      ResultSet rs = stmt.executeQuery("SELECT * FROM owners ORDER BY courseName ASC");
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
        course.setRating(rs.getDouble("rating"));
        course.setNumberRatings(rs.getDouble("numberRatings"));

        if(course.getCourseLogo().equals("null")){
          course.setCourseLogo("/images/tee-rificBall.png");
        }

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

      String getOwnerInfo = "SELECT * FROM  owners WHERE courseName='"+ convertFromSnakeCase(convertedName) + "'";

      ResultSet info = stmt.executeQuery(getOwnerInfo);

      info.next();

      String timeOpenStr = info.getString("timeOpen");
      String timeOpenSegments[] = timeOpenStr.split(":");
      String timeOpenHrStr = timeOpenSegments[0];
      
      String timeCloseStr = info.getString("timeClose");
      String timeCloseSegments[] = timeCloseStr.split(":");
      String timeCloseHrStr = timeCloseSegments[0];
      
      Integer timeOpenHr = Integer.parseInt(timeOpenHrStr);
      Integer timeCloseHr = Integer.parseInt(timeCloseHrStr);

      String city = info.getString("city");
      String country = info.getString("country");

      model.put("courseName", courseID);
      model.put("username", user);
      model.put("course", courseHoles);
      model.put("city", city);
      model.put("country", country);
      model.put("OpenTime", timeOpenHr);
      model.put("CloseTime", timeCloseHr);
      

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

      userCreateScorecardsTable(connection);

      ResultSet rs = stmt.executeQuery("SELECT * FROM scorecards WHERE userName='" + user + "' ORDER by date DESC");
      ArrayList<Scorecard> output = new ArrayList<Scorecard>();

      while(rs.next()) {
        Scorecard scorecard = new Scorecard();
        scorecard.setGameID(rs.getString("id"));
        scorecard.setUserName(rs.getString("userName"));
        scorecard.setDatePlayed(rs.getString("date"));
        scorecard.setCoursePlayed(rs.getString("course"));
        scorecard.setCoursePlayedSC(convertToSnakeCase(scorecard.getCoursePlayed()));
        scorecard.setTeesPlayed(rs.getString("teesPlayed"));
        scorecard.setHolesPlayed(rs.getString("holesPlayed"));
        scorecard.setFormatPlayed(rs.getString("formatPlayed"));
        scorecard.setAttestor(rs.getString("attestor"));

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
    path = "tee-rific/scorecards/bestScores/{username}"
  )
  public String getBestScores(@PathVariable("username")String user,  Map<String, Object> model, HttpServletRequest request) throws Exception {
    
    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if(!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      ArrayList<LowestScore> lowestScores = new ArrayList<LowestScore>();
      ArrayList<Scorecard> scorecards = new ArrayList<Scorecard>();

      //go through the 'completed' (active == f) games in scorecards (need course, strokes, gameID, date )
      String getScorecardsInfo = "SELECT * FROM scorecards WHERE username='" + user + "' AND active='f' ORDER BY course ASC";
      ResultSet scorecardInfo = stmt.executeQuery(getScorecardsInfo);
      while(scorecardInfo.next()){
        Scorecard scorecard = new Scorecard();

        scorecard.setActive(scorecardInfo.getBoolean("active"));
        scorecard.setGameID(scorecardInfo.getString("id"));
        scorecard.setUserName(scorecardInfo.getString("username"));
        scorecard.setDatePlayed(scorecardInfo.getString("date"));
        scorecard.setCoursePlayed(scorecardInfo.getString("course"));

        //get the stroke information
        String hole;
        ArrayList<Integer> strokesForGame = new ArrayList<Integer>();
        for(int i = 0; i < 18; i++){
          Integer holeNum = i + 1;
          String holeNumString = holeNum.toString();
          hole = "s" + holeNumString;
          
          strokesForGame.add(scorecardInfo.getInt(hole));   //add to strokes if user completed hole
        }

        //adds the strokes arrayList to the scorecard
        scorecard.setStrokes(strokesForGame);

        //add the scorecard to scorecard list
        scorecards.add(scorecard);
      }

      //get the courses par, compare with the user strokes to calculate score for the game if the user particpated on every hole for a valid game
      for(int i = 0; i < scorecards.size(); i++){
        String courseName = scorecards.get(i).getCoursePlayed();
        String courseNameSC = convertToSnakeCase(courseName);
        ArrayList<Integer> pars = new ArrayList<Integer>();
        Boolean validGame = true;
        
        //get the course pars
        String getCoursePars = "SELECT par FROM " + courseNameSC + " ORDER BY holeNumber DESC";
        ResultSet coursePars = stmt.executeQuery(getCoursePars);
        while(coursePars.next()){
          pars.add(coursePars.getInt("par"));
        }
        
        //calculate score
        int score = 0;
        for(int j = 0; j < pars.size(); j++){
          int strokesForHole = scorecards.get(i).getStrokes().get(j);
          int par = pars.get(j);

          if(strokesForHole == 0){    //the user did not particpate on the hole, therefore, the
            validGame = false;
            break;     //goes to next iteration of the loop
           }
          
          int scoreOnHole = strokesForHole - par;
          score += scoreOnHole;
        }

        if(validGame){
          LowestScore scoreForGame = new LowestScore();
          scoreForGame.setCourseName(scorecards.get(i).getCoursePlayed());
          scoreForGame.setScore(score);
          scoreForGame.setGameID(scorecards.get(i).getGameID());
          scoreForGame.setDate(scorecards.get(i).getDatePlayed());

          //check the LowestScore list for the course played, if the score for this game is lower, replace, else leave as is. If the course is not in the list, add to list
          Boolean inLowestScores = false;
          for(int k = 0; k < lowestScores.size(); k++){
            //if course name is in lowest scores
            if(lowestScores.get(k).getCourseName().equals(scoreForGame.getCourseName())){
              //compare score
              inLowestScores = true;
              if(lowestScores.get(k).getScore() > scoreForGame.getScore()){
                //replace score
                lowestScores.get(k).setScore(scoreForGame.getScore());
              }
            }
          }
        
          //if no course is found in the list, add lowest score to an arrayList and put in model
          if(!inLowestScores){
            lowestScores.add(scoreForGame);
          } 
        }
      }
      
      model.put("bestScores", lowestScores);
      model.put("username", user);

      return "Scorecard/bestScores";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }//getBestScores()


  private void printLowestScores(ArrayList<LowestScore> lowestScores){
    for(int i = 0; i < lowestScores.size(); i++){
      String course = lowestScores.get(i).getCourseName();
      int score = lowestScores.get(i).getScore();

      System.out.println("Course: " + course + ", Score: " + score);
    }
  }


  @GetMapping(
          path = "/tee-rific/scorecards/{username}/{courseName}/{gameID}"
  )
  public String getSpecificScorecard(@PathVariable Map<String, String> pathVars, Map<String, Object> model, HttpServletRequest request) throws Exception {
    String user = pathVars.get("username");
      String courseNameSC = pathVars.get("courseName");
      String gameID = pathVars.get("gameID");
      String courseName = convertFromSnakeCase(courseNameSC);

    if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if(!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      //get the scorecard info
      String getScorecardInfo = "SELECT * FROM scorecards WHERE id='"+gameID+"' AND course='"+courseName+"' AND username='" + user + "'";
      ResultSet scoreCardInfo = stmt.executeQuery(getScorecardInfo);

      Scorecard scorecard = new Scorecard();
      ArrayList<Integer> strokes = new ArrayList<Integer>();

      while(scoreCardInfo.next()){
        scorecard.setActive(scoreCardInfo.getBoolean("active"));
        scorecard.setGameID(scoreCardInfo.getString("id"));
        scorecard.setDatePlayed(scoreCardInfo.getString("date"));
        scorecard.setCoursePlayed(scoreCardInfo.getString("course"));
        scorecard.setTeesPlayed(scoreCardInfo.getString("teesPlayed"));
        scorecard.setHolesPlayed(scoreCardInfo.getString("holesPlayed"));
        scorecard.setFormatPlayed(scoreCardInfo.getString("formatPlayed"));
        scorecard.setAttestor(scoreCardInfo.getString("attestor"));

        for(int i = 1; i <= 18; i++){
          String holeNum = "s" + String.valueOf(i);
          strokes.add(scoreCardInfo.getInt(holeNum));
        }
      }

      String getCourseInfo = "SELECT * FROM " + courseNameSC;
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

      //prep for wrapper class
      ArrayList<ScorecardHole> info = new ArrayList<ScorecardHole>();
      for(int i = 0; i < 18; i++){
        ScorecardHole holeInfo = new ScorecardHole();
        holeInfo.setHoleInfo(courseHoles.get(i));
        holeInfo.setStroke(strokes.get(i));
        info.add(holeInfo);
      }
      
      //create a wrapper class to store the arrayList so that it can be binded
      WrapperScorecardHoles scorecardWrapper = new WrapperScorecardHoles();
      scorecardWrapper.setScorecardHoles(info);
      scorecardWrapper.setActive(scorecard.isActive());


      //get tee-time from bookings
      String getTime = "SELECT teetime FROM bookings WHERE gameId='" + gameID + "'";
      ResultSet startTime = stmt.executeQuery(getTime);
      while(startTime.next()){
        getTime = startTime.getString("teetime");
      }

      //get particpants
      Integer participantReservations = 0;
      String getSlots = "SELECT numPlayers FROM bookings WHERE gameID='" + gameID + "'";
      ResultSet slots = stmt.executeQuery(getSlots);
      while(slots.next()){
        participantReservations = slots.getInt("numPlayers");
      }

      //get friends scorecards
      ArrayList<Scorecard> friendScorecards = new ArrayList<Scorecard>();
      
      String getFriendsScorecardInfo = "SELECT * FROM scorecards WHERE id='"+gameID+"' AND course='"+courseName+ "'";
      ResultSet friendScoreCardInfo = stmt.executeQuery(getFriendsScorecardInfo);

      while(friendScoreCardInfo.next()){
        
        if(!friendScoreCardInfo.getString("username").equals(user)){    //will only add to friends scorecardList if the current scorecard is not the user's
          Scorecard friendScorecard = new Scorecard();
          ArrayList<Integer> friendStrokes = new ArrayList<Integer>();

          for(int i = 1; i <= 18; i++){                 //get the strokes for each hole
            String holeNum = "s" + String.valueOf(i);
            friendStrokes.add(friendScoreCardInfo.getInt(holeNum));
          }
          friendScorecard.setUserName(friendScoreCardInfo.getString("username"));
          friendScorecard.setStrokes(friendStrokes);

          friendScorecards.add(friendScorecard);
        }
      }


      //get course logo from owners
      String getLogo = "SELECT courseLogo FROM owners WHERE coursename='" + courseName + "'";
      ResultSet logoURL = stmt.executeQuery(getLogo);
      while(logoURL.next()){
        getLogo = logoURL.getString("courseLogo");
        System.out.println("getting logo field from DB = '" + getLogo + "'");
      }

      if(getLogo.equals("null")){
        getLogo = "/images/tee-rificBall.png";      //tee-rificBallLogo
        System.out.println("No Image for Course, Tee-rific Ball assigned...");
      }
    
      //a field to store the name of a player to add
      PlayerToAdd newPlayer = new PlayerToAdd();


      //get the max number of players set out by the booking
      String getMaxPlayerCount = "SELECT numPlayers FROM bookings WHERE gameID='" + gameID + "'";
      ResultSet maxCount = stmt.executeQuery(getMaxPlayerCount);
      Integer maxPlayerCount = 0;
      while(maxCount.next()){
        maxPlayerCount = maxCount.getInt("numPlayers");
      }

      //checks the current slots already in use
      String getCurrentNumPlayers = "SELECT * FROM scorecards WHERE id='" + gameID + "'";
      ResultSet count = stmt.executeQuery(getCurrentNumPlayers);
      Integer currentPlayerCount = 0;
      while(count.next()){
        currentPlayerCount++;
      }
    
      model.put("currentPlayers", currentPlayerCount);
      model.put("maxPlayers", maxPlayerCount);
      model.put("invitee", newPlayer);
      model.put("friends", friendScorecards);
      model.put("slots", participantReservations);
      model.put("scoresWrapper", scorecardWrapper);
      model.put("logo", getLogo);
      model.put("teeTime", getTime);
      model.put("gameID", gameID);
      model.put("username", user);
      model.put("scorecard", scorecard);
      model.put("courseName", courseNameSC);

      return "Scorecard/game";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }//getSpecificScorecard()


@PostMapping(
  path = "/tee-rific/scorecards/{username}/{courseName}/{gameID}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String updateScorecard(@PathVariable("gameID")String gameID, @PathVariable("courseName")String course, @PathVariable("username")String user, WrapperScorecardHoles scoreWrapper, Map<String, Object> model){
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();

    boolean isActive = scoreWrapper.isActive();
    String updateStatus = "UPDATE scorecards SET active='" + isActive + "' WHERE id='" + gameID + "' AND username='" + user + "'";
    stmt.executeUpdate(updateStatus);

    for(int i = 0; i < 18; i++){
      Integer stroke = scoreWrapper.getScorecardHoles().get(i).getStroke();
      String updateDB = "UPDATE scorecards SET s" + Integer.toString(i + 1) + "='" + stroke + "' WHERE id='" + gameID + "' AND username='" + user + "'";
      stmt.executeUpdate(updateDB);   
    }

    if(isActive){
      return "redirect:/tee-rific/scorecards/" + user + "/" + course + "/" + gameID;
    }else{
      return "redirect:/tee-rific/rating/" + user + "/" + course;
    }
  }catch (Exception e) {
    model.put("message", e.getMessage());
    return "LandingPages/error";
  }
}//updateScorecard()


@GetMapping(
  path = "/tee-rific/scorecards/{username}/{course}/{gameID}/invite"
)
public String getGameInvite(@PathVariable("username")String user, @PathVariable("course")String course, @PathVariable("gameID")String gameID, HttpServletRequest request){

   if(null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if(!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    return "redirect:/tee-rific/scorecards/" + user + "/" + course + "/" + gameID;
}


@PostMapping(
  path = "/tee-rific/scorecards/{username}/{course}/{gameID}/invite"
)
public String inviteToGame(@PathVariable("username")String user, @PathVariable("course")String course, @PathVariable("gameID")String gameID, PlayerToAdd playerToAdd, Map<String, Object> model) {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();

    //get the max number of players set out by the booking
    String getMaxPlayerCount = "SELECT numPlayers FROM bookings WHERE gameID='" + gameID + "'";
    ResultSet maxCount = stmt.executeQuery(getMaxPlayerCount);
    Integer maxPlayerCount = 0;
    while(maxCount.next()){
      maxPlayerCount = maxCount.getInt("numPlayers");
    }
    System.out.println("MaxPlayerCount for gameID = " + maxPlayerCount);

    //checks the current slots already in use
    String getCurrentNumPlayers = "SELECT * FROM scorecards WHERE id='" + gameID + "'";
    ResultSet count = stmt.executeQuery(getCurrentNumPlayers);
    Integer currentPlayerCount = 0;
    while(count.next()){
      currentPlayerCount++;
    }
    System.out.println("CurrentPlayerCount for gameID = " + currentPlayerCount);

    //create a new scorecard if free slots
    
    if(currentPlayerCount < maxPlayerCount){
      //get the inviters scorecard from the DB
      Scorecard invitee = new Scorecard();

      String getInviterScorecard = "SELECT * FROM scorecards WHERE username='" + user + "' AND id='" + gameID + "'";
      ResultSet inviterScorecardDB = stmt.executeQuery(getInviterScorecard);
      while(inviterScorecardDB.next()){
        invitee.setActive(inviterScorecardDB.getBoolean("active"));
        invitee.setGameID(inviterScorecardDB.getString("id"));
        invitee.setUserName(playerToAdd.getName());
        invitee.setDatePlayed(inviterScorecardDB.getString("date"));
        invitee.setCoursePlayed(inviterScorecardDB.getString("course"));
        invitee.setTeesPlayed(inviterScorecardDB.getString("teesPlayed"));
        invitee.setHolesPlayed("0");
        invitee.setFormatPlayed(inviterScorecardDB.getString("formatPlayed"));
        invitee.setAttestor(inviterScorecardDB.getString("attestor"));

        ArrayList<Integer> initHoleStrokes = new ArrayList<Integer>();
        for(int i = 0 ; i < 18; i++){
          initHoleStrokes.add(0);
        }
        
        invitee.setStrokes(initHoleStrokes);
      }

      //check if playerToAdd is valid user
      Boolean isValidUser = false;
      String checkIfInDB = "SELECT * FROM users WHERE userName='" + playerToAdd.getName() + "'";
      ResultSet inviteeInDB = stmt.executeQuery(checkIfInDB);
      while(inviteeInDB.next()){
        if(inviteeInDB.getString("priority").equals("GOLFER")){
          isValidUser = true;
        }
      }

      //check if playerToAdd is in game already
      Boolean playerInGame = false;
      String checkIfInGame = "SELECT userName FROM scorecards WHERE userName='" + playerToAdd.getName() + "' AND id='" + gameID + "'";
      ResultSet inviteeInGame = stmt.executeQuery(checkIfInGame);
      while(inviteeInGame.next()){
        playerInGame = true;
      }

      //if playerToAdd is not currently playing game, add to game
      if(!playerInGame && isValidUser){
        userInsertScorecard(connection, playerToAdd.getName(), invitee);
        System.out.println(user + " added a new player to the " + gameID + " on " + course);
        System.out.println("Adding the new scorecard to Database for: " + playerToAdd.getName());
      }

    }else{
      System.out.println(user + " failed to add " + playerToAdd.getName() + " to the game");
    }

      return "redirect:/tee-rific/scorecards/" + user + "/" + course + "/" + gameID;
  }catch (Exception e) {
    model.put("message", e.getMessage());
    return "LandingPages/error";
  }
}



  @GetMapping(
          path = "tee-rific/rating/{username}/{courseName}"
  )
  public String getRatingPage(@PathVariable Map<String, String> pathVars, Map<String, Object> model, HttpServletRequest request) throws Exception {
    String user = pathVars.get("username");
    String courseNameSC = pathVars.get("courseName");
    if (null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if (!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }
    try (Connection connection = dataSource.getConnection()) {

      Statement stmt = connection.createStatement();
      stmt.executeUpdate(getSQLNewTableOwner());
      ResultSet rs = stmt.executeQuery("SELECT * FROM owners WHERE courseName='" + courseNameSC + "'");
      CourseOwner course1 = new CourseOwner();
      while (rs.next()) {
        course1.setRating(rs.getDouble("rating"));
        course1.setNumberRatings(rs.getDouble("numberRatings"));
      }
      double tempRating = (course1.getRating()*course1.getNumberRatings());
      double numberReviewsBefore = course1.getNumberRatings();

      model.put("course", convertFromSnakeCase(courseNameSC));
      model.put("oldRating", tempRating);
      model.put("courseRating", course1);
      model.put("nameCourse", courseNameSC);
      model.put("numReviews", numberReviewsBefore);
      return "Booking&ViewingCourses/review";

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
          path = "tee-rific/rating/{courseName}/{oldRating}/{oldNumberReviews}"
  )
  public String handleReviewSubmission(Map<String, Object> model, HttpServletRequest request, CourseOwner course1, @PathVariable("courseName") String course, @PathVariable("oldRating") double tempRating, @PathVariable("oldNumberReviews") double numReviews) throws Exception{
    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      numReviews++;
      double newRating = (course1.getRating() + tempRating)/numReviews;

      double roundedRating = Math.round(newRating*100.0)/100.0;

      stmt.executeUpdate("UPDATE owners SET rating = '" + roundedRating + "' WHERE courseName = '" + course + "'");
      stmt.executeUpdate("UPDATE owners SET numberRatings = '" + numReviews + "' WHERE courseName = '" + course + "'");
    }catch (Exception e){
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
    return "redirect:/tee-rific/feedback/" + request.getSession().getAttribute("username") + "/" + course;
  }

  @GetMapping(
          path = "tee-rific/feedback/{username}/{courseName}"
  )
  public String getFeedbackPage(@PathVariable("username")String user, @PathVariable("courseName") String course, Map<String, Object> model, HttpServletRequest request) {
    if (null == (request.getSession().getAttribute("username"))) {
      return "redirect:/";
    }

    if (!user.equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/feedback/" + request.getSession().getAttribute("username") + "/" + course;
    }
    CourseOwner givenFeedback = new CourseOwner();

    model.put("givenFeedback", givenFeedback);
  return "Booking&ViewingCourses/feedback";
  }

  @PostMapping(
          path = "tee-rific/feedback/{username}/{courseName}/post"
  )
  public String handleFeedbackSubmission(CourseOwner givenFeedback, HttpServletRequest request, @PathVariable("username")String user, @PathVariable("courseName") String course, Map<String, Object> model) {
    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String courseSnake = convertToSnakeCase(course);
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + courseSnake + "Feedback (username varchar(1000), feedback varchar(10000))");
      stmt.executeUpdate("INSERT INTO " + courseSnake + "Feedback (username, feedback) VALUES ('"+ user +"','"+givenFeedback.getFeedback()+"')");
    }catch (Exception e){
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
    return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
  }

@GetMapping(
        path = "/tee-rific/owner/feedback/{username}/{courseName}"
)
public String showAllFeedback(@PathVariable("username")String user, @PathVariable("courseName") String course, Map<String, Object> model, HttpServletRequest request) throws Exception {

  if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
    return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
  }

  if(null == (request.getSession().getAttribute("username"))) {
    return "redirect:/";
  }

  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate(getSQLNewTableOwner());
    String courseSnake = convertToSnakeCase(course);
    ResultSet rs = stmt.executeQuery("SELECT * FROM " + courseSnake + "Feedback");
    ArrayList<CourseOwner> output = new ArrayList<CourseOwner>();
    while(rs.next()) {
      CourseOwner temp = new CourseOwner();

      temp.setUsername(rs.getString("username"));
      temp.setFeedback(rs.getString("feedback"));

      output.add(temp);
    }
    model.put("feedback", output);
    model.put("username", user);
    return "Owner/courseFeedback";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "LandingPages/error";
  }
}





// HELPER BOIS (for booking) - Chino
public void userCreateScorecardsTable(Connection connection) throws Exception{
  Statement stmt = connection.createStatement();
  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS scorecards (active boolean, id varchar(100), userName varchar(100), date varchar(100), course varchar(100), " + 
                    "teesPlayed varchar(100), holesPlayed varchar(100), formatPlayed varchar(100), attestor varchar(100), " + 
                    "s1 integer, s2 integer, s3 integer, s4 integer, s5 integer, s6 integer, s7 integer, s8 integer, " + 
                    "s9 integer, s10 integer, s11 integer, s12 integer, s13 integer, s14 integer, s15 integer, s16 integer, " + 
                    "s17 integer, s18 integer)");
}

public void userInsertScorecard(Connection connection, String username, Scorecard scorecard) throws Exception {
  Statement stmt = connection.createStatement();
  stmt.executeUpdate("INSERT INTO scorecards (active, id, userName, date, course, teesPlayed, holesPlayed, formatPlayed, attestor) VALUES (" +
                      "'" + scorecard.isActive() + "', '" + scorecard.getGameID() + "', '" + username + "','" + scorecard.getDatePlayed() + "', '" + scorecard.getCoursePlayed() +
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
          path = "/tee-rific/viewTournament/{tournamentId}/{username}"
  )
  public String viewSelectedTournament(@PathVariable("username")String user, Map<String, Object> model, @PathVariable("tournamentId") String tournamentId, HttpServletRequest request)
  {
    if(!user.equals(request.getSession().getAttribute("username"))) {
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
  
      Tournament tournament = new Tournament();
      while (rs.next())
      {
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
      }

  
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

        temp.setUsername(listO.getString("username"));

        temp.setCourseName(listO.getString("coursename"));
        temp.setAddress(listO.getString("address"));
        temp.setCity(listO.getString("city"));
        temp.setCountry(listO.getString("country"));
        temp.setPhoneNumber(listO.getString("phonenumber"));
        temp.setTimeOpen(listO.getString("timeopen"));
        temp.setTimeClose(listO.getString("timeclose"));
        temp.setBookingInterval(listO.getString("bookinginterval"));
        temp.setRating(listO.getDouble("rating"));
        temp.setNumberRatings(listO.getDouble("numberratings"));

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
      String sql = "SELECT * FROM " + searchCourse;
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

   // LIST OF BOOKINGS AND RENTALS
//--------------------------------
  @GetMapping(
          path = "/tee-rific/admin/bookings"
  )
  public String listBookings(Map<String, Object> model, HttpServletRequest request)
  {

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    if(!"admin".equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      createTablesForBooking(connection);

      ResultSet listB = stmt.executeQuery("SELECT * FROM bookings");
      ArrayList<TeeTimeBooking> output = new ArrayList<TeeTimeBooking>();
      while (listB.next()) {
        TeeTimeBooking temp = new TeeTimeBooking();

        temp.setGameID(listB.getInt("gameid"));
        temp.setCourseName(listB.getString("coursename"));
        temp.setUsername(listB.getString("username"));
        temp.setDate(listB.getString("date"));
        temp.setTime(listB.getString("teetime"));
        temp.setNumPlayers(listB.getInt("numplayers"));
        temp.setRentalID(listB.getString("rentalid"));

        output.add(temp);
      }
      model.put("bookingList",output);
      return "Admin/listOfBookings";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  public void createTablesForBooking(Connection connection) throws Exception {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (gameID serial, courseName varchar(100), username varchar(100), date date, teetime time, numplayers integer, rentalID varchar(20))");
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rentals (id integer, courseName varchar(100), username varchar(100), dateCheckout timestamp DEFAULT now(), numBalls integer, numCarts integer, numClubs integer)");
    userCreateScorecardsTable(connection); //Creates scorecard db if not created
  }

  @PostMapping(
          path = "/tee-rific/admin/bookings/clear",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String clearBookingDB(Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String getBookings = "SELECT * FROM bookings";
      ResultSet getBookingDetails = stmt.executeQuery(getBookings);

      while(getBookingDetails.next()){
        Statement clearOtherDBs = connection.createStatement();
        String courseName = convertFromSnakeCase(getBookingDetails.getString("coursename"));
        int gameID = getBookingDetails.getInt("gameid");
        
        EquipmentCart updateInv = new EquipmentCart();

        //get the rental quantities
        int balls = 0;
        int clubs = 0;
        int carts = 0;
        ResultSet removeStock = clearOtherDBs.executeQuery("SELECT * FROM rentals WHERE id=" + gameID);
        while(removeStock.next()){
          balls = removeStock.getInt("numballs");
          carts = removeStock.getInt("numcarts");
          clubs = removeStock.getInt("numclubs");
        }

        //negate cart values
        balls = 0 - balls;
        carts = 0 - carts;
        clubs = 0 - clubs;

        updateInv.setNumBalls(balls);
        updateInv.setNumCarts(carts);
        updateInv.setNumClubs(clubs);

        //subtract from inventory
        updateInventory(connection, updateInv, courseName);

        clearOtherDBs.executeUpdate("DELETE FROM scorecards WHERE id='"+gameID+"'");
        clearOtherDBs.executeUpdate("DELETE FROM rentals WHERE id='"+gameID+"'");
      }
      
      stmt.executeUpdate("DROP TABLE bookings");

      return "redirect:/tee-rific/admin/bookings/";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @PostMapping(
          path = "/tee-rific/admin/bookings/{gameId}",
          consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String deleteBooking(Map<String, Object> model, @PathVariable("gameId") int gameID){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String getBookings = "SELECT * FROM bookings WHERE gameid='" + gameID + "'";
      ResultSet getBookingDetails = stmt.executeQuery(getBookings);
      getBookingDetails.next();

      String courseName = convertFromSnakeCase(getBookingDetails.getString("coursename"));
      
      EquipmentCart updateInv = new EquipmentCart();

      //get the rental quantities
      int balls = 0;
      int clubs = 0;
      int carts = 0;
      ResultSet removeStock = stmt.executeQuery("SELECT * FROM rentals WHERE id='" + gameID + "'");
      while(removeStock.next()){
        balls = removeStock.getInt("numballs");
        carts = removeStock.getInt("numcarts");
        clubs = removeStock.getInt("numclubs");
      }

      //negate cart values
      balls = 0 - balls;
      carts = 0 - carts;
      clubs = 0 - clubs;

      updateInv.setNumBalls(balls);
      updateInv.setNumCarts(carts);
      updateInv.setNumClubs(clubs);

      //subtract from inventory
      updateInventory(connection, updateInv, courseName);

      stmt.executeUpdate("DELETE FROM scorecards WHERE id='"+gameID+"'");
      stmt.executeUpdate("DELETE FROM rentals WHERE id='"+gameID+"'");
      stmt.executeUpdate("DELETE FROM bookings WHERE gameid='"+gameID+"'");            

      return "LandingPages/deleteSuccess";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }

  @GetMapping(
          path = "/tee-rific/admin/bookings/rental/{rentalID}"
  )
  public String viewRentalDetails(Map<String, Object> model, @PathVariable("rentalID") String id, HttpServletRequest request){

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

    if(!"admin".equals(request.getSession().getAttribute("username"))) {
      return "redirect:/tee-rific/home/" + request.getSession().getAttribute("username");
    }

    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM rentals WHERE id='" + id + "'";
      ResultSet rentalDetails = stmt.executeQuery(sql);

      ArrayList<RentalDetail> output = new ArrayList<RentalDetail>();
      while (rentalDetails.next()) {
        System.out.println("in while");
        RentalDetail temp = new RentalDetail();

        temp.setCourseName(rentalDetails.getString("coursename"));
        temp.setUsername(rentalDetails.getString("username"));
        temp.setDateCheckout(rentalDetails.getString("datecheckout"));
        temp.setNumBalls(rentalDetails.getInt("numballs"));
        temp.setNumCarts(rentalDetails.getInt("numcarts"));
        temp.setNumClubs(rentalDetails.getInt("numclubs"));

        output.add(temp);
      }

      model.put("rID", id);
      model.put("details", output);
      return "Admin/rentalDetails";
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


  @GetMapping(
    path = "/tee-rific/nuke/{user}"
  )
  public String nukeCompleted(@PathVariable("user")String user, HttpServletRequest request){

    if(!user.equals(request.getSession().getAttribute("username")) && (request.getSession().getAttribute("username") != (null))) {
      return "redirect:/tee-rific/aboutUs/" + request.getSession().getAttribute("username");
    }

    if(request.getSession().getAttribute("username") == (null)) {
      return "redirect:/";
    }

      System.out.println("System Database Has Been Nuked!");
      return "LandingPages/nuked";
  }//nukeCompleted()


  @PostMapping(
    path = "/tee-rific/nuke/{user}"
  )
  public String nukeDB(@PathVariable("user")String user, Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      //delete course tables
      String getOwners = getSQLNewTableOwner();     //create new owner table if it does not already exist
      stmt.executeUpdate(getOwners);

      getOwners = "SELECT * From owners";
      ResultSet courseDetails = stmt.executeQuery(getOwners);
      

      ArrayList<String> courses = new ArrayList<String>();

      while(courseDetails.next()){
        courses.add(convertToSnakeCase(courseDetails.getString("courseName")));
      }

      for(int i = 0; i < courses.size(); i++){
        String removeCourseTable = "DROP TABLE IF EXISTS " + courses.get(i);
        String removeCourseTableFeedback = "DROP TABLE IF EXISTS " + courses.get(i) + "Feedback";
        stmt.executeUpdate(removeCourseTable);
        stmt.executeUpdate(removeCourseTableFeedback);
        System.out.println("Removed Course Table: " + courses.get(i));
      }

      stmt.executeUpdate("DROP TABLE IF EXISTS owners");       //delete owners
      stmt.executeUpdate("DROP TABLE IF EXISTS bookings");     //delete bookings
      stmt.executeUpdate("DROP TABLE IF EXISTS scorecards");   //delete scorecards
      stmt.executeUpdate("DROP TABLE IF EXISTS inventory");    //delete inventory
      stmt.executeUpdate("DROP TABLE IF EXISTS rentals");      //delete rentals
      stmt.executeUpdate("DROP TABLE IF EXISTS tournaments");  //delete tournaments
      stmt.executeUpdate("DROP TABLE IF EXISTS users");        //delete users
    
      System.out.println("System Is In Process Of Deletion");
      return "redirect:/tee-rific/nuke/" + user;
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "LandingPages/error";
    }
  }//

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
  //     JSONObject data_obj = (JSONObject) parse.parse(inline);

  //     JSONObject objects = (JSONObject) data_obj.get("data I want here");

// for (int i = 0; i < objects.size(); i++)
// {
//   JSONObject x = (JSONObject) objects.get(i);

// }
  //   }
  

  // }


}
