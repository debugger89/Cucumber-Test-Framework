#Author: Chathura Dushmantha
Feature: TradeMe Web Auto Search
  This feture will test the automotive search feature in trademe web application

  @web
  Scenario Outline: Search used car listing and verify output
    Given User is on the home page
    And navigated to the "Motors" tab
    Then the user navigates to "Cars" tab and selects "Used" option
    And user provides <search_keyword>, <make>, <model>, <body_style>
    And clicks on Search button
    Then user click on the first seach result
    And verify the <number_plate>, <body_style>, <kilometers>, <seats>

    Examples: 
      | search_keyword                   | make            | model   | body_style | number_plate | kilometers | seats |
      | "2016 Dual Front Airbag Package" | "Mercedes-Benz" | "C 180" | "Sedan"    | "VLOGER"     | "15,000km" | "5"   |
