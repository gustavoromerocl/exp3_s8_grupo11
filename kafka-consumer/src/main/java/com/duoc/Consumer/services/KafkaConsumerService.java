package com.duoc.Consumer.services;

import java.time.LocalDateTime;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.duoc.Consumer.models.Alert;
import com.duoc.Consumer.repositories.AlertRepository;


@Service
public class KafkaConsumerService {
    
    @Autowired AlertRepository alertRepository;
 
    @KafkaListener(topics = "alertas", groupId = "group_id")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String key = record.key();
            String message = record.value();  
            System.out.println("Consumed message: " + message + "; Consumed Key: " + key);
            
            var alert = new Alert();
            alert.setReceivedDate(LocalDateTime.now());
            alert.setMessage(message);
            alertRepository.save(alert);
        } catch(Exception ex) {
            System.out.println("Alert Error: " + ex.getMessage());
        }
    }
}
