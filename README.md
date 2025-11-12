# Bank REST API

**REST API** для работы с банковскими пользователями и ролями, построенное на **Spring Boot**, с управлением схемы базы данных через **Liquibase** и интеграцией Docker. В проекте реализованы тесты с использованием **JUnit 5**.

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Yes-lightgrey)
![JUnit 5](https://img.shields.io/badge/JUnit-5-red)

---

## 📌 Описание проекта

- Spring Boot REST API
- Управление схемой базы данных через **Liquibase**
- Документация API через **Springdoc OpenAPI / Swagger**
- Возможность запуска через **Docker Compose**
- Тесты с использованием **JUnit 5**
---

## ⚙️ Требования

- Java JDK 20
- Maven (для сборки локально)
- Docker & Docker Compose (рекомендуется)
- PostgreSQL (если запускаем без Docker)
- JUnit 5

---

## 📝 Документация API

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI YAML**: `docs/openapi.yaml`

> Файл `docs/openapi.yaml` содержит полную спецификацию всех эндпоинтов, параметров, моделей и ответов API.  
> Можно использовать его для генерации клиентского кода или интеграции с Postman.

---

## 🚀 Запуск проекта

### Через Docker (рекомендуется)

1. Разархивируйте проект.
```bash
git clone https://github.com/DilshodKarimov/bank_rest.git
```

2. Перейдите в корневую папку проекта.
```bash
cd bank_rest 
```
3. Соберите и запустите приложение:

```bash
docker compose build
docker compose up app 
```
После запуска:

API доступен по адресу: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui/index.html

YAML спецификация OpenAPI: http://localhost:8080/v3/api-docs.yaml

4. Запуситить тесты:
```bash
docker compose build
docker compose up spring-tests
# или 
mvn test 
```

---
### 📂 Структура проекта
```swift
src/
 ├─ main/
 │   ├─ docs/
 │   │   └─  openapi.yaml/
 │   ├─ java/com/example/bank_rest/
 │   │   ├─ config/
 │   │   ├─ controller/
 │   │   ├─ dto/
 │   │   ├─ entity/
 │   │   ├─ exception/
 │   │   ├─ repository/
 │   │   ├─ service/
 │   │   └─ util/ 
 │   └─ resources/
 │       ├─ application.yaml
 │       └─ db/changelog/db.changelog-master.yaml
 │       └─ db/changelog/
 │           └─ changes/
 ├─ test/java/com/example/bank_rest/
 │  └─ controller/
```
---