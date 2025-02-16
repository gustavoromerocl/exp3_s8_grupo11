package com.duoc.Consumer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.Consumer.models.Alert;

public interface AlertRepository  extends JpaRepository<Alert, Long>{
    
}
