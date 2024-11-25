package com.nvko.cms.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvko.cms.CmsService;
import com.nvko.cms.configuration.ConfigDetectService;
import com.nvko.cms.configuration.IncomingChannel;
import com.nvko.cms.configuration.model.endpoints.EndpointDefinition;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;

import java.lang.reflect.Method;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerRegistrar implements KafkaListenerConfigurer {

    private final ConfigDetectService configDetectService;
    private final CmsService cmsService;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;
    private final ObjectMapper jsonObjectMapper;

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        final Map<IncomingChannel, EndpointDefinition> endpointDefinitions = configDetectService.getEndpointDefinitions();
        endpointDefinitions.entrySet().forEach(it -> {
            try {
                registerListenerForTopic(it);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registerListenerForTopic(Map.Entry<IncomingChannel, EndpointDefinition> key) throws NoSuchMethodException {
        MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        final String topic = key.getKey().getKafkaTopic();
        endpoint.setId(topic);
        endpoint.setGroupId("dynamic-listener-group");
        endpoint.setTopics(topic);
        endpoint.setBean(this);
        Method processMessageMethod = KafkaListenerRegistrar.class.getDeclaredMethod("processMessage", ConsumerRecord.class);
        endpoint.setMethod(processMessageMethod);
        kafkaListenerEndpointRegistry.registerListenerContainer(endpoint, kafkaListenerContainerFactory, true);
    }

    private void processMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
        final String topic = record.topic();
        final Map<String, Object> payload = jsonObjectMapper.readValue(record.value(), Map.class);
        cmsService.handleRequest(topic, payload);
    }
}