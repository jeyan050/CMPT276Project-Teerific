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
  return "login";
}


@PostMapping(
  path = "/tee-rific/login",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String checkLoginInfo(Map<String, Object> model, User user) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(30), username varchar(30), password varchar(100), fname varchar(30), lname varchar(30), email varchar(30), gender varchar(30))");
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
      if(priority.equals(priorities[0])){           //golferAccount
        return "home";
      }else if(priority.equals(priorities[1])){     //ownerAccount
        return "ownerHome";
      }else{                                        //adminAccount
        return "adminHome";
      } 
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

      String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

      user.setPriority(priorities[0]);      //sets the priority of the user to 'GOLFER'

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(30), username varchar(30), password varchar(100), fname varchar(30), lname varchar(30), email varchar(30), gender varchar(30))");
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
        return "success";     
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
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

  return "ownerSignUp";
}

@PostMapping(
  path = "/tee-rific/signup/Owner",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleBrowserOwnerSubmit(Map<String, Object> model, CourseOwner owner){
  //create a new user, owner, golfcourse, then add to database

  try(Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();

    //TODO: fix password encrypt

    String encryptedPassword = BCrypt.hashpw(owner.getPassword(), BCrypt.gensalt());


    //TODO: will need to figure out way to store image into sql later - Mike

    owner.setNumHoles(18);
    String ownerInfo = getSQLNewTableOwner();
    String insertOwners = getSQLInsertOwner(owner, encryptedPassword);


    //add user to database
    stmt.executeUpdate(ownerInfo);
    stmt.executeUpdate(insertOwners);
    
    
    //snake case the course name 
    /* 'THIS IS BETTER THAN CAMAL CASE' for this situation:  
        - cannot have spaces in name or else it breaks the SQL query
        - snake case works best as it makes for easy conversion back to orginal formate
        - camel case is disregarded by the SQL, implying no way of knowing where to split the words to convert back to original format */
    String updatedCourseName = convertToSnakeCase(owner.getCourseName());
    String courseInfo = "CREATE TABLE IF NOT EXISTS " + updatedCourseName + "(holeNumber integer, yardage integer, par integer, handicap integer)";
    stmt.executeUpdate(courseInfo);


    //initializes a table to keep track of the course hole details
    for(int i = 0; i < owner.getNumHoles(); i++){
      String insertHole = "INSERT INTO " + updatedCourseName + "(" + "holeNumber, yardage, par, handicap) VALUES (' " + (i + 1) + "', '0', '0', '0')";
      stmt.executeUpdate(insertHole);
    }

    //add to user table
    String userInfo = getSQLNewTableUsers();              
    
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
      return "ownerCreated";     
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}


//helper
String getSQLNewTableOwner() {
  return  "CREATE TABLE IF NOT EXISTS owners (" +
          "courseName varchar(100), address varchar(50), city varchar(30), country varchar(40), website varchar(150), phoneNumber varchar(15), " + 
          "courseLogo varchar(150), " +               //TODO: will need to fix this one image storage is figured out - MIKE
          "directionsToCourse varchar(500), description varchar(500), weekdayRates varchar(10), weekendRates varchar(10), numHoles integer, " + 
          "userName varchar(50), password varchar(100),firstName varchar(50),lastName varchar(50),email varchar(75),yardage varchar(20),gender varchar(20))";
}


//helper 
String getSQLInsertOwner(CourseOwner owner, String secretPW){

  return "INSERT INTO owners ( " + 
          "courseName, address, city, country, website, phoneNumber, courseLogo, " +
          "directionsToCourse, description, weekdayRates, weekendRates, numHoles, " +
          "userName, password, firstName, lastName, email, yardage, gender) VALUES ('" +
          owner.getCourseName() + "','" + owner.getAddress() + "','" + owner.getCity() + "','" + 
          owner.getCountry() + "','" + owner.getWebsite() + "','" + owner.getPhoneNumber() + "','" +  
          owner.getCourseLogo() + "','" + owner.getDirectionsToCourse() + "','" + owner.getDescription() + "','" + 
          owner.getWeekdayRates() + "','" +  owner.getWeekendRates() + "','" + owner.getNumHoles() + "','" + 
          owner.getUsername() + "','" + secretPW + "','" + owner.getFname() + "','" + owner.getLname() + "','" + 
          owner.getEmail() + "','" + owner.getYardage() + "', '" + owner.getGender() + "')";
}

//helper 
String getSQLDeleteOwner(CourseOwner owner, String secretPW){

  return "DELETE FROM owners WHERE courseName='" + owner.getCourseName() + "' and address='" + owner.getAddress() + 
          "' and city='" + owner.getCity() +  "' and country='" + owner.getCountry() + "' and website='"  +
          owner.getWebsite() + "' and phoneNumber='" + owner.getPhoneNumber() + "' and courseLogo='" +
          owner.getCourseLogo() + "' and directionsToCourse='" + owner.getDirectionsToCourse() + "' and description='" +
          owner.getDescription() + "' and weekdayRates='" + owner.getWeekdayRates() + "' and weekendRates='" +
          owner.getWeekendRates() + "' and numHoles='" + owner.getNumHoles() + "' and userName='" + owner.getUsername() + 
          "' and password='" + secretPW + "' and firstName='" + owner.getFname() + "' and lastName='" + owner.getLname() +
          "' and email='" + owner.getEmail() + "' and yardage='" + owner.getYardage() + "' and gender='" + owner.getGender() + "'";
}



//helper
String getSQLNewTableUsers(){
  return "CREATE TABLE IF NOT EXISTS users (priority varchar(30), username varchar(30), password varchar(100), fname varchar(30), lname varchar(30), email varchar(30), gender varchar(30))";
}


//helper
String getSQLInsertUser(User user, String secretPW){

  return "INSERT INTO users (priority, username, password, fname, lname, email, gender) VALUES ('" + user.getPriority() + "','" + user.getUsername() + "','" + secretPW + "','" + user.getFname() + "','" + user.getLname() + "','" + user.getEmail() + "','" + user.getGender() + "')";
}


String convertToSnakeCase(String toConvert){
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


String convertFromSnakeCase(String toConvert){
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
  path = "/tee-rific/home"
)
public String getHomePage(Map<String, Object> model){
  return "home";
}//getHomePage()

@GetMapping(
  path = "/tee-rific/ownerHome"
)
public String getOwnerHomePage(Map<String, Object> model){
  return "ownerHome";
}

//**********************
// MODIFY ACCOUNT
//**********************

@GetMapping(
  path = "/tee-rific/editOwnerAccount"
)
public String getEditOwnerAccountPage(){
  return "editAccountOwner";
}

//TODO: add a method to update the desired information

//**********************
// MODIFY COURSE DETAILS
//**********************

@GetMapping(
  path = "/tee-rific/golfCourseDetails"
)
public String getCourseDetails(){
  return "golfCourseDetails";
}

//TODO: add a method to modify the golf course details


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
  boolean validAppointment = true;
  if(validAppointment){
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

@PostMapping(
  path = "/tee-rific/createTournament",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleTournamentCreation(Map<String, Object> model, Tournament tournament) throws Exception
{
  try (Connection connection = dataSource.getConnection())
  {
    Statement stmt = connection.createStatement();
    //this part does not work correctly, idk why, keeps giving "ERROR: column "buy_in" of relation "tournaments" does not exist Position: 51" when you try to create the tournament
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tournaments (id serial, name varchar(50), participant_slots integer, buy_in integer, first_prize varchar(30), second_prize varchar(30), third_prize varchar(30), age_requirement integer, game_mode varchar(30), club_name varchar(50))");
    // System.out.println("test 1");
    stmt.executeUpdate("INSERT INTO tournaments (name, participant_slots, buy_in, first_prize, second_prize, third_prize, age_requirement, game_mode, club_name) VALUES ('" + tournament.getName() + "','" + tournament.getParticipantSlots() + "','" + tournament.getBuyIn() + "','" + tournament.getFirstPrize() + "','" + tournament.getSecondPrize() + "','" + tournament.getThirdPrize()+ "','" + tournament.getAgeRequirement() + "','" + tournament.getGameMode() + "','" + tournament.getClubName() + "')");
    // System.out.println("test 2");
    System.out.println(tournament.getBuyIn());
    return "redirect:/tee-rific/avalableTournaments";
  } catch (Exception e) 
  {
    model.put("message", e.getMessage());
    return "error"; 
  }
}

@GetMapping(
  path = "/tee-rific/viewTournament/{tid}"
)
public String viewSelectedTournament(Map<String, Object> model, @PathVariable String tid)
{
  try(Connection connection = dataSource.getConnection())
  {
    Statement stmt = connection.createStatement();
    model.put("id", tid);
    ResultSet rs = stmt.executeQuery("SELECT * FROM tournaments WHERE id =" + tid);
    ArrayList<Tournament> output = new ArrayList<Tournament>();
    while (rs.next())
    {
      Tournament tournament = new Tournament();
      tournament.setId(rs.getInt("id"));
      tournament.setName(rs.getString("name"));
      tournament.setParticipantSlots(rs.getInt("participant_slots"));
      tournament.setBuyIn(rs.getInt("buy_in"));
      tournament.setFirstPrize(rs.getString("first_prize"));
      tournament.setSecondPrize(rs.getString("second_prize"));
      tournament.setThirdPrize(rs.getString("third_prize"));
      tournament.setAgeRequirement(rs.getInt("age_requirement"));
      tournament.setGameMode(rs.getString("game_mode"));
      tournament.setClubName(rs.getString("club_name"));

      output.add(tournament);
    }

    model.put("tournaments", output); //
    Tournament tournament = new Tournament();
    model.put("tournament", tournament); //
    return "viewTournament";

  } catch (Exception e)
  {
    model.put("message", e.getMessage());
    return "error";
  }
}

@GetMapping(
  path = "/tee-rific/tournamentDelete"
)
public String displayDeleteTournamentPage()
{
  return "tournamentDelete";
}

@PostMapping(
  path = "/tee-rific/tournamentDelete",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String deleteTournament(Map<String, Object> model, Tournament tournament)
{
  try (Connection connection = dataSource.getConnection())
  {
    Statement stmt = connection.createStatement();
    stmt.execute("DELETE FROM tournaments WHERE id = " + tournament.getId());
    return "redirect:/tee-rific/avalableTournaments";
  } catch (Exception e)
  {
    model.put("message", e.getMessage());
    return "error";
  }
}

@GetMapping(
  path = "/tee-rific/tournamentSignUp"
)
public String tournamentSignUp()
{
  return "tournamentSignUp";
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
