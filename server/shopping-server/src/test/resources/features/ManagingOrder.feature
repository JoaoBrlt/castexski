@catalog
Feature: Managing the catalog

  This feature support the way an Employee can add an item to the catalog or remove it.

  Scenario: The employee add new cards to the catalog.
    When an employee add to the catalog a regular card named Cartex at the price of 2.0
    And an employee add to the catalog a special card named Cartex at the price of 10.0
    And an employee add to the catalog a pass named Discovery with a duration of 3 day at the regular price of 35.0 and 25.0 for the children
    Then the catalog contains a regular card named Cartex
    And the catalog contains a special card named Cartex
    And the catalog contains a pass named Discovery with a duration of 3 days

  Scenario: The employee remove cards and passes from the catalog.
    Given a catalog with a pass named Discovery with a duration of 3 days at the regular price of 35.0 and 25.0 for the children
    And a catalog with a pass named Discovery with a duration of 2 days at the regular price of 25.0 and 20.0 for the children
    And a catalog with a regular card named Cartex at the price of 2.0
    And a catalog with a special card named Cartex at the price of 10.0
    When an employee remove from the catalog the pass named Discovery with a duration of 3 days
    And an employee remove from the catalog the special card named Cartex
    Then the catalog contains a regular card named Cartex
    And the catalog contains a pass named Discovery with a duration of 2 days
    But the catalog doesn't contains a pass named Discovery with a duration of 3 days
    And the catalog doesn't contains a special card named Cartex