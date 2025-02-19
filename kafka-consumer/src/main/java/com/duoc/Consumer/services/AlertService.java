package com.duoc.Consumer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.Consumer.models.Alert;
import com.duoc.Consumer.repositories.AlertRepository;

@Service
public class AlertService {

    @Autowired AlertRepository alertRepository;



    public List<Alert> List() {
        return alertRepository.findAll();
    }

    public Alert Save(Alert alert) {
        return alertRepository.save(alert);
    }
    
}
