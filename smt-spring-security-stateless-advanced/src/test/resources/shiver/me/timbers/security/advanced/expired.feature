Feature: It is possible for a sign in to expire

  Scenario Outline: A sign in can expire
    Given the token expiry is "<duration>" "<unit>"
    And the the user is signed in with the with the username "<username>" and password "<password>"
    And the "<duration>" "<unit>" has passed
    When the user goes to the home page
    Then the user should be on the sign in page

    Examples:
      | duration | unit         | username    | password      |
      | 100      | MILLISECONDS | ExpiredUser | test password |
