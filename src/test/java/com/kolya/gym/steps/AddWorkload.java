package com.kolya.gym.steps;

import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddWorkload {

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;


    @Then("^a trainer workload should be added to the mongo db$")
    public void a_trainer_workload_should_be_added_to_mongo_db(){
        TrainerWorkload trainerWorkload = trainerWorkloadRepo.findById(scenarioContext.getData().getUsername()).orElse(null);
        assertNotNull(trainerWorkload);
    }

    @Then("^a trainer workload shouldn't be added to the mongo db$")
    public void a_trainer_workload_should_not_be_added_to_mongo_db(){
        TrainerWorkload trainerWorkload = trainerWorkloadRepo.findById(scenarioContext.getData().getUsername()).orElse(null);
        assertNull(trainerWorkload);
    }
}
