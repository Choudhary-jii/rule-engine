
////package com.zeotap.ruleengine.service;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.zeotap.ruleengine.model.ASTNode;
////import com.zeotap.ruleengine.model.Rule;
////import com.zeotap.ruleengine.model.User;
////import com.zeotap.ruleengine.repository.RuleRepository;
////import com.zeotap.ruleengine.repository.UserRepository;
////import com.zeotap.ruleengine.util.RuleParser;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////import java.util.HashMap;
////import java.util.List;
////import java.util.Map;
////
////@Service
////public class RuleService {
////
////    @Autowired
////    private RuleRepository ruleRepository;
////
////    @Autowired
////    private UserRepository userRepository;
////
////    RuleParser ruleParser = new RuleParser();
////
////    public Rule createRule(String ruleString) {
////        // Validate the rule string format
////        if (!isValidRuleString(ruleString)) {
////            System.err.println("Invalid rule format: " + ruleString);
////            throw new IllegalArgumentException("Invalid rule format: " + ruleString);
////        }
////
////        try {
////            // Log the rule being parsed
////            System.out.println("Creating rule with string: " + ruleString);
////
////            // Parse the rule string into an ASTNode (Abstract Syntax Tree)
////            ASTNode astNode = RuleParser.parse(ruleString);
////
////            // Serialize the ASTNode to a JSON string
////            ObjectMapper objectMapper = new ObjectMapper();
////            String serializedAst = objectMapper.writeValueAsString(astNode);
////
////            // Create a new Rule entity
////            Rule rule = new Rule();
////            rule.setRuleString(ruleString); // Store the original rule string
////            rule.setAst(serializedAst);     // Store the serialized ASTNode
////
////            return ruleRepository.save(rule); // Store the rule in the database
////        } catch (Exception e) {
////            // Log the error for better debugging
////            System.err.println("Error parsing rule: " + e.getMessage());
////            throw new RuntimeException("Error parsing rule: " + e.getMessage());
////        }
////    }
////
////
//////    public boolean isValidRuleString(String rule) {
//////        // Enhanced regex to validate rule formats
//////        String regex = "^(\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'.*?')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'.*?')\\s*$";
//////        return rule != null && rule.matches(regex);
//////    }
////
//////    public boolean isValidRuleString(String rule) {
//////        String regex = "^(\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*$";
//////        return rule != null && rule.matches(regex);
//////    }
////
////
////    public boolean isValidRuleString(String rule) {
////        String regex = "^((\\s*\\((\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*\\))|\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*'))(\\s*(AND|OR)\\s*((\\s*\\((\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*\\))|\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')))*$";
////        return rule != null && rule.matches(regex);
////    }
////
////
////    public List<Rule> getAllRules() {
////        return ruleRepository.findAll(); // Retrieve all rules from the database
////    }
////
////    public void deleteRule(Long id) {
////        ruleRepository.deleteById(id); // Delete the rule by ID
////    }
////
////    public ASTNode parseRule(String ruleString) {
////        return RuleParser.parse(ruleString);
////    }
////
////    public ASTNode combineRules(List<String> ruleStrings) {
////        if (ruleStrings == null || ruleStrings.isEmpty()) {
////            throw new IllegalArgumentException("No rule strings provided for combination");
////        }
////
////        List<ASTNode> ruleASTs = ruleStrings.stream()
////                .map(RuleParser::parse) // Parse strings into AST nodes
////                .toList();
////
////        Map<String, Integer> operatorFrequency = new HashMap<>();
////        for (ASTNode ast : ruleASTs) {
////            countOperators(ast, operatorFrequency); // Count operators in each AST
////        }
////
////        String mostFrequentOperator = operatorFrequency.entrySet().stream()
////                .max(Map.Entry.comparingByValue())
////                .map(Map.Entry::getKey)
////                .orElse("AND");
////
////        ASTNode combinedRoot = new ASTNode(mostFrequentOperator);
////        for (ASTNode ast : ruleASTs) {
////            if (!isRedundant(combinedRoot, ast)) {  // Minimize redundant checks
////                combinedRoot.addChild(ast);
////            }
////        }
////
////        return combinedRoot;
////    }
////
////    private void countOperators(ASTNode node, Map<String, Integer> operatorFrequency) {
////        if (node.getChildren().isEmpty()) return; // Leaf node, no operator
////
////        String value = node.getValue();
////        if (value.equals("AND") || value.equals("OR")) {
////            operatorFrequency.put(value, operatorFrequency.getOrDefault(value, 0) + 1);
////        }
////
////        for (ASTNode child : node.getChildren()) {
////            countOperators(child, operatorFrequency);
////        }
////    }
////
////    private boolean isRedundant(ASTNode combinedRoot, ASTNode newNode) {
////        for (ASTNode child : combinedRoot.getChildren()) {
////            if (child.equals(newNode)) {
////                return true;
////            }
////        }
////        return false;
////    }
////
////    public boolean evaluateRule(ASTNode rule, Map<String, Object> userData) {
////        return rule.evaluate(userData);
////    }
////
////    public List<User> evaluateAllUsers(ASTNode rule) {
////        List<User> allUsers = userRepository.findAll();
////        return allUsers.stream()
////                .filter(user -> evaluateRule(rule, convertUserToMap(user)))  // Pass user data after conversion
////                .toList(); // Return users who meet the rule criteria
////    }
////
////    private Map<String, Object> convertUserToMap(User user) {
////        Map<String, Object> userData = new HashMap<>();
////        userData.put("id", user.getId());
////        userData.put("name", user.getName());
////        userData.put("age", user.getAge());
////        userData.put("department", user.getDepartment());
////        userData.put("income", user.getIncome());
////        userData.put("spend", user.getSpend());
////        return userData;
////    }
////
////    public Rule updateRule(Long ruleId, String newRuleString) {
////        // Validate the new rule string format
////        if (!isValidRuleString(newRuleString)) {
////            throw new IllegalArgumentException("Invalid rule format: " + newRuleString);
////        }
////
////        // Fetch the existing rule from the database
////        Rule existingRule = ruleRepository.findById(ruleId)
////                .orElseThrow(() -> new RuntimeException("Rule not found"));
////
////        // Parse the new rule string into an ASTNode
////        ASTNode newAstNode = RuleParser.parse(newRuleString);
////
////        // Serialize the ASTNode to JSON
////        try {
////            ObjectMapper objectMapper = new ObjectMapper();
////            String serializedAst = objectMapper.writeValueAsString(newAstNode); // Convert ASTNode to JSON
////
////            // Update the rule fields
////            existingRule.setRuleString(newRuleString);
////            existingRule.setAst(serializedAst); // Store the serialized AST as a JSON string
////
////            // Save the updated rule back to the database
////            return ruleRepository.save(existingRule);
////
////        } catch (Exception e) {
////            throw new RuntimeException("Error updating rule: " + e.getMessage());
////        }
////    }
////
////}
//
//
//package com.zeotap.ruleengine.service;


