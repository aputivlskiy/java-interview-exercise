package com.heavenhr.exercise.jobs.api;

import com.heavenhr.exercise.jobs.domain.JobApplicationService;
import com.heavenhr.exercise.jobs.entity.JobApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs/{jobOfferId}/applications")
public class JobApplicationController {

    private final JobApplicationService applicationService;

    @Autowired
    public JobApplicationController(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping()
    public Collection<JobApplicationListItem> listJobApplications(@PathVariable Long jobOfferId) {
        return applicationService.findAllApplicationsForJob(jobOfferId)
                .stream().map(JobApplicationListItem::new).collect(Collectors.toList());
    }

    @GetMapping("/{jobAppId}")
    public JobApplication getApplication(@PathVariable Long jobOfferId, @PathVariable Long jobAppId) {
        return applicationService.getJobApplication(jobOfferId, jobAppId);
    }

    @PostMapping()
    public ResponseEntity<?> createJobApplication(@PathVariable Long jobOfferId,
                                                  @Valid @RequestBody JobApplication jobApplication) {

        applicationService.createJobApplication(jobOfferId, jobApplication);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(jobApplication.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{jobAppId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleStatus(@PathVariable Long jobOfferId, @PathVariable Long jobAppId,
                             @Valid @RequestBody ToggleStatusPatch statusPatch) {
        applicationService.toggleJobApplicationStatus(jobOfferId, jobAppId, statusPatch.getStatus());
    }

}
