package com.tico.sender.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
class TopicConfigurations {
    List<TopicConfiguration> topics;

    @Getter
    @Setter
    static class TopicConfiguration {
        String name;
        Integer numPartitions = 1;
        Short replicationFactor = 1;

        NewTopic toNewTopic() {
            return new NewTopic(this.name, this.numPartitions, this.replicationFactor);
        }
    }
}
