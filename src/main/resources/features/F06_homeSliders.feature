@smoke
  Feature: F06_homeSlideres | Check if the sliders are working
    Scenario:   first slider is clickable on home page
      When clicking on the first slider
      Then check if u are rotated to nokia page

    Scenario: second slider is clickable on home page
      When clicking on the second slider
      Then check if u are rotated to iphone page