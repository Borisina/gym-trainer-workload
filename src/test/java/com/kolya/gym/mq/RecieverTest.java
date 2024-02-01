package com.kolya.gym.mq;

import com.kolya.gym.data.ActionType;
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
import java.util.Date;
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

    private String QUEUE_NAME_WORKLOAD;
    private String QUEUE_NAME_DLQ;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        QUEUE_NAME_WORKLOAD = "QUEUE_NAME_WORKLOAD";
        Field field = reciever.getClass().getDeclaredField("QUEUE_NAME_WORKLOAD");
        field.setAccessible(true);
        field.set(reciever, QUEUE_NAME_WORKLOAD);


        QUEUE_NAME_DLQ = "QUEUE_NAME_DLQ";
        Field field3 = reciever.getClass().getDeclaredField("QUEUE_NAME_DLQ");
        field3.setAccessible(true);
        field3.set(reciever, QUEUE_NAME_DLQ);
    }

    @Test
    public void recieveAdd_validData_shouldAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setActionType(ActionType.ADD);
        reciever.recieveWorkloadRequest(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(0)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(1)).changeWorkload(any(UUID.class), eq(data));
    }

    @Test
    public void recieveAdd_invalidData_shouldNotAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setUsername(null); // This will lead to failed validation
        data.setActionType(ActionType.ADD);

        doThrow(IllegalArgumentException.class).when(trainerWorkloadService).validateRequestData(data);

        reciever.recieveWorkloadRequest(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(0)).changeWorkload(any(UUID.class), any(TrainerWorkloadRequestData.class));
    }

    @Test
    public void recieveDelete_validData_shouldAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setUsername("valid_username");
        data.setActionType(ActionType.DELETE);

        reciever.recieveWorkloadRequest(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(0)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(1)).changeWorkload(any(UUID.class), eq(data));
    }

    @Test
    public void recieveDelete_invalidData_shouldNotAddTraining() {
        TrainerWorkloadRequestData data = new TrainerWorkloadRequestData();
        data.setUsername(null); // This will lead to failed validation
        data.setActionType(ActionType.DELETE);

        doThrow(IllegalArgumentException.class).when(trainerWorkloadService).validateRequestData(data);

        reciever.recieveWorkloadRequest(data);

        verify(trainerWorkloadService, times(1)).validateRequestData(data);
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(TrainerWorkloadRequestData.class), any(MessagePostProcessor.class));
        verify(trainerWorkloadService, times(0)).changeWorkload(any(UUID.class), any(TrainerWorkloadRequestData.class));
    }
}