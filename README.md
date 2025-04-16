# TimeAway - Leave Management System

TimeAway is a comprehensive leave management system built with Spring Boot that helps organizations efficiently manage employee leave requests, balances, and approvals.

## Features

- **Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (ADMIN, MANAGER, EMPLOYEE)
  - Token refresh mechanism

- **Leave Management**
  - Create and manage leave requests
  - Multiple leave types support
  - Leave balance tracking
  - Leave request approval workflow

- **Statistics & Reporting**
  - Department-wise leave statistics
  - Overall organization leave metrics
  - Employee leave history

## Technology Stack

- Java 17
- Spring Boot 3.4.4
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT Authentication
- Swagger/OpenAPI Documentation

## Prerequisites

- JDK 17 or later
- Maven 3.6+
- PostgreSQL 14+
- Docker (optional)

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/timeaway.git
   cd timeaway
   ```

2. **Configure the database**
   
   Update `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/timeaway_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The application will be available at `http://localhost:8080`

## API Documentation

The API documentation is available through Swagger UI:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

## API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Authenticate user
- `POST /api/v1/auth/refresh-token` - Refresh authentication token

### Leave Requests
- `POST /api/v1/leave-requests` - Create a leave request
- `GET /api/v1/leave-requests` - Get all leave requests
- `GET /api/v1/leave-requests/{id}` - Get specific leave request
- `PUT /api/v1/leave-requests/{id}` - Update leave request status

### Leave Statistics
- `GET /api/v1/leave-statistics/overall` - Get overall leave statistics
- `GET /api/v1/leave-statistics/departments` - Get department-wise statistics

### Leave Balances
- `GET /api/v1/leave-balances/me` - Get current user's leave balances

## Security

- All endpoints except authentication endpoints require JWT authentication
- Leave statistics endpoints require ADMIN or MANAGER role
- Leave request approval requires ADMIN or MANAGER role

## Email Notifications

The system supports email notifications for:
- Leave request submissions
- Leave request status updates
- Leave balance updates

Configure email settings in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-specific-password
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 