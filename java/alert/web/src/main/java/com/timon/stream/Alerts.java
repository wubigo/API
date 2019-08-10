package com.timon.stream;

import com.timon.common.JsonUtil;

import com.timon.domain.Alert;
import lombok.extern.slf4j.Slf4j;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;

import java.util.Properties;

@Slf4j
public class Alerts {



    /**
     * stream.map get a single alert every time
     * @param fc
     */
    public static void getAlert(FlinkConsumer fc) {
        log.info("bootstrap.servers={}", fc.getBootstrap_servers());
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", fc.getBootstrap_servers());
        properties.setProperty("group.id", fc.getGroup_id());
        properties.setProperty("auto.offset.reset", "earliest");

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        FlinkKafkaConsumer fkc = new FlinkKafkaConsumer<>(fc.getTopics(), new SimpleStringSchema(), properties);
        // fkc.assignTimestampsAndWatermarks(new CustomWatermarkEmitter());
        DataStream<String> stream = env.addSource(fkc);
        DataStream<AlertWithCount> as =
                stream.map(new MapFunction<String, AlertWithCount>() {
                    @Override
                    public AlertWithCount map(String s) {
                        Alert[]  alerts =  JsonUtil.readArray(s);

                        log.info("{} alerts fetched", alerts.length);
                        if ( !"c, m, w".contains(s) ) {
                            log.error("invalid level={}", s);
                            return null;
                        }
                        return new AlertWithCount(s, 1);
                    }
                })
                .filter( aw -> aw != null)
                .keyBy("level")
                .timeWindow(Time.seconds(60), Time.seconds(60))
                .reduce((ac1, ac2) -> new AlertWithCount(ac1.level, ac1.count + ac2.count));
//                .reduce(new ReduceFunction<AlertWithCount>() {
//                    @Override
//                    public AlertWithCount reduce(AlertWithCount a, AlertWithCount b) {
//                        return new AlertWithCount(a.level, a.count + b.count);
//                    }
//                });

        as.print();

        try {
            env.execute("alert history");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * stream.map get a single alert every time
     * @param fc
     */
    public static void getAlerts(FlinkConsumer fc) {
        log.info("bootstrap.servers={}", fc.getBootstrap_servers());
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", fc.getBootstrap_servers());
        properties.setProperty("group.id", fc.getGroup_id());
        properties.setProperty("auto.offset.reset", "earliest");

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        FlinkKafkaConsumer fkc = new FlinkKafkaConsumer<>(fc.getTopics(), new SimpleStringSchema(), properties);
        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost(fc.getRedis_host()).build();
        // fkc.assignTimestampsAndWatermarks(new CustomWatermarkEmitter());
        DataStream<String> stream = env.addSource(fkc);
        DataStream<AlertWithCount> as =
                stream.flatMap(new FlatMapFunction<String, AlertWithCount>() {
                                    @Override
                                    public void flatMap(String jsonAlerts, Collector<AlertWithCount> out) {
                                        Alert[]  alerts =  JsonUtil.readArray(jsonAlerts);
                                        log.info("{} alerts fetched", alerts.length);
                                        for ( int i=0; i < alerts.length ; i++ ) {
                                            Alert alert = alerts[i];
                                            out.collect(new AlertWithCount(alert.getLevel(), 1L));
                                        }
                                   }
                                })
                        .filter(aw -> aw != null)
                        .keyBy("level")
                        .timeWindow(Time.seconds(180), Time.seconds(180))
                        .reduce((ac1, ac2) -> new AlertWithCount(ac1.level, ac1.count + ac2.count));
        RedisSink<AlertWithCount> redisSink = new RedisSink<>(conf, new RedisAlertMapper());
        //sink state into reids
        as.addSink( redisSink );
        as.print();

        try {
            env.execute("alert history");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Data type for words with count
    public static class AlertWithCount {

        public String level;
        public long count;

        public AlertWithCount() {}

        public AlertWithCount(String level, long count) {
            this.level = level;
            this.count = count;
        }



        @Override
        public String toString() {
            return level + " : " + count;
        }
    }

    /*
    127.0.0.1:6379> HMSET user:1000 username antirez password P1pp0 age 34
    OK
    127.0.0.1:6379> HMGET user:1000
    (error) ERR wrong number of arguments for 'hmget' command
    127.0.0.1:6379> HMGET user:1000 username
    1) "antirez"
    127.0.0.1:6379> HMGET user:1000 password
    1) "P1pp0"
    127.0.0.1:6379> KEYS *
    1) "user:1000"
    2) "_METRIC_Group550"
     */
    static class RedisAlertMapper implements RedisMapper<AlertWithCount> {

        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.LPUSH, "alert:");
        }

        @Override
        public String getKeyFromData(AlertWithCount data) {
            return "alert:history:24h";
        }

        @Override
        public String getValueFromData(AlertWithCount data) {
            return data.toString() ;
        }
    }
}
