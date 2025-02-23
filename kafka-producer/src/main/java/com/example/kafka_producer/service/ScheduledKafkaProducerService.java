package com.example.kafka_producer.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduledKafkaProducerService {

  private final KafkaProducerService kafkaProducerService;
  private final ObjectMapper objectMapper;
  private final Random random = new Random();

  // 🔹 Definir tipos de señales vitales sin depender de `VitalSign`
  private static final String[] VITAL_SIGN_TYPES = {
      "BodyTemperature", "RespiratoryRate", "PulseRate",
      "DiastolicBloodPressure", "SystolicBloodPressure"
  };

  public ScheduledKafkaProducerService(KafkaProducerService kafkaProducerService) {
    this.kafkaProducerService = kafkaProducerService;
    this.objectMapper = new ObjectMapper();
  }

  @Scheduled(fixedRate = 60000) // Ejecuta cada 1 minuto (60,000 ms)
  public void sendRandomMessage() {
    try {
      // 🔹 Seleccionar aleatoriamente un tipo de vital sign
      String type = VITAL_SIGN_TYPES[random.nextInt(VITAL_SIGN_TYPES.length)];

      // 🔹 Generar un valor aleatorio en base al tipo
      double value = generateRandomValue(type);

      // 🔹 Asignar unidad correcta
      String unit = getUnit(type);

      // 🔹 Simular `patientId` entre 2 y 6
      int patientId = random.nextInt(5) + 2;

      // 🔹 Generar fecha de medición en formato ISO
      String measurementDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());

      // 🔹 Crear JSON del mensaje
      String message = objectMapper
          .writeValueAsString(new VitalSignMessage(type, value, unit, measurementDate, patientId));

      // 🔹 Enviar a Kafka
      kafkaProducerService.sendMessage(message);
      System.out.println("📤 Mensaje enviado a Kafka: " + message);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 🔹 Genera valores realistas en base al tipo de vital sign
  private double generateRandomValue(String type) {
    switch (type) {
      case "BodyTemperature":
        return 28.0 + (random.nextDouble() * 15.0); // 28.0 - 43.0 (Incluye valores extremos)
      case "RespiratoryRate":
        return 8 + (random.nextDouble() * 20); // 8 - 28 respiraciones por minuto
      case "PulseRate":
        return 40 + (random.nextDouble() * 100); // 40 - 140 latidos por minuto
      case "DiastolicBloodPressure":
        return 60 + (random.nextDouble() * 100); // 60 - 160 mmHg
      case "SystolicBloodPressure":
        return 90 + (random.nextDouble() * 60); // 90 - 150 mmHg
      default:
        return 0;
    }
  }

  // 🔹 Asigna unidades correctas según el tipo de vital sign
  private String getUnit(String type) {
    return switch (type) {
      case "BodyTemperature" -> "°F";
      case "RespiratoryRate", "PulseRate" -> "bpm";
      case "DiastolicBloodPressure", "SystolicBloodPressure" -> "mmHg";
      default -> "";
    };
  }

  // 🔹 Clase interna para representar el mensaje
  private record VitalSignMessage(String type, double value, String unit, String measurementDate, int patientId) {
  }
}
