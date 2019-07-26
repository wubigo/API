#  本地测试

```
mvn spring-boot:run
```

#  打包

```
mvn -Dmaven.test.skip=true package
```

# 部署测试

```
http://localhost:8080/timon-alert-0.1/actuator/mappings
```

# 改变消息发送主题

```
java -jar timon-alert-0.1.jar --spring.application.json='{"spring.kafka.topic.alert-raw":"test"}'
```

# 改变消费组

```
java -jar timon-alert-0.1.jar --spring.application.json='{"spring.kafka.consumer.group-id":"timon-alert"}'
```


# API

```$xslt
http://localhost/swagger-ui.html
```