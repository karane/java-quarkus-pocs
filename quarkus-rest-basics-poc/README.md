# quarkus-rest-basics-poc

RESTful endpoints with Quarkus REST: `@GET`, `@POST`, `@PUT`, `@DELETE`, path/query params, request/response bodies, and JSON serialization with Jackson.

## Prerequisites

- Java 17+
- Maven 3.9+
- jq

## Running

### Dev mode

```bash
mvn quarkus:dev
```

### Run tests

```bash
mvn test
```

## Checking results / output

### List all products

```bash
curl -s http://localhost:8080/products | jq .
```

### Filter by query param

```bash
curl -s "http://localhost:8080/products?category=electronics" | jq .
```

### Get by path param

```bash
curl -s http://localhost:8080/products/1 | jq .
```

### Create — POST with JSON body

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Monitor","price":499.99,"category":"electronics"}'
```

### Update — PUT

```bash
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Gaming Laptop","price":1599.99,"category":"electronics"}'
```

### Delete

```bash
curl -X DELETE http://localhost:8080/products/1
```
