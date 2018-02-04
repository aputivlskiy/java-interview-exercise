package com.heavenhr.exercise.jobs.domain;

import com.heavenhr.exercise.jobs.data.JobApplicationRepository;
import com.heavenhr.exercise.jobs.entity.JobApplication;
import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import com.heavenhr.exercise.jobs.entity.JobOffer;
import com.heavenhr.exercise.jobs.error.ApplicationEmailNotUniqueException;
import com.heavenhr.exercise.jobs.error.IllegalStatusTransitionException;
import com.heavenhr.exercise.jobs.error.JobApplicationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
public class JobApplicationService {

    public static final String STATUS_TRANSITION_RULES = "statusTransitionRules";

    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferService jobOfferService;
    private final Map<JobApplicationStatus, Set<JobApplicationStatus>> statusTransitionRules;
    private final JobApplicationStatusListener statusListener;

    @Autowired
    public JobApplicationService(JobApplicationRepository applicationRepository,
                                 JobOfferService jobOfferService,
                                 JobApplicationStatusListener statusListener,
                                 @Qualifier(STATUS_TRANSITION_RULES) Map<JobApplicationStatus, Set<JobApplicationStatus>> statusTransitionRules) {
        this.jobApplicationRepository = applicationRepository;
        this.jobOfferService = jobOfferService;
        this.statusListener = statusListener;
        this.statusTransitionRules = statusTransitionRules;
    }

    public JobApplication getJobApplication(Long jobOfferId, Long jobAppId) {
        return jobApplicationRepository.findByIdAndJobOfferId(jobAppId, jobOfferId)
                .orElseThrow(() -> new JobApplicationNotFoundException(jobAppId));
    }

    public Collection<JobApplication> findAllApplicationsForJob(Long jobOfferId) {
        return jobApplicationRepository.findAllByJobOfferId(jobOfferId);
    }

    public JobApplication createJobApplication(Long jobOfferId, JobApplication jobApplication) {

        validateEmailIsUnique(jobOfferId, jobApplication.getEmail());

        JobOffer jobOffer = jobOfferService.getJobOffer(jobOfferId);
        jobApplication.setJobOffer(jobOffer);
        jobApplication.setStatus(JobApplicationStatus.APPLIED);

        JobApplication newJobApplication = jobApplicationRepository.save(jobApplication);

        statusListener.applicationStatusChanged(newJobApplication);

        return newJobApplication;
    }

    public void toggleJobApplicationStatus(Long jobOfferId, Long jobAppId, JobApplicationStatus status) {
        JobApplication jobApplication = getJobApplication(jobOfferId, jobAppId);

        validateStatusTransition(jobApplication.getStatus(), status);
        jobApplication.setStatus(status);

        statusListener.applicationStatusChanged(jobApplication);

        jobApplicationRepository.save(jobApplication);
    }

    private void validateEmailIsUnique(Long jobOfferId, String email) {
        jobApplicationRepository.findByJobOfferIdAndEmailIgnoreCase(jobOfferId, email).ifPresent((existingJobApp) -> {
            throw new ApplicationEmailNotUniqueException(email, existingJobApp);
        });
    }

    private void validateStatusTransition(JobApplicationStatus currentStatus, JobApplicationStatus newStatus) {
        Set<JobApplicationStatus> rules = statusTransitionRules.get(currentStatus);
        if (rules == null) {
            throw new IllegalStatusTransitionException(currentStatus, newStatus);
        }

        if (!rules.contains(newStatus)) {
            throw new IllegalStatusTransitionException(currentStatus, newStatus);
        }
    }
}
