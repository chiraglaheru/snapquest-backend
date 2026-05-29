package com.snapquest.snapquest.controller;

import com.snapquest.snapquest.model.Submission;
import com.snapquest.snapquest.service.SubmissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<?> submit(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SubmitRequest request) {

        Submission submission = submissionService.submit(
                userDetails.getUsername(),
                request.questId(),
                request.imageBase64());

        return ResponseEntity.ok(Map.of(
                "submissionId", submission.getId(),
                "verified",submission.getVerified(),
                "note",submission.getVerificationNote(),
                "submittedAt",submission.getSubmittedAt()
        ));
    }



    @GetMapping("/my")
    public ResponseEntity<List<Submission>> mySubmissions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                submissionService.getByUser(userDetails.getUsername()));
    }

    @GetMapping("/quest/{questId}")
    public ResponseEntity<List<Submission>> byQuest(@PathVariable Long questId) {
        return ResponseEntity.ok(submissionService.getByQuest(questId));
    }

    record SubmitRequest(
            @NotNull Long questId,
            @NotBlank String imageBase64) {}
}