package com.nvko.cms.rule.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MinLengthRule implements Rule {

    private final int minLength;

    @Override
    public boolean apply(String value) {
        return value.length() >= minLength;
    }

}
