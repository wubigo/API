Configuring the context path is servlet-specific. 
To reflect that fact and avoid confusion when using WebFlux, 
the configuration property was renamed to server.servlet.context-path(start-web)

https://github.com/spring-projects/spring-boot/issues/10129



Spring WebFlux does not support HTTP/2 with Netty. 
There is also no support for pushing resources programmatically to the client.