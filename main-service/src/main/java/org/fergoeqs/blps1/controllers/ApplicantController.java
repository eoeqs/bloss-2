package org.fergoeqs.blps1.controllers;

import jakarta.validation.Valid;
import org.fergoeqs.blps1.dto.ApplicantRequest;
import org.fergoeqs.blps1.dto.ApplicantResponse;
import org.fergoeqs.blps1.dto.ResumeResponse;
import org.fergoeqs.blps1.models.applicantdb.Applicant;
import org.fergoeqs.blps1.models.applicantdb.Resume;
import org.fergoeqs.blps1.services.ApplicantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApplicantResponse> createApplicant(
            @Valid @RequestBody ApplicantRequest request) {
        ApplicantResponse response = applicantService.createApplicant(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteApplicant(@PathVariable Long id) {
        applicantService.deleteApplicant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Applicant> getApplicantById(@PathVariable Long id) {
        return applicantService.getApplicantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/resumes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resume> addResume(@PathVariable Long id, @Valid @RequestBody Resume resume) {
        Resume createdResume = applicantService.addResume(id, resume);
        return ResponseEntity.ok(createdResume);
    }

    @GetMapping("/{id}/resumes")
    public ResponseEntity<?> getResumesByApplicantId(@PathVariable Long id,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ResumeResponse> resumes = applicantService.getResumesByApplicantId(id, PageRequest.of(page, size));
        return ResponseEntity.ok(resumes.getContent());
    }
}