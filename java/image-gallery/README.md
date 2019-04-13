# start service registry service

```
docker run -it --rm --network host  --name eureka-server eureka:v0.1
```

# start image-gallery service

```
git clone git@github.com:wubigo/API.git
cd API/java/image-gallery

docker build -t image-gallery:v0.1 .

docker run -it --rm --network host  --name image-gallery image-gallery:v0.1

```

启动第二个实例


```
docker run -it --network host  --name image-gallery image-gallery:v0.1 exec java -jar image-gallery-0.1.jar --server.port=8301
```
