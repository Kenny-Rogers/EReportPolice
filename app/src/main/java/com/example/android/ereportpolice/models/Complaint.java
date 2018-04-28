package com.example.android.ereportpolice.models;

/**
 * Created by krogers on 4/26/18.
 */

public class Complaint {
    private long id;
    private String nature_of_issue;
    private String complainant_id;
    private String type_issue;
    private String date_time_of_report;

    public Complaint() {
    }

    public Complaint(long id, String nature_of_issue, String complainant_id, String type_issue, String date_time_of_report) {
        this.id = id;
        this.nature_of_issue = nature_of_issue;
        this.complainant_id = complainant_id;
        this.type_issue = type_issue;
        this.date_time_of_report = date_time_of_report;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNature_of_issue() {
        return nature_of_issue;
    }

    public void setNature_of_issue(String nature_of_issue) {
        this.nature_of_issue = nature_of_issue;
    }

    public String getComplainant_id() {
        return complainant_id;
    }

    public void setComplainant_id(String complainant_id) {
        this.complainant_id = complainant_id;
    }

    public String getType_issue() {
        return type_issue;
    }

    public void setType_issue(String type_issue) {
        this.type_issue = type_issue;
    }

    public String getDate_time_of_report() {
        return date_time_of_report;
    }

    public void setDate_time_of_report(String date_time_of_report) {
        this.date_time_of_report = date_time_of_report;
    }
}
