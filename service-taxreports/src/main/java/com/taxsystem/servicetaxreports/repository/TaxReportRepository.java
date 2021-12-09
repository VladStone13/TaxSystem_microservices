package com.taxsystem.servicetaxreports.repository;

import com.taxsystem.servicetaxreports.model.TaxReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxReportRepository extends JpaRepository<TaxReport, Long> {
}
