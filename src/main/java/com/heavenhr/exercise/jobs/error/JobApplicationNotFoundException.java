package com.heavenhr.exercise.jobs.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JobApplicationNotFoundException extends RuntimeException {

    public JobApplicationNotFoundException(Long jobAppId) {
        super("Could not find job application with id: " + jobAppId);
    }
}
