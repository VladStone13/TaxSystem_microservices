package com.taxsystem.servicetaxreports.service.impl;

import com.taxsystem.servicetaxreports.dto.TaxReportResponseDto;
import com.taxsystem.servicetaxreports.dto.UserDto;
import com.taxsystem.servicetaxreports.enums.UserType;
import com.taxsystem.servicetaxreports.model.TaxReport;
import com.taxsystem.servicetaxreports.repository.TaxReportRepository;
import com.taxsystem.servicetaxreports.service.TaxReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaxReportServiceImpl implements TaxReportService {
    private final String userUrlAdress ="http://service-identity:8080/user";

    private final TaxReportRepository taxReportRepository;

    @Override
    public List<TaxReport> getAll() {
        return taxReportRepository.findAll();
    }

    @Override
    public TaxReport getTaxReportById(long id) throws IllegalArgumentException {
        final Optional<TaxReport> optionalTaxReport = taxReportRepository.findById(id);

        if (optionalTaxReport.isPresent()) {
            return optionalTaxReport.get();
        }
        else {
            throw new IllegalArgumentException("Invalid tax report id");
        }
    }

    private boolean checkForCorrectness(Long userId, UserType userType) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(userId);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/dto/" + userId,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND
                && userResponse.getBody().userType() == userType;
    }

    @Override
    public long createTaxReport(String text, Long userId, Long inspectorId)
            throws IllegalArgumentException {
        final TaxReport taxReport = new TaxReport(text, userId, inspectorId);

        if (!checkForCorrectness(userId, UserType.USER)
                || !checkForCorrectness(inspectorId, UserType.INSPECTOR)) {
            throw new IllegalArgumentException("Bad request");
        }
        else {
            final TaxReport savedTaxReport = taxReportRepository.save(taxReport);
            return savedTaxReport.getId();
        }

    }

    @Override
    public void updateTaxReport(long id, String text, Long userId, Long inspectorId)
            throws IllegalArgumentException {
        final Optional<TaxReport> optionalTaxReport = taxReportRepository.findById(id);

        if (optionalTaxReport.isEmpty()) {
            throw new IllegalArgumentException("Invalid tax report id");
        }

        final TaxReport taxReport = optionalTaxReport.get();

        if (text != null && !text.isBlank()) taxReport.setText(text);
        if (userId != null && userId != -1) {
            if (!checkForCorrectness(userId, UserType.USER)) {
                throw new IllegalArgumentException("Invalid user id");
            }

            taxReport.setUserId(userId);
        }
        if (inspectorId != null && inspectorId != -1) {
            if (!checkForCorrectness(inspectorId, UserType.INSPECTOR)) {
                throw new IllegalArgumentException("Invalid inspector id");
            }

            taxReport.setUserId(userId);
        }

        taxReportRepository.save(taxReport);
    }

    @Override
    public void deleteTaxReport(long id) {
        taxReportRepository.deleteById(id);
    }

    private String getNameById(long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/" +id,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getBody().name();
    }

    private TaxReportResponseDto taxReportToResponse(TaxReport taxReport) {
        final String text = taxReport.getText();
        final String userName = getNameById(taxReport.getUserId());
        final String inspectorName = getNameById(taxReport.getInspectorId());

        return new TaxReportResponseDto(text, userName, inspectorName);
    }

    @Override
    public List<TaxReportResponseDto> getAllResponse() {
        List<TaxReport> taxReports = getAll();
        List<TaxReportResponseDto> taxReportResponseDtos = new ArrayList<>();

        for(TaxReport taxReport: taxReports) {
            taxReportResponseDtos.add(taxReportToResponse(taxReport));
        }

        return taxReportResponseDtos;
    }

    @Override
    public TaxReportResponseDto getTaxResponseDtoById(long id)
            throws IllegalArgumentException {
        try {
            TaxReport taxReport = getTaxReportById(id);
            return taxReportToResponse(taxReport);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
