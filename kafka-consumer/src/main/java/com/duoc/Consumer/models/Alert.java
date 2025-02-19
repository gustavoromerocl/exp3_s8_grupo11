package com.duoc.Consumer.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vitalSignId;
    private Long patientId;
    private String patientName;
    private String fieldName;
    private Double alertValue;
    private String unit;
    private String alertLevel;
    private LocalDateTime receivedDate;



    public Alert() {
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public Long getVitalSignId() {
        return vitalSignId;
    }



    public void setVitalSignId(Long vitalSignId) {
        this.vitalSignId = vitalSignId;
    }



    public Long getPatientId() {
        return patientId;
    }



    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }



    public String getPatientName() {
        return patientName;
    }



    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }



   



    public Double getAlertValue() {
        return alertValue;
    }



    public void setAlertValue(Double alertValue) {
        this.alertValue = alertValue;
    }



    public String getUnit() {
        return unit;
    }



    public void setUnit(String unit) {
        this.unit = unit;
    }



    public String getAlertLevel() {
        return alertLevel;
    }



    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }



    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }



    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }



    public String getFieldName() {
        return fieldName;
    }



    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


   
    
    

    
}
