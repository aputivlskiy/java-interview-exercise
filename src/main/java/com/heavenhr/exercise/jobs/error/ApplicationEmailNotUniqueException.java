package com.heavenhr.exercise.jobs.error;

import com.heavenhr.exercise.jobs.entity.JobApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationEmailNotUniqueException extends RuntimeException {

    public ApplicationEmailNotUniqueException(String email, JobApplication jobApp) {
        super(String.format("Application with same email (%s) already exists for the job %s",
                email, jobApp.getJobOffer().getJobTitle()));
    }
}
