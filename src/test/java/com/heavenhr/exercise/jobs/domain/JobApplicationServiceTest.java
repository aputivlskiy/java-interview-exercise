package com.heavenhr.exercise.jobs.domain;

import com.heavenhr.exercise.jobs.data.JobApplicationRepository;
import com.heavenhr.exercise.jobs.entity.JobApplication;
import com.heavenhr.exercise.jobs.entity.JobApplicationStatus;
import com.heavenhr.exercise.jobs.entity.JobOffer;
import com.heavenhr.exercise.jobs.error.ApplicationEmailNotUniqueException;
import com.heavenhr.exercise.jobs.error.IllegalStatusTransitionException;
import com.heavenhr.exercise.jobs.error.JobApplicationNotFoundException;
import com.heavenhr.exercise.jobs.error.JobOfferNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static com.heavenhr.exercise.jobs.entity.JobApplicationStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobApplicationServiceTest {

    private static final Long JOB_OFFER_ID = 1L;
    private static final Long JOB_APPLICATION_ID = 1L;
    private static final String EMAIL = "some@email.com";
    private static final JobApplicationStatus STATUS = APPLIED;
    private static final String RESUME = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod nibh.";

    private static final Map<JobApplicationStatus, Set<JobApplicationStatus>> RULES;

    static {
        Map<JobApplicationStatus, Set<JobApplicationStatus>> rules = new EnumMap<>(JobApplicationStatus.class);
        rules.put(APPLIED, EnumSet.of(REJECTED, INVITED));
        RULES = Collections.unmodifiableMap(rules);
    }

    @Mock
    private JobOffer jobOfferMock;

    @Mock
    private JobApplication applicationMock;

    @Mock
    private JobApplicationRepository repositoryMock;

    @Mock
    private JobOfferService offerServiceMock;

    @Mock
    private JobApplicationStatusListener statusListenerMock;

    private JobApplicationService applicationService;


    @Before
    public void setUp() {
        when(applicationMock.getId()).thenReturn(JOB_APPLICATION_ID);
        when(applicationMock.getJobOffer()).thenReturn(jobOfferMock);
        when(applicationMock.getEmail()).thenReturn(EMAIL);
        when(applicationMock.getStatus()).thenReturn(STATUS);
        when(applicationMock.getResume()).thenReturn(RESUME);

        applicationService = new JobApplicationService(
                repositoryMock, offerServiceMock, statusListenerMock, RULES);
    }

    @Test
    public void getJobApplicationShouldReturnApplicationIfExists() {
        when(repositoryMock.findByIdAndJobOfferId(JOB_APPLICATION_ID, JOB_OFFER_ID))
                .thenReturn(Optional.of(applicationMock));

        JobApplication application = applicationService.getJobApplication(JOB_OFFER_ID, JOB_APPLICATION_ID);
        assertThat(application.getId()).isEqualTo(JOB_APPLICATION_ID);
        assertThat(application.getJobOffer()).isEqualTo(jobOfferMock);
        assertThat(application.getStatus()).isEqualTo(STATUS);
        assertThat(application.getEmail()).isEqualTo(EMAIL);
        assertThat(application.getResume()).isEqualTo(RESUME);
    }

    @Test(expected = JobApplicationNotFoundException.class)
    public void getJobOfferShouldFailIfNotExists() {
        when(repositoryMock.findByIdAndJobOfferId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        applicationService.getJobApplication(2L, 3L);
    }

    @Test
    public void findAllApplicationsForJobShouldReturnApplications() {
        when(repositoryMock.findAllByJobOfferId(JOB_OFFER_ID)).thenReturn(Collections.singletonList(applicationMock));

        Collection<JobApplication> applications = applicationService.findAllApplicationsForJob(JOB_OFFER_ID);
        assertThat(applications.size()).isEqualTo(1);
        assertThat(applications).containsOnly(applicationMock);
    }

    @Test
    public void createJobApplicationShouldSaveApplication() {
        when(offerServiceMock.getJobOffer(JOB_OFFER_ID)).thenReturn(jobOfferMock);
        when(repositoryMock.findByJobOfferIdAndEmailIgnoreCase(JOB_OFFER_ID, EMAIL)).thenReturn(Optional.empty());
        when(repositoryMock.save(applicationMock)).thenReturn(applicationMock);

        JobApplication application = applicationService.createJobApplication(JOB_OFFER_ID, applicationMock);

        assertThat(application).isEqualTo(applicationMock);
        verify(repositoryMock).findByJobOfferIdAndEmailIgnoreCase(JOB_OFFER_ID, EMAIL);
        verify(repositoryMock).save(applicationMock);
        verify(offerServiceMock).getJobOffer(JOB_OFFER_ID);
        verify(applicationMock).setStatus(APPLIED);
        verify(statusListenerMock).applicationStatusChanged(applicationMock);
    }

    @Test(expected = JobOfferNotFoundException.class)
    public void createJobApplicationShouldFailIfNoJobOffer() {
        when(offerServiceMock.getJobOffer(JOB_OFFER_ID)).thenThrow(new JobOfferNotFoundException(JOB_OFFER_ID));
        when(repositoryMock.findByJobOfferIdAndEmailIgnoreCase(JOB_OFFER_ID, EMAIL)).thenReturn(Optional.empty());
        applicationService.createJobApplication(JOB_OFFER_ID, applicationMock);
    }

    @Test(expected = ApplicationEmailNotUniqueException.class)
    public void createJobApplicationShouldFailIfEmailNotUnique() {
        when(offerServiceMock.getJobOffer(JOB_OFFER_ID)).thenReturn(jobOfferMock);
        when(repositoryMock.findByJobOfferIdAndEmailIgnoreCase(JOB_OFFER_ID, EMAIL))
                .thenReturn(Optional.of(applicationMock));

        applicationService.createJobApplication(JOB_OFFER_ID, applicationMock);
    }

    @Test
    public void toggleJobApplicationStatusShouldSaveStatus() {
        final JobApplicationStatus newStatus = JobApplicationStatus.INVITED;

        when(repositoryMock.findByIdAndJobOfferId(JOB_APPLICATION_ID, JOB_OFFER_ID))
                .thenReturn(Optional.of(applicationMock));
        applicationService.toggleJobApplicationStatus(JOB_OFFER_ID, JOB_APPLICATION_ID, newStatus);

        verify(applicationMock).setStatus(newStatus);
        verify(repositoryMock).save(applicationMock);
        verify(statusListenerMock).applicationStatusChanged(applicationMock);
    }

    @Test(expected = IllegalStatusTransitionException.class)
    public void toggleJobApplicationStatusShouldFailIfStatusIsTerminal() {
        final JobApplicationStatus newStatus = JobApplicationStatus.INVITED;

        when(repositoryMock.findByIdAndJobOfferId(JOB_APPLICATION_ID, JOB_OFFER_ID))
                .thenReturn(Optional.of(applicationMock));
        when(applicationMock.getStatus()).thenReturn(JobApplicationStatus.REJECTED);

        applicationService.toggleJobApplicationStatus(JOB_OFFER_ID, JOB_APPLICATION_ID, newStatus);
    }

    @Test(expected = IllegalStatusTransitionException.class)
    public void toggleJobApplicationStatusShouldFailIfNewStatusIsNotAllowed() {
        final JobApplicationStatus newStatus = JobApplicationStatus.HIRED;

        when(repositoryMock.findByIdAndJobOfferId(JOB_APPLICATION_ID, JOB_OFFER_ID))
                .thenReturn(Optional.of(applicationMock));

        applicationService.toggleJobApplicationStatus(JOB_OFFER_ID, JOB_APPLICATION_ID, newStatus);
    }
}