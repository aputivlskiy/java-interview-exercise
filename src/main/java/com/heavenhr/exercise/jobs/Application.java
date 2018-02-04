package com.heavenhr.exercise.jobs;

import com.heavenhr.exercise.jobs.domain.DummyApplicationFlowServiceImpl;
import com.heavenhr.exercise.jobs.domain.JobApplicationFlowService;
import com.heavenhr.exercise.jobs.domain.JobApplicationService;
import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

import static com.heavenhr.exercise.jobs.entity.JobApplicationStatus.*;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public JobApplicationFlowService getJobApplicationFlowService() {
        return new DummyApplicationFlowServiceImpl();
    }

    @Bean
    @Qualifier(JobApplicationService.STATUS_TRANSITION_RULES)
    public Map<JobApplicationStatus, Set<JobApplicationStatus>> getStatusTransitionRules() {

        Map<JobApplicationStatus, Set<JobApplicationStatus>> rules = new EnumMap<>(JobApplicationStatus.class);

        rules.put(APPLIED, EnumSet.of(REJECTED, INVITED));
        rules.put(INVITED, EnumSet.of(REJECTED, HIRED));

        return Collections.unmodifiableMap(rules);
    }
}
