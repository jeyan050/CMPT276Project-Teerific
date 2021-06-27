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
    return "index";
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

@GetMapping(
  path = "/tee-rific/login"
)
public String getLoginPage(Map<String, Object> model){
  return "login";
}//getLoginPage()


@PostMapping(
  path = "/tee-rific/login",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String checkLoginInfo(Map<String, Object> model){
  boolean loginGood = true;
  if(loginGood){
    System.out.println("Login Successful\n");
    return "home";
  }
  System.out.println("Login not valid\n");
  return "login";
}//checkLoginInfo()

  //**********************
// SIGN-UP
//**********************

@GetMapping(
        path = "/tee-rific/signup"
)
public String getSignupPage(Map<String, Object> model) {
    User user = new User();
    model.put("newUser", user);
    return "signup";
}
@PostMapping(
        path = "/tee-rific/signup"
)

//im thinking maybe not using a serial id since we are only allowing one unique username for each user (meaning a unique id specifier likely isn't required) - Kyle

public String handleBrowserNewUserSubmit(Map<String, Object> model, User user) throws Exception {
    try(Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username varchar(30), password varchar(30), fname varchar(30), lname varchar(30), email varchar(30), gender varchar(30))");
      stmt.executeUpdate("INSERT INTO users (username, password, fname, lname, email, gender) VALUES ('" + user.getUsername() + "','" + user.getPassword() + "','" + user.getFname() + "','" + user.getLname() + "','" + user.getEmail() + "','" + user.getGender() + "')");
      ResultSet rs = stmt.executeQuery("SELECT * FROM users");
      ArrayList<User> output = new ArrayList<User>();
      while (rs.next()) {
        User user1 = new User();
        user1.setUsername(rs.getString("username"));
        user1.setPassword(rs.getString("password"));
        user1.setFname(rs.getString("fname"));
        user1.setLname(rs.getString("lname"));
        user1.setEmail(rs.getString("email"));
        user1.setGender(rs.getString("gender"));
        output.add(user1);
      }
      model.put("users", output);
      return "success";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
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
// ABOUT-US
//**********************

@GetMapping(
  path = "/tee-rific/aboutUs"
)
public String aboutDevelopers(Map<String, Object> model){
  //this is optional, if you guys feel comfortable doing so, we can upload 'selfies' of our team and maybe talk about our development process
  return "aboutUs";
}//aboutDevelopers()


}
