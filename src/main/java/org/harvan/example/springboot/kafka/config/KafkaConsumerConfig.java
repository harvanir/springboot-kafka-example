/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.harvan.example.springboot.kafka.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.harvan.example.springboot.kafka.KafkaConsumerTestConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (6 May 2018)
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
  private final Log logger = LogFactory.getLog(getClass());

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  Log logger() {
    return logger;
  }

  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> configs = new HashMap<>();

    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumerTestConstant.GROUP_ID);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

    return new DefaultKafkaConsumerFactory<>(configs);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(consumerFactory());
    factory
        .getContainerProperties()
        .setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);

    return factory;
  }
}
