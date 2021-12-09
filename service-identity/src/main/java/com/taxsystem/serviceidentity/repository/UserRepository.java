package com.taxsystem.serviceidentity.repository;

import com.taxsystem.serviceidentity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
