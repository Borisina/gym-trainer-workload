package com.kolya.gym.steps;

import com.kolya.gym.data.ActionType;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import com.kolya.gym.service.JwtService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jms.core.JmsTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
