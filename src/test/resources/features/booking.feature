@api
Feature: As a user I can Get, Post, Delete and Update the Restful Booker APIs

  Background:
    Given I authenticate into the Restful Booker API

  Scenario: I can get all bookings
    When I send a GET request to retrieve all bookings
    Then I receive a 200 status code
    And the response contains a list of bookings

  Scenario: I can create a new booking
    When I send a POST request with valid booking details
    Then I receive a 200 status code
    And the response contains the new booking id

  Scenario: I can get a booking by id
    When I send a GET request for a specific booking
    Then I receive a 200 status code
    And the response contains the correct booking details

  Scenario: I can update a booking
    When I send a PUT request with updated booking details
    Then I receive a 200 status code
    And the response contains the updated details

  Scenario: I can delete a booking
    When I send a DELETE request for a booking
    Then I receive a 201 status code