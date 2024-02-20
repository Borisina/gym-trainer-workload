package com.kolya.gym.component.steps;

import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DeleteWorkload {
    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;
    @Autowired
    private ScenarioComponentContext scenarioContext;


    @Then("^the trainer workload should be deleted from the mongo db$")
    public void the_training_should_be_deleted_from_the_database() throws InterruptedException {
        verify(trainerWorkloadRepo,times(1)).deleteById(scenarioContext.getData().getUsername());
    }
}
