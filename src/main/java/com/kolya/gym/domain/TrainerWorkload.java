package com.kolya.gym.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
@CompoundIndex(def = "{'firstName': 1, 'lastName': 1}")
public class TrainerWorkload {
    @Id
    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;

    private Map<Integer, Map<Month, Integer>> workload;

    public TrainerWorkload() {
        workload = new HashMap<>();
    }

    public TrainerWorkload(Trainer trainer) {
        workload = new HashMap<>();
        setActive(trainer.isActive());
        setFirstName(trainer.getFirstName());
        setLastName(trainer.getLastName());
        setUsername(trainer.getUsername());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Map<Integer, Map<Month, Integer>> getWorkload() {
        return workload;
    }

    public void setWorkload(Map<Integer, Map<Month, Integer>> workload) {
        this.workload = workload;
    }

    public void populateWithTrainer(Trainer trainer){

    }
}
