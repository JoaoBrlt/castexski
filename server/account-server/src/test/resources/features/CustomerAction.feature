Feature: Customer Action

  This feature supports different actions done by a Customer on their account

  Background:
    Given a customer named Daym Son with email daym@mail.mail

  Scenario: A new customer registers
    When new customer Jeremy Beremy registers
      And their email jeremb@mail.mail is available
    Then their account is created

  Scenario: Editing your account information
    When Account daym@mail.mail edits their first name to John
      And Account daym@mail.mail edits their last name to Cena
    Then their account is updated

  Scenario: Unavailable email
    When Account daym@mail.mail edits their email to john@mail.mail
    And Email john@mail.mail is already taken
    Then their email account is not updated