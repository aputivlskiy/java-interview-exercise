package com.heavenhr.exercise.jobs.domain;

import com.heavenhr.exercise.jobs.entity.JobApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyApplicationFlowServiceImpl implements JobApplicationFlowService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void applicationApplied(JobApplication jobApplication) {
        log.info("New application from applicant {} has been submitted for the position {}.",
                jobApplication.getEmail(), jobApplication.getJobOffer().getJobTitle());
    }

    @Override
    public void applicantInvited(JobApplication jobApplication) {
        log.info("Applicant {} has been invited to the interview for the position {}.",
                jobApplication.getEmail(), jobApplication.getJobOffer().getJobTitle());
    }

    @Override
    public void applicationRejected(JobApplication jobApplication) {
        log.info("Application from applicant {} has been rejected for the position {}.",
                jobApplication.getEmail(), jobApplication.getJobOffer().getJobTitle());
    }

    @Override
    public void applicantHired(JobApplication jobApplication) {
        log.info("Applicant {} has been hired for the position {}!",
                jobApplication.getEmail(), jobApplication.getJobOffer().getJobTitle());
    }
}
