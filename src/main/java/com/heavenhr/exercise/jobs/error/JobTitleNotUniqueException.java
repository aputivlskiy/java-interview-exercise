package com.heavenhr.exercise.jobs.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JobTitleNotUniqueException extends RuntimeException {

    public JobTitleNotUniqueException(String jobTitle) {
        super("Job offer with same title already exists: " + jobTitle);
    }
}
