#  本地测试

```
sudo systemctl start redis.service
nohup mvn spring-boot:run >/dev/null 2>&1   &
tail -f ../logs/timon-core.log
```

#  打包

```
mvn -Dmaven.test.skip=true package
```



# docker image 

```
mvn -Dmaven.test.skip=true install dockerfile:build
docker run -it --network host registry.cn-beijing.aliyuncs.com/j8/alert-core:0.1
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

```
http://localhost/swagger-ui.html
```