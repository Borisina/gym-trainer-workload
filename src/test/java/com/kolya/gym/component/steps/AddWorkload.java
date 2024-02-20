package com.kolya.gym.component.steps;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AddWorkload {

    @Autowired
    private ScenarioComponentContext scenarioContext;

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${mq.queue.name.dlq}")
    private String QUEUE_NAME_DLQ;

    @Then("^a trainer workload should be added to the mongo db$")
    public void a_trainer_workload_should_be_added_to_mongo_db(){
        verify(trainerWorkloadRepo,times(1)).save(any(TrainerWorkload.class));
    }

    @Then("^a trainer workload shouldn't be added to the mongo db$")
    public void a_trainer_workload_should_not_be_added_to_mongo_db(){
        verify(trainerWorkloadRepo,times(0)).save(any(TrainerWorkload.class));
    }

    @Then("^the trainerWorkloadRequestData should be pushed to the DLQ$")
    public void the_trainerWorkloadRequestData_should_pusged_to_the_DLQ$(){
        verify(jmsTemplate,times(1)).convertAndSend(eq(QUEUE_NAME_DLQ), eq(scenarioContext.getData()), any(MessagePostProcessor.class));
    }

}
