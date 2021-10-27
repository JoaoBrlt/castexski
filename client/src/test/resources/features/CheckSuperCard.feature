Feature: Check Super Card

  Background:
    Given a registered resort named resort-F with mail resort-f@mail.fr in city PolyVille
    And a new ski lift named lift-0 with open at true is created
    And a new ski lift named lift-1 with open at false is created
    And a user named Pascal Legitimus with email pl@mail.fr is created
    And a new entryCard named Super-Card for which super card is true and cost is 8 is created
    And a new entryPass named SUPER_ORIGINAL which lasts 24 hours and costs 0 for children and 0 for adults is created
    And a new entryPass named SUPER_FREE_EIGHTH which lasts 24 hours and costs 0 for children and 0 for adults is created
    And a new entryPass named SUPER_FREE_HOUR which lasts 1 hours and costs 0 for children and 0 for adults is created
    And pl@mail.fr logs in
    And he adds 1 Super-Card for which super card is true to his cart
    And he adds the credit card pl-cb of number 1100008969830000, security code 111, expire month 01 and expire year 2023 to his account
    And he pays for his cart
    And an employee links pl@mail.fr Super-Card card to the physical 0123456789101112

  Scenario: user check an opened lift
    When he check its physical card 0123456789101112 to the ski lift lift-0
    Then the access terminal says passed!
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Super-Card for which super card is true is removed
    And the entryPass named SUPER_ORIGINAL which lasts 24 hours is removed
    And the entryPass named SUPER_FREE_EIGHTH which lasts 24 hours is removed
    And the entryPass named SUPER_FREE_HOUR which lasts 1 hours is removed
    And the resort resort-F is removed

  Scenario: user check a closed lift
    When he check its physical card 0123456789101112 to the ski lift lift-1
    Then the access terminal says refused!
    #clean-up
    And he exits
    And the user for mail pl@mail.fr is removed
    And the entryCard named Super-Card for which super card is true is removed
    And the entryPass named SUPER_ORIGINAL which lasts 24 hours is removed
    And the entryPass named SUPER_FREE_EIGHTH which lasts 24 hours is removed
    And the entryPass named SUPER_FREE_HOUR which lasts 1 hours is removed
    And the resort resort-F is removed