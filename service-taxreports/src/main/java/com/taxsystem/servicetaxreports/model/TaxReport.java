package com.taxsystem.servicetaxreports.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TaxReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String text;
    private Long userId;
    private Long inspectorId;

    public TaxReport() {

    }

    public TaxReport(String text, Long userId, Long inspectorId) {
        this.text = text;
        this.userId = userId;
        this.inspectorId = inspectorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }
}
