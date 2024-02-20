Feature: Add workload

  Scenario: Add workload
    Given a trainerWorkloadRequestData with trainerUsername "T.T", trainingDate "2020-10-10", duration 20 and actionStatus "ADD"
    When the trainerWorkloadRequestData is pushed to the mq
    Then a trainer workload should be added to the mongo db


