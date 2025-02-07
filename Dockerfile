# Etapa 1: Construção da aplicação
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar o código-fonte
COPY src ./src

# Construir a aplicação
RUN mvn clean package -DskipTests

# Etapa 2: Criar a imagem final
FROM eclipse-temurin:21-jre-alpine

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o JAR construído da etapa anterior
COPY --from=build /app/target/rotafood-api-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

