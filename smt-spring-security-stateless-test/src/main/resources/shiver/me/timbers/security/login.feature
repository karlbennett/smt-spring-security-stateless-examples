Feature: It is possible for a user to sign in

  Scenario: A signed out user cannot view the home page
    Given the user is not signed in
    When the user goes to the home page
    Then the user should be on the sign in page

  Scenario Outline: A user can sign in
    Given a user with the username "<username>" and password "<password>" exists
    And the user is not signed in
    And the user goes to the home page
    When the user enters a username of "<username>"
    And the user enters a password of "<password>"
    And the user signs in
    Then the user should be on the home page
    And the user should be signed in as "<username>"

    Examples:
      | username | password      |
      | TestUser | test password |
