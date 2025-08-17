# 🏨 Hotel Management – Java MVC (Swing + DAO + Factory + Decorator)

## 📖 Overview

This project is a **Hotel Management Application** built with **Java Swing** using the **MVC architecture**. It manages clients, rooms, and reservations, while demonstrating the use of design patterns such as **DAO**, **Factory**, and **Decorator**.

The system allows:

* Managing **clients** (VIP or regular)
* Managing **rooms** (types, prices, availability)
* Creating and tracking **reservations**
* Adding **optional services** (Spa, Minibar) using the Decorator pattern
* Storing and retrieving data with DAO pattern (MySQL or SQLite)

---

## ✨ Features

* Login system for hotel staff
* Client management with **VIP/Regular distinction**
* Room management with **availability & pricing**
* Reservation system (check-in / check-out)
* Extendable services (Spa, Minibar) with Decorator pattern
* Database integration with JDBC

---

## 🧱 Architecture

* **MVC**: clear separation between Models, Views (Swing), and Controllers
* **DAO Pattern**: manage database access (Guests, Rooms, Reservations)
* **Factory Pattern**: creation of guest objects (RegularGuest, VipGuest)
* **Decorator Pattern**: extend room functionalities with additional services

**Project Structure**

```
hotel-management-mvc-java/
├── src/
│   ├── main/java/com/hotel/
│   │   ├── app/                # Main application launcher
│   │   ├── controller/         # Controllers (Guest, Room, Reservation)
│   │   ├── dao/                # Data Access Objects
│   │   ├── model/              # Entities and design patterns
│   │   └── view/               # Swing user interface
│   └── resources/              # Config files (db.properties)
├── schema.sql                  # Database schema (MySQL/SQLite)
├── pom.xml                     # Maven build file
├── README.md                   # Documentation
└── LICENSE                     # License file
```

---

## 🚀 Getting Started

### Prerequisites

* Java 17+
* Maven or Gradle
* MySQL (or SQLite)

### Installation

```bash
git clone https://github.com/<your-username>/hotel-management-mvc-java.git
cd hotel-management-mvc-java
mvn clean install
```

### Run

```bash
mvn exec:java -Dexec.mainClass="com.hotel.app.Main"
```

### Database Configuration

Edit `src/main/resources/db.properties`:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/hotel_db
db.user=root
db.password=changeme
```

Create schema:

```bash
mysql -u root -p < schema.sql
```

---

## 📊 Future Improvements

* Role-based access (Admin, Reception)
* Internationalization (FR/AR/EN)
* Export reports (PDF/CSV)
* Enhanced UI/UX
* Statistics (occupancy rate, revenue tracking)

---

## 👥 Team

* SAHLI Oumaima
* TIBERKANINE Bouchra
* EL HAJJI Belkacem
* **ELKHARRATI Jawad**

Supervisor: **Pr. Naji Abdelwahab**

---
