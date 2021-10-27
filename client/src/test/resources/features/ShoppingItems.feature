Feature: Shopping Items

  Background:
    When the catalog is empty
    And a user named Pascal Legitimus with email pl@mail.fr is created

  Scenario: user logs in and adds a card to his cart and removes it
    #init
    When a new entryCard named Super-Cartex for which super card is true and cost is 15 is created
    And pl@mail.fr logs in
    #add and remove card test
    And he adds 1 Super-Cartex for which super card is true to his cart
    Then for 1 item, his cart displays 1 Super-Cartex for price 15 as a supercartex
    And he removes 1 Super-Cartex for which super card is true from his cart
    Then his cart is empty
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Super-Cartex for which super card is true is removed


  Scenario: user logs in and adds a credit card to his account
    #init
    When pl@mail.fr logs in
    #add credit card test
    And he adds the credit card pascal-cb of number 1100008969830000, security code 111, expire month 01 and expire year 2031 to his account
    Then pl@mail.fr exists and has the credit card pascal-cb
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    Then the user for mail pl@mail.fr does not exist

  Scenario: user logs in, adds a credit card to his account, and pays for a card and a pass
    #init
    When a new entryCard named Normal-Card for which super card is false and cost is 9 is created
    And a new entryPass named Discovery which lasts 12 hours and costs 13 for children and 17 for adults is created
    And a new entryPass named Pro-pass which lasts 120 hours and costs 30 for children and 45 for adults is created
    And pl@mail.fr logs in
    #add items and remove pass test
    And he adds 1 Normal-Card for which super card is false to his cart
    And he adds 1 Discovery which lasts 12 hours and with children at false
    And he adds 1 Pro-pass which lasts 120 hours and with children at true
    And he removes 1 Discovery which lasts 12 hours and with children at false from his cart
    Then for 2 item, his cart displays 1 Normal-Card for price 9 as a card
    And for 2 item, his cart displays 1 Pro-pass for price 30 as a pass
    #add credit card and pay test
    When he adds the credit card pascal-cb of number 1100008969830000, security code 111, expire month 01 and expire year 2031 to his account
    And he pays for his cart
    Then his cart is empty
    And for 1 card, his account displays a Normal-Card
    And for 1 pass, his account displays a Pro-pass
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Normal-Card for which super card is false is removed
    And the entryPass named Discovery which lasts 12 hours is removed
    And the entryPass named Pro-pass which lasts 120 hours is removed
