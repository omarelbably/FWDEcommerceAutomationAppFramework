@smoke
  Feature: F05_hoverCategories | select random category from three hovarable categories
    Scenario: hover over categories
      When user hover category and select subcategory then check if the name equals to page title
      And user go to subcategory page
      Then user check if page title equals subcategory name