//@Service
//public class RuleService {
//
//    @Autowired
//    private RuleRepository ruleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private final RuleParser ruleParser;
//
//    @Autowired
//    public RuleService(RuleParser ruleParser) {
//        this.ruleParser = ruleParser;
//    }
//
//    public Rule createRule(String ruleString) {
//        // Validate the rule string format
//        if (!isValidRuleString(ruleString)) {
//            System.err.println("Invalid rule format: " + ruleString);
//            throw new IllegalArgumentException("Invalid rule format: " + ruleString);
//        }
//
//        try {
//            // Log the rule being parsed
//            System.out.println("Creating rule with string: " + ruleString);
//
//            // Parse the rule string into an ASTNode (Abstract Syntax Tree)
//            ASTNode astNode = ruleParser.parse(ruleString);  // Changed to instance method
//
//            // Serialize the ASTNode to a JSON string
//            ObjectMapper objectMapper = new ObjectMapper();
//            String serializedAst = objectMapper.writeValueAsString(astNode);
//
//            // Create a new Rule entity
//            Rule rule = new Rule();
//            rule.setRuleString(ruleString); // Store the original rule string
//            rule.setAst(serializedAst);     // Store the serialized ASTNode
//
//            return ruleRepository.save(rule); // Store the rule in the database
//        } catch (Exception e) {
//            // Log the error for better debugging
//            System.err.println("Error parsing rule: " + e.getMessage());
//            throw new RuntimeException("Error parsing rule: " + e.getMessage());
//        }
//    }
//

