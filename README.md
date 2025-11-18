# Monitoring demo app

## Клонируйте репозиторий:

```bash
git clone https://github.com/nikolay-977/monitoring
cd monitoring
 ```

## Сборка приложения
```bash
mvn clean package
```

## Docker build
```bash
docker build -t monitoring-demo-app:latest .
```

## Запуск приложения
```bash
java -jar target/monitoring-0.0.1-SNAPSHOT.jar
```

## Postman-коллекция для тестировавния

[Learning Platform API.postman_collection.json](src/test/resources/postman/Learning%20Platform%20API.postman_collection.json)
