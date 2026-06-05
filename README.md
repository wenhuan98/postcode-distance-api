# postcode-distance-api

A simple API to calculate the straight line distance between two postcodes in the UK using Haversine formula. 

## Installation
1. Clone the repository
2. Install dependencies using `mvn clean install`

## Setup MySQL Database
1. cd docker (to docker directory)
2. Run `docker-compose -f docker-compose-db.yml up -d` to start the MySQL database container
3. The database will be available at `localhost:3308` with username `postcode_user` (refer application.properties for more details)

## Flyway Migration
1. Start the application using `mvn spring-boot:run`, and it will automatically run the Flyway migrations on startup.
2. The migration scripts are located in `src/main/resources/db/migration` and will create the necessary tables and insert sample data.
3. Alternatively, you can run the Flyway migrations manually using `mvn flyway:migrate -Dflyway.url=jdbc:mysql://localhost:3308/postcode_db -Dflyway.user=postcode_user -Dflyway.password=password`.

## API Overview

- Base URL: `http://localhost:8080`
- Content type: `application/json`
- Authentication: JWT Bearer token is required for all `/api/v1/postcodes/**` endpoints

### Authentication Flow
1. Request a token from `/api/auth/get-token`
2. Use the returned token in the `Authorization` header for protected endpoints

Example header:

```http
Authorization: Bearer <your-jwt-token>
```

## Available Endpoints

### 1) Get JWT Token

**Endpoint**

```http
POST /api/auth/get-token
```

**Authentication**

No authentication required. But username and password is required in the request body

**Request Body**

```json
{
  "username": "test-user",
  "password": "test-password"
}
```

**Successful Response** — `200 OK`

```text
<jwt-token>
```

---

### 2) Calculate Distance Between Two Postcodes

**Endpoint**

```http
POST /api/v1/postcodes/distance?country=UK
```

**Authentication**

Bearer token required.

**Request Body**

```json
{
  "origin": "SW1A 1AA",
  "destination": "SW1A 2AA"
}
```

**Successful Response** — `200 OK`

```json
{
  "origin": {
    "postcode": "SW1A 1AA",
    "coordinate": {
      "latitude": 51.501009,
      "longitude": -0.141588
    }
  },
  "destination": {
    "postcode": "SW1A 2AA",
    "coordinate": {
      "latitude": 51.503541,
      "longitude": -0.12767
    }
  },
  "distanceBetween": 0.98,
  "measureUnit": "km"
}
```

---

### 3) Add a Postcode Location

**Endpoint**

```http
POST /api/v1/postcodes?country=UK
```

**Authentication**

Bearer token required.

**Request Body**

```json
{
  "postcode": "SW1A 3AA",
  "coordinate": {
    "latitude": 51.501009,
    "longitude": -0.141588
  }
}
```

**Successful Response** — `201 Created`

```json
{
  "postcode": "SW1A 3AA",
  "coordinate": {
    "latitude": 51.501009,
    "longitude": -0.141588
  }
}
```

**Error Response** — `409 Conflict`

```json
{
  "timestamp": "2026-06-05T12:34:56Z",
  "status": 409,
  "error": "Conflict",
  "message": "Postcode already exists in the system: SW1A 2AA"
}
```

---

### 4) Update a Postcode Location

**Endpoint**

```http
PUT /api/v1/postcodes?country=UK&postcode=AB10%201XG
```

**Authentication**

Bearer token required.

**Request Body**

```json
{
  "latitude": 12.22,
  "longitude": -0.141588
}
```

**Successful Response** — `200 OK`

```json
{
  "postcode": "AB10 1XG",
  "coordinate": {
    "latitude": 12.22,
    "longitude": -0.141588
  }
}
```

**Error Response** — `404 Not Found`

```json
{
  "timestamp": "2026-06-05T12:34:56Z",
  "status": 404,
  "error": "Not Found",
  "message": "Postcode not found: AB10 XXX"
}
```

---

### 5) Delete a Postcode Location

**Endpoint**

```http
DELETE /api/v1/postcodes?country=UK&postcode=AB10%201XG
```

**Authentication**

Bearer token required.

**Request Body**

No request body.

**Successful Response** — `204 No Content`

Response body is empty.

## Quick Test Flow

1. Get a token
2. Use that token to call the protected postcode endpoints

Example sequence:

```bash
curl -X POST http://localhost:8080/api/auth/get-token \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
```

Then use the returned token:

```bash
curl -X POST "http://localhost:8080/api/v1/postcodes/distance?country=UK" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{"origin":"SW1A 1AA","destination":"SW1A 2AA"}'
```
