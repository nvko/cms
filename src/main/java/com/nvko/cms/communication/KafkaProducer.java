package com.nvko.cms.communication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public void sendToSuccessQueue(String topic, Map<String, Object> message) {
        kafkaTemplate.send(topic, message);
        log.info("Message sent to success queue [{}]: {}", topic, message);
    }

    public void sendToFailureQueue(String topic, Map<String, Object> message) {
        kafkaTemplate.send(topic, message);
        log.info("Message sent to failure queue [{}]: {}", topic, message);
    }

}
