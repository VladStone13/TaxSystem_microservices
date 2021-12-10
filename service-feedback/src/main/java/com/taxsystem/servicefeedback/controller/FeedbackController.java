package com.taxsystem.servicefeedback.controller;

import com.taxsystem.servicefeedback.dto.FeedbackDto;
import com.taxsystem.servicefeedback.dto.FeedbackFullInfoDto;
import com.taxsystem.servicefeedback.dto.TaxReportResponseDto;
import com.taxsystem.servicefeedback.model.Feedback;
import com.taxsystem.servicefeedback.service.impl.FeedbackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/feedback")
@RestController
public class FeedbackController {
    private final FeedbackServiceImpl feedbackService;

    @GetMapping
    public ResponseEntity<List<Feedback>> getAll() {
        final List<Feedback> feedbacks = feedbackService.getAll();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getById(@PathVariable long id) {
        try {
            Feedback feedback = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(feedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> change(@RequestBody FeedbackDto feedbackDto) {
        final long taxReportId = feedbackDto.taxReportId();
        final int userMark = feedbackDto.userMark();
        final int inspectorMark = feedbackDto.inspectorMark();
        final String complaint = feedbackDto.complaint();

        try {
            final long feedbackId = feedbackService.createFeedback(taxReportId, userMark,
                    inspectorMark, complaint);
            final String feedbackUri = "/feedback/" + feedbackId;

            return ResponseEntity.created(URI.create(feedbackUri)).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> change(@PathVariable long id,
                                       @RequestBody FeedbackDto feedbackDto) {
        final long taxReportId;
        if (feedbackDto.taxReportId() == null) {
            taxReportId = -1;
        }
        else {
            taxReportId = feedbackDto.taxReportId();
        }

        final int userMark = feedbackDto.userMark();
        final int inspectorMark = feedbackDto.inspectorMark();
        final String complaint = feedbackDto.complaint();

        try {
            feedbackService.updateFeedback(id, taxReportId, userMark,
                    inspectorMark, complaint);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<List<FeedbackFullInfoDto>> getAllInfo() {
        final List<FeedbackFullInfoDto> feedbackFullInfoDtos = feedbackService.getAllFullInfo();
        return ResponseEntity.ok(feedbackFullInfoDtos);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<FeedbackFullInfoDto> getInfoById(@PathVariable long id) {
        try {
            FeedbackFullInfoDto feedbackFullInfoDto = feedbackService.getFullInfoById(id);
            return ResponseEntity.ok(feedbackFullInfoDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
