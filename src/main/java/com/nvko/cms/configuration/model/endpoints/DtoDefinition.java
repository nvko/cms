package com.nvko.cms.configuration.model.endpoints;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoDefinition {

    private List<FieldDefinition> fields;

    @Getter
    @Setter
    public static class FieldDefinition {
        private String path;
        private List<RuleDefinition> rules;
    }

    @Getter
    @Setter
    public static class RuleDefinition {
        private RuleTypeEnum type;
        private Object value;
    }

}
