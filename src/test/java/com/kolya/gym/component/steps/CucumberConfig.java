package com.kolya.gym.component.steps;


import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;

@CucumberContextConfiguration
@SpringBootTest(properties = "spring.profiles.active=c-test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberConfig {

    @MockBean
    private TrainerWorkloadRepo trainerWorkloadRepo;

    @MockBean
    private JmsTemplate jmsTemplate;
}
