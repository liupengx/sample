package com.esgyn.kafka.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esgyn.kafka.service.KConsumer;

public class KConsumerImpl implements KConsumer {
	private static final Logger log = LoggerFactory.getLogger(KConsumerImpl.class);
	private final KafkaConsumer<String, String> consumer;
	private String topic;

	public KConsumerImpl(Properties config) {

		consumer = new KafkaConsumer<>(config);
		this.topic = config.getProperty("topic", "null");
		System.out.println("topic:"+this.topic);
	}

	public void start() {
		consumer.subscribe(this.topic);
		
		while (true) {
			log.warn("go...");
			System.out.println("goo...");
			Map<String, ConsumerRecords<String, String>> map = consumer.poll(0);
			if(map == null){
				try {
					System.out.println("sleep ...");
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				continue;
			}
			log.info(map.size() + "___");
			for (Entry<String, ConsumerRecords<String, String>> records : map.entrySet()) {
				for (ConsumerRecord<String, String> r : records.getValue().records()) {

					try {
						System.out.println("Received message: (" + r.key() + ", " + r.value() + ") at offset " + r.offset());
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
	}

}
