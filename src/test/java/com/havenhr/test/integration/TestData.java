package com.havenhr.test.integration;


import com.heavenhr.exercise.jobs.entity.JobApplication;
import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import com.heavenhr.exercise.jobs.entity.JobOffer;

import java.time.LocalDate;

/**
 * Contains data for integration tests.
 * Usually we would place this data into some external source, like json or yaml config file
 */
public class TestData {

    private final JobOffer offer1;
    private final JobOffer offer2;
    private final JobApplication application1;
    private final JobApplication application2;
    private final JobApplication application3;

    TestData() {
        offer1 = new JobOffer("Backend Developer", LocalDate.of(2018, 2, 10));
        offer2 = new JobOffer("Frontend Developer", LocalDate.of(2018, 3, 11));

        application1 = new JobApplication("some1@email.com", "Resume test for application1");
        application2 = new JobApplication("some2@email.com", "Resume test for application2");
        application3 = new JobApplication("some3@email.com", "Resume test for application3");

        application1.setStatus(JobApplicationStatus.APPLIED);
        application2.setStatus(JobApplicationStatus.APPLIED);
        application3.setStatus(JobApplicationStatus.APPLIED);
    }

    public JobOffer getOffer1() {
        return offer1;
    }

    public JobOffer getOffer2() {
        return offer2;
    }

    public JobApplication getApplication1() {
        return application1;
    }

    public JobApplication getApplication2() {
        return application2;
    }

    public JobApplication getApplication3() {
        return application3;
    }
}
