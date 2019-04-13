# start service registry service

```
docker run -it --rm --network host  --name eureka-server eureka:v0.1
```

# start images service

```
git clone git@github.com:wubigo/API.git
cd API/java/image-service

docker build -t image-service:v0.1 .

docker run -it --rm --network host  --name image-service image-service:v0.1

```


启动第二个实例


```
docker run -it --rm --network host  --name image-service2 image-service:v0.1  "exec java -jar image-service-0.1.jar --server.port=8801"
```
