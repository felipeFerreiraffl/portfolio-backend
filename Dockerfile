# Usando uma imagem com Java 17 e Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Usando uma imagem leve para rodar o app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/portfolio-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta 8080
EXPOSE 8080

# Criação do application.properties real
ENTRYPOINT ["/bin/sh", "-c", "echo \"spring.data.mongodb.uri=${MONGODB_URI}\" > application.properties && \
echo \"api.rawg.key=${RAWG_API_KEY}\" >> application.properties && \
echo \"api.rawg.url=${RAWG_API_URL}\" >> application.properties && \
mkdir -p config && mv application.properties config/ && \
java -Dspring.config.additional-location=file:./config/ -jar app.jar"]
