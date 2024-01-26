package com.kolya.gym.db;

import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class InMemoryStorageTest {

    private InMemoryStorage inMemoryStorage;

    @Before
    public void setUp() {
        inMemoryStorage = new InMemoryStorage();
    }

    @Test
    public void addTrainingTest() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer1");
        Training training = new Training();
        training.setDate(new Date());
        training.setDuration(30);

        inMemoryStorage.addTraining(trainer, training);
        assertNotNull(inMemoryStorage.getTrainingByUsername("trainer1"));
    }

    @Test
    public void deleteTrainingTest() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer1");
        Training training = new Training();
        training.setDate(new Date());
        training.setDuration(30);

        inMemoryStorage.addTraining(trainer, training);
        assertNotNull(inMemoryStorage.getTrainingByUsername("trainer1"));

        // удаление тренировки
        inMemoryStorage.deleteTraining(trainer, training);
        assertNotNull(inMemoryStorage.getTrainingByUsername("trainer1"));
    }

    @Test
    public void getTrainingByUsernameTest() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer1");
        Training training = new Training();
        training.setDate(new Date());
        training.setDuration(30);

        inMemoryStorage.addTraining(trainer, training);

        assertNotNull(inMemoryStorage.getTrainingByUsername("trainer1"));
    }
}