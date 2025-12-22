# 🚀 Smart Learning Progress Tracker (Backend)

A **production-ready Spring Boot + MongoDB backend** to track learning progress across topics, manage confidence levels, revisions, deadlines, and analytics — built using **clean architecture** and **real-world backend best practices**.

This project demonstrates **enterprise-grade backend engineering**, including **stateless JWT security**, **role-based access control (RBAC)**, **refresh token lifecycle management**, and **secure API documentation**.

---

## 🌍 Live Deployment

**Backend API (Render):**  
👉 https://smart-learning-progress-tracker.onrender.com

> All APIs are secured using JWT authentication.  
> Users must **register and login** to access protected endpoints.

---

## ✨ Key Features

### 📚 Learning Management
- Topic-based learning tracker
- Confidence scoring with revision history
- Deadline & overdue tracking
- Learning statistics dashboard
- Advanced search & sorting
- Pagination support
- Bulk upload support (**ADMIN only**)

---

### 🔐 Security & Authentication
- User **registration & login**
- JWT-based **stateless authentication**
- Short-lived **Access Tokens**
- Long-lived **Refresh Tokens**
- Secure logout via **refresh-token invalidation**
- Role-based authorization (**USER / ADMIN**)
- Clean **401 Unauthorized** & **403 Forbidden** handling
- Swagger secured with **JWT Bearer authentication**

---

## 🏗️ Tech Stack

| Layer        | Technology |
|-------------|------------|
| Language     | Java 21 |
| Framework    | Spring Boot 3.5+ |
| Security     | Spring Security + JWT |
| Database     | MongoDB Atlas |
| Build Tool   | Maven |
| API Docs     | Swagger (springdoc-openapi) |
| Deployment   | Docker + Render |

---

## 🧠 Architecture Overview



Client
↓
Controller
↓
Service
↓
Repository
↓
MongoDB


### Design Principles
- Stateless REST APIs
- Clear separation of concerns
- DTO-based API contracts (**no entity leakage**)
- Versioned APIs (`/api/v1`)

---

## 🔑 Authentication & Authorization Flow

### 📝 User Registration
**POST** `/auth/register`

- Registers a new user
- Password is securely **hashed**
- Default role assigned (**USER**)
- User must login after registration to obtain tokens

---

### 🔐 Login
**POST** `/auth/login`

- Validates credentials
- Returns:
  - **Access Token** (JWT – short-lived)
  - **Refresh Token** (UUID – long-lived, stored in DB)

---

### 🔁 Token Refresh
**POST** `/auth/refresh`

- Uses refresh token to issue a new access token
- No re-login required

---

### 🔓 Logout
**POST** `/auth/logout`

- Invalidates refresh token server-side
- Access token expires naturally

---

## 👥 Role-Based Access Control (RBAC)

| Role  | Permissions |
|------|-------------|
| USER | Read topics, revise, view statistics |
| ADMIN | Bulk upload, admin-level operations |

Authorization enforced using:
```java
@PreAuthorize("hasRole('ADMIN')")

🔐 Token Storage Strategy
Access Token

Stored client-side only

Not stored in DB

Not stored in server session

Sent via:

Authorization: Bearer <ACCESS_TOKEN>

Refresh Token

Stored server-side (MongoDB) with expiry

Stored client-side securely

Enables logout & session control

📘 Swagger API Documentation

Swagger UI is secured with JWT authentication.

📍 Access
https://smart-learning-progress-tracker.onrender.com/swagger-ui/index.html

Steps

Register a user using /auth/register

Login to obtain access token

Click Authorize 🔒

Enter:

Bearer <ACCESS_TOKEN>

🔗 API Endpoints
🔐 Authentication
Method	Endpoint	Description
POST	/auth/register	Register new user
POST	/auth/login	Login
POST	/auth/refresh	Refresh access token
POST	/auth/logout	Logout
📚 Topics
Method	Endpoint	Access
GET	/api/v1/topics	USER / ADMIN
GET	/api/v1/topics/stats	USER / ADMIN
GET	/api/v1/topics/search	USER / ADMIN
POST	/api/v1/topics/bulk	ADMIN
PUT	/api/v1/topics/{id}/revise	USER
PUT	/api/v1/topics/{id}/complete	USER
🧪 Sample API Response
{
  "success": true,
  "message": "Topics fetched successfully",
  "data": {
    "id": "69216cdfc8e4268ee15ee0ad",
    "name": "Spring Boot Auto Configuration",
    "confidence": 85,
    "status": "STRONG",
    "completed": false,
    "deadline": "2025-12-15"
  }
}

🔐 Security Highlights (Interview-Ready)

Fully stateless JWT authentication

No server-side session storage

Secure password hashing

Refresh tokens stored with expiry

Logout without JWT blacklisting

Least-privilege role enforcement

Secured Swagger UI

Environment-based secret management

Design choice:
Access tokens are short-lived and stateless, while refresh tokens are stored server-side to enable logout and controlled session renewal.

▶️ Running the Project Locally
1️⃣ Clone the repository
git clone https://github.com/ddhanushd/Smart-Learning-Progress-Tracker.git
cd Smart-Learning-Progress-Tracker

2️⃣ Configure MongoDB
spring.data.mongodb.uri=${MONGODB_URI}

3️⃣ Run the application
mvn spring-boot:run


Server:

http://localhost:9090

🚀 Future Enhancements

Angular frontend integration

Token rotation strategy

Email verification during registration

Audit logging

Rate limiting

Cloud monitoring & alerts

👤 Author

D Dhanush
Backend Developer | Java & Spring Boot

🔗 GitHub: https://github.com/ddhanushd

⭐ Like this project?

Give it a ⭐ on GitHub — contributions and feedback are welcome!
