package com.kolya.gym.steps;


import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

@CucumberContextConfiguration
@SpringBootTest(properties = "spring.profiles.active=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberConfig {

    private JmsTemplate jmsTemplate;

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    @Value("${mq.queue.name.workload}")
    private String QUEUE_NAME_WORKLOAD;

    @Value("${mq.queue.name.dlq}")
    private String QUEUE_NAME_DLQ;

    @Before
    @After
    public void deleteTrainers(){
        trainerWorkloadRepo.deleteAll();
    }

    @After("@queueTest")
    @Before("@queueTest")
    public void before(){
        clearQueue(QUEUE_NAME_WORKLOAD);
        clearQueue(QUEUE_NAME_DLQ);
    }

    private void clearQueue(String queueName) {
        while (true) {
            Object message = jmsTemplate.receive(queueName);
            if (message == null) {
                break;
            }
        }
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate){
        int RECEIVE_TIMEOUT = 500;
        jmsTemplate.setReceiveTimeout(RECEIVE_TIMEOUT);
        this.jmsTemplate = jmsTemplate;
    }
}
