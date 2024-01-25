package com.kolya.gym.service;

import com.kolya.gym.data.RequestData;
import com.kolya.gym.db.TrainerWorkload;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainerWorkloadService {

    private final Logger logger = LoggerFactory.getLogger(TrainerWorkloadService.class);

    @Autowired
    private TrainerWorkloadRepo trainerWorkloadRepo;

    public Trainer addTraining(UUID transactionId, RequestData requestData){
        logger.info("Transaction ID: {}, Adding training {}", transactionId, requestData);
        Trainer trainer = getTrainerFromRequestData(requestData);
        Training training = getTrainingFromRequestData(requestData);
        trainerWorkloadRepo.addTraining(trainer, training);
        logger.info("Transaction ID: {}, Training was added. Trainer:{}, Training: {}", transactionId, trainer, training);
        return trainer;
    }

    public Trainer deleteTraining(UUID transactionId, RequestData requestData){
        logger.info("Transaction ID: {}, Deleting training {}", transactionId, requestData);
        Trainer trainer = getTrainerFromRequestData(requestData);
        Training training = getTrainingFromRequestData(requestData);
        trainerWorkloadRepo.deleteTraining(trainer, training);
        logger.info("Transaction ID: {}, Training was deleted. Trainer:{}, Training: {}", transactionId, trainer, training);
        return trainer;
    }

    public TrainerWorkload getByUsername(UUID transactionId, String username){
        logger.info("Transaction ID: {}, Getting trainer's workload by trainer's username = {}", transactionId, username);
        TrainerWorkload trainerWorkload = trainerWorkloadRepo.getTrainingByUsername(username);
        logger.info("Transaction ID: {}, Trainer's workload returned: {}", transactionId, trainerWorkload);
        return trainerWorkload;
    }

    private Trainer getTrainerFromRequestData(RequestData requestData){
        Trainer trainer = new Trainer();
        trainer.setUsername(requestData.getUsername());
        trainer.setFirstName(requestData.getFirstName());
        trainer.setLastName(requestData.getLastName());
        trainer.setActive(requestData.isActive());
        return trainer;
    }

    private Training getTrainingFromRequestData(RequestData requestData){
        Training training = new Training();
        training.setDate(requestData.getTrainingDate());
        training.setDuration(requestData.getTrainingDuration());
        return training;
    }

    public void validateRequestData(RequestData requestData) throws IllegalArgumentException{
        if (Strings.isBlank(requestData.getFirstName())) throw new IllegalArgumentException("First Name can't be empty");
        if (Strings.isBlank(requestData.getLastName())) throw new IllegalArgumentException("Last Name can't be empty");
        if (Strings.isBlank(requestData.getUsername())) throw new IllegalArgumentException("Username Name can't be empty");
        if (requestData.getTrainingDate()==null) throw new IllegalArgumentException("Training Date can't be null");
        if (requestData.getTrainingDuration()==null) throw new IllegalArgumentException("Training Duration can't be empty");
        if (requestData.getTrainingDuration()<0) throw new IllegalArgumentException("Training Duration can't be negative");
        if (requestData.isActive()==null) throw new IllegalArgumentException("Active Status can't be null");
    }

}