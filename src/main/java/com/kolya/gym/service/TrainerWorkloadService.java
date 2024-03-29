package com.kolya.gym.service;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.data.YearMonth;
import com.kolya.gym.domain.Month;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.domain.Training;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TrainerWorkloadService {

    private final Logger logger = LoggerFactory.getLogger(TrainerWorkloadService.class);

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    public void changeWorkload(UUID transactionId, TrainerWorkloadRequestData trainerWorkloadData) {
        switch(trainerWorkloadData.getActionType()){
            case ADD:
                addTraining(transactionId, trainerWorkloadData);
                break;
            case DELETE:
                deleteTraining(transactionId, trainerWorkloadData);
                break;
        }
    }

    private void addTraining(UUID transactionId, TrainerWorkloadRequestData requestData){
        logger.info("Transaction ID: {}, Adding training {}", transactionId, requestData);
        Trainer trainer = getTrainerFromRequestData(requestData);
        Training training = getTrainingFromRequestData(requestData);
        addTrainingToMongo(trainer, training);
        logger.info("Transaction ID: {}, Training was added. Trainer:{}, Training: {}", transactionId, trainer, training);
    }

    private void deleteTraining(UUID transactionId, TrainerWorkloadRequestData requestData){
        logger.info("Transaction ID: {}, Deleting training {}", transactionId, requestData);
        Trainer trainer = getTrainerFromRequestData(requestData);
        Training training = getTrainingFromRequestData(requestData);
        deleteTrainingFromMongo(trainer, training);
        logger.info("Transaction ID: {}, Training was deleted. Trainer:{}, Training: {}", transactionId, trainer, training);
    }

    public TrainerWorkload getByUsername(UUID transactionId, String username){
        logger.info("Transaction ID: {}, Getting trainer's workload by trainer's username = {}", transactionId, username);
        TrainerWorkload trainerWorkload = trainerWorkloadRepo.findByUsername(username);
        logger.info("Transaction ID: {}, Trainer's workload returned: {}", transactionId, trainerWorkload);
        return trainerWorkload;
    }

    private Trainer getTrainerFromRequestData(TrainerWorkloadRequestData requestData){
        Trainer trainer = new Trainer();
        trainer.setUsername(requestData.getUsername());
        trainer.setFirstName(requestData.getFirstName());
        trainer.setLastName(requestData.getLastName());
        trainer.setActive(requestData.isActive());
        return trainer;
    }

    private Training getTrainingFromRequestData(TrainerWorkloadRequestData requestData){
        Training training = new Training();
        training.setDate(requestData.getTrainingDate());
        training.setDuration(requestData.getTrainingDuration());
        return training;
    }

    public void validateRequestData(TrainerWorkloadRequestData requestData) throws IllegalArgumentException{
        if (Strings.isBlank(requestData.getFirstName())) throw new IllegalArgumentException("First Name can't be empty");
        if (Strings.isBlank(requestData.getLastName())) throw new IllegalArgumentException("Last Name can't be empty");
        if (Strings.isBlank(requestData.getUsername())) throw new IllegalArgumentException("Username can't be empty");
        if (requestData.getTrainingDate()==null) throw new IllegalArgumentException("Training Date can't be null");
        if (requestData.getTrainingDuration()==null) throw new IllegalArgumentException("Training Duration can't be empty");
        if (requestData.getTrainingDuration()<0) throw new IllegalArgumentException("Training Duration can't be negative");
        if (requestData.isActive()==null) throw new IllegalArgumentException("Active Status can't be null");
        if (requestData.getActionType()==null) throw new IllegalArgumentException("ActionType can't be null");
    }

    private void addTrainingToMongo(Trainer trainer, Training training){
        TrainerWorkload trainerWorkload = getWorkload(trainer,training);
        Map<Integer,Map<Month,Integer>> workload = trainerWorkload.getWorkload();
        YearMonth yearMonth = getYearAndMonthFromDate(training.getDate());
        Map<Month,Integer> monthDurationMap = workload.get(yearMonth.getYear());
        int oldDuration = monthDurationMap.getOrDefault(yearMonth.getMonth(),0);

        int updatedDuration = oldDuration + training.getDuration();
        monthDurationMap.put(yearMonth.getMonth(),updatedDuration);
        trainerWorkloadRepo.save(trainerWorkload);
    }

    private void deleteTrainingFromMongo(Trainer trainer, Training training){
        TrainerWorkload trainerWorkload = getWorkload(trainer,training);
        Map<Integer,Map<Month,Integer>> workload = trainerWorkload.getWorkload();
        YearMonth yearMonth = getYearAndMonthFromDate(training.getDate());
        Map<Month,Integer> monthDurationMap = workload.get(yearMonth.getYear());
        int oldDuration = monthDurationMap.getOrDefault(yearMonth.getMonth(),0);

        int updatedDuration = oldDuration - training.getDuration();
        putOrRemove(updatedDuration,workload,yearMonth.getYear(),yearMonth.getMonth());
        if (workload.isEmpty()){
            trainerWorkloadRepo.deleteById(trainer.getUsername());
        }else{
            trainerWorkloadRepo.save(trainerWorkload);
        }
    }

    private TrainerWorkload getWorkload(Trainer trainer, Training training){
        TrainerWorkload trainerWorkload = trainerWorkloadRepo.findById(trainer.getUsername())
                .orElse(new TrainerWorkload(trainer));
        updateTrainer(trainerWorkload,trainer);
        Map<Integer, Map<Month, Integer>> workload =  trainerWorkload.getWorkload();
        YearMonth yearMonth = getYearAndMonthFromDate(training.getDate());
        workload.putIfAbsent(yearMonth.getYear(), new HashMap<>());
        return trainerWorkload;
    }

    private YearMonth getYearAndMonthFromDate(Date date){
        int STARTING_POINT_FOR_YEARS = 1900;
        int year = date.getYear() + STARTING_POINT_FOR_YEARS;
        Month month = Month.values()[date.getMonth()];
        return new YearMonth(year,month);
    }

    private void updateTrainer(TrainerWorkload trainerWorkload, Trainer trainer){
        trainerWorkload.setFirstName(trainer.getFirstName());
        trainerWorkload.setLastName(trainer.getLastName());
        trainerWorkload.setActive(trainer.isActive());
    }

    private void putOrRemove(int updatedDuration, Map<Integer, Map<Month, Integer>> workload, int year, Month month){
        Map<Month, Integer> monthDurationMap = workload.get(year);
        if (updatedDuration>0){
            monthDurationMap.put(month,updatedDuration);
        }else{
            removeCascade(workload, year, month);
        }
    }

    private void removeCascade(Map<Integer, Map<Month, Integer>> workload, int year, Month month){
        Map<Month, Integer> monthDurationMap = workload.get(year);
        monthDurationMap.remove(month);
        if (monthDurationMap.isEmpty()){
            workload.remove(year);
        }
    }
}
