package com.portfolio.jgsilveira.customersportfolio.model;

import java.util.Date;

public class ReportFilters {

    private String name;

    private Date bornedFrom;

    private Date bornedTo;

    private Date startDate;

    private Date endDate;

    private String state;

    private String document;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBornedFrom() {
        return bornedFrom;
    }

    public void setBornedFrom(Date bornedFrom) {
        this.bornedFrom = bornedFrom;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getBornedTo() {
        return bornedTo;
    }

    public void setBornedTo(Date bornedTo) {
        this.bornedTo = bornedTo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

}
