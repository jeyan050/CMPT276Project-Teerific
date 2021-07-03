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

package com.example;

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

import javax.sql.DataSource;
import javax.swing.JOptionPane;
import java.nio.charset.StandardCharsets;
import java.security.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

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

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
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
  return "login";
}


@PostMapping(
  path = "/tee-rific/login",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String checkLoginInfo(Map<String, Object> model, User user) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    String sql = "SELECT * FROM users WHERE username = '" + user.getUsername() + "'";
    ResultSet rs = stmt.executeQuery(sql);

    int checkIfUserExists = 0;
    String checkPassword = "";
    while (rs.next()){
      checkIfUserExists++;
      checkPassword = rs.getString("password");
    }
    if (checkIfUserExists > 0 && (user.getPassword().equals(checkPassword))){
      return "home";
    }
    failedLogin = true;
    return "redirect:/tee-rific/login";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
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
    return "signup";
}
@PostMapping(
        path = "/tee-rific/signup"
)

//im thinking maybe not using a serial id since we are only allowing one unique username for each user (meaning a unique id specifier likely isn't required) - Kyle

public String handleBrowserNewUserSubmit(Map<String, Object> model, User user) throws Exception {
    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String preEncrypt = user.getPassword();
      byte[] bytesOfPassword = preEncrypt.getBytes(StandardCharsets.UTF_8);
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] encryptedPassword = md.digest(bytesOfPassword);

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username varchar(30), password varchar(100), fname varchar(30), lname varchar(30), email varchar(30), gender varchar(30))");
      stmt.executeUpdate("INSERT INTO users (username, password, fname, lname, email, gender) VALUES ('" + user.getUsername() + "','" + encryptedPassword + "','" + user.getFname() + "','" + user.getLname() + "','" + user.getEmail() + "','" + user.getGender() + "')");
      
      String sql = "SELECT username FROM users WHERE username ='"+user.getUsername()+"'";
      ResultSet rs = stmt.executeQuery(sql);
      int checkCount = 0;
      while (rs.next()){
        checkCount++;
      }

      if (checkCount > 1){
        stmt.executeUpdate("DELETE FROM users WHERE username='"+user.getUsername() + "' and password='"+ encryptedPassword + "' and fname='"+user.getFname() + "' and lname='"+user.getLname() + "' and email='"+user.getEmail() + "' and gender='"+user.getGender()+"'");
        usernameError = true;
        return "redirect:/tee-rific/signup";
      } else {
        return "success";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
}

@GetMapping("errorSignup")
public String redirectToErrorPage(){
  return "errorSignup";
}

//**********************
// ADMIN SIGN-UP
//**********************

@GetMapping(
  path = "/tee-rific/signup/admin"
)
public String getAdminSignUpPage(){
  return "adminSignUp";
}

//**********************
// HOME PAGE
//**********************

@GetMapping(
  path = "/tee-rific/home"
)
public String getHomePage(Map<String, Object> model){
  return "home";
}//getHomePage()


//**********************
// BOOKING
//**********************

@GetMapping(
  path = "/tee-rific/booking"
)
public String getBookingPage(){
  return "booking";
}//getBookingPage()


@PostMapping(
  path = "/tee-rific/booking",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String updateSchedule(){
  boolean validApppointment = true;
  if(validApppointment){
    return "redirect:/tee-rific/bookingSuccessful";
  }
  return "booking";
}//updateSchedule()


@GetMapping(
  path = "/tee-rific/bookingSuccessful"
)
public String bookingSuccessful(){
  return "bookingSuccessful";
}//bookingSuccessful()


//**********************
// RENT EQUIPMENT
//**********************
@GetMapping(
  path = "/tee-rific/rentEquipment"
)
public String rentEquipment(Map<String, Object> model) {
  EquipmentCart cart = new EquipmentCart();
  model.put("cart", cart);
  return "rentEquipment";
}

@PostMapping(
  path = "/tee-rific/rentEquipment",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String updateInventory(Map<String, Object> model, EquipmentCart cart) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");

    // QUESTION: Is the app gonna handle payment as well? (Might be hard)    ******* NO, Bobby said not to with what we are designing -- Mike *****
    // Calculate updated values for stock
    rs.next();                                                // Right now I have it so we have 2 columns: name(of item) varchar and stock integer 
    int ballStock = rs.getInt("stock");                       // I assume we have 3 items: balls, carts, clubs SUBJECT TO CHANGE - Chino
    int updatedBallStock = ballStock - cart.getNumBalls();
    rs.next();
    int golfCartStock = rs.getInt("stock");
    int updatedGolfCartStock = golfCartStock - cart.getNumCarts();
    rs.next();
    int clubStock = rs.getInt("stock");
    int updatedClubStock = clubStock - cart.getNumClubs();

    // Update inventory table
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedBallStock+"' WHERE name = 'balls'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedGolfCartStock+"' WHERE name = 'carts'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedClubStock+"' WHERE name = 'clubs'");

    // SUGGESTION: Maybe tie the user to the 'reciept'? Like create another field in User class
    // I'm not sure if the app is aware of who is "logged in" at this point, would appreciate clarification

    // Redirect to checkout page
    return "redirect:/tee-rific/rentEquipment/checkout";
  }
}

