package com.zeotap.ruleengine.controller;

import com.zeotap.ruleengine.dto.RuleRequest;
import com.zeotap.ruleengine.model.ASTNode;
import com.zeotap.ruleengine.model.Rule;
import com.zeotap.ruleengine.model.User;
import com.zeotap.ruleengine.model.EvaluationRequest; // Ensure you import the DTO
import com.zeotap.ruleengine.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/create")
    public ResponseEntity<Rule> createRule(@RequestBody RuleRequest ruleRequest) {
        try {
            Rule rule = ruleService.createRule(ruleRequest.getRuleString());
            return ResponseEntity.ok(rule);
        } catch (IllegalArgumentException e) {
            // Log the error and return a bad request response
            System.err.println("Invalid rule format: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // Log unexpected errors
            System.err.println("Error creating rule: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // API to get all rules
    @GetMapping
    public ResponseEntity<List<Rule>> getAllRules() {
        List<Rule> rules = ruleService.getAllRules();
        return new ResponseEntity<>(rules, HttpStatus.OK);
    }

    // API to delete a rule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // API to combine multiple rules
    @PostMapping("/combine")
    public ResponseEntity<ASTNode> combineRules(@RequestBody List<String> ruleStrings) {
        ASTNode combinedRule = (ASTNode) ruleService.combineRules(ruleStrings);
        return ResponseEntity.ok(combinedRule);
    }

    // API to evaluate a combined rule with user data
    @PostMapping("/evaluate")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody EvaluationRequest request) {
        boolean result = ruleService.evaluateRule(request.getCombinedRule(), request.getUserData());
        return ResponseEntity.ok(result);
    }

    // API to evaluate all users based on a given rule
    @PostMapping("/evaluate-all")
    public ResponseEntity<List<User>> evaluateAllUsers(@RequestBody ASTNode combinedRule) {
        List<User> matchingUsers = ruleService.evaluateAllUsers(combinedRule);
        return ResponseEntity.ok(matchingUsers);
    }

    // API to update an existing rule
    @PutMapping("/{id}")
    public ResponseEntity<Rule> updateRule(@PathVariable Long id, @RequestBody String newRuleString) {
        Rule updatedRule = ruleService.updateRule(id, newRuleString);
        return ResponseEntity.ok(updatedRule);
    }
}
