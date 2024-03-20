package com.kolya.gym.steps;

import com.kolya.gym.data.ActionType;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.Month;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import com.netflix.discovery.converters.Auto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonSteps {

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${mq.queue.name.workload}")
    private String QUEUE_NAME_WORKLOAD;

    @Value("${mq.queue.name.dlq}")
    private String QUEUE_NAME_DLQ;

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    private int WAIT_TIME = 500;

    @Given("a trainerWorkloadRequestData with trainerUsername {string}, trainingDate {string}, duration {int} and actionStatus {string}")
    public void a_trainer_workload_request_data(String trainerUsername, String trainingDateStr, int duration, String actionStatus) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = formatter.parse(trainingDateStr);
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setActionType(ActionType.valueOf(actionStatus));
        data.setTrainingDate(date);
        data.setActive(true);
        data.setUsername(trainerUsername);
        data.setTrainingDuration(duration);
        data.setFirstName("first");
        data.setLastName("last");
        scenarioContext.setData(data);
    }

    @Given("a trainer workload \\(username {string}, year {int}, month {string}, duration {int}\\), that is already saved in the mongo db")
    public void a_trainer_workload_in_the_mongo_db(String username, int year, String month, int duration) throws InterruptedException {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setActive(true);
        trainerWorkload.setFirstName("first");
        trainerWorkload.setLastName("last");
        trainerWorkload.setUsername(username);
        Map<Integer, Map<Month, Integer>> workload = new HashMap<>();
        Map<Month, Integer> monthMap = new HashMap<>();
        monthMap.put(Month.valueOf(month),duration);
        workload.put(year, monthMap);
        trainerWorkload.setWorkload(workload);
        trainerWorkloadRepo.save(trainerWorkload);
        scenarioContext.setTrainerWorkload(trainerWorkload);
    }


    @When("^the trainerWorkloadRequestData is pushed to the mq$")
    public void the_data_is_pushed_to_the_mq() throws InterruptedException {
        jmsTemplate.convertAndSend(QUEUE_NAME_WORKLOAD, scenarioContext.getData());
        Thread.sleep(WAIT_TIME);
    }

    @Then("^the trainerWorkloadRequestData should be pushed to the DLQ$")
    public void the_trainerWorkloadRequestData_should_pusged_to_the_DLQ$() throws InterruptedException {
        Object message = jmsTemplate.receiveAndConvert(QUEUE_NAME_DLQ);
        TrainerWorkloadRequestData trainerWorkloadRequestData= null;
        if (message instanceof TrainerWorkloadRequestData){
            trainerWorkloadRequestData = (TrainerWorkloadRequestData) message;
        }
        assertNotNull(trainerWorkloadRequestData);
        assertEquals(scenarioContext.getData().getUsername(), trainerWorkloadRequestData.getUsername());
        assertEquals(scenarioContext.getData().getFirstName(), trainerWorkloadRequestData.getFirstName());
        assertEquals(scenarioContext.getData().getUsername(), trainerWorkloadRequestData.getUsername());
    }
}
