Feature: Checking passport predicate completion

  # Need to expand range of data - maybe move to unit tests
  Scenario: Predicate completion of passports with home and no locator
    Given passport data has been exported
    When completion runs for that data
    Then I receive the correct result
