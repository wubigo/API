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
