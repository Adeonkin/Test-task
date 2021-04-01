Feature: Ui Test
  @UI
  Scenario: UITest

    # Step 1
    Given open site "http://test-app.d6.dev.devcaz.com/admin/login"
    Then the user is authorized with username "admin1" and password "[9k<k8^z!+$$GkuP"

    # Step 2
    When open gamers table

    # Step 3
    Then check table sorting by column 'Name'