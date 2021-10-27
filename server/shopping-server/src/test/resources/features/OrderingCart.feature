@cart
Feature: Ordering Regular Card

  This feature support the way a Customer can order the regular card through the SuperCastex system

  Background:
    Given a customer named Marcel Eat logged with the email marceleatdu06@gmail.com, and his credit card saved into his account. Digits: 1234567812345678 Exp. month: 10 Exp. year: 2023 CVV: 123

  Scenario: The cart is empty by default
    Then the cart of marceleatdu06@gmail.com contains 0 different item

  Scenario: adding 3 regular Castex card to a cart
    When marceleatdu06@gmail.com wants to order 3 regular cards Cartex
    Then the cart of marceleatdu06@gmail.com contains 3 regular cards Cartex
    And the cart of marceleatdu06@gmail.com contains 1 different item

  Scenario: Modifying the number of regular card inside an order
    When marceleatdu06@gmail.com wants to order 3 regular cards Cartex
    And marceleatdu06@gmail.com wants to remove 2 regular cards Cartex
    Then the cart of marceleatdu06@gmail.com contains 1 regular cards Cartex
    And the cart of marceleatdu06@gmail.com contains 1 different item
