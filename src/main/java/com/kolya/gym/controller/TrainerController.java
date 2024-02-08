package com.kolya.gym.controller;

import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.service.TrainerWorkloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Api(value = "API for trainer's workload", tags = "TrainerWorkload")
@RequestMapping("/trainer-workload")
@RestController
public class TrainerController {

    private final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    @Autowired
    private TrainerWorkloadService trainerWorkloadService;

    @ApiOperation(value = "Get trainer's training durations by username", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainer Workload returned"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @GetMapping("/{username}")
    public ResponseEntity<?> getTrainerWorkload(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /trainer-workload/{} was called", transactionId, username);
        TrainerWorkload trainerWorkload = trainerWorkloadService.getByUsername(transactionId, username);
        logger.info("Transaction ID: {}, 200 OK, Trainer Workload was returned: {} ", transactionId,  trainerWorkload);
        return ResponseEntity.status(HttpStatus.OK).body(trainerWorkload);
    }
}
