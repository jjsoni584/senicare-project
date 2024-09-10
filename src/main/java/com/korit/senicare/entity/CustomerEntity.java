package com.korit.senicare.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "customers")
@Table(name = "customers")
public class CustomerEntity {
    
    @Id
    @GeneratedValue
    private Integer customerNumber;
    private String profileImage;
    private String name;
    private String birth;
    private String charger;
    private String address;
    private String location;

}
