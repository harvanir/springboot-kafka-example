/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.harvan.example.springboot.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (6 May 2018)
 */
@Component
public class Sender {
    private final Log logger = LogFactory.getLog(getClass());
    private KafkaTemplate<String, String> kafkaTemplate;

    Log logger() {
        return logger;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String payload) {
        if (logger().isDebugEnabled()) {
            logger().debug(String.format("Sending payload='{%s}' to topic='{%s}'", payload, topic));
        }

        kafkaTemplate.send(topic, payload);
    }
}