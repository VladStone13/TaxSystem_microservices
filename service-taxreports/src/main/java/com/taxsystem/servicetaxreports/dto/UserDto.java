package com.taxsystem.servicetaxreports.dto;

import com.taxsystem.servicetaxreports.enums.UserType;

public record UserDto(String name, String password, String passportId, UserType userType) {
}

