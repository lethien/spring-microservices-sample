# spring-microservices-sample
Follow Learn Microservices with Spring-Boot book

Simple Rest Microservices using:
- Spring MVC
- RabbitMQ
- Eureka + Ribbon
- Zuul

Book link: https://www.amazon.ca/Learn-Microservices-Spring-Boot-Practical/dp/1484231643

To run:
- Install Maven
- Install RabbitMQ and start it on your local
- Start service-registry (localhost:8761) (Eureka + Rabbit)
- Start gateway (localhost:8000) (Zuul)
- Start social-multiplication (localhost:8082) and gamification (localhost:8081) (Rest services)
- Deploy social-mulitplication-ui using a local server (Ex Tomcat)

*Note:
- start a Spring-boot project using Maven in Windows cmd: 
    + navigate to root folder of the project
    + mvnw spring-boot:run
- port of services can be changed in application.properties, server.port