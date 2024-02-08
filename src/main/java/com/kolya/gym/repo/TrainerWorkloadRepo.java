package com.kolya.gym.repo;

import com.kolya.gym.domain.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainerWorkloadRepo extends MongoRepository<TrainerWorkload, String> {
    TrainerWorkload findByUsername(String username);
}
