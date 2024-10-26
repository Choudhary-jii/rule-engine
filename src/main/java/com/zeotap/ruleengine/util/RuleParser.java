package com.zeotap.ruleengine.util;

import com.zeotap.ruleengine.model.ASTNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class RuleParser {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\s*(=>|<=|!=|>|<|=|AND|OR|\\(|\\)|\\w+|\\'[^\\']*\\'|\\d+)\\s*");

    public ASTNode parse(String rule) {
        List<String> tokens = tokenize(rule);
        Stack<String> operatorStack = new Stack<>();
        Stack<ASTNode> operandStack = new Stack<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                operandStack.push(new ASTNode(token));
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    processOperator(operatorStack, operandStack);
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pop(); // Remove the '('
                }
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                    processOperator(operatorStack, operandStack);
                }
                operatorStack.push(token);
            } else {
                throw new IllegalArgumentException("Unexpected token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            processOperator(operatorStack, operandStack);
        }

        // The final result should be on the operand stack
        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression. Operand stack size: " + operandStack.size());
        }

        return operandStack.pop();
    }

    protected List<String> tokenize(String rule) {
        List<String> tokens = new ArrayList<>();
        var matcher = TOKEN_PATTERN.matcher(rule);

        while (matcher.find()) {
            tokens.add(matcher.group().trim());
        }

        // Debugging: Print tokens
        System.out.println("Tokens: " + tokens);

        return tokens;
    }

    private boolean isOperand(String token) {
        return token.matches("[a-zA-Z]+") || token.matches("'[^']*'") || token.matches("\\d+");
    }

    private boolean isOperator(String token) {
        return token.equals(">") || token.equals("<") || token.equals("=") ||
                token.equals("AND") || token.equals("OR") || token.equals("!=");
    }

    private int precedence(String operator) {
        switch (operator) {
            case "AND":
                return 1;
            case "OR":
                return 0;
            case ">":
            case "<":
            case "=":
            case "!=":
                return 2;
            default:
                return -1;
        }
    }

    private void processOperator(Stack<String> operatorStack, Stack<ASTNode> operandStack) {
        if (operatorStack.isEmpty()) {
            throw new IllegalArgumentException("No operator available to process.");
        }

        String operator = operatorStack.pop();

        // Ensure we have enough operands to work with
        if (operandStack.size() < 2) {
            throw new IllegalArgumentException("Insufficient operands for operator: " + operator);
        }

        ASTNode right = operandStack.pop();
        ASTNode left = operandStack.pop();

        // Create a new ASTNode with the operator and its operands
        ASTNode node = new ASTNode(operator);
        node.addChild(left);
        node.addChild(right);

        operandStack.push(node); // Push the new node onto the operand stack
    }
}
