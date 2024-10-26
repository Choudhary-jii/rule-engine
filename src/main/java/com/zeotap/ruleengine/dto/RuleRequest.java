package com.zeotap.ruleengine.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class RuleRequest {
    private String ruleString;

    // Getters and Setters
    public String getRuleString() {
        return ruleString;
    }

    public void setRuleString(String ruleString) {
        this.ruleString = ruleString;
    }
}
