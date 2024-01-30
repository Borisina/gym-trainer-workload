package com.kolya.gym.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

public class TrainerWorkloadRequestData {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date trainingDate;
    private Integer trainingDuration;

    public TrainerWorkloadRequestData() {
    }

    @JsonCreator
    public TrainerWorkloadRequestData(@JsonProperty("username") String username,
                                      @JsonProperty("firstName") String firstName,
                                      @JsonProperty("lastName") String lastName,
                                      @JsonProperty("isActive") Boolean isActive,
                                      @JsonProperty("trainingDate") Date trainingDate,
                                      @JsonProperty("trainingDuration")Integer trainingDuration) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainerWorkloadRequestData that = (TrainerWorkloadRequestData) o;
        return Objects.equals(username, that.username) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(isActive, that.isActive) && Objects.equals(trainingDate, that.trainingDate) && Objects.equals(trainingDuration, that.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, isActive, trainingDate, trainingDuration);
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
                '}';
    }
}
