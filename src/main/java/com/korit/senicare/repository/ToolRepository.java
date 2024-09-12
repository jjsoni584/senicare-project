package com.korit.senicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.korit.senicare.entity.ToolsEntity;

@Repository
public interface ToolRepository extends JpaRepository<ToolsEntity, Integer>{
    
}
