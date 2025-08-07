# Sử dụng JDK 21
FROM eclipse-temurin:21-jdk

# Thư mục làm việc trong container
WORKDIR /app

# Copy file JAR từ Maven build vào container
COPY target/Sales_Analyst-0.0.1-SNAPSHOT.jar app.jar

# Expose port ứng dụng
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
