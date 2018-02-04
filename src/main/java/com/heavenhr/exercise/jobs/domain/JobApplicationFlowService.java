package com.heavenhr.exercise.jobs.domain;

import com.heavenhr.exercise.jobs.entity.JobApplication;

public interface JobApplicationFlowService {

    void applicationApplied(JobApplication jobApplication);

    void applicantInvited(JobApplication jobApplication);

    void applicationRejected(JobApplication jobApplication);

    void applicantHired(JobApplication jobApplication);

}
