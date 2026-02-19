# Axon server
# local: D:/Workspace/course/project/library-management-system/_software/AxonServer-2025.2.5
java -jar .\axonserver.jar
# docker:
docker run -d --name axon-server -p 8024:8024 -p 8124:8124 docker.axoniq.io/axoniq/axonserver

# redis: 
# https://www.docker.com/blog/how-to-use-the-redis-docker-official-image/
docker run --name redis -p 6379:6379 -d redis
# redis cli
docker exec -it redis redis-cli

# Jmeter: D:\application\apache-jmeter-5.6.3\bin
java -jar ApacheJMeter.jar

# run spring boot override port
.\mvnw spring-boot:run `"-Dspring-boot.run.arguments=--server.port=xxxx`"