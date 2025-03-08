# ClozingTag Microservices

## Overview
ClozingTag Inc provides a **Testing-as-a-Service (TaaS)** platform, offering mobile device management, authorization, notifications, and API gateway services within a microservices architecture. The platform is containerized with **Docker** and orchestrated using **Kubernetes**.

## Microservices
This repository contains five key services:

1. **ClozingTag Discovery Service (Eureka Server)**
2. **ClozingTag Gateway Service (Spring Cloud Gateway)**
3. **ClozingTag Auth Service (Spring Security & JWT Authentication)**
4. **ClozingTag Device Service (Spring Boot & JPA for device management)**
5. **ClozingTag Notification Service (Spring Boot & messaging system for notifications)**

Each service is designed to run independently but communicates through service discovery and API gateway.

---

## 1. ClozingTag Discovery Service (Eureka Server)

### Description
This service acts as the **Service Registry**, helping other microservices discover and communicate with each other.

### Features
- Service discovery for microservices
- High availability with multiple Eureka instances

### Setup Instructions
#### Build and Run Locally
```bash
git clone https://github.com/ClozingTag/clozingtag-discovery-service.git
cd clozingtag-discovery-service
mvn clean install
mvn spring-boot:run
```
- The service will be exposed on `http://localhost:8761`.

---

## 2. ClozingTag Gateway Service

### Description
This service is the **API Gateway**, handling routing, authentication, and security policies.

### Features
- Global CORS configuration
- Authentication and authorization with JWT
- API routing and load balancing

### Setup Instructions
#### Build and Run Locally
```bash
git clone https://github.com/ClozingTag/clozingtag-gateway-service.git
cd clozingtag-gateway-service
mvn clean install
```
To have the redis db local instance
```bash
docker compose up -d 
```
To run the application
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run

```
- API Gateway will be available at `http://localhost:8181/webjars/swagger-ui/index.html`. with a select definition to show the contract of the other services

---

## 3. ClozingTag Auth Service

### Description
Handles user authentication, role-based access control, and JWT token management.

### Features
- User registration & login
- JWT-based authentication
- Role-based authorization

### Setup Instructions
#### Build and Run Locally
```bash
git clone https://github.com/ClozingTag/clozingtag-auth-service.git
cd clozingtag-auth-service
mvn clean install
```
To have the postgres db local instance
```bash
docker compose up -d 
```
To run the application
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

- The service will be available at `http://localhost:8181/webjars/swagger-ui/index.html?urls.primaryName=ClozingTag+Auth+Service`.

---

## 4. ClozingTag Device Service

### Description
Manages mobile devices assigned to staff.

### Features
- CRUD operations for devices
- Fetch devices by brand and state
- Role-based permissions for device modifications

### API Endpoints
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | /devices | Create a new device. |
| PUT | /devices/{id} | Update an existing device. |
| GET | /devices/{id} | Fetch a single device by ID. |
| GET | /devices | Fetch all devices (paginated). |
| GET | /devices/brand/{brand} | Fetch devices by brand (paginated). |
| GET | /devices/state/{state} | Fetch devices by state (paginated). |
| DELETE | /devices/{id} | Delete a device by ID. |

