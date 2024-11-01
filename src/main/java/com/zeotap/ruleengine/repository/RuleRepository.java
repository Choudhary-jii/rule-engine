package com.zeotap.ruleengine.repository;

import com.zeotap.ruleengine.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
}
