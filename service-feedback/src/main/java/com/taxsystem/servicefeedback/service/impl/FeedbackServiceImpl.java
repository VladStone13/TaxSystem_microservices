package com.taxsystem.servicefeedback.service.impl;

import com.taxsystem.servicefeedback.dto.FeedbackFullInfoDto;
import com.taxsystem.servicefeedback.dto.TaxReportResponseDto;
import com.taxsystem.servicefeedback.model.Feedback;
import com.taxsystem.servicefeedback.repository.FeedbackRepository;
import com.taxsystem.servicefeedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final String taxReportUrlAdress = "http://service-taxreports:8081/report";

    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback getFeedbackById(long id) throws IllegalArgumentException {
        final Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);

        if (optionalFeedback.isPresent()) {
            return optionalFeedback.get();
        }
        else {
            throw new IllegalArgumentException("Invalid feedback id");
        }
    }

    private boolean checkForCorrectness(Long texReportId) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(texReportId);

        final ResponseEntity<TaxReportResponseDto> taxReportResponseDtoResponseEntity = restTemplate
                .exchange(taxReportUrlAdress + "/" + texReportId,
                        HttpMethod.GET, userRequest, TaxReportResponseDto.class);

        return taxReportResponseDtoResponseEntity.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    @Override
    public long createFeedback(Long taxReportId, int userMark, int inspectorMark,
                               String complaint) throws IllegalArgumentException {
        final Feedback feedback = new Feedback(taxReportId, userMark, inspectorMark, complaint);

        if (!checkForCorrectness(taxReportId)) {
            throw new IllegalArgumentException("Bad request");
        }
        else {
            final Feedback savedFeedback = feedbackRepository.save(feedback);
            return savedFeedback.getId();
        }
    }

    @Override
    public void updateFeedback(long id, Long taxReportId, int userMark, int inspectorMark,
                               String complaint) throws IllegalArgumentException {
        final Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);

        if (optionalFeedback.isEmpty()) {
            throw new IllegalArgumentException("Invalid feedback id");
        }

        final Feedback feedback = optionalFeedback.get();

        if (taxReportId != null && taxReportId != -1) {
            if(!checkForCorrectness(taxReportId)) {
                throw new IllegalArgumentException("Invalid user id");
            }

            feedback.setTaxReportId(taxReportId);
        }
        if (userMark != 0) {
            feedback.setUserMark(userMark);
        }
        if (inspectorMark != 0) {
            feedback.setInspectorMark(inspectorMark);
        }
        if (complaint != null && !complaint.isBlank()) feedback.setComplaint(complaint);

        feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(long id) {
        feedbackRepository.deleteById(id);
    }

    private TaxReportResponseDto getTaxReportById(long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<TaxReportResponseDto> taxReportResponseDtoResponseEntity = restTemplate
                .exchange(taxReportUrlAdress + "/" + id,
                        HttpMethod.GET, userRequest, TaxReportResponseDto.class);

        return taxReportResponseDtoResponseEntity.getBody();
    }

    private FeedbackFullInfoDto feedbackToFullInfo(Feedback feedback) {
        final TaxReportResponseDto taxReportResponseDto
                = getTaxReportById(feedback.getTaxReportId());
        final String text = taxReportResponseDto.text();
        final String userName = taxReportResponseDto.userName();
        final String inspectorName = taxReportResponseDto.inspectorName();
        final int userMark = feedback.getUserMark();
        final int inspectorMark = feedback.getInspectorMark();
        final String complaint = feedback.getComplaint();

        return new FeedbackFullInfoDto(text, userName, inspectorName, userMark,
                inspectorMark, complaint);
    }

    @Override
    public List<FeedbackFullInfoDto> getAllFullInfo() {
        List<Feedback> feedbacks = getAll();
        List<FeedbackFullInfoDto> feedbackFullInfoDtos = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            feedbackFullInfoDtos.add(feedbackToFullInfo(feedback));
        }

        return feedbackFullInfoDtos;
    }

    @Override
    public FeedbackFullInfoDto getFullInfoById(long id) throws IllegalArgumentException {
        try {
            Feedback feedback = getFeedbackById(id);
            return feedbackToFullInfo(feedback);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
