package com.kolya.gym.component.steps;

import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AddWorkload {

    @Autowired
    private ScenarioComponentContext scenarioContext;

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    @Then("^a trainer workload should be added to the mongo db$")
    public void a_trainer_workload_should_be_added_to_mongo_db(){
        verify(trainerWorkloadRepo,times(1)).save(any(TrainerWorkload.class));
    }

}
