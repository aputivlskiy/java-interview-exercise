package com.heavenhr.exercise.jobs.api;

import com.heavenhr.exercise.jobs.entity.JobApplication;
import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import org.springframework.hateoas.core.Relation;

@Relation(collectionRelation = "applications")
public class JobApplicationListItem {

    private final JobApplication application;

    public JobApplicationListItem(JobApplication application) {
        this.application = application;
    }

    public Long getId() {
        return application.getId();
    }

    public String getEmail() {
        return application.getEmail();
    }

    public JobApplicationStatus getStatus() {
        return application.getStatus();
    }
}