//
//    public List<Rule> getAllRules() {
//        return ruleRepository.findAll(); // Retrieve all rules from the database
//    }
//
//    public void deleteRule(Long id) {
//        ruleRepository.deleteById(id); // Delete the rule by ID
//    }
//
//    public ASTNode parseRule(String ruleString) {
//        return ruleParser.parse(ruleString); // Changed to instance method
//    }
//
//    public ASTNode combineRules(List<String> ruleStrings) {
//        if (ruleStrings == null || ruleStrings.isEmpty()) {
//            throw new IllegalArgumentException("No rule strings provided for combination");
//        }
//
//        List<ASTNode> ruleASTs = ruleStrings.stream()
//                .map(ruleParser::parse) // Changed to instance method
//                .toList();
//
//        Map<String, Integer> operatorFrequency = new HashMap<>();
//        for (ASTNode ast : ruleASTs) {
//            countOperators(ast, operatorFrequency); // Count operators in each AST
//        }
//
//        String mostFrequentOperator = operatorFrequency.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse("AND");
//
//        ASTNode combinedRoot = new ASTNode(mostFrequentOperator);
//        for (ASTNode ast : ruleASTs) {
//            if (!isRedundant(combinedRoot, ast)) {  // Minimize redundant checks
//                combinedRoot.addChild(ast);
//            }
//        }
//
//        return combinedRoot;
//    }
//
//    private void countOperators(ASTNode node, Map<String, Integer> operatorFrequency) {
//        if (node.getChildren().isEmpty()) return; // Leaf node, no operator
//
//        String value = node.getValue();
//        if (value.equals("AND") || value.equals("OR")) {
//            operatorFrequency.put(value, operatorFrequency.getOrDefault(value, 0) + 1);
//        }
//
//        for (ASTNode child : node.getChildren()) {
//            countOperators(child, operatorFrequency);
//        }
//    }
//
//    private boolean isRedundant(ASTNode combinedRoot, ASTNode newNode) {
//        for (ASTNode child : combinedRoot.getChildren()) {
//            if (child.equals(newNode)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean evaluateRule(ASTNode rule, Map<String, Object> userData) {
//        return rule.evaluate(userData);
//    }
//
//    public List<User> evaluateAllUsers(ASTNode rule) {
//        List<User> allUsers = userRepository.findAll();
//        return allUsers.stream()
//                .filter(user -> evaluateRule(rule, convertUserToMap(user)))  // Pass user data after conversion
//                .toList(); // Return users who meet the rule criteria
//    }
//
//    private Map<String, Object> convertUserToMap(User user) {
//        Map<String, Object> userData = new HashMap<>();
//        userData.put("id", user.getId());
//        userData.put("name", user.getName());
//        userData.put("age", user.getAge());
//        userData.put("department", user.getDepartment());
//        userData.put("income", user.getIncome());
//        userData.put("spend", user.getSpend());
//        return userData;
//    }
//
//    public Rule updateRule(Long ruleId, String newRuleString) {
//        // Validate the new rule string format
//        if (!isValidRuleString(newRuleString)) {
//            throw new IllegalArgumentException("Invalid rule format: " + newRuleString);
//        }
//
//        // Fetch the existing rule from the database
//        Rule existingRule = ruleRepository.findById(ruleId)
//                .orElseThrow(() -> new RuntimeException("Rule not found"));
//
//        // Parse the new rule string into an ASTNode
//        ASTNode newAstNode = ruleParser.parse(newRuleString); // Changed to instance method
//
//        // Serialize the ASTNode to JSON
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String serializedAst = objectMapper.writeValueAsString(newAstNode); // Convert ASTNode to JSON
//
//            // Update the rule fields
//            existingRule.setRuleString(newRuleString);
//            existingRule.setAst(serializedAst); // Store the serialized AST as a JSON string
//
//            // Save the updated rule back to the database
//            return ruleRepository.save(existingRule);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error updating rule: " + e.getMessage());
//        }
//    }
//
//}
//@Service
//public class RuleService {
//
//    @Autowired
//    private RuleRepository ruleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private final RuleParser ruleParser;
//
//    @Autowired
//    public RuleService(RuleParser ruleParser) {
//        this.ruleParser = ruleParser;
//    }
//
//    public Rule createRule(String ruleString) {
//        if (!isValidRuleString(ruleString)) {
//            System.err.println("Invalid rule format: " + ruleString);
//            throw new IllegalArgumentException("Invalid rule format: " + ruleString);
//        }
//
//        try {
//            System.out.println("Creating rule with string: " + ruleString);
//
//            // Parse the rule string into an ASTNode (Abstract Syntax Tree)
//            ASTNode astNode = ruleParser.parse(ruleString);  // Ensure parse() returns ASTNode
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            String serializedAst = objectMapper.writeValueAsString(astNode);
//
//            Rule rule = new Rule();
//            rule.setRuleString(ruleString);
//            rule.setAst(serializedAst);
//
//            return ruleRepository.save(rule);
//        } catch (Exception e) {
//            System.err.println("Error parsing rule: " + e.getMessage());
//            throw new RuntimeException("Error parsing rule: " + e.getMessage());
//        }
//    }
//
//    public boolean isValidRuleString(String rule) {
//        String regex = "^((\\s*\\((\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*\\))|\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*'))(\\s*(AND|OR)\\s*((\\s*\\((\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*\\))|\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')))*$";
//        return rule != null && rule.matches(regex);
//    }
//
//    public ASTNode parseRule(String ruleString) {
//        return ruleParser.parse(ruleString);  // Ensure parse() returns ASTNode
//    }
//
////    public ASTNode combineRules(List<String> ruleStrings) {
////        if (ruleStrings == null || ruleStrings.isEmpty()) {
////            throw new IllegalArgumentException("No rule strings provided for combination");
////        }
////
////        // Stream map ruleParser::parse expects ruleParser.parse() to return ASTNode
////        List<ASTNode> ruleASTs = ruleStrings.stream()
////                .map(ruleParser::parse) // Parse strings into AST nodes
////                .toList();
////
////        Map<String, Integer> operatorFrequency = new HashMap<>();
////        for (ASTNode ast : ruleASTs) {
////            countOperators(ast, operatorFrequency);
////        }
////
////        String mostFrequentOperator = operatorFrequency.entrySet().stream()
////                .max(Map.Entry.comparingByValue())
////                .map(Map.Entry::getKey)
////                .orElse("AND");
////
////        ASTNode combinedRoot = new ASTNode(mostFrequentOperator);
////        for (ASTNode ast : ruleASTs) {
////            if (!isRedundant(combinedRoot, ast)) {
////                combinedRoot.addChild(ast);
////            }
////        }
////
////        return combinedRoot;
////    }
//
//    private void countOperators(ASTNode node, Map<String, Integer> operatorFrequency) {
//        if (node.getChildren().isEmpty()) return;
//
//        String value = node.getValue();
//        if (value.equals("AND") || value.equals("OR")) {
//            operatorFrequency.put(value, operatorFrequency.getOrDefault(value, 0) + 1);
//        }
//
//        for (ASTNode child : node.getChildren()) {
//            countOperators(child, operatorFrequency);
//        }
//    }
//
//    private boolean isRedundant(ASTNode combinedRoot, ASTNode newNode) {
//        for (ASTNode child : combinedRoot.getChildren()) {
//            if (child.equals(newNode)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}




