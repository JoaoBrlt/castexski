Feature: A skier swiping his card at a SkiLift

  This feature support the way a skier can swipes his card to a lift and see if he is allowed to take it or not

  Background:
    Given a opened resort named ISOLA9999
    And a ski lift named gotta go fast belonging to ISOLA9999
    And a complete access pass allowing anyone to go through gotta go fast

  Scenario: going to a closed ski lift
    When the skier swipes his card at a closed ski lift
    Then the ski lift does not allow the skier to go

  Scenario: going to an opened ski lift but not having the correct pass
    When the skier swipes his card at a opened ski lift
      And his card has the no access pass
    Then the ski lift does not allow the skier to go

  Scenario: going to an opened ski lift but not having the correct pass
    When the skier swipes his card at a opened ski lift
      And his card has the complete access pass
    Then the ski lift allows the skier to go