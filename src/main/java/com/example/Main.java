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
  //create a new user, owner, golf course, then add to database

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

    // Initialize rental inventory of golf course - Chino
    ownerCreateInventory(connection);

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
          "courseName varchar(100), address varchar(100), city varchar(100), country varchar(100), website varchar(150), phoneNumber varchar(100), " +
          "courseLogo varchar(150), " +               //TODO: will need to fix this one image storage is figured out - MIKE
          "directionsToCourse varchar(500), description varchar(500), weekdayRates varchar(100), weekendRates varchar(100), numHoles integer, " +
          "userName varchar(100), password varchar(100),firstName varchar(100),lastName varchar(100),email varchar(100),yardage varchar(100),gender varchar(100))";
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
  return "CREATE TABLE IF NOT EXISTS users (priority varchar(100), username varchar(100), password varchar(100), fname varchar(100), lname varchar(100), email varchar(100), gender varchar(100))";
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

@GetMapping(
  path = "/tee-rific/adminHome"
)
public String getAdminHomePage(Map<String, Object> model){
  return "adminHome";
}

//**********************
// MODIFY ACCOUNT
//**********************

//TODO: get all the courses, display ratings, allow user to rate courses

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
//TODO: Add extra style 
// TODO: Corner cases (out of stock case, quantity in cart > stock case, negative number case)
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
public String handleShop(Map<String, Object> model, EquipmentCart cart) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    // Create a table with our cart data inside to display on checkout
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cart (numBalls integer, numCarts integer, numClubs integer)");
    stmt.executeUpdate("INSERT INTO cart VALUES ('"+cart.getNumBalls()+"', '"+cart.getNumCarts()+"', '"+cart.getNumClubs()+"')");
  }
    
    // Redirect to checkout page
    return "redirect:/tee-rific/rentEquipment/checkout";
}

@GetMapping(
  path="/tee-rific/rentEquipment/checkout"
)
public String handleViewCart(Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    // Create a table with our cart data inside to display on checkout
    EquipmentCart toView = getUserCartContentsFromDB(connection);
    toView.printfields();
    model.put("userCart", toView);
    return "rentEquipmentCheckout";
  }
}

@PostMapping(
  path = "/tee-rific/rentEquipment/checkout",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleCheckout() throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    EquipmentCart cart = getUserCartContentsFromDB(connection);
    updateInventory(connection, cart);
    stmt.executeUpdate("DROP TABLE cart");

    // Create table of rentals so employees can keep track
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS LiveRentals (id serial, username varchar(100), dateCheckout timestamp DEFAULT now(), numBalls integer, numCarts integer, numClubs integer)");
    //TODO: Link user to rental
    stmt.executeUpdate("INSERT INTO LiveRentals (username, numBalls, numCarts, numClubs) VALUES ('temp', '"+cart.getNumBalls()+"', '"+cart.getNumCarts()+"', '"+cart.getNumClubs()+"')");
  }
  return "redirect:/tee-rific/rentEquipment/checkout/success";
}

@GetMapping(
  path="/tee-rific/rentEquipment/checkout/success"
)
public String rentSuccessPage() {
  return "rentEquipmentSuccess";
}


// ------ OWNER'S PAGE ------ //
// TODO: Add style to table in viewInventory page
@GetMapping(
  path="/tee-rific/golfCourseDetails/inventory"
)
public String viewInventory(Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");

    ArrayList<Equipment> eqs = new ArrayList<Equipment>();
    while (rs.next()) {
      Equipment eq = new Equipment();
      eq.setItemName(rs.getString("name"));
      eq.setStock(rs.getInt("stock"));

      eqs.add(eq);
    }
    model.put("eqsArray", eqs);
    return "inventory";
  }
}

@GetMapping(
  path="/tee-rific/golfCourseDetails/inventory/update"
)
public String invUpdate(Map<String, Object> model) {
  EquipmentCart cart = new EquipmentCart();
  model.put("ownerCart", cart);
  return "inventoryUpdate";
}
@PostMapping(
  path="/tee-rific/golfCourseDetails/inventory/update",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleInvUpdate(Map<String, Object> model, EquipmentCart cart) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    ownerUpdateInventory(connection, cart);
  }
  return "redirect:/tee-rific/golfCourseDetails/inventory";
}

// HELPER FUNCTIONS
public void updateInventory(Connection connection, EquipmentCart cart) throws Exception {
  Statement stmt = connection.createStatement();
  ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");

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
  stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedBallStock+"' WHERE name = 'balls'");
  stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedGolfCartStock+"' WHERE name = 'carts'");
  stmt.executeUpdate("UPDATE inventory SET stock ='"+updatedClubStock+"' WHERE name = 'clubs'");
}

public EquipmentCart getUserCartContentsFromDB(Connection connection) throws Exception {
  Statement stmt = connection.createStatement();
  ResultSet rs = stmt.executeQuery("SELECT * FROM cart");
  rs.next();

  EquipmentCart ret = new EquipmentCart();
  ret.setNumBalls(rs.getInt("numballs"));
  ret.setNumCarts(rs.getInt("numcarts"));
  ret.setNumClubs(rs.getInt("numclubs"));

  return ret;
}

public void ownerCreateInventory(Connection connection) throws Exception {
  Statement stmt = connection.createStatement();
  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS inventory (name varchar(100), stock integer DEFAULT 0)");
  stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('balls')");
  stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('carts')");
  stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('clubs')");
  
}

