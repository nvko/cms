package com.nvko.cms.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IncomingChannel {
    private String restEndpoint;
    private String kafkaTopic;
}
