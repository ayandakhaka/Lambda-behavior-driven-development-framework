Feature: Register User Functionality
  As a new customer
  I want to create an account on the e-commerce website
  So that I can log in and manage my account

  Background:
    Given User launches the lambdaTest home page
    When User hovers over the My Account menu

  Scenario: Successful Registration with Valid Details
    And User clicks on the Register link
    When User enters all the mandatory fields with valid data
    And User clicks on the Privacy Policy checkbox
    And User clicks on the Continue button
    Then User should see a confirmation message indicating successful registration
    
  Scenario: Registration with Already Registered Email
    And User is logged out
    And User clicks on the Register link
    When User enters all the mandatory fields with an already registered email
    And User clicks on the Privacy Policy checkbox
    And User clicks on the Continue button
    Then User should see an error message indicating that the email is already registered
    
  Scenario: Registration with Missing Mandatory Fields
    # And User is logged out
    And User clicks on the Register link
    When User leaves one or more mandatory fields empty
    And User clicks on the Privacy Policy checkbox
    And User clicks on the Continue button
    Then User should see error messages indicating which mandatory fields are missing
      | First Name must be between 1 and 32 characters! |
      | Last Name must be between 1 and 32 characters!  |
      | E-Mail Address does not appear to be valid!     |
      | Telephone must be between 3 and 32 characters!  |
      | Password must be between 4 and 20 characters!   |
      
    Scenario: Registration with Invalid Email Format
    # And User is logged out
    And User clicks on the Register link
    When User enters an invalid email format in the email field
    And User fills in all other mandatory fields with valid data
    And User clicks on the Privacy Policy checkbox
    And User clicks on the Continue button
    Then User should see an error message indicating that the email format is invalid

	Scenario: Registration without Accepting Privacy Policy
    # And User is logged out
    And User clicks on the Register link
    When User enters all the mandatory fields with valid data
    And User clicks on the Continue button
    Then User should see an error message indicating that the Privacy Policy must be accepted

	Scenario: Registration with Weak Password
    # And User is logged out
    And User clicks on the Register link
    When User enters all the mandatory fields with valid data but a weak password
    And User clicks on the Privacy Policy checkbox
    And User clicks on the Continue button
    Then User should see an error message indicating that the password is too weak
 
 	Scenario: Registration with Mismatched Password Confirmation
    # And User is logged out
    And User clicks on the Register link
    When User enters all the mandatory fields with valid data but mismatched password and confirmation
    And User clicks on the Privacy Policy checkbox
    And User clicks on the Continue button
    Then User should see an error message indicating that the password and confirmation do not match
  



  