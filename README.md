# spring-cloud-contract-examples [![Build Status](https://travis-ci.org/daggerok/spring-cloud-contract-examples.svg?branch=master)](https://travis-ci.org/daggerok/spring-cloud-contract-examples)
Develop lighten stably tested microservices infrastructure.

## maven

### mvn

first, implement [maven-rest-mvc-functional-producer](maven-rest-mvc-functional-producer) according to agreed contracts

```bash
jdk11 # point JAVA_HOME to JDK 11
./mvnw -f maven-rest-mvc-functional-producer clean deploy
```

next implement [maven-rest-mvc-functional-consumer](maven-rest-mvc-functional-consumer) by using `maven-rest-mvc-functional-producer-stubs` artifact...

```bash
jdk11 # point JAVA_HOME to JDK 11
./mvnw -f maven-rest-mvc-functional-consumer
```

## resources

* https://cloud.spring.io/spring-cloud-contract/reference/html/project-features.html#contract-dsl-multiple
* https://cloud.spring.io/spring-cloud-contract/1.2.x/multi/multi__spring_cloud_contract_stub_runner.html#_classpath_scanning
* https://github.com/spring-guides/gs-contract-rest/tree/master/complete/
* https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html
* https://cloud.spring.io/spring-cloud-contract/2.0.x/multi/multi__contract_dsl.html#contract-dsl-http-top-level-elements
* [GitHub: Spring Cloud Contract Samples](https://github.com/spring-cloud-samples/spring-cloud-contract-samples)
* https://www.youtube.com/watch?v=MDydAqL4mYE
* https://www.youtube.com/watch?v=sAAklvxmPmk
* https://cloud.spring.io/spring-cloud-contract/reference/html

## reference

* [Spring Cloud Contract Verifier Setup](https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract.html#_spring_cloud_contract_verifier_setup)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#production-ready)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/maven-plugin/)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
