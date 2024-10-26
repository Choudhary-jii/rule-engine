package com.zeotap.ruleengine.util;

import com.zeotap.ruleengine.model.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class RuleParserTest {

    private RuleParser ruleParser;

    @BeforeEach
    void setUp() {
        ruleParser = new RuleParser();
    }

    @Test
    void testParseValidRule() {
        String rule = "age > 30 AND department = 'Sales'";
        ASTNode result = ruleParser.parse(rule);
        assertNotNull(result);
        assertEquals("AND", result.getValue());
        List<ASTNode> children = result.getChildren();
        assertEquals(2, children.size());
        // Add assertions for the child nodes as needed
    }

    @Test
    void testParseComplexRule() {
        String rule = "(age > 30 AND department = 'Sales') OR income < 50000";
        ASTNode result = ruleParser.parse(rule);
        assertNotNull(result);
        assertEquals("OR", result.getValue());
        List<ASTNode> children = result.getChildren();
        assertEquals(2, children.size());
        // Add assertions for the child nodes as needed
    }

    @Test
    void testParseInvalidRule() {
        String invalidRule = "age > 30 AND";
        assertThrows(IllegalArgumentException.class, () -> {
            ruleParser.parse(invalidRule);
        });
    }

    @Test
    void testTokenize() {
        String rule = "age > 30 AND department = 'Sales'";
        List<String> tokens = ruleParser.tokenize(rule);
        assertNotNull(tokens);
        assertEquals(5, tokens.size()); // There are 5 tokens in this rule
        assertEquals("age", tokens.get(0));
        assertEquals(">", tokens.get(1));
        assertEquals("30", tokens.get(2));
        assertEquals("AND", tokens.get(3));
        assertEquals("department", tokens.get(4));
    }

    // Add more tests as needed for edge cases, invalid inputs, etc.
}
