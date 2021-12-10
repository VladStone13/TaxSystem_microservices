package com.taxsystem.servicefeedback.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    Long id;

    private Long taxReportId;
    private int userMark;
    private int inspectorMark;
    private String complaint;

    public Feedback() {
    }

    public Feedback(Long taxReportId, int userMark, int inspectorMark, String complaint) {
        this.taxReportId = taxReportId;
        this.userMark = userMark;
        this.inspectorMark = inspectorMark;
        this.complaint = complaint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaxReportId() {
        return taxReportId;
    }

    public void setTaxReportId(Long taxReportId) {
        this.taxReportId = taxReportId;
    }

    public int getUserMark() {
        return userMark;
    }

    public void setUserMark(int userMark) {
        this.userMark = userMark;
    }

    public int getInspectorMark() {
        return inspectorMark;
    }

    public void setInspectorMark(int inspectorMark) {
        this.inspectorMark = inspectorMark;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}
