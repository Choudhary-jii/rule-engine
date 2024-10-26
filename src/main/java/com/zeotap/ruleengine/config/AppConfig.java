package com.zeotap.ruleengine.config;

import com.zeotap.ruleengine.util.RuleParser;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RuleParser ruleParser() {
        return new RuleParser(); // Create and return a new instance of RuleParser
    }
}
