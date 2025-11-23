# 🚀 Smart Learning Progress Tracker (Backend)

A production-ready **Spring Boot + MongoDB** backend application to track learning progress, manage topic confidence, revisions, deadlines, and analytics — designed with clean architecture and real-world best practices.

> Built to demonstrate enterprise-level backend engineering skills including security, DTO mapping, versioned APIs, and scalable design.

---

## ✨ Key Features

* ✅ Topic-based learning tracker
* ✅ Confidence scoring with revision history
* ✅ Deadline & overdue tracking
* ✅ Bulk upload support
* ✅ Statistics dashboard API
* ✅ Advanced search & sorting
* ✅ Pagination support
* ✅ API Response Wrapper (standardized responses)
* ✅ DTO Mapping (no entity leakage)
* ✅ Versioned APIs (/api/v1)
* ✅ Swagger API Documentation
* ✅ Secure MongoDB configuration (Environment variables)

---

## 🏗️ Tech Stack

| Layer         | Technology                  |
| ------------- | --------------------------- |
| Language      | Java 21                     |
| Framework     | Spring Boot 3.5+            |
| Database      | MongoDB Atlas               |
| Documentation | Swagger (springdoc-openapi) |
| Build Tool    | Maven                       |
| Security      | Environment Variables       |

---

## 📦 Project Structure

```
Smart-Learning-Progress-Tracker
│
├── src/main/java/com/tracker
│   ├── controller
│   ├── service
│   ├── repository
│   ├── dto
│   ├── mapper
│   └── model
│
├── src/main/resources
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## 🔐 Security Configuration

MongoDB credentials are **NOT stored in code**.
They are injected using environment variables.

### ✅ Required Environment Variable

```
MONGODB_URI=mongodb+srv://<username>:<password>@cluster.mongodb.net/smart_learning_db
```

### application.properties

```properties
spring.data.mongodb.uri=${MONGODB_URI}
```

> Never commit passwords or credentials to GitHub.

---

## ▶️ Running the Project

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/ddhanushd/Smart-Learning-Progress-Tracker.git
cd Smart-Learning-Progress-Tracker
```

### 2️⃣ Set Environment Variable (Windows)

```powershell
setx MONGODB_URI "mongodb+srv://user:password@cluster.mongodb.net/smart_learning_db"
```

Restart IDE after setting.

### 3️⃣ Run Application

```
mvn spring-boot:run
```

Server starts at:

```
http://localhost:9090
```

---

## 📘 Swagger Documentation

Access interactive API docs here:

```
http://localhost:9090/swagger-ui/index.html
```

---

## 🔗 API Endpoints

Base URL:

```
/api/v1/topics
```

| Method | Endpoint       | Description        |
| ------ | -------------- | ------------------ |
| POST   | /              | Create Topic       |
| GET    | /              | Get All Topics     |
| GET    | /weak          | Weak Topics        |
| PUT    | /{id}/revise   | Revise Topic       |
| PUT    | /{id}/complete | Mark as Complete   |
| PUT    | /{id}/deadline | Update Deadline    |
| GET    | /stats         | Learning Stats     |
| GET    | /sorted        | Sort by Confidence |
| GET    | /search?q=     | Search Topics      |
| GET    | /overdue       | Overdue Topics     |
| POST   | /bulk          | Bulk Upload        |

---

## 📊 Sample Response

```json
{
  "success": true,
  "message": "Topics fetched successfully",
  "data": {
    "id": "69216cdfc8e4268ee15ee0ad",
    "name": "Spring Boot Auto Configuration",
    "confidence": 85,
    "status": "STRONG",
    "completed": false,
    "deadline": "2025-12-15",
    "revisions": [
      {
        "revisedAt": "2025-11-22T14:05:38",
        "oldConfidence": 30,
        "newConfidence": 85,
        "note": "Completed mock interview"
      }
    ]
  }
}
```

---

## 🧠 Architecture Highlights

* DTO Mapping: Separation of database and API contract
* Layered architecture (Controller → Service → Repository)
* Clean business logic isolation
* Secure credentials strategy
* Versioned API design

---

## 🚀 Future Enhancements

* 🔐 User Authentication (JWT)
* 👥 Multi-user support
* 📅 Smart reminder scheduler
* 🌐 Angular Frontend Integration
* ☁️ Cloud Deployment

---

## 👤 Author

**D Dhanush**
Backend Developer | Java & Spring Boot

GitHub: [https://github.com/ddhanushd](https://github.com/ddhanushd)

---

## ⭐ If you like this project

Please give it a ⭐ on GitHub and feel free to contribute!

---

> This project demonstrates production-level backend development practices and is ideal for showcasing in interviews and portfolios.
