package com.heavenhr.exercise.jobs.api;

import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;

import javax.validation.constraints.NotNull;

public class ToggleStatusPatch {

    @NotNull
    private JobApplicationStatus status;

    public JobApplicationStatus getStatus() {
        return status;
    }
}