@GetMapping(
  path = "/tee-rific/rentEquipment/checkout"
)
public String handleCheckout() {
  return "checkout";
}



// HELPER FUNCTIONS FOR OWNER PAGE
public void ownerCreateInventory(Map<String, Object> model, EquipmentCart cart) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS inventory (name varchar(20), stock integer DEFAULT 0)");
    stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('balls')");
    stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('carts')");
    stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('clubs')");

    ownerUpdateInventory(model, cart);
  }
}

public void ownerInsertNewItem(String nameOfItem) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('"+nameOfItem+"')");
  }
}

public void ownerDeleteItem(String nameOfItem) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DELETE FROM inventory WHERE name='"+nameOfItem+"'");
  }
}

public void ownerUpdateInventory(Map<String, Object> model, EquipmentCart cart) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");

    rs.next();                    
    int ballStock = rs.getInt("stock");                       
    int updatedBallStock = ballStock + cart.getNumBalls();
    rs.next();
    int golfCartStock = rs.getInt("stock");
    int updatedGolfCartStock = golfCartStock + cart.getNumCarts();
    rs.next();
    int clubStock = rs.getInt("stock");
    int updatedClubStock = clubStock + cart.getNumClubs();

    // Update inventory table
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedBallStock+"' WHERE name = 'balls'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedGolfCartStock+"' WHERE name = 'carts'");
    stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedClubStock+"' WHERE name = 'clubs'");
  }
}



//**********************
// SCORECARD
//**********************

@GetMapping(
  path = "/tee-rific/scorecard"
)
public String getScorecard(){
  return "scorecard";
}//getScorecard()


@PostMapping(
  path = "/tee-rific/scorecard",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String updateScorecard(){
  return "scorecard";
}//updateScorecard()


@GetMapping(
  path = "/tee-rific/scoresHistory"
)
public String getAllScores(){
  return "scoresHistory";
}//getAllScores()


@GetMapping(
  path = "/tee-rific/scoreHistory/{course}"
)
public String getScoresFromCourse(@PathVariable("course") String course){
  return "scoresHistory";
}//getScoresFromCourse()


//**********************
// TOURNAMENT
//**************

@GetMapping(
  path = "/tee-rific/tournament"
)
public String tournament()
{
  return "tournament";
}

@GetMapping(
  path = "/tee-rific/avalableTournaments"
)
public String avalableTournaments()
{
  return "avalableTournaments";
}

@GetMapping(
  path = "/tee-rific/createTournament"
)
public String createTournament(Map<String, Object> model)
{
  Tournament tournament = new Tournament();
  model.put("newTournament", tournament);
  return "createTournament";
}

@GetMapping(
  path = "/tee-rific/tournamentDelete"
)
public String deleteTournament()
{
  return "tournamentDelete";
}

@GetMapping(
  path = "/tee-rific/tournamentSignUp"
)
public String tournamentSignUp()
{
  return "tournamentSignUp";
}
//**********************
// USER ACCOUNT
//**********************

@GetMapping(
  path = "/tee-rific/userProfile"
)
public String userProfile()
{
  return "userProfile";
}

@GetMapping(
  path = "/tee-rific/accountDeleted"
)
public String accountDeleted()
{
  return "accountDeleted";
}

@PostMapping( //HAS NOT BEEN TESTED
  path = "/tee-rific/accountDeleted",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String deleteUser(Map<String, Object> model, User user) throws Exception
{
  try (Connection connection = dataSource.getConnection())
  {
    Statement stmt = connection.createStatement();
    // stmt.execute("DELETE FROM users WHERE username = " user)
    return "redirect:/tee-rific/accountDeleted";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  } 
}



//**********************
// ABOUT-US
//**********************

@GetMapping(
  path = "/tee-rific/aboutUs"
)
public String aboutDevelopers(Map<String, Object> model){
  //this is optional, if you guys feel comfortable doing so, we can upload 'selfies' of our team and maybe talk about our development process
  return "aboutUs";
}//aboutDevelopers()


//**********************
// LOGOUT
//**********************

@GetMapping(
  path = "/tee-rific/logout"
)
public String Logout(){
  //log the user out
  return "logout";
}

}
