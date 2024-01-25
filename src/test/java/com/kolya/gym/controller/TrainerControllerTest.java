package com.kolya.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.data.RequestData;
import com.kolya.gym.db.TrainerWorkload;
import com.kolya.gym.service.TrainerWorkloadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    }

    @Test
    public void addTrainingTest() throws Exception {
        RequestData requestData = new RequestData();
        Mockito.doNothing().when(trainerWorkloadService).validateRequestData(requestData);
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isOk());
        Mockito.verify(trainerWorkloadService).addTraining(Mockito.any(), Mockito.any(RequestData.class));
    }

    @Test
    public void addTrainingBadRequestTest() throws Exception {
        RequestData requestData = new RequestData();
        Mockito.doThrow(new IllegalArgumentException("Invalid")).when(trainerWorkloadService).validateRequestData(requestData);
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteTrainingTest() throws Exception {
        RequestData requestData = new RequestData();
        Mockito.doNothing().when(trainerWorkloadService).validateRequestData(requestData);
        mockMvc.perform(delete("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isOk());
        Mockito.verify(trainerWorkloadService).deleteTraining(Mockito.any(), Mockito.any(RequestData.class));
    }

    @Test
    public void deleteTrainingBadRequestTest() throws Exception {
        RequestData requestData = new RequestData();
        Mockito.doThrow(new IllegalArgumentException("Invalid")).when(trainerWorkloadService).validateRequestData(requestData);
        mockMvc.perform(delete("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTrainerWorkloadTest() throws Exception {
        String username = "testUser";
        TrainerWorkload trainerWorkload = new TrainerWorkload(); // Вставьте корректные данные
        Mockito.when(trainerWorkloadService.getByUsername(Mockito.any(), Mockito.eq(username))).thenReturn(trainerWorkload);
        mockMvc.perform(get("/trainer-workload/" + username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(trainerWorkloadService).getByUsername(Mockito.any(), Mockito.eq(username));

    }
}