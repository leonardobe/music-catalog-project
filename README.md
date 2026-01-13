# 🎵 Music Catalog

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Backend](https://img.shields.io/badge/Backend-Java-success)
![Database](https://img.shields.io/badge/Database-PostgreSQL-blue)
![API](https://img.shields.io/badge/API-Deezer-lightgrey)
![License](https://img.shields.io/badge/License-MIT-green)

A clean and robust **Java back-end console application** built with **Spring Boot** that integrates with the **Deezer public API** to fetch artist and track data, persisting only **user-selected information** into a **PostgreSQL database**.

The project emphasizes **real-world backend practices**, including layered architecture, external API integration using **Spring WebClient**, DTO-based data mapping, **JPQL queries**, database consistency, and defensive programming — making it highly aligned with **Java Backend Developer roles**.

---

## 🚀 Features

* 🎤 Register artists using data fetched from the Deezer API
* 💾 Cache artist data locally to avoid repeated external API calls
* 🎶 Register tracks **on demand** for an existing artist
* 🚫 Prevent duplicate tracks for the same artist
* 📄 List all registered tracks by artist
* 🧠 Clear separation between domain logic, API integration, and persistence
* 🖥️ Interactive console-based menu
* ⚠️ Graceful validation and error handling
* 🧱 Layered architecture (Controller / Service / Client / Repository / DTO)

---

## 🧠 Architectural Highlights

* External API data is **never blindly persisted**
* Only **user-confirmed entities** are stored in the database
* Artists and tracks are modeled with a **bidirectional JPA relationship**
* Business rules are enforced at the **service layer**
* Duplicate track registration is prevented at:
  * Application level (service validation)
  * Database level (unique constraint)
* JPQL is prioritized over native SQL for learning and clarity
* Configuration is managed using **YAML**

---

## 🛠️ Tech Stack

* **Java 17+**
* **Spring Boot 3**
* **Spring Data JPA**
* **Spring WebClient**
* **PostgreSQL**
* **Jackson**
* **Maven**
* **YAML (`application.yml`)**

---

## 📁 Project Structure

```text
br.com.leo.musiccatalog
├── controller/    # Console interaction and menu flow
├── service/       # Business logic and domain rules
├── client/        # Deezer API integration (WebClient)
├── repository/    # Spring Data JPA repositories (JPQL)
├── domain/
│   ├── entity/    # JPA entities (Artist, Track)
│   └── dto/       # Deezer API DTOs
├── config/        # WebClient configuration
└── MusicCatalogApplication.java
```

> This structure follows separation of concerns, improving readability, maintainability, and scalability in backend systems.

---

## 📦 API Reference

#### Deezer Public API

**Base URL:**

```
https://api.deezer.com
```

> The application abstracts Deezer-specific API details, exposing a clean and user-friendly console experience.

---

## ⚙️ Configuration (application.yml)

The project uses **YAML** configuration.
You must provide your own database credentials.

```code
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/music_catalog
    username: YOUR_DB_USERNAME
    password: YOUR_DB_PASSWORD
```
--- 

## ▶️ How to Run

### Prerequisites

* Java 17 or higher
* Maven
* PostgreSQL

### Steps

#### 1. Clone the repository:

```bash
https://github.com/leonardobe/music-catalog-project.git
```

#### 2. Create the database:

```sql
CREATE DATABASE music_catalog;
```

#### 3. Configure application.yml
Set your PostgreSQL credentials.

#### 4. Run the application

```bash
mvn spring-boot:run
```

> The application starts directly in the terminal.

---

## 🎮 Console Menu 

```text
===============================
        MUSIC CATALOG
===============================
1 - Register artist
2 - Show artist information
3 - Register track for an artist
4 - List tracks by artist
0 - Exit
```
---

## 📌 Business Rules

* An artist can only be registered once
* A track can only be registered once per artist
* Tracks are never automatically imported
* External API calls are avoided when data already exists locally
* Tracks must always be associated with an existing artist

---

## 📈 Learning Outcomes

This project reinforces key Java backend development concepts, including:

* External API consumption using Spring WebClient
* Layered architecture and separation of concerns
* DTO-based JSON mapping with Jackson
* JPQL queries and relational modeling with JPA
* Domain-driven validation and consistency
* Console applications built with production-level backend code
* Clean, maintainable, and scalable Java design

---

## 📄 License

This project is licensed under the **MIT License**.

---

## 👨‍💻 Author

Developed by **Leonardo**.  
Focused on Java back-end development, clean architecture, and continuous learning.
