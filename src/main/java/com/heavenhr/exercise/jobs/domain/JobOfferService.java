package com.heavenhr.exercise.jobs.domain;

import com.heavenhr.exercise.jobs.data.JobOfferRepository;
import com.heavenhr.exercise.jobs.entity.JobOffer;
import com.heavenhr.exercise.jobs.error.JobOfferNotFoundException;
import com.heavenhr.exercise.jobs.error.JobTitleNotUniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;

    @Autowired
    public JobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    public JobOffer getJobOffer(Long jobOfferId) {
        return Optional.ofNullable(jobOfferRepository.findOne(jobOfferId))
                .orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));
    }

    public Collection<JobOffer> findAllJobOffers() {
        return jobOfferRepository.findAll();
    }


    public JobOffer createJobOffer(JobOffer jobOffer) {
        validateJobTitleIsUnique(jobOffer.getJobTitle());
        return jobOfferRepository.save(jobOffer);
    }

    private void validateJobTitleIsUnique(String jobOffer) {
        jobOfferRepository.findByJobTitleIgnoreCase(jobOffer).ifPresent((existingJobOffer) -> {
            throw new JobTitleNotUniqueException(existingJobOffer.getJobTitle());
        });
    }
}
