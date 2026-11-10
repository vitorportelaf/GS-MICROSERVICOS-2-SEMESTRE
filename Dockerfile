# Etapa 1: compila o projeto com Maven
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /opt/app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# Etapa 2: imagem final só com o Java (JRE) e o app.jar
FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
RUN addgroup -S horacerta && adduser -S horacerta -G horacerta
USER horacerta
COPY --from=build /opt/app/target/app.jar /opt/app/app.jar

# No container o padrão é produção (variáveis DB_*). Para local: -e SPRING_PROFILES_ACTIVE=dev
ENV SPRING_PROFILES_ACTIVE=prd
EXPOSE 8080
CMD [ "java", "-jar", "app.jar" ]
