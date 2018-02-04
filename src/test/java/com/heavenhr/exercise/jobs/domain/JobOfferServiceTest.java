package com.heavenhr.exercise.jobs.domain;

import com.heavenhr.exercise.jobs.data.JobOfferRepository;
import com.heavenhr.exercise.jobs.entity.JobOffer;
import com.heavenhr.exercise.jobs.error.JobOfferNotFoundException;
import com.heavenhr.exercise.jobs.error.JobTitleNotUniqueException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobOfferServiceTest {

    private static final Long JOB_OFFER_ID = 1L;
    private static final String TITLE = "Software Developer";
    private static final LocalDate START_DATE = LocalDate.of(2018, 2, 10);

    @Mock
    private JobOffer jobOfferMock;

    @Mock
    private JobOfferRepository repositoryMock;

    @InjectMocks
    private JobOfferService offerService;

    @Before
    public void setUp() {
        when(jobOfferMock.getId()).thenReturn(JOB_OFFER_ID);
        when(jobOfferMock.getJobTitle()).thenReturn(TITLE);
        when(jobOfferMock.getStartDate()).thenReturn(START_DATE);
    }

    @Test
    public void getJobOfferShouldReturnOfferIfExists() {
        when(repositoryMock.findOne(JOB_OFFER_ID)).thenReturn(jobOfferMock);

        JobOffer jobOffer = offerService.getJobOffer(JOB_OFFER_ID);
        assertThat(jobOffer.getJobTitle()).isEqualTo(TITLE);
        assertThat(jobOffer.getStartDate()).isEqualTo(START_DATE);
    }

    @Test(expected = JobOfferNotFoundException.class)
    public void getJobOfferShouldFailIfNotExists() {
        offerService.getJobOffer(2L);
    }

    @Test
    public void findAllJobOffersShouldReturnOffers() {
        when(repositoryMock.findAll()).thenReturn(Collections.singletonList(jobOfferMock));

        Collection<JobOffer> jobOffers = offerService.findAllJobOffers();
        assertThat(jobOffers.size()).isEqualTo(1);
        assertThat(jobOffers).containsOnly(jobOfferMock);
    }

    @Test
    public void createJobOfferShouldSaveOffer() {
        when(repositoryMock.findByJobTitleIgnoreCase(TITLE)).thenReturn(Optional.empty());
        when(repositoryMock.save(jobOfferMock)).thenReturn(jobOfferMock);

        JobOffer jobOffer = offerService.createJobOffer(jobOfferMock);

        assertThat(jobOffer).isEqualTo(jobOfferMock);
        verify(repositoryMock).findByJobTitleIgnoreCase(TITLE);
        verify(repositoryMock).save(jobOfferMock);
    }

    @Test(expected = JobTitleNotUniqueException.class)
    public void createJobOfferShouldFailIfTitleIsNotUnique() {
        when(repositoryMock.findByJobTitleIgnoreCase(TITLE)).thenReturn(Optional.of(jobOfferMock));

        offerService.createJobOffer(jobOfferMock);
    }
}