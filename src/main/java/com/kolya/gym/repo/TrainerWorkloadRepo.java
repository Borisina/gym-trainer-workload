package com.kolya.gym.repo;

import com.kolya.gym.db.TrainerWorkload;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;

public interface TrainerWorkloadRepo {
    void addTraining(Trainer trainer, Training training);
    void deleteTraining(Trainer trainer, Training training);
    TrainerWorkload getTrainingByUsername(String username);
}