public void ownerInsertNewItem(Connection connection, String nameOfItem) throws Exception {
  Statement stmt = connection.createStatement();
  stmt.executeUpdate("INSERT INTO inventory (name) VALUES ('"+nameOfItem+"')");
}

public void ownerDeleteItem(Connection connection, String nameOfItem) throws Exception {
  Statement stmt = connection.createStatement();
  stmt.executeUpdate("DELETE FROM inventory WHERE name='"+nameOfItem+"'");
}

public void ownerUpdateInventory(Connection connection, EquipmentCart cart) throws Exception {
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
  stmt.executeUpdate("UPDATE inventory SET stock ='"+cart.getNumBalls()+"' WHERE name = 'balls'");
  stmt.executeUpdate("UPDATE inventory SET stock ='"+cart.getNumCarts()+"' WHERE name = 'carts'");
  stmt.executeUpdate("UPDATE inventory SET stock ='"+cart.getNumClubs()+"' WHERE name = 'clubs'");
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
  path = "/tee-rific/availableTournaments"
)
public String availableTournaments(Map<String, Object> model)
{
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
    return "availableTournaments";

  } catch (Exception e)
  {
    model.put("message", e.getMessage());
    return "error";
  }
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
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tournaments (id serial, name varchar(100), participant_slots integer, buy_in integer, first_prize varchar(100), second_prize varchar(100), third_prize varchar(100), age_requirement integer, game_mode varchar(100), club_name varchar(100))");
    stmt.executeUpdate("INSERT INTO tournaments (name, participant_slots, buy_in, first_prize, second_prize, third_prize, age_requirement, game_mode, club_name) VALUES ('" + tournament.getName() + "','" + tournament.getParticipantSlots() + "','" + tournament.getBuyIn() + "','" + tournament.getFirstPrize() + "','" + tournament.getSecondPrize() + "','" + tournament.getThirdPrize()+ "','" + tournament.getAgeRequirement() + "','" + tournament.getGameMode() + "','" + tournament.getClubName() + "')");
    System.out.println(tournament.getBuyIn());
    return "redirect:/tee-rific/availableTournaments";
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
    return "redirect:/tee-rific/availableTournaments";
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

@PostMapping( //TODO: User Account Deletion Has Not Been Tested
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
// ADMIN BUTTONS
//**********************

// LIST OF USERS
//--------------------------------
@GetMapping(
  path = "/tee-rific/adminHome/users"
)
public String listUsers(Map<String, Object> model)
{
  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (priority varchar(30), username varchar(30), password varchar(100), fname varchar(30), lname varchar(30), email varchar(30), gender varchar(30))");
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
    return "listOfUsers";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  } 
}

@PostMapping(
  path = "/tee-rific/adminHome/users/{username}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String deleteUser(Map<String, Object> model, @PathVariable("username") String name){
  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    String sql = "DELETE FROM users WHERE username='"+name+"'";
    stmt.executeUpdate(sql);

    return "deleteSuccess";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  } 
}

// LIST OF TOURNAMENTS
//--------------------------------
@GetMapping(
  path = "/tee-rific/adminHome/tournaments"
)
public String listTournaments(Map<String, Object> model)
{
  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tournaments (id serial, name varchar(50), participant_slots integer, buy_in integer, first_prize varchar(30), second_prize varchar(30), third_prize varchar(30), age_requirement integer, game_mode varchar(30), club_name varchar(50))");
    ResultSet listT = stmt.executeQuery("SELECT * FROM tournaments IF ");
    ArrayList<Tournament> output = new ArrayList<Tournament>();
    while (listT.next()) {
      Tournament temp = new Tournament();

      temp.setId(listT.getInt("id"));
      temp.setName(listT.getString("name"));
      temp.setParticipantSlots(listT.getInt("participant_slots"));
      temp.setBuyIn(listT.getInt("buy_in"));
      temp.setFirstPrize(listT.getString("first_prize"));
      temp.setSecondPrize(listT.getString("second_prize"));
      temp.setThirdPrize(listT.getString("third_prize"));
      temp.setAgeRequirement(listT.getInt("age_requirement"));
      temp.setGameMode(listT.getString("game_mode"));
      temp.setClubName(listT.getString("club_name"));

      output.add(temp);
    }

    model.put("tournamentList",output);
    return "listOfTournaments";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  } 
}

@PostMapping(
  path = "/tee-rific/adminHome/tournaments/{id}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String deleteTournament(Map<String, Object> model, @PathVariable("id") long id){
  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    String sql = "DELETE FROM tournaments WHERE id="+id;
    stmt.executeUpdate(sql);

    return "deleteSuccess";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  } 
}

// LIST OF OWNERS AND GOLF COURSES
//--------------------------------
@GetMapping(
  path = "/tee-rific/adminHome/owners"
)
public String listOwners(Map<String, Object> model)
{
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
    return "listOfOwners";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  } 
}

@PostMapping(
  path = "/tee-rific/adminHome/owner/{username}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String deleteOwner(Map<String, Object> model, @PathVariable("username") String name){
  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    String sql = "DELETE FROM owners WHERE username='"+name+"'";
    stmt.executeUpdate(sql);

    return "deleteSuccess";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  } 
}

@GetMapping(
  path = "/tee-rific/adminHome/owners/golfCourse/{courseName}"
)
public String viewGolfCourse(Map<String, Object> model, @PathVariable("courseName") String course){
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
    return "courseDetails";
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
