Feature: It is possible for a sign in to expire

  Scenario Outline: A sign in expire
    Given the token expiry is "<expiry>" milliseconds
    And the the user is signed in with the with the username "<username>" and password "<password>"
    And the "<expiry>" milliseconds has passed
    When the user goes to the home page
    Then the user should be on the sign in page

    Examples:
      | expiry | username    | password      |
      | 100    | ExpiredUser | test password |
