package com.nvko.cms.configuration.model.endpoints;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EndpointConfig {

    private List<EndpointDefinition> endpoints;

}
