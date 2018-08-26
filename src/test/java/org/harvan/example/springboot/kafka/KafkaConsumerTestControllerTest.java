package org.harvan.example.springboot.kafka;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.harvan.example.springboot.Application;
import org.harvan.example.springboot.kafka.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TODO: Fix configuration
 * 
 * @author Harvan Irsyadi
 * @version 1.0.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { Application.class })
public class KafkaConsumerTestControllerTest extends SpringBootEmbeddedKafka {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private Sender sender;

	@Test
	public void testList() throws InterruptedException, ExecutionException {
		sender.send("test", String.format("Message {%s}", (Math.random() * 1000)));

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResponseEntity<String[]> responseEntity = restTemplate.getForEntity("/kafka/testconsumer/list", String[].class);
		List<String> body = Arrays.asList(responseEntity.getBody());

		System.out.println(String.format("body: %s", body));
		assertNotNull(body);
	}
}