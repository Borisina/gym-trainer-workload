package com.kolya.gym.mq;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.service.TrainerWorkloadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecieverTest {
    @InjectMocks
    private Reciever reciever;
    @Mock
    private TrainerWorkloadService trainerWorkloadService;
    @Mock
    private JmsTemplate jmsTemplate;

    private String QUEUE_NAME_ADD;
    private String QUEUE_NAME_DELETE;
    private String QUEUE_NAME_DLQ;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        QUEUE_NAME_ADD = "QUEUE_NAME_ADD";
        Field field = reciever.getClass().getDeclaredField("QUEUE_NAME_ADD");
        field.setAccessible(true);
        field.set(reciever, QUEUE_NAME_ADD);

        QUEUE_NAME_DELETE = "QUEUE_NAME_DELETE";
        Field field2 = reciever.getClass().getDeclaredField("QUEUE_NAME_DELETE");
        field2.setAccessible(true);
        field2.set(reciever, QUEUE_NAME_DELETE);

        QUEUE_NAME_DLQ = "QUEUE_NAME_DLQ";
        Field field3 = reciever.getClass().getDeclaredField("QUEUE_NAME_DLQ");
        field3.setAccessible(true);
        field3.set(reciever, QUEUE_NAME_DLQ);
    }

    @Test
    public void recieveAdd_validData_shouldAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setUsername("valid_username");

        reciever.recieveAdd(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(0)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(1)).addTraining(any(UUID.class), eq(data));
    }

    @Test
    public void recieveAdd_invalidData_shouldNotAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setUsername(null); // This will lead to failed validation

        doThrow(IllegalArgumentException.class).when(trainerWorkloadService).validateRequestData(data);

        reciever.recieveAdd(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(0)).addTraining(any(UUID.class), any(TrainerWorkloadRequestData.class));
    }

    @Test
    public void recieveDelete_validData_shouldAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setUsername("valid_username");

        reciever.recieveDelete(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(0)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(1)).deleteTraining(any(UUID.class), eq(data));
    }

    @Test
    public void recieveDelete_invalidData_shouldNotAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setUsername(null); // This will lead to failed validation

        doThrow(IllegalArgumentException.class).when(trainerWorkloadService).validateRequestData(data);

        reciever.recieveDelete(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(0)).deleteTraining(any(UUID.class), any(TrainerWorkloadRequestData.class));
    }
}