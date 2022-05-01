#Author: Chathura Dushmantha
Feature: TradeMe API Create Listing & Validte
  This feture will test adding a listing via Listing API methods and validate the added listing

  @api
  Scenario Outline: Add a listing and validate the listed
    Given user is authenticated to add a new listing
    And sends a "POST" request to "Selling" api including <category>, <title>, <subtitle>, <description>, <start_price>, <buy_now_price>, <duration>, <shipping>
    Then the response will contain <description>
    When user sends a GET request to "Selling/Listings" to retrieve the added listing
    Then the response will contain the <category>, <title>, <subtitle>, <description>, <start_price>, <buy_now_price>, <duration>, <shipping>

    Examples: 
      | category              | title             | subtitle              | description                             | start_price | buy_now_price | duration | shipping |
      | "0002-0356-0032-2273" | "Apple Iphone 13" | "Brand new Iphone 13" | "Brand new Apple Iphone 13 is for sale" |         800 |           850 | "Thirty" | "Free"   |
