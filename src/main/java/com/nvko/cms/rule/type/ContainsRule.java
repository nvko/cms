package com.nvko.cms.rule.type;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ContainsRule implements Rule {

    private List<String> keywords;

    @Override
    public boolean apply(String value) {
        return keywords.stream().noneMatch(value.toLowerCase()::contains);
    }

}
