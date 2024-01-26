package com.kolya.gym.db;

import com.kolya.gym.domain.Month;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryStorage implements TrainerWorkloadRepo {

    private Map<Trainer, TrainerWorkload> storage = new HashMap<>();



    public TrainerWorkload getTrainingByUsername(String username){
        return storage.get(new Trainer(username));
    }

    public void addTraining(Trainer trainer, Training training){
        updateTraining(trainer, training, true);
    }

    public void deleteTraining(Trainer trainer, Training training){
        updateTraining(trainer, training, false);
    }

    private void updateTraining(Trainer trainer, Training training, boolean isAdd){
        TrainerWorkload trainerWorkload = storage.computeIfAbsent(trainer, k -> new TrainerWorkload());
        Map<Integer, Map<String, Integer>> list =  trainerWorkload.getWorkload();
        Month month = Month.values()[training.getDate().getMonth()];
        int year = training.getDate().getYear()+1900;
        Map<String, Integer> monthDurationMap = list.computeIfAbsent(year, k -> new HashMap<>());
        int oldDuration = monthDurationMap.getOrDefault(month.name(),0);
        int updatedDuration = 0;
        if (isAdd){
            updatedDuration = oldDuration + training.getDuration();
        }else{
            updatedDuration = oldDuration - training.getDuration();
            if (updatedDuration<=0){
                updateListIfNeeded(trainer, year ,month.name());
                return;
            }
        }
        monthDurationMap.put(month.name(),updatedDuration);
    }

    private void updateListIfNeeded(Trainer trainer, int year ,String month){
        TrainerWorkload trainerWorkload = storage.get(trainer);
        if (trainerWorkload.getWorkload().isEmpty()){
            storage.remove(trainer);
        }
        Map<String, Integer> monthDurationMap = trainerWorkload.getWorkload().get(year);
        monthDurationMap.remove(month);
        if (monthDurationMap.isEmpty()){
            trainerWorkload.getWorkload().remove(year);
        }
    }

}
