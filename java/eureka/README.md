
```
mvn package
docker build -t eureka:v0.1
docker run -it --rm --network host  --name eureka-server eureka:v0.1
```
