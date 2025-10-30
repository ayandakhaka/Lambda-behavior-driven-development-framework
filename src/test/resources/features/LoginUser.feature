Feature: Login User Functionality
  As a registered customer
  I want to log in to my account on the e-commerce website
  So that I can access my account details and make purchases

  Background:
    Given User launches the lambdaTest home page
    When User hovers over the My Account menu

  Scenario: Successful Login with Valid Credentials
    And User clicks on the Login link
    When User enters valid email and password
    And User clicks on the Login button
    Then User should be redirected to their account dashboard
    
  Scenario: Login with Invalid Password
  	And User is logged out
    And User clicks on the Login link
    When User enters a valid email and an invalid password
    And User clicks on the Login button
    Then User should see an error message indicating incorrect password
    
  Scenario: Login with Unregistered Email
    #And User is logged out
    When User clicks on the Login link
    And User enters an unregistered email and any password
    And User clicks on the Login button
    Then User should see an error message indicating unregistered email
    
  
    
    
    

 