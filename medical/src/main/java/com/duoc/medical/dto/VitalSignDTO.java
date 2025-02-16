package com.duoc.medical.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VitalSignDTO {

  private Long id;
  private String type;
  private Double value;
  private String unit;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime measurementDate;

  @JsonProperty("patientId") // Mapea `patientId` del JSON
  private Long patientId;

  private Boolean hist; // Se incluye `hist` para no perder datos

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public LocalDateTime getMeasurementDate() {
    return measurementDate;
  }

  public void setMeasurementDate(LocalDateTime measurementDate) {
    this.measurementDate = measurementDate;
  }

  public Long getPatientId() {
    return patientId;
  }

  public void setPatientId(Long patientId) {
    this.patientId = patientId;
  }

  public Boolean getHist() {
    return hist;
  }

  public void setHist(Boolean hist) {
    this.hist = hist;
  }
}
