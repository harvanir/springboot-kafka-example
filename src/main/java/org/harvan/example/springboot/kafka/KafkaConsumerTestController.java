/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.harvan.example.springboot.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (6 May 2018)
 */
@RestController
@RequestMapping("/kafka/testconsumer")
public class KafkaConsumerTestController {
    private final Logger logger = LogManager.getLogger(getClass());
    private List<String> data = new CopyOnWriteArrayList<>();
    private Sender sender;

    Logger logger() {
        return logger;
    }

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @RequestMapping("/list")
    public List<String> list() {
        if (logger().isDebugEnabled()) {
            logger().debug(String.format("Data : %s", data));
        }

        return data;
    }

    @GetMapping("/send/{payload}")
    public String send(@PathVariable String payload) {
        sender.send(KafkaConsumerTestConstant.TOPIC, payload);

        if (logger().isDebugEnabled()) {
            logger().debug("Published: {}", payload);
        }

        return payload;
    }

    /**
     * Another option:<br/>
     *
     * <pre>
     * <code>@KafkaListener(id = "loader", groupId = GROUP_ID, topicPartitions = {</code>
     * <code>@TopicPartition(topic = TOPIC, partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")) })</code>
     * </pre>
     *
     * @param message
     */
    @KafkaListener(topics = KafkaConsumerTestConstant.TOPIC, groupId = KafkaConsumerTestConstant.GROUP_ID)
    public void listen(@Payload String message) {
        data.add(message);

        if (logger().isDebugEnabled()) {
            logger().debug(String.format("Received message : %s", message));
        }
    }
}