/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.harvan.example.springboot.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
   * Another option:<br>
   *
   * <pre>
   * <code>@KafkaListener(id = "loader", groupId = GROUP_ID, topicPartitions = {</code>
   * <code>@TopicPartition(topic = TOPIC, partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")) })</code>
   * </pre>
   */
  @KafkaListener(
      topics = KafkaConsumerTestConstant.TOPIC,
      groupId = KafkaConsumerTestConstant.GROUP_ID)
  public void listen(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
    String message = consumerRecord.value();
    data.add(message);

    if (logger().isDebugEnabled()) {
      logger().debug(String.format("Received message : %s", message));
    }
    if ("test".equals(message)) {
      throw new RuntimeException("Expected exception.");
    }

    acknowledgment.acknowledge();
  }

  @KafkaListener(
      topics = KafkaConsumerTestConstant.TOPIC,
      groupId = KafkaConsumerTestConstant.GROUP_ID)
  public void listen2(ConsumerRecord<String, String> consumerRecord) {
    String message = consumerRecord.value();
    data.add(message);

    if (logger().isDebugEnabled()) {
      logger().debug(String.format("Received message : %s", message));
    }
  }

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> configs = new HashMap<>();

    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumerTestConstant.GROUP_ID);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

    return new DefaultKafkaConsumerFactory<>(configs);
  }

  @GetMapping("/seek/{offset}")
  public String seek(@PathVariable Long offset) {
    TopicPartition tp = new TopicPartition("test", 0);

    try (Consumer<String, String> consumer = consumerFactory().createConsumer()) {
      consumer.assign(Collections.singleton(tp));
      consumer.seek(tp, offset);

      ConsumerRecords<String, String> poll = consumer.poll(100); // TODO optimize
      Iterator<ConsumerRecord<String, String>> iterator = poll.iterator();

      if (iterator.hasNext()) {
        ConsumerRecord<String, String> next = iterator.next(); // TODO validate
        return next.value();

        //        Map<TopicPartition, OffsetAndMetadata> commits =
        //            Collections.singletonMap(
        //                new TopicPartition(tp.topic(), tp.partition()), new
        // OffsetAndMetadata(offset));
        //        consumer.commitSync(commits);

      }
    }

    return "NOT_FOUND";
  }
}
