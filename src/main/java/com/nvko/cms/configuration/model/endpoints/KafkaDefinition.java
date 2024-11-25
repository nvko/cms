package com.nvko.cms.configuration.model.endpoints;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaDefinition {

    private TopicDefinition topic;

    @Getter
    @Setter
    public static class TopicDefinition {

        private String incoming;
        private OutgoingDefinition outgoing;

    }

    @Getter
    @Setter
    public static class OutgoingDefinition {

        private String success;
        private String failure;

    }
}
