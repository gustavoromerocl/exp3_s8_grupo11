package com.duoc.Consumer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.Consumer.models.Alert;
import com.duoc.Consumer.services.AlertService;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    

    @Autowired
    private AlertService alertService;


    @GetMapping
    public ResponseEntity<List<Alert>> findAll() {
        return ResponseEntity.ok(alertService.List());
    }

}
