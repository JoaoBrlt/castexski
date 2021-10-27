Feature: Initiate Catalog

  Scenario: nothing is done
    Then the catalog is empty

  Scenario: one supercard registration
    #Add item
    When a new entryCard named Super-Cartex for which super card is true and cost is 10 is created
    Then for 1 item, the catalog displays the supercartex Super-Cartex for price 10
    #Clear item
    And the entryCard named Super-Cartex for which super card is true is removed
    Then the catalog is empty

  Scenario: one card registration
    #Add item
    When a new entryCard named Normal-Card for which super card is false and cost is 5 is created
    Then for 1 item, the catalog displays the card Normal-Card for price 5
    #Clear item
    And the entryCard named Normal-Card for which super card is false is removed
    Then the catalog is empty

  Scenario: one pass registration
    #Add item
    When a new entryPass named Discovery which lasts 24 hours and costs 10 for children and 11 for adults is created
    Then for 1 item, the catalog displays the pass Discovery which lasts 24 hours and costs 10 for children and 11 for adults
    #Clear item
    And the entryPass named Discovery which lasts 24 hours is removed
    Then the catalog is empty

  Scenario: few items registration
    #Add items
    When a new entryCard named Super-Cartex for which super card is true and cost is 15 is created
    And a new entryCard named Normal-Card for which super card is false and cost is 11 is created
    And a new entryPass named Discovery which lasts 72 hours and costs 23 for children and 17 for adults is created
    And a new entryCard named Master-Card for which super card is false and cost is 20 is created
    Then for 4 item, the catalog displays the supercartex Super-Cartex for price 15
    And for 4 item, the catalog displays the card Normal-Card for price 11
    And for 4 item, the catalog displays the card Master-Card for price 20
    And for 4 item, the catalog displays the pass Discovery which lasts 72 hours and costs 23 for children and 17 for adults
    #Clear items
    And the entryCard named Super-Cartex for which super card is true is removed
    And the entryCard named Normal-Card for which super card is false is removed
    And the entryCard named Master-Card for which super card is false is removed
    And the entryPass named Discovery which lasts 72 hours is removed
    Then the catalog is empty
