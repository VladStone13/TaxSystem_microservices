package com.taxsystem.servicetaxreports.controller;

import com.taxsystem.servicetaxreports.dto.TaxReportDto;
import com.taxsystem.servicetaxreports.dto.TaxReportResponseDto;
import com.taxsystem.servicetaxreports.service.impl.TaxReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/report")
@RestController
public class TaxReportController {
    private final TaxReportServiceImpl taxReportService;

    @GetMapping
    public ResponseEntity<List<TaxReportResponseDto>> getAll() {
        final List<TaxReportResponseDto> taxReportResponseDtos = taxReportService.getAllResponse();
        return ResponseEntity.ok(taxReportResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxReportResponseDto> getById(@PathVariable long id) {
        try {
            TaxReportResponseDto taxReportResponseDto = taxReportService.getTaxResponseDtoById(id);

            return ResponseEntity.ok(taxReportResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody TaxReportDto taxReportDto) {
        final String text = taxReportDto.text();
        final long userId = taxReportDto.userId();
        final long inspectorId = taxReportDto.inspectorId();

        try {
            final long taxReportId = taxReportService.createTaxReport(text, userId, inspectorId);
            final String taxReportUri = "/report/" + taxReportId;

            return ResponseEntity.created(URI.create(taxReportUri)).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> change(@PathVariable long id, @RequestBody TaxReportDto taxReportDto) {
        final String text = taxReportDto.text();
        final long userId;
        if (taxReportDto.userId() == null) {
            userId = -1;
        }
        else {
            userId = taxReportDto.userId();
        }

        final long inspectorId;
        if (taxReportDto.inspectorId() == null) {
            inspectorId = -1;
        }
        else {
           inspectorId = taxReportDto.inspectorId();
        }

        try {
            taxReportService.updateTaxReport(id, text, userId, inspectorId);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        taxReportService.deleteTaxReport(id);

        return ResponseEntity.noContent().build();
    }

}
