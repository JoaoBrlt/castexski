Feature: Check Normal Card

  Background:
    Given a registered resort named resort-F with mail resort-f@mail.fr in city PolyVille
    And a new ski lift named lift-0 with open at true is created
    And a new ski lift named lift-1 with open at true is created
    And a new ski lift named lift-2 with open at false is created
    And a new ski lift named lift-3 with open at false is created
    And a user named Pascal Legitimus with email pl@mail.fr is created
    And a new entryCard named Simple-Card for which super card is false and cost is 8 is created
    And a new entryPass named Simple-Pass which lasts 2 hours and costs 2 for children and 1 for adults is created
    And an lift-1 access for the pass Simple-Pass
    And an lift-2 access for the pass Simple-Pass
    And pl@mail.fr logs in
    And he adds 1 Simple-Card for which super card is false to his cart
    And he adds 1 Simple-Pass which lasts 2 hours and with children at false
    And he adds the credit card pl-cb of number 1100008969830000, security code 111, expire month 01 and expire year 2023 to his account
    And he pays for his cart
    And an employee links pl@mail.fr Simple-Card card to the physical 0123456789101112
    And he links his Simple-Pass which lasts 2 hours and with children at false to the physical 0123456789101112

  Scenario: user check an opened lift he can't access with its pass
    When he check its physical card 0123456789101112 to the ski lift lift-0
    Then the access terminal says refused!
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Simple-Card for which super card is false is removed
    And the entryPass named Simple-Pass which lasts 2 hours is removed
    And the resort resort-F is removed

  Scenario: user check an opened lift he can access with its pass
    When he check its physical card 0123456789101112 to the ski lift lift-1
    Then the access terminal says passed!
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Simple-Card for which super card is false is removed
    And the entryPass named Simple-Pass which lasts 2 hours is removed
    And the resort resort-F is removed

  Scenario: user check a closed lift he can access with its pass
    When he check its physical card 0123456789101112 to the ski lift lift-2
    Then the access terminal says refused!
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Simple-Card for which super card is false is removed
    And the entryPass named Simple-Pass which lasts 2 hours is removed
    And the resort resort-F is removed

  Scenario: user check a closed lift he can't access with its pass
    When he check its physical card 0123456789101112 to the ski lift lift-3
    Then the access terminal says refused!
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Simple-Card for which super card is false is removed
    And the entryPass named Simple-Pass which lasts 2 hours is removed
    And the resort resort-F is removed