package com.zeotap.ruleengine.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleString;

    @Lob
    private String ast;  // Transient field for AST (or can be persisted depending on your requirement)
}

