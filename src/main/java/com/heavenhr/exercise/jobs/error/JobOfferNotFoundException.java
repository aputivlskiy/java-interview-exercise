package com.heavenhr.exercise.jobs.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JobOfferNotFoundException extends RuntimeException {

    public JobOfferNotFoundException(Long jobOfferId) {
        super("Could not find job offer with id: " + jobOfferId);
    }
}
