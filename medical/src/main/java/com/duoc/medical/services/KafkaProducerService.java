package com.duoc.medical.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

  private static final String ALERT_TOPIC = "alertas";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void sendAlert(String message) {
    kafkaTemplate.send(ALERT_TOPIC, message);
    System.out.println("Mensaje de alerta enviado a Kafka: " + message);
  }
}
