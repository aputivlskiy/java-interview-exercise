package com.heavenhr.exercise.jobs.data;

import com.heavenhr.exercise.jobs.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByJobOfferIdAndEmailIgnoreCase(Long jobOfferId, String email);

    Collection<JobApplication> findAllByJobOfferId(Long jobOfferId);

    Optional<JobApplication> findByIdAndJobOfferId(Long jobAppId, Long jobOfferId);

    @Query("select A from JobApplication A where A.id = :id and A.jobOffer.id = :offerId")
    Optional<JobApplication> findByIdForJobOffer(@Param("id") Long jobAppId, @Param("offerId") Long jobOfferId);
}
