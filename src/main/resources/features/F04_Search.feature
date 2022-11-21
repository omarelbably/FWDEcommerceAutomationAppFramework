@smoke
  Feature: F04_Search | users will be able to search for products with different parameters

    Scenario Outline: user could search using product name
      When user clicks on search field
      And user search with "<productName>"
      Then user could find "<productName>" relative results
      Examples:
      |productName|
      |book|
      |laptop|
      |nike|


    Scenario Outline: user could search using product name
      When user clicks on search field
      And user search with "<sku>"
      Then user could find "<sku>" inside product detail page
      Examples:
        |sku|
        |SCI_FAITH|
        |APPLE_CAM|
        |SF_PRO_11|