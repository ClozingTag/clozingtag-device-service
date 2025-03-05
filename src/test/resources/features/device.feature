Feature: ClozingTag Device Service

  Scenario: Create a new device
    Given I am authenticated as "user" with role "ROLE_USER"
    Given a device with name "My Device", brand "My Brand", and state "AVAILABLE"
    When I send a POST request to "/v1/devices" with the device data
    Then the response status code should be 201
    And the response body should contain the device name "My Device"
    And the response body should contain the device brand "My Brand"
    And the response body should contain the device state "AVAILABLE"

  Scenario: Get a device by ID
    Given I am authenticated as "user" with role "ROLE_USER"
    When I send a GET request to "/v1/devices/1"
    Then the response status code should be 200
    And the response body should contain the device name "My Device"
    And the response body should contain the device brand "My Brand"
    And the response body should contain the device state "AVAILABLE"

  Scenario: Update fully an existing device
    Given I am authenticated as "user" with role "ROLE_USER"
    When I send a PUT request to "/v1/devices/1" with new state "IN_USE"
    Then the response status code should be 200
    And the response body should contain the device name "My Device"
    And the response body should contain the device brand "My Brand"
    And the response body should contain the device state "IN_USE"

  Scenario: Delete an existing device throws 400 error (can't delete in use)
    Given I am authenticated as "user" with role "ROLE_USER"
    When I send a DELETE request to "/v1/devices/1"
    Then the response status code should be 400


  Scenario: Update fully an existing device throws 400 error (can't update in use)
    Given I am authenticated as "user" with role "ROLE_USER"
    When I send a PUT request to "/v1/devices/1" with new state "AVAILABLE"
    Then the response status code should be 400

  Scenario: Update partially an existing device
    Given I am authenticated as "user" with role "ROLE_USER"
    When I send a PUT partial request to "/v1/devices/1" with new state "INACTIVE"
    Then the response status code should be 200
    And the response body should contain the device name "My Device"
    And the response body should contain the device brand "My Brand"
    And the response body should contain the device state "INACTIVE"

  Scenario: Delete an existing device
    Given I am authenticated as "user" with role "ROLE_USER"
    When I send a DELETE request to "/v1/devices/1"
    Then the response status code should be 204
    And the device should be deleted from the database


