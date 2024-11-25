package com.nvko.cms.rule;

import com.nvko.cms.configuration.ConfigDetectService;
import com.nvko.cms.configuration.model.endpoints.DtoDefinition;
import com.nvko.cms.configuration.model.endpoints.EndpointDefinition;
import com.nvko.cms.configuration.model.endpoints.RuleTypeEnum;
import com.nvko.cms.exception.RuleException;
import com.nvko.cms.rule.type.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleService {

    private final ConfigDetectService configDetectService;

    public RuleChain getRuleChain(EndpointDefinition endpoint) {
        final List<DtoDefinition.RuleDefinition> ruleDefinitions = getRuleDefinitions(endpoint);
        final List<Rule> rules = ruleDefinitions.stream().map(this::createRule).toList();
        return new RuleChain(rules);
    }

    private Rule createRule(DtoDefinition.RuleDefinition ruleDefinition) {
        final RuleTypeEnum ruleType = ruleDefinition.getType();
        return switch (ruleType) {
            case CONTAINS -> {
                final List<String> keywordNames = (List<String>) ruleDefinition.getValue();
                final List<String> keywords = new ArrayList<>();
                keywordNames.forEach(it -> keywords.addAll(configDetectService.getKeywords(it)));
                yield new ContainsRule(keywords);
            }
            case MAX_LENGTH -> new MaxLengthRule((Integer) ruleDefinition.getValue());
            case MIN_LENGTH -> new MinLengthRule(((Integer) ruleDefinition.getValue()));
            default -> throw new RuleException("Rule with type = " + ruleDefinition.getType() + " is not supported.");
        };
    }

    private static List<DtoDefinition.RuleDefinition> getRuleDefinitions(EndpointDefinition endpoint) {
        return endpoint
                .getDto()
                .getFields()
                .stream()
                .map(DtoDefinition.FieldDefinition::getRules)
                .flatMap(Collection::stream)
                .toList();
    }

}
