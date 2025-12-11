# Docker Lab - Spring Boot Edition

A complete containerized application demonstrating Docker, Spring Boot, PostgreSQL, and Traefik reverse proxy with Docker Secrets for secure credential management.

![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)
![Traefik](https://img.shields.io/badge/Traefik-37CDBE?style=flat&logo=traefikproxy&logoColor=white)

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [Services](#services)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Security](#security)
- [Development](#development)
- [Troubleshooting](#troubleshooting)

## 🎯 Overview

This project demonstrates a production-ready microservices architecture using:

- **Spring Boot 3.2** - RESTful API with JPA/Hibernate
- **PostgreSQL** - Custom database image with pre-loaded data
- **Traefik v3** - Reverse proxy with automatic service discovery
- **Docker Compose** - Multi-container orchestration
- **Docker Secrets** - Secure credential management
- **Nginx** - Static content servers for demonstration

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│                   Traefik                       │
│            Reverse Proxy (Port 80)              │
└─────────────────┬───────────────────────────────┘
                  │
         ┌────────┴────────┐
         │                 │
    ┌────▼────┐      ┌────▼────────┐
    │  Spring │      │   Nginx     │
    │  Boot   │      │  Services   │
    │   API   │      │             │
    └────┬────┘      └─────────────┘
         │
    ┌────▼──────────┐
    │  PostgreSQL   │
    │   Database    │
    └───────────────┘
```

## 📦 Prerequisites

- Docker Desktop 20.10+
- Docker Compose 2.0+
- JDK 17+ (for local development)
- Maven 3.6+ (for local development)
- Git

## 📁 Project Structure

```
docker-lab/
├── springboot-app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/demo/
│   │       │   ├── DemoApplication.java
│   │       │   ├── config/
│   │       │   │   └── DataSourceConfig.java
│   │       │   ├── controller/
│   │       │   │   ├── HelloController.java
│   │       │   │   └── EmployeeController.java
│   │       │   ├── model/
│   │       │   │   └── Employee.java
│   │       │   └── repository/
│   │       │       └── EmployeeRepository.java
│   │       └── resources/
│   │           └── application.properties
│   ├── pom.xml
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── secrets/
│   │   └── db_password.txt
│   └── nginx/
│       ├── frontend/
│       ├── product/
│       └── service/
└── postgres-custom/
    ├── Dockerfile
    └── init/
        └── init.sql
```

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/khoalololo/docker-springboot-lab
cd docker-lab
```

### 2. Build Custom PostgreSQL Image

```bash
cd postgres-custom
docker build -t postgres-custom .
```

### 3. Build Spring Boot Image

```bash
cd ../springboot-app
docker build -t springboot-demo .
```

### 4. Create Docker Secret

```bash
# Create secrets directory
mkdir secrets
echo "abc123-" > secrets/db_password.txt
```

### 5. Start All Services

```bash
docker compose up -d
```

### 6. Wait for Services to Start

```bash
# Wait about 30-45 seconds for all services to initialize
docker compose logs -f
```

### 7. Access Services

- **Frontend Dashboard**: http://localhost/
- **Spring Boot API**: http://localhost/api/employees
- **Traefik Dashboard**: http://localhost:8090
- **Adminer (DB UI)**: http://localhost/adminer
- **Product Service**: http://localhost/product
- **Service Portal**: http://localhost/service

## 🛠️ Services

### Traefik (Reverse Proxy)

- **Port**: 80 (HTTP), 8090 (Dashboard)
- **Purpose**: Routes all incoming traffic to appropriate services
- **Features**: Automatic service discovery, path-based routing, load balancing

### Spring Boot API

- **Technology**: Spring Boot 3.2, JPA/Hibernate
- **Backend Port**: 8080 (internal)
- **Access**: http://localhost/api/*
- **Features**: RESTful API, database integration, Docker Secrets support

### PostgreSQL Database

- **Version**: PostgreSQL Alpine
- **Database**: humanresource
- **Features**: Custom image with pre-loaded sample data
- **Tables**: employees, departments
- **Sample Data**: 8 employees, 5 departments

### Adminer

- **Access**: http://localhost/adminer
- **Purpose**: Web-based database management
- **Credentials**: 
  - Server: `postgres`
  - Username: `postgres`
  - Password: `abc123-`
  - Database: `humanresource`

### Nginx Services

- **Product Service**: http://localhost/product
- **Service Portal**: http://localhost/service
- **Frontend**: http://localhost/

## 🔌 API Endpoints

### Employee Management

```bash
# Get all employees
GET http://localhost/api/employees

# Get employee by ID
GET http://localhost/api/employees/{id}

# Get employees by department
GET http://localhost/api/employees/department/{deptId}

# Search employees by last name
GET http://localhost/api/employees/search?lastName=Smith

# Create new employee
POST http://localhost/api/employees
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "555-0100",
  "hireDate": "2024-01-15",
  "jobTitle": "Developer",
  "salary": 75000.00,
  "departmentId": 1
}

# Update employee
PUT http://localhost/api/employees/{id}

# Delete employee
DELETE http://localhost/api/employees/{id}
```

### Health Check

```bash
# Application health status
GET http://localhost/api/health

# Simple hello endpoint
GET http://localhost/hello
```

## ⚙️ Configuration

### Environment Variables

The application uses Docker Secrets for sensitive data:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/humanresource
  SPRING_DATASOURCE_USERNAME: postgres
  SPRING_DATASOURCE_PASSWORD_FILE: /run/secrets/db_password
```

### Database Configuration

PostgreSQL is configured with:
- Database: `humanresource`
- User: `postgres`
- Password: Read from Docker Secret
- Port: 5432 (internal)

### Traefik Routing

Services are configured with labels:

```yaml
labels:
  - "traefik.enable=true"
  - "traefik.http.routers.api.rule=Host(`localhost`) && PathPrefix(`/api`)"
  - "traefik.http.routers.api.entrypoints=web"
```

## 🔒 Security

### Docker Secrets

Passwords are managed using Docker Secrets:

- Secrets are stored in memory (tmpfs), not on disk
- Not visible in `docker inspect`
- Not visible in environment variables
- Isolated per service

### Verify Secrets Working

```bash
# Should show PASSWORD_FILE, not plain password
docker exec springboot-api env | findstr PASSWORD

# Secret file exists in container
docker exec springboot-api cat /run/secrets/db_password
```

### Network Isolation

- All services communicate via private `share_net` network
- Only Traefik exposes port 80 externally
- Database not directly accessible from outside

## 💻 Development

### Local Development (Without Docker)

```bash
# Start PostgreSQL locally
docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=abc123- postgres:alpine

# Run Spring Boot
cd springboot-app
./mvnw spring-boot:run
```

### Rebuild After Code Changes

```bash
# Rebuild Spring Boot image
cd springboot-app
docker build -t springboot-demo .

# Restart services
docker compose up -d --force-recreate api
```

### View Logs

```bash
# All services
docker compose logs -f

# Specific service
docker logs -f springboot-api
docker logs -f postgres-db
docker logs -f traefik-gateway
```

## 🐛 Troubleshooting

### Services Not Starting

```bash
# Check container status
docker ps -a

# Check logs for errors
docker compose logs

# Restart specific service
docker compose restart api
```

### Database Connection Issues

```bash
# Verify database is ready
docker exec postgres-db psql -U postgres -d humanresource -c "SELECT 1;"

# Check employee count
docker exec postgres-db psql -U postgres -d humanresource -c "SELECT COUNT(*) FROM employees;"
```

### Port Conflicts

```bash
# Windows: Find what's using a port
netstat -ano | findstr :80
netstat -ano | findstr :8090

# Kill process (replace PID)
taskkill /PID <PID> /F
```

### Complete Reset

```bash
# Stop and remove everything including volumes
docker compose down -v

# Rebuild images
docker build -t postgres-custom ../postgres-custom
docker build -t springboot-demo .

# Start fresh
docker compose up -d
```

### Database is Empty

```bash
# Remove volumes to recreate with init data
docker compose down -v
docker compose up -d

# Wait 30 seconds, then verify
docker exec postgres-db psql -U postgres -d humanresource -c "SELECT COUNT(*) FROM employees;"
```

## 📊 Testing

### Test API Endpoints

```bash
# Using curl
curl http://localhost/api/employees
curl http://localhost/api/employees/1
curl http://localhost/api/health

# Using PowerShell
Invoke-WebRequest http://localhost/api/employees
```

### Test Traefik Routing

```bash
# All routes should return 200
curl -I http://localhost/
curl -I http://localhost/api/employees
curl -I http://localhost/product
curl -I http://localhost/service
curl -I http://localhost/adminer
```

## 🛑 Stopping Services

```bash
# Stop all services
docker compose down

# Stop and remove volumes (fresh start next time)
docker compose down -v

# Stop specific service
docker compose stop api
```

## 📝 Notes

- First startup takes 30-45 seconds for database initialization
- PostgreSQL init scripts only run on first database creation
- Traefik dashboard shows all active routes and services
- Spring Boot uses embedded Tomcat by default
- Database data persists in Docker volumes (unless removed with `-v`)


Created as part of Docker learning lab

---

**Need Help?** Check the Troubleshooting section or review container logs with `docker compose logs -f`
