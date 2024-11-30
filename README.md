
## Pizza Management System

### Developed by:
Dijanira and Alfred

[GitHub Repository Link](https://github.com/DijaniraMuachifi/PizzajavaSeminar.git)

---

### Table of Contents:
1. [Introduction](#introduction)
2. [Project Requirements](#project-requirements)
3. [General Project Structure](#general-project-structure)
   - Technologies Used
   - Folder Structure
4. [Database](#database)
   - Table Structures
   - Table Relationships
5. [Implemented Features](#implemented-features)
   - Home Page
   - Registration and Login
   - User Roles
   - Data Display
   - Contact Form
   - Stored Messages
6. [RESTful API](#restful-api)
   - Available Endpoints
   - Testing with cURL and Postman
7. [GitHub Usage](#github-usage)
   - Commit Structure
   - Repository Organization
8. [Screenshots](#screenshots)
9. [Conclusion](#conclusion)
10. [Future Improvements](#future-improvements)

---

### 1. Introduction
The **Pizza Management System** is a web application designed for efficient pizza ordering, management, and user interaction. Developed with modern web technologies, the system features user authentication, database integration, and a RESTful API. It allows different users (administrators, registered users, and guests) to interact with the platform in various ways. 

- **Administrators** can manage the pizza menu and orders.
- **Registered users** can place orders, manage their shopping cart, and track order status.
- **Guests** can browse the menu without logging in.

The system aims to showcase how modern technologies can be used to create secure, scalable, and efficient web applications.

### Key Features:
- **User Authentication & Role Management**: Users can register, log in, and access features based on their roles.
- **Pizza Menu Management**: Admins can add, update, or delete pizzas.
- **Order Management**: Users can place and track orders; admins manage and dispatch them.
- **RESTful API**: The system offers an API to interact with user and order data.

---

### 2. Project Requirements
Key functional requirements of the project include:
- **Home Page**: Introduction to the company and navigation options.
- **User Registration and Login**: Secure access and role-based features.
- **Data Display**: Display of users, pizzas, and categories.
- **Contact Form**: A functional contact form with server-side validation.

The project employs GitHub for version control, clear commit messages, and structured development processes.

---

### 3. General Project Structure
#### Technologies Used:
- **Frontend**: HTML5, CSS3, Bootstrap
- **Backend**: Java (Spring Boot), RESTful API
- **Database**: MySQL
- **Version Control**: GitHub

#### Folder Structure:
```
/src                - Source code
/main               - Core application functionality
/java               - Backend logic (Spring Boot)
/resources          - Configuration files
/webapp             - Frontend files (HTML, CSS, JS)
/public             - Static files (images, fonts)
/database           - SQL schema setup
/docs               - Documentation
/tests              - Unit tests
/git                - Git-related files
```

---

### 4. Database
#### Table Structures:
1. **Users**: Stores user details, roles, and credentials.
2. **Orders**: Stores details of orders placed by users.
3. **Pizzas**: Contains information about available pizzas.
4. **Categories**: Stores pizza categories (e.g., vegetarian, meat lovers).

#### Table Relationships:
- **Users → Orders**: One-to-many relationship (a user can place many orders).
- **Pizzas → Categories**: Many-to-one relationship (each pizza belongs to one category).

---

### 5. Implemented Features
1. **Home Page**: Introduction to the system with navigation links.
2. **Registration and Login**: Secure user access with role-based control.
3. **User Roles**: Role management for Admins, Users, and Guests.
4. **Data Display**: Dynamic display of user, pizza, and category data.
5. **Contact Form**: A form with server-side validation for user inquiries.

---

### 6. RESTful API
#### Available Endpoints:
- **Users**:
  - `GET /api/users`: Retrieve all users.
  - `POST /api/users`: Add a new user.
- **Orders**:
  - `GET /api/orders`: Retrieve all orders.
  - `POST /api/orders`: Create a new order.

#### Testing with cURL:
```bash
curl -X GET http://localhost:8080/api/orders
curl -X GET http://localhost:8080/api/pizzas
curl -X GET http://localhost:8080/api/categories
```

---

### 7. GitHub Usage
- **Commit Structure**: Each commit is meaningful and clearly described.
- **Repository Organization**: The repository follows best practices, ensuring easy collaboration and understanding.

---

### 8. Screenshots
- **Home Page**: Overview of the system.
- **Registration Form**: Interface for user signup.

---

### 9. Conclusion
The **Pizza Management System** demonstrates advanced web development skills and serves as a functional platform for pizza ordering. The system is built with best practices in mind, including secure authentication, database management, and RESTful API design.

---

### 10. Future Improvements
- Detailed reporting and analytics for users and orders.
- Integration with third-party APIs for payment processing or enhanced user interaction.

---

This documentation will help developers, collaborators, and users understand the project’s structure and functionality.
