FROM openjdk:17-jdk-slim
# ADD books-shelf.jar application.jar
ENTRYPOINT ["java", "-jar","application.jar"]

ENV DATABASE_URL jdbc:postgresql://babar.db.elephantsql.com:5432/gtsfkczj
ENV DATABASE_USERNAME gtsfkczj
ENV DATABASE_PASSWORD kkhj1lGXZgdnHilSZFhbE7V3r-aCsTTz
ENV DATABASE_DRIVER org.postgresql.Driver

ENV SPRING_DATASOURCE_URL jdbc:postgresql://babar.db.elephantsql.com:5432/gtsfkczj
ENV SPRING_DATASOURCE_USERNAME gtsfkczj
ENV SPRING_DATASOURCE_PASSWORD kkhj1lGXZgdnHilSZFhbE7V3r-aCsTTz

EXPOSE 8080