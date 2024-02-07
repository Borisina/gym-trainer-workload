package com.kolya.gym.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TrainerWorkloadRequestData {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date trainingDate;
    private Integer trainingDuration;

    private ActionType actionType;

    public TrainerWorkloadRequestData() {
    }

    @JsonCreator
    public TrainerWorkloadRequestData(@JsonProperty("username") String username,
                                      @JsonProperty("firstName") String firstName,
                                      @JsonProperty("lastName") String lastName,
                                      @JsonProperty("isActive") Boolean isActive,
                                      @JsonProperty("trainingDate") Date trainingDate,
                                      @JsonProperty("trainingDuration") Integer trainingDuration,
                                      @JsonProperty("actionType") ActionType actionType) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.actionType = actionType;
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

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "TrainerWorkloadRequestData{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                ", actionType=" + actionType +
                '}';
    }
}
