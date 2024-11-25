package com.nvko.cms.rule.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MaxLengthRule implements Rule {

    private final int maxLength;

    @Override
    public boolean apply(String value) {
        return value.length() <= maxLength;
    }

}
