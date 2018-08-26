package org.harvan.example.springboot.kafka;
/*
This is an duplicated way to configure an embedded kafka cause maven and the IDE is running them different
Feel free to optimize
 */

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
// @EmbeddedKafka(count=1, topics="test")
public abstract class SpringBootEmbeddedKafka {
	@Autowired
	public KafkaTemplate<String, String> template;

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "test");

	@BeforeClass
	public static void setUpClass() {
		// FIXME: Couldn't find kafka server configurations - kafka server is listening
		// on a random port so i overwrite the client config here should be other way
		// around
		System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
		System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", embeddedKafka.getZookeeperConnectionString());
	}
}