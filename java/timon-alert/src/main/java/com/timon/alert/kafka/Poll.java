package com.timon.alert.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//@Service
@Slf4j
public class Poll {


    private KafkaConsumer<String, String> kafkaConsumer;

    public void fetch() {
        kafkaConsumer.subscribe(Arrays.asList("users"));

        while (true) {

            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(200));

            for (ConsumerRecord<String, String> record : records) {

            /*
            Whenever there's a new message in the Kafka topic, we'll get the message in this loop, as the record object.
             */

            /*
            Getting the message as a string from the record object.
             */
                String message = record.value();

            /*
            Logging the received message to the console.
             */
                log.info("Received message: " + message);



            /*
            Once we finish processing a Kafka message, we have to commit the offset so that we don't end up consuming the same message endlessly. By default, the consumer object takes care of this. But to demonstrate how it can be done, we have turned this default behaviour off, instead, we're going to manually commit the offsets.
            The code for this is below. It's pretty much self explanatory.
             */
                {
                    Map<TopicPartition, OffsetAndMetadata> commitMessage = new HashMap<>();

                    commitMessage.put(new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1));

                    kafkaConsumer.commitSync(commitMessage);

                    log.info("Offset committed to Kafka.");
                }
            }
        }
    }
}
