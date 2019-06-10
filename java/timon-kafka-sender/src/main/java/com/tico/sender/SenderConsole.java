package com.tico.sender;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

@Slf4j
@SpringBootApplication
public class SenderConsole implements CommandLineRunner {


    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${spring.kafka.topic.alert-raw}")
    private String topicName;

    private CountDownLatch latch;


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SenderConsole.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

        String s = readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/cisco2950交换机.json");
        latch = new CountDownLatch(10);
        send(s);

    }

    @NonNull
    public static String readUrl(String urlString) throws Exception{
        URL url = new URL(urlString);
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())) ){
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } catch ( Exception e){
            log.error(e.getMessage());
        }
        return "";
    }

    void send(String s){
        for ( int i=0; i < 10 ; i++ ) {
            log.info("send {}th msg kafka", i);
            this.kafkaTemplate().send(topicName, s);


        }


    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());

        props.forEach((k,v)->log.info("Item : {}  value :{} ", k, v));
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /*
     inject custom version KafkaTemplate in the Spring’s application context.
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /*
    instructing the Kafka’s AdminClient bean (already in the context)
    to create a topic with the given configuration
     */
    @Bean
    public NewTopic createTopic() {
        return new NewTopic(topicName, 1, (short) 1);
    }


}
