package com.heavenhr.exercise.jobs.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "APPLICATION", indexes = {
        @Index(name = "idx_uniq_email_per_offer", columnList = "job_offer_id, email", unique = true)
})
public class JobApplication {

    private static final int RESUME_MAX_LENGTH = 512;
    private static final int EMAIL_MAX_LENGTH = 128;

    @ManyToOne
    private JobOffer jobOffer;

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Length(max = EMAIL_MAX_LENGTH, message = "Email max length is exceeded")
    @Column(length = EMAIL_MAX_LENGTH, nullable = false)
    private String email;

    @NotBlank(message = "Resume text is required")
    @Length(max = RESUME_MAX_LENGTH, message = "Resume max length is exceeded")
    @Lob
    @Column(length = RESUME_MAX_LENGTH, nullable = false)
    private String resume;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private JobApplicationStatus status;

    public JobApplication() {
    }

    public JobApplication(String email, String resume) {
        this.email = email;
        this.resume = resume;
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(JobOffer jobOffer) {
        this.jobOffer = jobOffer;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getResume() {
        return resume;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }
}
