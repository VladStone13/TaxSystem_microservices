package com.taxsystem.servicepayment.dto;

import com.taxsystem.servicepayment.enums.UserType;

public record UserDto(String name, String password, String passportId, UserType userType) {
}

