package com.zeotap.ruleengine.model;

import com.zeotap.ruleengine.Validation.ValidSpend;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
@ValidSpend
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 75, message = "Age must be no more than 75")
    private int age;

    @NotNull(message = "Department is required")
    @Size(min = 2, max = 30, message = "Department must be between 3 and 15 characters")
    private String department;

    @NotNull(message = "Income is required")
    @DecimalMin(value = "20000.0", message = "Income must be at least 20,000")
    @DecimalMax(value = "200000.0", message = "Income must be less than 2,00,000")
    private Double income;

    @DecimalMin(value = "0.0", message = "Spend cannot be negative")
    @DecimalMax(value = "200000.0", message = "Spend must be less than income")
    private Double spend;

    @PrePersist
    private void validateSpend() {
        if (spend != null && income != null && spend >= income) {
            throw new IllegalArgumentException("Spend must be less than income");
        }
    }
}
