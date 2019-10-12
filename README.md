```
./mvnw clean install
```

The idea is pretty simple, an application that makes a REST call to some external service and then puts that result into Redis.  But the interesting bit is Redis is using TestContainers on a dynamic port.  Wiremock is being used to mock the other service on a dynamic port and all that is being used within the application, in essence this would not break a CI server.
