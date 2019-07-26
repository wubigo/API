package com.timon.stream;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Getter
@Setter
public class FlinkConsumer {


    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrap_servers;
    @Value("${spring.kafka.consumer.group-id}")
    private String group_id;
    @Value("${spring.kafka.topics}")
    private String topics;
    @Value("${spring.redis.host}")
    private String redis_host;

}
