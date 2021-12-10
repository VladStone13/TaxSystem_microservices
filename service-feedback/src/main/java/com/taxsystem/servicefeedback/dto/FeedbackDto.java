package com.taxsystem.servicefeedback.dto;

public record FeedbackDto(Long taxReportId, int userMark, int inspectorMark, String complaint) {
}
