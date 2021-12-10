package com.taxsystem.servicefeedback.service;

import com.taxsystem.servicefeedback.dto.FeedbackFullInfoDto;
import com.taxsystem.servicefeedback.model.Feedback;
import com.taxsystem.servicefeedback.repository.FeedbackRepository;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAll();
    Feedback getFeedbackById(long id) throws IllegalArgumentException;
    long createFeedback(Long taxReportId, int userMark, int inspectorMark, String complaint)
            throws IllegalArgumentException;
    void updateFeedback(long id, Long taxReportId, int userMark, int inspectorMark, String complaint)
            throws IllegalArgumentException;
    void deleteFeedback(long id);
    List<FeedbackFullInfoDto> getAllFullInfo();
    FeedbackFullInfoDto getFullInfoById(long id) throws IllegalArgumentException;
}
