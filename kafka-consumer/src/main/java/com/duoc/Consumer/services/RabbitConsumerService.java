package com.duoc.Consumer.services;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.Consumer.dto.VitalSignAlertMessage;
import com.duoc.Consumer.models.Alert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class RabbitConsumerService {
    
    @Autowired AlertService alertService;
    @Autowired RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = "alert")
    public void receiveAlert(String message) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            System.out.println("⚠️ Alerta Recibida recibida: " + message);

            VitalSignAlertMessage alertMessage = objectMapper.readValue(message, VitalSignAlertMessage.class);

            var alert = new Alert();
            
            alert.setVitalSignId(alertMessage.getVitalSignId());
            alert.setPatientId(alertMessage.getPatientId());
            alert.setPatientName(alertMessage.getPatientName());
            alert.setFieldName(alertMessage.getField());
            alert.setAlertValue(alertMessage.getValue());
            alert.setUnit(alertMessage.getUnit());
            alert.setAlertLevel(alertMessage.getLevel());
            
            alert.setReceivedDate(LocalDateTime.now());
            
            alertService.Save(alert);


        } catch(Exception ex) {
            System.out.println("Alert Error: " + ex.getMessage());
        }
        
    }
}
