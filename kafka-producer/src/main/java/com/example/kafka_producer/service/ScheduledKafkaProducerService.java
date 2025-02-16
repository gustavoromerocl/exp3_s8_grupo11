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

  public ScheduledKafkaProducerService(KafkaProducerService kafkaProducerService) {
    this.kafkaProducerService = kafkaProducerService;
    this.objectMapper = new ObjectMapper();
  }

  @Scheduled(fixedRate = 60000) // Ejecuta cada 1 minuto (60,000 ms)
  public void sendRandomMessage() {
    try {
      // Generar valores aleatorios
      double temperature = 27 + (random.nextDouble() * 3); // Rango entre 36.0 - 39.0
      int patientId = random.nextInt(5) + 2; // IDs entre 1 - 5
      String measurementDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());

      // Crear JSON del mensaje
      String message = objectMapper
          .writeValueAsString(new VitalSignMessage("BodyTemperature", temperature, "°F", measurementDate, patientId));

      // Enviar a Kafka
      kafkaProducerService.sendMessage(message);

      System.out.println("Mensaje enviado a Kafka: " + message);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Clase interna para el JSON del mensaje
  private record VitalSignMessage(String type, double value, String unit, String measurementDate, int patientId) {
  }
}
