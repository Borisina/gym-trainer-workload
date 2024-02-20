@queueTest
Feature: Delete workload

  Scenario: Delete workload
    Given a trainerWorkloadRequestData with trainerUsername "T.T", trainingDate "2020-03-10", duration 20 and actionStatus "DELETE"
    And  a trainer workload (username "T.T", year 2020, month "MARCH", duration 20), that is already saved in the mongo db
    When the trainerWorkloadRequestData is pushed to the mq
    Then the trainer workload should be deleted from the mongo db