package com.zeotap.ruleengine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeotap.ruleengine.model.ASTNode;
import com.zeotap.ruleengine.model.Rule;
import com.zeotap.ruleengine.model.User;
import com.zeotap.ruleengine.repository.RuleRepository;
import com.zeotap.ruleengine.repository.UserRepository;
import com.zeotap.ruleengine.util.RuleParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private UserRepository userRepository;

    private final RuleParser ruleParser;

    @Autowired
    public RuleService(RuleParser ruleParser) {
        this.ruleParser = ruleParser;
    }

    public Rule createRule(String ruleString) {
        if (!isValidRuleString(ruleString)) {
            throw new IllegalArgumentException("Invalid rule format: " + ruleString);
        }

        try {
            ASTNode astNode = ruleParser.parse(ruleString);  // Ensure parse() returns ASTNode

            ObjectMapper objectMapper = new ObjectMapper();
            String serializedAst = objectMapper.writeValueAsString(astNode);

            Rule rule = new Rule();
            rule.setRuleString(ruleString);
            rule.setAst(serializedAst);

            return ruleRepository.save(rule);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing rule: " + e.getMessage());
        }
    }

    public List<Rule> getAllRules() {
        return ruleRepository.findAll(); // Retrieve all rules from the database
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id); // Delete the rule by ID
    }

    public Rule updateRule(Long ruleId, String newRuleString) {
        if (!isValidRuleString(newRuleString)) {
            throw new IllegalArgumentException("Invalid rule format: " + newRuleString);
        }

        Rule existingRule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found"));

        ASTNode newAstNode = ruleParser.parse(newRuleString);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String serializedAst = objectMapper.writeValueAsString(newAstNode);

            existingRule.setRuleString(newRuleString);
            existingRule.setAst(serializedAst);

            return ruleRepository.save(existingRule);
        } catch (Exception e) {
            throw new RuntimeException("Error updating rule: " + e.getMessage());
        }
    }

    public boolean evaluateRule(ASTNode rule, Map<String, Object> userData) {
        return rule.evaluate(userData); // Assuming you have a method `evaluate` in your ASTNode class
    }

    public List<User> evaluateAllUsers(ASTNode rule) {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> evaluateRule(rule, convertUserToMap(user)))
                .toList();
    }

    public List<ASTNode> combineRules(List<String> ruleStrings) {
        if (ruleStrings == null || ruleStrings.isEmpty()) {
            throw new IllegalArgumentException("No rule strings provided for combination");
        }

        // Stream map ruleParser::parse expects ruleParser.parse() to return ASTNode
        List<ASTNode> ruleASTs = ruleStrings.stream()
                .map(ruleParser::parse) // Parse strings into AST nodes
                .toList();

        Map<String, Integer> operatorFrequency = new HashMap<>();
        for (ASTNode ast : ruleASTs) {
            countOperators(ast, operatorFrequency);
        }

        String mostFrequentOperator = operatorFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("AND");

        ASTNode combinedRoot = new ASTNode(mostFrequentOperator);
        for (ASTNode ast : ruleASTs) {
            if (!isRedundant(combinedRoot, ast)) {
                combinedRoot.addChild(ast);
            }
        }

        return (List<ASTNode>) combinedRoot; // Return the combined ASTNode
    }

    private Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("age", user.getAge());
        userData.put("department", user.getDepartment());
        userData.put("income", user.getIncome());
        userData.put("spend", user.getSpend());
        return userData;
    }

    public boolean isValidRuleString(String rule) {
        String regex = "^((\\s*\\((\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*\\))|\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*'))(\\s*(AND|OR)\\s*((\\s*\\((\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*(AND|OR)\\s*)*\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')\\s*\\))|\\s*\\w+\\s*(>=|<=|>|<|=|!=)\\s*(\\d+|'[^']*')))*$";
        return rule != null && rule.matches(regex);
    }

    private void countOperators(ASTNode node, Map<String, Integer> operatorFrequency) {
        if (node.getChildren().isEmpty()) return;

        String value = node.getValue();
        if (value.equals("AND") || value.equals("OR")) {
            operatorFrequency.put(value, operatorFrequency.getOrDefault(value, 0) + 1);
        }

        for (ASTNode child : node.getChildren()) {
            countOperators(child, operatorFrequency);
        }
    }

    private boolean isRedundant(ASTNode combinedRoot, ASTNode newNode) {
        for (ASTNode child : combinedRoot.getChildren()) {
            if (child.equals(newNode)) {
                return true;
            }
        }
        return false;
    }
}
