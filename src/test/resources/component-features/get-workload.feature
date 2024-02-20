Feature: Get workload

  Scenario: Get workload
    Given a trainer workload (username "T.T", year 2020, month "MARCH", duration 20), that is already saved in the mongo db
    And a jwt for access
    When get request to get workload by trainer's username occurs
    Then the response should have json trainer workload info