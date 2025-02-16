package com.duoc.Consumer.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.Consumer.models.Alert;
import com.duoc.Consumer.repositories.AlertRepository;

@Service
public class RabbitConsumerService {

    @Autowired AlertRepository alertRepository;
    @Autowired RabbitTemplate rabbitTemplate;
    
    @RabbitListener(queues = "alert")
    public void receiveAlert(String message) {

        try {
            System.out.println("Alert: " + message);

            var alert = new Alert();
            alert.setReceivedDate(LocalDateTime.now());
            alert.setMessage(message);
            alertRepository.save(alert);
        } catch(Exception ex) {
            System.out.println("Alert Error: " + ex.getMessage());
        }
        
    }


    @RabbitListener(queues = "summary")
    public void receiveSummary(String message) {
        try {
            System.out.println("Summary: " + message);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

            String fileName = "vital-sign-summary_" + timestamp + ".json";
            String filePath = "/app/summary-logs/" + fileName;

            File directory = new File("/app/summary-logs");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            try (FileWriter fileWriter = new FileWriter(new File(filePath))) {
                fileWriter.write(message);
                fileWriter.flush();
                System.out.println("File saved at: " + filePath);

                rabbitTemplate.convertAndSend("exchange-rabbit", "routing-key-summary-res", message);
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
            
        } catch(Exception ex) {
            System.out.println("Summary Error: " + ex.getMessage());
        }
    }
}
