# Используйте официальный образ OpenJDK 11
FROM openjdk:11-jdk

# Переменная сборки для jar файла
ARG JAR_FILE=target/*.war

# Создание каталога /app в контейнере
WORKDIR /app

# Копирование jar файла из локальной целевой папки в контейнер
COPY ${JAR_FILE} app.war

# Запуск приложения с помощью java -jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "app.war"]