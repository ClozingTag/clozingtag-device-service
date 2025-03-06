## Overview
ClosingTag Inc is a mobile application testing service provider  (TAAS) Testing as a service, that assigns mobile devices to staff and can control the state of the each device and send notifications about the state of the device

# Device Service API
The Devices Service is service in a Microservice Architecuture built by ClosingTag Inc with Spring Boot for managing device resources. It provides functionalities for creating, reading, updating, and deleting devices, with domain validations, pagination, and more. 
It is containerized using Docker and can be deployed on Kubernetes.

## Features

### Device Management
- Create, update, fetch, and delete devices.
- Fetch devices by brand or state.
- Pagination support for fetching devices.

### Authentication & Authorization
- JWT-based authentication.
- Role-based access control (e.g., only admins can delete devices)

### Audit Logging
- Track device modifications (create, update, delete).

### API Documentation
- Swagger UI for interactive API documentation.

### Containerization
- Dockerized application with Kubernetes deployment support.

## Technologies Used
- Java 21+
- Spring Boot 3.x
- PostgreSQL (database)
- Redis (caching)
- Docker (containerization)
- Kubernetes (orchestration)
- Swagger (API documentation)
- JWT (authentication)
- Mockito Junit (unit testing)
- Cucumber (integration testing)

## Prerequisites
- Java 21+
- Maven 3.9+
- Docker
- Kubernetes (optional)
- PostgreSQL
- Redis

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/ClozingTag/clozingtag-device-service.git
cd clozingtag-device-service
```


### 4. Build and Run the Application
- Build the application using Maven:

```bash
mvn clean install
```

- Run the application:

```bash
mvn spring-boot:run
```

### 5. Access the API
- The API will be available at `http://localhost:8080`.
- Access Swagger UI at `http://localhost:8080/swagger-ui.html`.
- Open Api Docs at `http://localhost:8080/v3/api-docs`.

## Kubernetes Deployment

### 1. Apply Kubernetes Configuration
- Deploy the application using the provided `deployment.yaml`:

```bash
kubectl apply -f deployment.yaml
```

### 2. Access the Application
- Expose the service:

```bash
kubectl expose deployment devices-api --type=LoadBalancer --port=8080
```

- Access the API using the external IP provided by Kubernetes.

## API Endpoints

| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | /devices | Create a new device. |
| PUT | /devices/{id} | Update an existing device. |
| GET | /devices/{id} | Fetch a single device by ID. |
| GET | /devices | Fetch all devices (paginated). |
| GET | /devices/brand/{brand} | Fetch devices by brand (paginated). |
| GET | /devices/state/{state} | Fetch devices by state (paginated). |
| DELETE | /devices/{id} | Delete a device by ID. |
| POST | /auth/login | Authenticate and get a JWT token. |
| GET | /login/oauth2/code/google | Login with Google OAuth2. |

## Authentication
- Use the `/auth/login` endpoint to authenticate and obtain a JWT token.
- Include the token in the Authorization header for secured endpoints:

```http
Authorization: Bearer <token>
```

## Rate Limiting
- Rate limiting is applied to all endpoints. The default limit is 100 requests per minute per IP.

## Audit Logging
- All device modifications (create, update, delete) are logged in the `audit_log` table.
- Query the `audit_log` table to view modification history.

## Testing

### Unit Tests
Run unit tests using Maven:
```bash
mvn test
```

### Integration Tests
Run Cucumber integration tests:
```bash
mvn verify
```

## Future Improvements
- Add support for Prometheus and Grafana for monitoring.
- Convert to a Microservice (OAuth2, gateway and auth service)
- Implement distributed tracing using Jaeger or Zipkin.
- Add API Gateway for advanced routing and filtering.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature/bugfix.
3. Submit a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Contact
For questions or feedback, please contact:
- **Saint Deemene**
- **Email:** saint@closingtag.com
- **GitHub:** [stdeemene](https://github.com/stdeemene)

