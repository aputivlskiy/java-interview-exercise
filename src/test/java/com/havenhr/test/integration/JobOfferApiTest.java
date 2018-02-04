package com.havenhr.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heavenhr.exercise.jobs.Application;
import com.heavenhr.exercise.jobs.data.JobApplicationRepository;
import com.heavenhr.exercise.jobs.data.JobOfferRepository;
import com.heavenhr.exercise.jobs.entity.JobApplication;
import com.heavenhr.exercise.jobs.entity.JobOffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class JobOfferApiTest {

    private static final String OFFERS_URI = "/jobs";

    private static final DateTimeFormatter jsonDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    private ObjectMapper objectMapper;

    private TestData testData;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JobOfferRepository offerRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Before
    public void setUp() {
        testData = new TestData();

        applicationRepository.deleteAll();
        offerRepository.deleteAll();
    }

    @Test
    public void testGetJobOffer() throws Exception {
        JobOffer jobOffer = offerRepository.save(testData.getOffer1());
        JobApplication app = testData.getApplication1();
        app.setJobOffer(jobOffer);
        applicationRepository.save(app);

        ResultActions result = mvc.perform(get(OFFERS_URI + "/" + jobOffer.getId()));

        validateStatus(result, status().isOk());
        validateOffer(result, "$", jobOffer, 1);
    }

    @Test
    public void testListJobOffers() throws Exception {
        JobOffer jobOffer1 = offerRepository.save(testData.getOffer1());
        JobOffer jobOffer2 = offerRepository.save(testData.getOffer2());
        JobApplication app1 = testData.getApplication1();
        app1.setJobOffer(jobOffer1);
        applicationRepository.save(app1);

        ResultActions result = mvc.perform(get(OFFERS_URI));

        validateStatus(result, status().isOk());
        result.andExpect(jsonPath("$.length()").value(2));
        validateOffer(result, "$[0]", jobOffer1, 1);
        validateOffer(result, "$[1]", jobOffer2, 0);
    }

    @Test
    public void testCreateJobOffer() throws Exception {
        JobOffer jobOffer = testData.getOffer1();

        MvcResult result = mvc.perform(post(OFFERS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jobOffer)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        JobOffer createdJobOffer = offerRepository.findAll().get(0);

        // TODO extract new offer id from location, assert equals to the one in db
        assertThat(createdJobOffer.getJobTitle()).isEqualTo(jobOffer.getJobTitle());
        assertThat(createdJobOffer.getStartDate()).isEqualTo(jobOffer.getStartDate());
        assertThat(createdJobOffer.getNumberOfApplications()).isEqualTo(0);
    }

    @Test
    public void testGetNonExistingJobOffer() throws Exception {
        mvc.perform(get(String.format(OFFERS_URI + "/%s", 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateJobOfferWithExistingTitle() throws Exception {
        offerRepository.save(testData.getOffer1());

        JobOffer newJobOffer = new JobOffer(testData.getOffer1().getJobTitle(), testData.getOffer1().getStartDate());

        mvc.perform(post(OFFERS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJobOffer)))
                .andExpect(status().isBadRequest());
    }

    private void validateStatus(ResultActions result, ResultMatcher status) throws Exception {
        result.andExpect(status)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private void validateOffer(ResultActions result, String base, JobOffer jobOffer, int applications) throws Exception {
        result
                .andExpect(jsonPath(base + ".id").value(jobOffer.getId()))
                .andExpect(jsonPath(base + ".jobTitle").value(jobOffer.getJobTitle()))
                .andExpect(jsonPath(base + ".startDate").value(jsonDateFormatter.format(jobOffer.getStartDate())))
                .andExpect(jsonPath(base + ".numberOfApplications").value(applications));
    }
}
