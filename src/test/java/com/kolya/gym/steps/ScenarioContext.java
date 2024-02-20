package com.kolya.gym.steps;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.TrainerWorkload;
import org.springframework.stereotype.Component;

@Component
public class ScenarioContext {

    private TrainerWorkloadRequestData data;
    private TrainerWorkload trainerWorkload;

    public TrainerWorkloadRequestData getData() {
        return data;
    }

    public void setData(TrainerWorkloadRequestData data) {
        this.data = data;
    }

    public TrainerWorkload getTrainerWorkload() {
        return trainerWorkload;
    }

    public void setTrainerWorkload(TrainerWorkload trainerWorkload) {
        this.trainerWorkload = trainerWorkload;
    }
}
