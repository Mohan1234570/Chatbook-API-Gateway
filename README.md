# Chatbook – Enterprise Microservices Architecture

## 📌 Overview
Chatbook is an **enterprise-style microservices application** built using **Spring Boot, Spring Cloud Gateway, OAuth2/JWT, Redis, and WebSockets**.

The system follows a **Single Entry Point Architecture**, where **all external traffic flows through the API Gateway**, including authentication, authorization, rate limiting, and routing.
---

## 🏗️ System Architecture


### Authentication
- Handled by **Auth Service**
- API Gateway routes `/api/auth/**` requests
- Auth Service issues **JWT tokens**

### Authorization
- Enforced **at API Gateway**
- JWT validated using **JWKS** from Auth Service
- Role-based access (`ROLE_USER`, `ROLE_ADMIN`)

### Why API Gateway as Resource Server?
✔ Centralized security  
✔ Zero trust between services  
✔ Backend services are never exposed directly  

---

## 🚦 Rate Limiting Strategy (Redis)

| Endpoint | Key Resolver | Purpose |
|-------|-------------|--------|
| `/api/auth/**` | IP Address | Prevent brute-force login attacks |
| `/api/chatbook/**` | Tenant/User ID | Fair usage per tenant |
| `/api/notifs/**` | Tenant/User ID | Protect notification APIs |

---

## 🔁 Request Flow Examples

### 🔐 Login Flow
1. Client → `POST /api/auth/login`
2. API Gateway routes request to Auth Service
3. Auth Service validates credentials
4. Auth Service issues JWT
5. Client stores JWT

---

### 📦 Protected API Flow
1. Client → `GET /api/chatbook/posts`
2. API Gateway validates JWT
3. API Gateway applies rate limiting
4. Request forwarded to Post Service
5. Response returned to client

---

### 🔔 WebSocket Flow
1. Client connects to `/ws/notifications`
2. API Gateway authenticates handshake
3. Connection forwarded to Notification Service
4. Real-time events pushed to client

---

## 🧩 Services Summary

| Service | Port | Responsibility |
|------|------|--------------|
| API Gateway | 8080 | Routing, Security, Rate Limiting |
| Auth Service | 8083 | Login, Register, JWT, Refresh |
| Post Service | 8084 | Post CRUD APIs |
| Notification Service | 8082 | Notifications + WebSocket |
| Redis | 6379 | Rate limiting backend |

---

## 🛠 Tech Stack

- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Security (OAuth2 Resource Server)
- JWT + JWKS
- Redis
- WebSocket
- Apache Kafka
- Maven

---

## ✅ Enterprise Best Practices Followed

✔ Single Entry Point (API Gateway)  
✔ Zero Trust Microservices  
✔ Centralized Security  
✔ Rate Limiting & Abuse Protection  
✔ Stateless JWT Authentication  
✔ Scalable & Cloud Ready  

---

## 🚀 Future Enhancements

- Centralized Logging (ELK Stack)
- Distributed Tracing (Zipkin / OpenTelemetry)
- Circuit Breakers (Resilience4j)
- API Documentation (Swagger via Gateway)
- Docker & Kubernetes Deployment

---

## 👨‍💻 Author
**Chatbook – Enterprise Microservices Project**

> This project demonstrates a **real-world, enterprise-grade API Gateway architecture** using Spring Cloud.

### Authentication
- Handled by **Auth Service**
- API Gateway routes `/api/auth/**` requests
- Auth Service issues **JWT tokens**

### Authorization
- Enforced **at API Gateway**
- JWT validated using **JWKS** from Auth Service
- Role-based access (`ROLE_USER`, `ROLE_ADMIN`)

### Why API Gateway as Resource Server?
✔ Centralized security  
✔ Zero trust between services  
✔ Backend services are never exposed directly  

---

## 🚦 Rate Limiting Strategy (Redis)

| Endpoint | Key Resolver | Purpose |
|-------|-------------|--------|
| `/api/auth/**` | IP Address | Prevent brute-force login attacks |
| `/api/chatbook/**` | Tenant/User ID | Fair usage per tenant |
| `/api/notifs/**` | Tenant/User ID | Protect notification APIs |

---

## 🔁 Request Flow Examples

### 🔐 Login Flow
1. Client → `POST /api/auth/login`
2. API Gateway routes request to Auth Service
3. Auth Service validates credentials
4. Auth Service issues JWT
5. Client stores JWT

---

### 📦 Protected API Flow
1. Client → `GET /api/chatbook/posts`
2. API Gateway validates JWT
3. API Gateway applies rate limiting
4. Request forwarded to Post Service
5. Response returned to client

---

### 🔔 WebSocket Flow
1. Client connects to `/ws/notifications`
2. API Gateway authenticates handshake
3. Connection forwarded to Notification Service
4. Real-time events pushed to client

---

## 🧩 Services Summary

| Service | Port | Responsibility |
|------|------|--------------|
| API Gateway | 8080 | Routing, Security, Rate Limiting |
| Auth Service | 8083 | Login, Register, JWT, Refresh |
| Post Service | 8084 | Post CRUD APIs |
| Notification Service | 8082 | Notifications + WebSocket |
| Redis | 6379 | Rate limiting backend |

---

## 🛠 Tech Stack

- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Security (OAuth2 Resource Server)
- JWT + JWKS
- Redis
- WebSocket
- Apache Kafka
- Maven

---

## ✅ Enterprise Best Practices Followed

✔ Single Entry Point (API Gateway)  
✔ Zero Trust Microservices  
✔ Centralized Security  
✔ Rate Limiting & Abuse Protection  
✔ Stateless JWT Authentication  
✔ Scalable & Cloud Ready  

---

## 🚀 Future Enhancements

- Centralized Logging (ELK Stack)
- Distributed Tracing (Zipkin / OpenTelemetry)
- Circuit Breakers (Resilience4j)
- API Documentation (Swagger via Gateway)
- Docker & Kubernetes Deployment

---

## 👨‍💻 Author
**Chatbook – Enterprise Microservices Project**

> This project demonstrates a **real-world, enterprise-grade API Gateway architecture** using Spring Cloud.
