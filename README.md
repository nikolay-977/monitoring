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
java -jar target/monitoring-demo-app-0.0.1-SNAPSHOT.jar
```
