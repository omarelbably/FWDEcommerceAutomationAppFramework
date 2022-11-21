@smoke
Feature: F07_followUs | check the follow us icons


  Scenario: Check facebook icon
    When clicking on the facebook icon
    Then verify the facebook url

  Scenario: Check twitter icon
    When clicking on the twitter icon
    Then verify the twitter url


  Scenario: Check Rss icon
    When clicking on the Rss icon
    Then verify the Rss url

  Scenario: Check youtube icon
    When clicking on the youtube icon
    Then verify the youtube url