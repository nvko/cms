package com.nvko.cms.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvko.cms.configuration.model.endpoints.EndpointConfig;
import com.nvko.cms.configuration.model.endpoints.EndpointDefinition;
import com.nvko.cms.configuration.model.keywords.KeywordsConfig;
import com.nvko.cms.exception.ConfigurationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.endsWithIgnoreCase;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigDetectService {

    private static final Map<IncomingChannel, EndpointDefinition> ENDPOINT_DEFINITIONS = new HashMap<>();
    private static final Map<String, List<String>> KEYWORD_DEFINITIONS = new HashMap<>();

    @Qualifier("yamlObjectMapper")
    private final ObjectMapper yamlObjectMapper;
    @Qualifier("jsonObjectMapper")
    private final ObjectMapper jsonObjectMapper;
    private final CmsProperties cmsProperties;

    @PostConstruct
    public void loadConfigs() {
        loadEndpointConfiguration();
        loadKeywordsConfiguration();
    }

    public EndpointDefinition getEndpointDefinition(String path) {
        return ENDPOINT_DEFINITIONS.entrySet()
                .stream()
                .filter(it -> it.getKey().getRestEndpoint().equals(path) || it.getKey().getKafkaTopic().equals(path))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new ConfigurationException("Configuration not found for path: " + path));
    }

    public List<String> getKeywords(String keywordName) {
        return KEYWORD_DEFINITIONS.get(keywordName);
    }

    public Map<IncomingChannel, EndpointDefinition> getEndpointDefinitions() {
        return ENDPOINT_DEFINITIONS;
    }

    private void loadEndpointConfiguration() {
        try {
            final String configPath = cmsProperties.getConfigPathEndpoints();
            final EndpointConfig config = resolveObjectMapper(configPath).readValue(new File(configPath), EndpointConfig.class);
            config.getEndpoints().forEach(it -> {
                ENDPOINT_DEFINITIONS.put(new IncomingChannel(it.getPath(), it.getKafka().getTopic().getIncoming()), it);
            });
            log.info("*\t Endpoint configuration loaded successfully, loaded {} configs.", ENDPOINT_DEFINITIONS.size());
        } catch (IOException e) {
            log.error("*\t Failed to load endpoint configuration: {}", e.getMessage(), e);
        }
    }

    private void loadKeywordsConfiguration() {
        try {
            final String configPath = cmsProperties.getConfigPathKeywords();
            final KeywordsConfig config = resolveObjectMapper(configPath).readValue(new File(configPath), KeywordsConfig.class);
            KEYWORD_DEFINITIONS.putAll(config.getKeywords());
            log.info("*\t Keywords configuration loaded successfully, loaded {} configs.", KEYWORD_DEFINITIONS.size());
        } catch (IOException e) {
            log.error("*\t Failed to load keywords configuration: {}", e.getMessage(), e);
        }
    }

    private ObjectMapper resolveObjectMapper(String configPath) {
        if (nonNull(configPath)) {
            if (endsWithIgnoreCase(configPath, ".yml") || endsWithIgnoreCase(configPath, ".yaml")) {
                return yamlObjectMapper;
            } else if (endsWithIgnoreCase(configPath, ".json")) {
                return jsonObjectMapper;
            } else {
                throw new ConfigurationException("Configuration format not supported! Supported formats: [yaml, json].");
            }
        } else {
            throw new ConfigurationException("Configuration not found!");
        }
    }

}
