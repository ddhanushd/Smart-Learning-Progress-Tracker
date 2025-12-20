🚀 Smart Learning Progress Tracker (Backend)

A production-ready Spring Boot + MongoDB backend to track learning progress across topics, manage confidence levels, revisions, deadlines, and analytics — designed using clean architecture and real-world backend best practices.

This project demonstrates enterprise-level backend engineering, including stateless security, role-based access control, refresh token lifecycle, and API documentation.

✨ Key Features
📚 Learning Management

Topic-based learning tracker

Confidence scoring with revision history

Deadline & overdue tracking

Learning statistics dashboard

Advanced search & sorting

Pagination support

Bulk upload support (ADMIN only)

🔐 Security & Authentication

JWT-based stateless authentication

Short-lived Access Tokens

Long-lived Refresh Tokens

Secure logout via refresh-token invalidation

Role-based authorization (USER / ADMIN)

Clean 401 Unauthorized & 403 Forbidden handling

Swagger secured with JWT Bearer authentication

🏗️ Tech Stack
Layer	Technology
Language	Java 21
Framework	Spring Boot 3.5+
Security	Spring Security + JWT
Database	MongoDB Atlas
Build Tool	Maven
API Docs	Swagger (springdoc-openapi)
🧠 Architecture Overview
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
MongoDB


Stateless REST APIs

Clear separation of concerns

DTO-based API contracts

Versioned APIs (/api/v1)

🔑 Authentication & Authorization Flow
🔐 Login
POST /auth/login


Validates credentials

Returns:

Access Token (JWT – short-lived)

Refresh Token (UUID – long-lived, stored in DB)

🔁 Token Refresh
POST /auth/refresh


Uses refresh token to issue a new access token

No re-login required

🔓 Logout
POST /auth/logout


Invalidates refresh token server-side

Access token expires naturally

👥 Role-Based Access
Role	Permissions
USER	Read topics, revise, stats
ADMIN	Bulk upload, admin operations

Authorization enforced using:

@PreAuthorize("hasRole('ADMIN')")

🔐 Token Storage Strategy
Access Token

Stored client-side only

Not stored in DB

Not stored in server session

Sent via Authorization: Bearer <token>

Refresh Token

Stored server-side (MongoDB) with expiry

Stored client-side securely

Enables logout & session control

📘 Swagger API Documentation

Swagger UI is secured with JWT.

📍 Access:

http://localhost:9090/swagger-ui/index.html


Click Authorize 🔒

Enter:

Bearer <ACCESS_TOKEN>

🔗 API Endpoints
Auth
Method	Endpoint	Description
POST	/auth/login	Login
POST	/auth/refresh	Refresh access token
POST	/auth/logout	Logout
Topics
Method	Endpoint	Access
GET	/api/v1/topics	USER / ADMIN
GET	/api/v1/topics/stats	USER / ADMIN
GET	/api/v1/topics/search	USER / ADMIN
POST	/api/v1/topics/bulk	ADMIN
PUT	/api/v1/topics/{id}/revise	USER
PUT	/api/v1/topics/{id}/complete	USER
🧪 Sample Response
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

Stateless JWT authentication

No server-side session storage

Refresh tokens stored with expiry

Logout without JWT blacklisting

Least-privilege role enforcement

Secure Swagger configuration

Design choice: Access tokens are short-lived and stateless, while refresh tokens are stored server-side to enable logout and controlled session renewal.

▶️ Running the Project
1️⃣ Clone
git clone https://github.com/ddhanushd/Smart-Learning-Progress-Tracker.git
cd Smart-Learning-Progress-Tracker

2️⃣ Configure MongoDB
spring.data.mongodb.uri=${MONGODB_URI}

3️⃣ Run
mvn spring-boot:run


Server:

http://localhost:9090

🚀 Future Enhancements

Angular frontend integration

Token rotation strategy

Audit logging

Rate limiting

Cloud deployment

👤 Author

D Dhanush
Backend Developer | Java & Spring Boot

GitHub: https://github.com/ddhanushd

⭐ Like this project?

Give it a ⭐ on GitHub — contributions are welcome!
