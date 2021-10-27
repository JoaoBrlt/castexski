Feature: Initiate Resort

  Background:
    Given a registered resort named resort-F with mail resort-f@mail.fr in city PolyVille

  Scenario: new ski lifts registration
    #Add items
    When a new ski lift named lift-0 with open at true is created
    And a new ski lift named lift-1 with open at false is created
    Then the ski lift lift-0 exists
    And the ski lift lift-1 exists
    And the ski lift lift-3 does not exist
    #Clear items
    And the ski lift named lift-0 is removed
    Then the ski lift lift-0 does not exist
    And the resort resort-F is removed
    Then the ski lift lift-1 does not exist