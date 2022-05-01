#Author: Chathura Dushmantha
Feature: TradeMe API Charity Search
  This feture will retrieve all charities via TradeMe API and verify the given charity name

  @api
  Scenario Outline: Get a list of charities via TradeMe API and look for the given charity
    Given user retrieves a list of "Charities"
    Then the response will contain a list of "Charities"
    And the response will contain <charity_name> charity

    Examples: 
      | charity_name |
      | "St John"    |
