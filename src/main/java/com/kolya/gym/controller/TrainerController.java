package com.kolya.gym.controller;

import com.kolya.gym.data.RequestData;
import com.kolya.gym.db.TrainerWorkload;
import com.kolya.gym.service.TrainerWorkloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Api(value = "API for trainer's workload", tags = "TrainerWorkload")
@RestController
public class TrainerController {

    private final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    @Autowired
    private TrainerWorkloadService trainerWorkloadService;


    @ApiOperation(value = "Add training", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Training added"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PostMapping
    public ResponseEntity<?> addTraining(@RequestBody RequestData requestData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, POST / was called with body {}", transactionId, requestData);
        try{
            trainerWorkloadService.validateRequestData(requestData);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        trainerWorkloadService.addTraining(transactionId, requestData);
        logger.info("Transaction ID: {}, 200 OK, Training was added", transactionId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }


    @ApiOperation(value = "Delete training", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Training deleted"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @DeleteMapping
    public ResponseEntity<?> deleteTraining(@RequestBody RequestData requestData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, DELETE /trainers was called with body {}", transactionId, requestData);
        try{
            trainerWorkloadService.validateRequestData(requestData);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        trainerWorkloadService.deleteTraining(transactionId, requestData);
        logger.info("Transaction ID: {}, 200 OK, Training was deleted", transactionId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @ApiOperation(value = "Get trainer's training durations by username", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainer Workload returned"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @GetMapping("/{username}")
    public ResponseEntity<?> getTrainerWorkload(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /{} was called", transactionId, username);
        TrainerWorkload trainerWorkload = trainerWorkloadService.getByUsername(transactionId, username);
        logger.info("Transaction ID: {}, 200 OK, Trainer Workload was returned: {} ", transactionId,  trainerWorkload);
        return ResponseEntity.status(HttpStatus.OK).body(trainerWorkload);
    }
}
