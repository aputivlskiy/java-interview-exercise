package com.heavenhr.exercise.jobs.error;

import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalStatusTransitionException extends RuntimeException {

    public IllegalStatusTransitionException(JobApplicationStatus currentStatus, JobApplicationStatus newStatus) {
        super(String.format("Application status can't be changed from %s to %s", currentStatus, newStatus));
    }
}