You can perform all/other the api calls from [this](http://localhost:8181/webjars/swagger-ui/index.html)  just select the definition add the token (only token no bearer as it will be auto added)

### Setup Instructions
#### Build and Run Locally
```bash
git clone https://github.com/ClozingTag/clozingtag-device-service.git
cd clozingtag-device-service
mvn clean install
```
To have the postgres db local instance
```bash
docker compose up -d 
```
To run the application
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

- The service will be available at `http://localhost:8181/webjars/swagger-ui/index.html?urls.primaryName=ClozingTag+Device+Service`.
---

## 5. ClozingTag Notification Service

### Description
Handles notification delivery related to device assignments.

### Features
- Sends notifications to users
- Supports email and push notifications

### Setup Instructions
#### Build and Run Locally
```bash
git clone https://github.com/ClozingTag/clozingtag-notification-service.git
cd clozingtag-notification-service
mvn clean install
```
To have the postgres db local instance
```bash
docker compose up -d 
```
To run the application
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

- The service will be available at `http://localhost:8181/webjars/swagger-ui/index.html?urls.primaryName=ClozingTag+Nofitication+Service`.
---

## Deployment Order
Prerequisites
Before deploying the services, ensure you have the following:
Minikube installed and running
Kubectl installed
Docker installed and configured
GitHub Container Registry (GHCR) secret added to Kubernetes

### 1. Start Minikube
```bash
minikube start --driver=docker
```

### **To deploy all services on Kubernetes:**
1. **Deploy Discovery Service**
   ```bash
   kubectl apply -f k8s/clozingtag-discovery-service-deployment.yaml
   ```
2. **Deploy Gateway Service**
   ```bash
   kubectl apply -f k8s/clozingtag-gateway-service-db-deployment.yaml
   kubectl apply -f k8s/clozingtag-gateway-service-deployment.yaml
   ```
3. **Deploy Auth Service (DB first, then service)**
   ```bash
   kubectl apply -f k8s/clozingtag-auth-service-db-deployment.yaml
   kubectl apply -f k8s/clozingtag-auth-service-deployment.yaml
   ```
4. **Deploy Device Service (DB first, then service)**
   ```bash
   kubectl apply -f k8s/clozingtag-device-service-db-deployment.yaml
   kubectl apply -f k8s/clozingtag-device-service-deployment.yaml
   ```
5. **Deploy Notification Service (DB first, then service)**
   ```bash
   kubectl apply -f k8s/clozingtag-notification-service-db-deployment.yaml
   kubectl apply -f k8s/clozingtag-notification-service-deployment.yaml
   ```

---

## Authentication & Security
## Authentication
- Create a role (no token needed) (can be done from the swagger page)
---bash
    curl -X 'POST' \
      'http://localhost:8181/api/auth/v1/roles' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
      "role": "User" //"Admin"
    }'
---
- Create a user (no token needed) (can be done from the swagger page)
---bash
      curl -X 'POST' \
      'http://localhost:8181/api/auth/v1/guests/user' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
      "lastname": "Saint",
      "firstname": "Smiles",
      "username": "saintsmiles@clozingtag.com",
      "password": "smiles"
    }'
---
- Response
```bash
 {
     "id": 1,
     "name": "Saint Smiles",
     "username": "saintsmiles@clozingtag.com",
     "lastname": "Saint",
     "firstname": "Smiles",
     "roles": [
        {
            "id": 2,
            "name": "ROLE_USER",
            "description": "User",
            "authority": "ROLE_USER"
        }
     ],
     "createdAt": "2025-03-08T08:30:12.678198"
 }
```
- Use Oauth2 login endpoint `/oauth2/token` to authenticate and obtain a JWT token.
```bash
curl --location --request POST 'http://localhost:8181/api/auth/oauth2/token?grant_type=password&username=saintsmiles%40clozingtag.com&password=smiles&scope=openid' \
--header 'Content-Type: application/json'
```
- Include the token in the Authorization header for secured endpoints:

```http
Authorization: Bearer <token>
```

## Audit Logging
- All device modifications (create, update, delete) are logged in the `audit_log` table.
- Query the `audit_log` table to view modification history.

## Testing

### Unit Tests (Device service only)
Run unit tests using Maven:
```bash
mvn test
```

### Integration Tests (Device service only)
Run Cucumber integration tests:
```bash
mvn verify
```
- **Role-Based Access Control (RBAC):** Only admin users can perform specific actions like deleting devices.
---

## Cleanup
To delete all services and databases:
```bash
kubectl delete -f k8s/
```
To stop Minikube:
```bash
minikube stop
```
To delete Minikube cluster:
```bash
minikube delete
```


## Future Improvements
- Implement **Prometheus & Grafana** for monitoring.
- Enable **distributed tracing** using Zipkin or Jaeger.
- Ensure delete is role base (Admin Only)

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

