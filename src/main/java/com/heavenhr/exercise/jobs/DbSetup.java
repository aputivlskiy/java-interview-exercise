package com.heavenhr.exercise.jobs;

import com.heavenhr.exercise.jobs.data.JobApplicationRepository;
import com.heavenhr.exercise.jobs.data.JobOfferRepository;
import com.heavenhr.exercise.jobs.entity.JobApplication;
import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import com.heavenhr.exercise.jobs.entity.JobOffer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DbSetup implements CommandLineRunner {

    private final JobOfferRepository offerRepository;
    private final JobApplicationRepository applicationRepository;

    public DbSetup(JobOfferRepository offerRepository, JobApplicationRepository applicationRepository) {
        this.offerRepository = offerRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void run(String... strings) {

        JobOffer jobOffer1 = new JobOffer("Junior software developer", LocalDate.of(2018, 1, 1));
        offerRepository.save(jobOffer1);

        JobApplication application1 = new JobApplication("my@email.com", "resume text");
        application1.setJobOffer(jobOffer1);
        application1.setStatus(JobApplicationStatus.APPLIED);
        applicationRepository.save(application1);


        JobOffer jobOffer2 = new JobOffer("Senior software developer", LocalDate.of(2018, 2, 1));
        offerRepository.save(jobOffer2);
    }
}
