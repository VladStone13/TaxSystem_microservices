package com.taxsystem.servicefeedback.dto;

public record FeedbackFullInfoDto(String text, String userName, String inspectorName,
                                  int userMark, int inspectorMark, String complaint) {
}
