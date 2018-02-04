package com.heavenhr.exercise.jobs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class JobOffer {

    private static final int TITLE_MAX_LENGTH = 128;

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Job title is required")
    @Length(max = TITLE_MAX_LENGTH, message = "Job title length is exceeded")
    @Column(nullable = false, length = TITLE_MAX_LENGTH, unique = true)
    private String jobTitle;

    @NotNull(message = "Job start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    @Formula("(select count(*) from APPLICATION A where A.job_offer_id=id)")
    private int numberOfApplications;

    public JobOffer() {
    }

    public JobOffer(String jobTitle, LocalDate startDate) {
        this.jobTitle = jobTitle;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    @JsonFormat(pattern = "dd.MM.yyyy")
    public LocalDate getStartDate() {
        return startDate;
    }

    public int getNumberOfApplications() {
        return numberOfApplications;
    }
}
