# 🎬 Movie Ticket Management System

A desktop-based movie ticket booking application built using **Java Swing** and **MySQL**. It allows **customers** to browse movies, view available showtimes, book. An **admin module** provides full control over movie listings and scheduling.

This project demonstrates strong integration between a GUI application and a database,showcasing core **DBMS principles** like normalization, foreign keys and relational querying.

---

## 📽️ Demo Video
🎥 [Watch the Project in Action]([https://drive.google.com/file/d/1QuE7cvm76AI5JazNoJWVuOspzrySgBuL/view?usp=drive_link])  

---

## 🧠 Key Features

### 👤 Customer Side
- 🎟️ Browse available movies and showtimes
- 🪑 View seat matrix and real-time availability
- 📅 Book tickets for specific dates/times
- 🧾 booking confirmation

### 🛠 Admin Panel
- ➕ Add, edit, or remove movie entries
- 🗓 Manage schedules and showtimes
- 📊 View booking records

---

## 🗃️ Database (DBMS Focus)

### 🧱 Schema Design
- `Admin`
- `Customers`
- `Movies`
- `Theaters`
- `Showtimes`
- `Seat_Info`
- `Seats`
- `Bookings`
- `Booked_Seats`

### 🔗 DB Concepts Implemented
- **Normalization** – 3NF schema, avoids redundancy
- **Foreign Keys** – enforce relational integrity
- **JOINs** – used in reports and dashboards
- **Indexes** – automatic on primary/foreign keys

---

## 💻 Technologies Used

| Technology     | Purpose                           |
|----------------|-----------------------------------|
| **Java**       | Core programming language         |
| **Swing**      | GUI components and layouts        |
| **MySQL**      | Backend database engine           |
| **JDBC**       | Java-MySQL connectivity layer     |
| **Eclipse**    | IDE for development               |

---

## 🔐 User Roles

| Role     | Access Capabilities                                          |
|----------|--------------------------------------------------------------|
| Admin    | Add/edit/delete movies, manage schedules, view reports       |
| Customer | Book tickets, view movies, see seat availability             |
