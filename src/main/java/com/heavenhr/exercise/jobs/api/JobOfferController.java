package com.heavenhr.exercise.jobs.api;

import com.heavenhr.exercise.jobs.domain.JobOfferService;
import com.heavenhr.exercise.jobs.entity.JobOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/jobs")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    @Autowired
    public JobOfferController(JobOfferService recruitingService) {
        this.jobOfferService = recruitingService;
    }

    @GetMapping()
    public Collection<JobOffer> listJobOffers() {
        return jobOfferService.findAllJobOffers();
    }

    @GetMapping("/{jobOfferId}")
    public JobOffer getJobOffer(@PathVariable Long jobOfferId) {
        return jobOfferService.getJobOffer(jobOfferId);
    }

    @PostMapping()
    public ResponseEntity<?> createJobOffer(@Valid @RequestBody JobOffer jobOffer) {

        jobOfferService.createJobOffer(jobOffer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(jobOffer.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
