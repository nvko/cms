package com.nvko.cms.configuration.model.endpoints;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndpointDefinition {

    private String path;
    private KafkaDefinition kafka;
    private DtoDefinition dto;

}
