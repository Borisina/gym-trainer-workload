package com.kolya.gym.service;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.repo.TrainerWorkloadRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrainerWorkloadServiceTest {

    @InjectMocks
    private TrainerWorkloadService trainerWorkloadService;

    @Mock
    private TrainerWorkloadRepo trainerWorkloadRepo;

    private TrainerWorkloadRequestData requestData;

    @Before
    public void setUp() {
        requestData = new TrainerWorkloadRequestData();
        requestData.setFirstName("firstName");
        requestData.setLastName("lastName");
        requestData.setUsername("username");
        requestData.setTrainingDate(new Date());
        requestData.setActive(true);
        requestData.setTrainingDuration(2);
    }

    @Test
    public void addTrainingTest() {
        trainerWorkloadService.addTraining(UUID.randomUUID(), requestData);
        verify(trainerWorkloadRepo, times(1)).addTraining(any(), any());
    }

    @Test
    public void deleteTrainingTest() {
        trainerWorkloadService.deleteTraining(UUID.randomUUID(), requestData);
        verify(trainerWorkloadRepo, times(1)).deleteTraining(any(), any());
    }

    @Test
    public void getByUsernameTest() {
        trainerWorkloadService.getByUsername(UUID.randomUUID(), "username");
        verify(trainerWorkloadRepo, times(1)).getTrainingByUsername("username");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateRequestDataTestWithoutFirstName() {
        requestData.setFirstName("");
        trainerWorkloadService.validateRequestData(requestData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateRequestDataTestWithoutLastName() {
        requestData.setLastName("");
        trainerWorkloadService.validateRequestData(requestData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateRequestDataTestWithoutUsername() {
        requestData.setUsername("");
        trainerWorkloadService.validateRequestData(requestData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateRequestDataTestWithoutActiveStatus() {
        requestData.setActive(null);
        trainerWorkloadService.validateRequestData(requestData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateRequestDataTestWithoutDate() {
        requestData.setTrainingDate(null);
        trainerWorkloadService.validateRequestData(requestData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateRequestDataTestWithoutDuration() {
        requestData.setTrainingDuration(null);
        trainerWorkloadService.validateRequestData(requestData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateRequestDataTestWithNegativeDuration() {
        requestData.setTrainingDuration(-1);
        trainerWorkloadService.validateRequestData(requestData);
    }
}