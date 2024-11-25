package com.nvko.cms.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "cms")
public class CmsProperties {

    private String configPathEndpoints;
    private String configPathKeywords;
    private boolean kafkaEnabled = false;

}
