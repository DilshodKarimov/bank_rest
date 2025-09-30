<<<<<<< HEAD
# bank_rest
=======
# Bank REST API

REST API для работы с банковскими пользователями и ролями, построенное на Spring Boot, с управлением схемы базы данных через Liquibase и интеграцией Docker.

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Yes-lightgrey)

---

## 📌 Описание проекта

- Spring Boot REST API
- Управление схемой базы данных через **Liquibase**
- Документация API через **Springdoc OpenAPI / Swagger**
- Возможность запуска через **Docker Compose** 

---

## ⚙️ Требования

- Java JDK 17
- Maven (для сборки локально)
- Docker & Docker Compose (рекомендуется)
- PostgreSQL (если запускаем без Docker)

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
2. Перейдите в корневую папку проекта.
3. Соберите и запустите контейнеры:

```bash
docker compose build
docker compose up
```
После запуска:

API доступен по адресу: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui/index.html

YAML спецификация OpenAPI: http://localhost:8080/v3/api-docs.yaml

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
```
---