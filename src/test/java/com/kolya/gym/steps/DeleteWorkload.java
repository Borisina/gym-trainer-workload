package com.kolya.gym.steps;

import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNull;

public class DeleteWorkload {
    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;
    @Autowired
    private ScenarioContext scenarioContext;


    @Then("^the trainer workload should be deleted from the mongo db$")
    public void the_training_should_be_deleted_from_the_database() throws InterruptedException {
        TrainerWorkload trainerWorkload = trainerWorkloadRepo.findById(scenarioContext.getData().getUsername()).orElse(null);
        assertNull(trainerWorkload);
    }
}
