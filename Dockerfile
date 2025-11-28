FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY . .
CMD ["java", "-version"]
