package com.zeotap.ruleengine.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ASTNode {
    private String value; // This will hold the operation or value (e.g., "AND", "OR", "age", etc.)
    private List<ASTNode> children; // Child nodes for a tree structure

    // Constructor for single value
    public ASTNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    // Constructor for operation with two child nodes
    public ASTNode(String value, ASTNode leftChild, ASTNode rightChild) {
        this.value = value;
        this.children = new ArrayList<>();
        this.children.add(leftChild);
        this.children.add(rightChild);
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    // The method that evaluates the rule with user data
    public boolean evaluate(Map<String, Object> userData) {
        if (children.isEmpty()) {
            // Leaf node, check condition against user data
            return evaluateComparison(userData);
        } else {
            // Internal node, combine results of child nodes
            switch (value) {
                case "AND":
                    return children.stream().allMatch(child -> child.evaluate(userData));
                case "OR":
                    return children.stream().anyMatch(child -> child.evaluate(userData));
                default:
                    throw new IllegalArgumentException("Unknown operation: " + value);
            }
        }
    }

    // Method to evaluate comparisons for leaf nodes
//    private boolean evaluateComparison(Map<String, Object> userData) {
//        // Assuming the value of the node is a string like "age > 30"
//        String[] parts = value.split(" "); // Split the string into parts
//        if (parts.length != 3) {
//            throw new IllegalArgumentException("Invalid comparison format: " + value);
//        }
//
//        String attribute = parts[0]; // e.g., "age"
//        String operator = parts[1];   // e.g., ">"
//        String comparisonValue = parts[2]; // e.g., "30"
//
//        Object userValue = userData.get(attribute); // Get value from user data
//
//        // Perform comparison based on the operator
//        switch (operator) {
//            case ">":
//                return ((Number) userValue).doubleValue() > Double.parseDouble(comparisonValue);
//            case "<":
//                return ((Number) userValue).doubleValue() < Double.parseDouble(comparisonValue);
//            case "==":
//                return userValue.equals(comparisonValue);
//            // Add more operators as needed
//            default:
//                throw new UnsupportedOperationException("Unsupported operator: " + operator);
//        }
  //  }


    private boolean evaluateComparison(Map<String, Object> userData) {
        String[] parts = value.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid comparison format: " + value);
        }

        String attribute = parts[0]; // e.g., "age" or "department"
        String operator = parts[1];   // e.g., ">", "==", etc.
        String comparisonValue = parts[2]; // e.g., "30" or "'HR'"

        Object userValue = userData.get(attribute); // Get value from user data

        if (userValue instanceof Number) {
            // Handle numeric comparisons
            double userNumber = ((Number) userValue).doubleValue();
            double comparisonNumber = Double.parseDouble(comparisonValue);

            switch (operator) {
                case ">":
                    return userNumber > comparisonNumber;
                case "<":
                    return userNumber < comparisonNumber;
                case "==":
                    return userNumber == comparisonNumber;
                case "!=":
                    return userNumber != comparisonNumber;
                default:
                    throw new UnsupportedOperationException("Unsupported operator: " + operator);
            }
        } else if (userValue instanceof String) {
            // Handle string comparisons
            String userString = userValue.toString();
            comparisonValue = comparisonValue.replace("'", ""); // Remove quotes from string values

            switch (operator) {
                case "==":
                    return userString.equals(comparisonValue);
                case "!=":
                    return !userString.equals(comparisonValue);
                default:
                    throw new UnsupportedOperationException("Unsupported operator for strings: " + operator);
            }
        } else {
            throw new IllegalArgumentException("Unsupported data type for comparison: " + userValue.getClass());
        }
    }

}
