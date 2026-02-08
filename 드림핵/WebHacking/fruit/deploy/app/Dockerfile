FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY ./fms .
# Package the application
RUN mvn clean package -DskipTests

# Use Tomcat image for runtime
FROM tomcat@sha256:d5431860b7c764d1a80604f9f938ffb057afbe6865751b09ac8671f50746c3bd

    
RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

COPY entrypoint.sh /usr/local/tomcat/entrypoint.sh
RUN chmod +x /usr/local/tomcat/entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/usr/local/tomcat/entrypoint.sh"]
CMD ["catalina.sh", "run"]
