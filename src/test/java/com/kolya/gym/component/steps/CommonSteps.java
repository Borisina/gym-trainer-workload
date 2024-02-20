package com.kolya.gym.component.steps;

import com.kolya.gym.data.ActionType;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.Month;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.mq.Reciever;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import io.cucumber.java.en.Given;
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

import static org.mockito.Mockito.when;

public class CommonSteps {

    @Autowired
    private ScenarioComponentContext scenarioContext;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${mq.queue.name.workload}")
    private String QUEUE_NAME_WORKLOAD;

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    @Autowired
    private Reciever reciever;

    @Given("^a trainerWorkloadRequestData with trainerUsername \"([^\"]*)\", trainingDate \"([^\"]*)\", duration ([0-9]*) and actionStatus \"([^\"]*)\"$")
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

    @Given("^a trainer workload \\(username \"([^\"]*)\", year ([0-9]*), month \"([^\"]*)\", duration ([0-9]*)\\), that is already saved in the mongo db$")
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
        when(trainerWorkloadRepo.findByUsername(username)).thenReturn(trainerWorkload);
        scenarioContext.setTrainerWorkload(trainerWorkload);
    }


    @When("^the trainerWorkloadRequestData is pushed to the mq$")
    public void the_data_is_pushed_to_the_mq() throws InterruptedException {
        reciever.recieveWorkloadRequest(scenarioContext.getData());
    }
}
