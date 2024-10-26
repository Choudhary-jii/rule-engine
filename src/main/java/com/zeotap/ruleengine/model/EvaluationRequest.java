package com.zeotap.ruleengine.model;

import lombok.Data;

import java.util.Map;

@Data
public class EvaluationRequest {
    private Map<String, Object> userData;
    private ASTNode combinedRule;

    // Getters and Setters
    public Map<String, Object> getUserData() {
        return userData;
    }

    public void setUserData(Map<String, Object> userData) {
        this.userData = userData;
    }

    public ASTNode getCombinedRule() {
        return combinedRule;
    }

    public void setCombinedRule(ASTNode combinedRule) {
        this.combinedRule = combinedRule;
    }
}
