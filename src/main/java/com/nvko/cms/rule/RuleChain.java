package com.nvko.cms.rule;

import com.nvko.cms.exception.RuleException;
import com.nvko.cms.rule.type.Rule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class RuleChain {

    private List<Rule> rules;

    public boolean applyAll(String value) throws RuleException {
        for (Rule rule : rules) {
            if (!rule.apply(value)) {
                log.warn("*\t Violation in rule: {}", rule.getClass().getSimpleName());
                throw new RuleException("Violation in rule: " + rule.getClass().getSimpleName());
            }
        }
        return true;
    }

}
