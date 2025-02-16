package com.duoc.medical.services;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.duoc.medical.dto.VitalSignDTO;
import com.duoc.medical.models.Patient;
import com.duoc.medical.models.VitalSign;
import com.duoc.medical.models.VitalSignAlertMessage;
import com.duoc.medical.models.VitalSignLevel;
import com.duoc.medical.models.VitalSignType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class KafkaConsumerService {

  private final KafkaProducerService kafkaProducerService;
  private final ObjectMapper objectMapper;

  @Autowired
  private PatientService patientService;

  public KafkaConsumerService(KafkaProducerService kafkaProducerService) {
    this.kafkaProducerService = kafkaProducerService;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  @KafkaListener(topics = "signos_vitales", groupId = "signos_vitales_group")
  public void consumeVitalSigns(ConsumerRecord<String, String> record) {
    try {
      // Convertir JSON en DTO
      VitalSignDTO vitalSignDTO = objectMapper.readValue(record.value(), VitalSignDTO.class);

      System.out.println("📥 Señal vital recibida: " + objectMapper.writeValueAsString(vitalSignDTO));

      // Buscar paciente
      Optional<Patient> patientOptional = patientService.getById(vitalSignDTO.getPatientId());
      if (patientOptional.isEmpty()) {
        System.out.println("❌ Paciente no encontrado para ID: " + vitalSignDTO.getPatientId());
        return;
      }

      Patient patient = patientOptional.get();

      // Convertir DTO a `VitalSign`
      VitalSign vitalSign = new VitalSign();
      vitalSign.setId(vitalSignDTO.getId());
      vitalSign.setType(VitalSignType.valueOf(vitalSignDTO.getType())); // Convertir el tipo a ENUM
      vitalSign.setValue(vitalSignDTO.getValue());
      vitalSign.setUnit(vitalSignDTO.getUnit());
      vitalSign.setMeasurementDate(vitalSignDTO.getMeasurementDate());
      vitalSign.setHist(vitalSignDTO.getHist()); // Asignar `hist`
      vitalSign.setPatient(patient); // Asegurar que `patient` está asignado correctamente

      // Detectar anomalía
      if (isAnomalous(vitalSign)) {
        sendAlert(vitalSign);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean isAnomalous(VitalSign vitalSign) {
    if (vitalSign.getVitalSignLevel() != VitalSignLevel.NORMAL) {
      try {
        // 🔹 Convertir `VitalSign` en JSON para depuración
        String vitalSignJson = objectMapper.writeValueAsString(vitalSign);
        System.out.println("⚠️ Anomalía detectada en la señal vital: " + vitalSignJson);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }

  private void sendAlert(VitalSign vitalSign) {
    try {
      if (vitalSign == null) {
        System.out.println("❌ No se puede enviar alerta, `vitalSign` es nulo.");
        return;
      }

      if (vitalSign.getPatient() == null) {
        System.out.println("❌ Paciente no encontrado en `vitalSign`, no se generará alerta.");
        return;
      }

      // Crear mensaje de alerta
      VitalSignAlertMessage alert = new VitalSignAlertMessage(
          vitalSign.getId(),
          vitalSign.getPatient().getId(),
          vitalSign.getPatient().getFirstName() + " " + vitalSign.getPatient().getLastName(),
          vitalSign.getType().toString(),
          vitalSign.getValue(),
          vitalSign.getUnit(),
          vitalSign.getVitalSignLevel().toString());

      // Convertir alerta a JSON
      String jsonMessage = objectMapper.writeValueAsString(alert);

      // Enviar al tópico de alertas
      kafkaProducerService.sendAlert(jsonMessage);
      System.out.println("🚨 Alerta generada y enviada: " + jsonMessage);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
