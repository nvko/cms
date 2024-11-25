package com.nvko.cms;

import com.nvko.cms.communication.KafkaProducer;
import com.nvko.cms.configuration.ConfigDetectService;
import com.nvko.cms.configuration.model.endpoints.EndpointDefinition;
import com.nvko.cms.exception.RuleException;
import com.nvko.cms.rule.RuleChain;
import com.nvko.cms.rule.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.nvko.cms.utils.MapUtils.getNestedValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class CmsService {

    private final ConfigDetectService configDetectService;
    private final RuleService ruleService;
    private final KafkaProducer kafkaProducer;

    public void handleRequest(String path, Map<String, Object> payload) {
        final EndpointDefinition endpointDefinition = configDetectService.getEndpointDefinition(path);
        processRules(endpointDefinition, payload);
    }

    private void processRules(EndpointDefinition endpointDefinition, Map<String, Object> payload) {
        final RuleChain ruleChain = ruleService.getRuleChain(endpointDefinition);
        endpointDefinition.getDto().getFields().forEach(it -> {
            final String fieldValue = getNestedValue(payload, it.getPath());
            try {
                ruleChain.applyAll(fieldValue);
                kafkaProducer.sendToSuccessQueue(endpointDefinition.getKafka().getTopic().getOutgoing().getSuccess(), payload);
            } catch (RuleException e) {
                kafkaProducer.sendToFailureQueue(endpointDefinition.getKafka().getTopic().getOutgoing().getFailure(), payload);
            }
        });
    }

}
