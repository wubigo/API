#  启动测试

```
mvn spring-boot:run
```

#  打包

```
mvn -Dmaven.test.skip=true package
```

# 改变消息发送主题

```
java -jar timon-alert-0.1.jar --spring.application.json='{"spring.kafka.topic.alert-raw":"test"}'
```

# 改变消费组

```
java -jar timon-alert-0.1.jar --spring.application.json='{"spring.kafka.consumer.group-id":"timon-alert"}'
```
