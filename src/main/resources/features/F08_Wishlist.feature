@smoke
  Feature: F08_wishlist | check the wishlist button and quantity

    Scenario: put a product in wishlist and get its quantity
      When put a product to wishlist
      And check the success message
      And user go to wishlist page
      Then check the product quantity