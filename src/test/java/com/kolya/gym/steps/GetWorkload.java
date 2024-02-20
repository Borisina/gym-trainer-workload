package com.kolya.gym.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.data.ActionType;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.Month;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import com.kolya.gym.service.JwtService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GetWorkload {
    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private JwtService jwtService;

    private ResponseEntity<String> response;
    private String jwt;

    @Given("^a jwt for access$")
    public void a_jwt(){
        jwt = jwtService.generateTokenForServices();
    }

    @When("^get request to get workload by trainer's username occurs$")
    public void get_request() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange("/trainer-workload/"+scenarioContext.getData().getUsername(), HttpMethod.GET, entity, String.class);
    }

    @Then("^the response should have json trainer workload info$")
    public void the_response_should_have_json_trainer_workload_info$() throws JsonProcessingException {
        String body = response.getBody();
        TrainerWorkload trainerWorkloadFromMongo =new ObjectMapper().readValue(body, TrainerWorkload.class);
        assertNotNull(trainerWorkloadFromMongo);
        assertEquals(scenarioContext.getTrainerWorkload().getUsername(), trainerWorkloadFromMongo.getUsername());
        assertEquals(scenarioContext.getTrainerWorkload().getWorkload(),trainerWorkloadFromMongo.getWorkload());
    }
}
