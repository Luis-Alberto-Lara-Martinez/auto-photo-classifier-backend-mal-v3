# *Auto Photo Classifier Backend*

Backend for the **Auto Photo Classifier** project, a web application that lets users select, preview, and download
Google Photos, automatically organized by year and month into ZIP files.

This service acts as a **REST API** between the frontend (Angular) and Google's APIs, centrally managing all
authentication and communication with Google.

## 🛠️ Tech Stack

- **Java 25 + Spring Boot 4.1.1**
- **PostgreSQL 18** as the database
- **OAuth2** for Google authentication
- Integration with the **Google Photos Picker API**

## 🧩 Architecture

- The **Angular frontend** is responsible solely for the user interface and consumes the endpoints exposed by this
  backend.
- The **Spring Boot backend** handles:
    - OAuth2 authentication with Google
    - Communication with the **Google Photos Picker API**
    - Organizing and packaging photos into ZIP files (by year/month)
    - Persistence of users, tokens, and Picker sessions
