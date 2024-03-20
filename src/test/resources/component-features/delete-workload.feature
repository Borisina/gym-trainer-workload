Feature: Delete workload

  Scenario: Delete workload
    Given a trainerWorkloadRequestData with trainerUsername "T.T", trainingDate "2020-03-10", duration 20 and actionStatus "DELETE"
    And  a trainer workload (username "T.T", year 2020, month "MARCH", duration 20), that is already saved in the mongo db
    When the trainerWorkloadRequestData is pushed to the mq
    Then the trainer workload should be deleted from the mongo db

  Scenario: Delete workload failure (no trainerUsername)
    Given a trainerWorkloadRequestData with trainerUsername "", trainingDate "2020-10-10", duration 20 and actionStatus "DELETE"
    When the trainerWorkloadRequestData is pushed to the mq
    Then the trainerWorkloadRequestData should be pushed to the DLQ

  Scenario: Delete workload failure (negative duration)
    Given a trainerWorkloadRequestData with trainerUsername "", trainingDate "2020-10-10", duration -20 and actionStatus "DELETE"
    When the trainerWorkloadRequestData is pushed to the mq
    Then the trainerWorkloadRequestData should be pushed to the DLQ