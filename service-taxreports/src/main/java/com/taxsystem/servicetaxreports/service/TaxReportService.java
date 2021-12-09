package com.taxsystem.servicetaxreports.service;

import com.taxsystem.servicetaxreports.dto.TaxReportResponseDto;
import com.taxsystem.servicetaxreports.model.TaxReport;

import java.util.List;

public interface TaxReportService {
    List<TaxReport> getAll();
    TaxReport getTaxReportById(long id) throws IllegalArgumentException;
    long createTaxReport(String text, Long userId, Long inspectorId) throws IllegalArgumentException;
    void updateTaxReport(long id, String text, Long userId, Long inspectorId)
            throws IllegalArgumentException;
    void deleteTaxReport(long id);
    List<TaxReportResponseDto> getAllResponse();
    TaxReportResponseDto getTaxResponseDtoById(long id) throws IllegalArgumentException;
}
