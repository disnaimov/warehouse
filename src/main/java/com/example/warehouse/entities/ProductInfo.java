package com.example.warehouse.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "product_info")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    @Id
    private UUID productFileId;
    @Column(name = "product_name")
    private String productName;

}
