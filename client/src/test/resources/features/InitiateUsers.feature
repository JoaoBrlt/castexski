Feature: Initiate Users

  Scenario: new users registration
    #Add users
    When a user named Jean Michel with email j.m@mail.fr is created
    And a user named Janne Micheline with email m.j@mail.fr is created
    Then the user for mail j.m@mail.fr exists
    And the user for mail m.j@mail.fr exists
    And the user for mail m.m@mail.fr does not exist
    #Clear users
    And the user for mail j.m@mail.fr is removed
    And the user for mail m.j@mail.fr is removed
    Then the user for mail j.m@mail.fr does not exist
    And the user for mail m.j@mail.fr does not exist