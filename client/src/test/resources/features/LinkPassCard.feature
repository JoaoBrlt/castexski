Feature: Link Pass Card

  Background:
    When the catalog is empty
    And a user named Pascal Legitimus with email pl@mail.fr is created
    And a new entryCard named Simple-Card for which super card is false and cost is 8 is created
    And a new entryCard named Super-Card for which super card is true and cost is 8 is created
    And a new entryPass named Simple-Pass which lasts 2 hours and costs 2 for children and 1 for adults is created
    And pl@mail.fr logs in

  Scenario: User buys card and links it to a physical one
    #init and buy items
    When he adds 1 Simple-Card for which super card is false to his cart
    And he adds the credit card pl-cb of number 1100008969830000, security code 111, expire month 01 and expire year 2023 to his account
    And he pays for his cart
    #a simple card link test
    And an employee links pl@mail.fr Simple-Card card to the physical 0123456789101112
    Then for 1 card, his account displays Simple-Card linked to 0123456789101112
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Simple-Card for which super card is false is removed
    And the entryCard named Super-Card for which super card is true is removed
    And the entryPass named Simple-Pass which lasts 2 hours is removed

  Scenario: User buys super card and links it to a physical one
    #init and buy items
    When he adds 1 Super-Card for which super card is true to his cart
    And he adds the credit card pl-cb of number 1100008969830000, security code 111, expire month 01 and expire year 2023 to his account
    And he pays for his cart
    #a super card link test
    And an employee links pl@mail.fr Super-Card card to the physical 0123456789101112
    Then for 1 card, his account displays Super-Card linked to 0123456789101112
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Super-Card for which super card is true is removed
    And the entryCard named Simple-Card for which super card is false is removed
    And the entryPass named Simple-Pass which lasts 2 hours is removed

  Scenario: User buys pass and card and links everything to a physical card
    #init and buy items
    When he adds 1 Simple-Card for which super card is false to his cart
    And he adds 1 Simple-Pass which lasts 2 hours and with children at true
    And he adds the credit card pl-cb of number 1100008969830000, security code 111, expire month 01 and expire year 2023 to his account
    And he pays for his cart
    #a simple card and pass link test
    And an employee links pl@mail.fr Simple-Card card to the physical 0123456789101112
    Then for 1 card, his account displays Simple-Card linked to 0123456789101112
    And he links his Simple-Pass which lasts 2 hours and with children at true to the physical 0123456789101112
    Then for 1 pass, his account displays Simple-Pass of duration 2 hours linked to 0123456789101112
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Simple-Card for which super card is false is removed
    And the entryCard named Super-Card for which super card is true is removed
    And the entryPass named Simple-Pass which lasts 2 hours is removed