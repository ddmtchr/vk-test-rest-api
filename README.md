## Запуск в Docker

- Запустить Docker Daemon
- Выполнить команду `docker-compose up --build` из корня проекта
- Приложение доступно на `http://localhost:8080/`

### Реализованный функционал:

- GET, POST, PUT, DELETE на `jsonplaceholder.typicode.com` (`/api/posts`, `/api/users`, `/api/albums`)
- Авторизация с использованием JWT, пользователи хранятся в БД
- Роли: ADMIN, POSTS, POSTS_VIEWER, POSTS_EDITOR, USERS, USERS_VIEWER, USERS_EDITOR, ALBUMS, ALBUMS_VIEWER, ALBUMS_EDITOR, WEBSOCKET
- Аудит запросов в БД PostgreSQL
- Inmemory кэш
- Регистрация и авторизация: `/api/auth/register`, `/api/auth/login`
- Unit, интеграционные тесты
- WebSocket echo server endpoint
