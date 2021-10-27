@super_cartex
Feature: A pass is automatically ordered when a customer use his super cartex

  This feature support the way a super cartex will automatically get the right pass when a skier swipe it
  Background:
    Given a customer named Marcel Eat logged with the email marceleatdu06@gmail.com, and his credit card saved into his account. Digits: 8969837812345678 Exp. month: 10 Exp. year: 2023 CVV: 123
    And the customer has a Super Cartex already linked with the physical card no 1234567891023456

  Scenario: The skier use the super cartex for the first time of the day
    When the customer swipe his Super Cartex to a ski lift
    Then a SUPER_FREE_HOUR pass is loaded on his Super Cartex
    And the access is granted

  Scenario: The skier use the super cartex after the end of his first free hour
    Given an expired free hour pass is on the customer's supercartex
    When the customer swipe his Super Cartex to a ski lift
    Then a SUPER_ORIGINAL pass is loaded on his Super Cartex
    And the access is granted

  Scenario: The skier use the super cartex for the eighth consecutive day
    Given an expired free hour pass is on the customer's supercartex
    And a first swipe 7 days before
    When the customer swipe his Super Cartex to a ski lift
    Then a SUPER_FREE_EIGHTH pass is loaded on his Super Cartex
    And the access is granted

  Scenario: The skier use the super cartex on a discount day
    Given an expired free hour pass is on the customer's supercartex
    And a new discount for the super cartex on today
    When the customer swipe his Super Cartex to a ski lift
    Then a SUPER_DISCOUNT_TODAY pass is loaded on his Super Cartex
    And the access is granted

  Scenario: The skier use the super cartex on a month day
    Given an expired free hour pass is on the customer's supercartex
    And a new discount for the super cartex on month
    When the customer swipe his Super Cartex to a ski lift
    Then a SUPER_DISCOUNT_THIS_MONTH pass is loaded on his Super Cartex
    And the access is granted