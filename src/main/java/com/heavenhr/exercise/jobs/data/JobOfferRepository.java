package com.heavenhr.exercise.jobs.data;

import com.heavenhr.exercise.jobs.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    Optional<JobOffer> findByJobTitleIgnoreCase(String jobTitle);

}
