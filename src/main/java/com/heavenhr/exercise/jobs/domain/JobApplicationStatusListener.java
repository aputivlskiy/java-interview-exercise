package com.heavenhr.exercise.jobs.domain;

import com.heavenhr.exercise.jobs.entity.JobApplication;
import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class JobApplicationStatusListener {

    private static final Consumer<JobApplication> DO_NOTHING = jobApplication -> {};

    private final Map<JobApplicationStatus, Consumer<JobApplication>> flows;

    @Autowired
    public JobApplicationStatusListener(JobApplicationFlowService flowService) {
        Map<JobApplicationStatus, Consumer<JobApplication>> flows = new HashMap<>();
        flows.put(JobApplicationStatus.APPLIED, flowService::applicationApplied);
        flows.put(JobApplicationStatus.INVITED, flowService::applicantInvited);
        flows.put(JobApplicationStatus.REJECTED, flowService::applicationRejected);
        flows.put(JobApplicationStatus.HIRED, flowService::applicantHired);

        this.flows = Collections.unmodifiableMap(flows);
    }

    public void applicationStatusChanged(JobApplication jobApplication) {
        Consumer<JobApplication> flow = flows.getOrDefault(jobApplication.getStatus(), DO_NOTHING);
        flow.accept(jobApplication);
    }
}
