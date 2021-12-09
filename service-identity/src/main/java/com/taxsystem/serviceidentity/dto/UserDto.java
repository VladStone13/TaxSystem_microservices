package com.taxsystem.serviceidentity.dto;

import com.taxsystem.serviceidentity.model.enums.UserType;

public record UserDto(String name, String password, String passportId, int userType) {
}
