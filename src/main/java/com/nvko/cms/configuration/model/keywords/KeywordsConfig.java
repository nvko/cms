package com.nvko.cms.configuration.model.keywords;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class KeywordsConfig {

    private Map<String, List<String>> keywords;

}
