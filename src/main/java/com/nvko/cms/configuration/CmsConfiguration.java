package com.nvko.cms.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CmsConfiguration {

    @Bean
    @Primary
    public ObjectMapper yamlObjectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }


}
