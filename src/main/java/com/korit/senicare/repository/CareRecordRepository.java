package com.korit.senicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareRecordRepository extends JpaRepository<CareRecordRepository, Integer>{
    
}
