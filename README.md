### Project structure

- ***core*** - contains entities & repositories definition (business model).
- ***persistence*** - contains the implementation of repositories for a specific DBMS.
- ***application*** - contains the high-level business logic (services), DTO.
- ***infrastructure*** - contains the implementation of classes as the technical details.
- ***api*** - contains REST / WebSocket API.
- ***boot*** - contains application runner (entry point), DI configuration.

### Build & Run

```shell
.\gradlew clean && .\gradlew build
```
```shell
docker-compose up -d
```

### Docs

#### REST

Swagger UI: http://localhost:8080/docs

#### WebSocket (STOMP)

Endpoint: http://localhost:8080/ws

##### Authorization

Authorization occurs after opening a WebSocket connection (after handshake) via STOMP. \
All CONNECT frames must have authorization header:

```
Authorization: Bearer <JWT>
```

##### Topics
- ***/messaging/{user_id}/conversations*** - subscribe to receive IDs of user conversations that have new messages.
- ***/messaging/{conversation_id}/messages*** - subscribe to receive IDs of new conversation messages.


