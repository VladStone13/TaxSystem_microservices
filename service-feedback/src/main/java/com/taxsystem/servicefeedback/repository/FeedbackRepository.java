package com.taxsystem.servicefeedback.repository;

import com.taxsystem.servicefeedback.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
