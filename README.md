# SpotHofra

SpotHofra is a Spring Boot application built with Maven. This document explains how to set up, build, run, and test the project locally, with a focus on Windows but also including cross‑platform commands.

---

## 1. Tools & Technologies Used

- **Java JDK** (version as defined in `pom.xml`, typically 17 or 21)
- **Spring Boot** (managed via `spring-boot-starter-parent` in `pom.xml`)
- **Maven** as the build and dependency management tool
- **Maven Wrapper** (`mvnw`, `mvnw.cmd`) for a consistent Maven version
- **IntelliJ IDEA** (or any Java IDE) for development
- **Git** for version control (optional but recommended)
- **PostgreSQL** as the relational database

---

## 2. Project Structure (Overview)

```text
SpotHofra/
  pom.xml                   # Maven configuration
  src/
    main/
      java/
        org/example/spothofra/
          SpotHofraApplication.java   # Main Spring Boot entry point
    resources/
      application.properties          # Application configuration
      static/                         # Static web assets (if any)
      templates/                      # Server-side templates (if any)
    test/
      java/
        org/example/spothofra/
          SpotHofraApplicationTests.java   # Example tests
  target/
    SpotHofra-0.0.1-SNAPSHOT.jar      # Built executable JAR (after build)
    ...
```

---

## 3. Prerequisites

Make sure you have the following installed:

1. **Java JDK**
   - Verify:
     ```bash
     java -version
     ```
   - Install from: https://adoptium.net/ or your preferred JDK provider.

2. **Maven** (optional if you use the Maven Wrapper `mvnw` / `mvnw.cmd`)
   - Verify:
     ```bash
     mvn -version
     ```
   - Install from: https://maven.apache.org/

3. **PostgreSQL database server**
   - Install from: https://www.postgresql.org/download/
   - Make sure the PostgreSQL service is running and that you know your:
     - host (default: `localhost`)
     - port (default: `5432`)
     - database name
     - username
     - password

4. **Git** (if you clone the repo instead of downloading it)
   - Verify:
     ```bash
     git --version
     ```

5. **IDE (recommended)**
   - IntelliJ IDEA Community or Ultimate Edition, Eclipse, VS Code with Java extensions, etc.

---

## 4. Database Configuration (PostgreSQL)

The application is configured to use **PostgreSQL** via `src/main/resources/application.properties`:

```properties
spring.application.name=SpotHofra

# Datasource
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME:SpotHofra}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASS:password}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 4.1 Default database name and credentials

If you **do not set any environment variables**, Spring Boot will use the default values defined after the colon (`:`) in the placeholders:

- `DB_NAME` → default: `SpotHofra`
- `DB_USER` → default: `postgres`
- `DB_PASS` → default: `password`

So by default the app will try to connect to:

- URL: `jdbc:postgresql://localhost:5432/SpotHofra`
- Username: `postgres`
- Password: `password`

To make this work you need to:

1. Create a database named `SpotHofra` in your local PostgreSQL instance.
2. Ensure user `postgres` with password `password` exists and has full access to that database.

> For real projects, change these credentials to something secure and **never** commit production passwords to version control.

### 4.2 Custom database name and credentials via environment variables

You can override the defaults without editing `application.properties` by setting the following environment variables **before** you start the application:

- `DB_NAME` – the PostgreSQL database name
- `DB_USER` – the PostgreSQL username
- `DB_PASS` – the PostgreSQL password

#### Windows (PowerShell)

```powershell
$env:DB_NAME = "my_database"
$env:DB_USER = "my_user"
$env:DB_PASS = "my_secure_password"

mvnw.cmd spring-boot:run
```

Or when running the JAR directly:

```powershell
$env:DB_NAME = "my_database"
$env:DB_USER = "my_user"
$env:DB_PASS = "my_secure_password"

java -jar target/SpotHofra-0.0.1-SNAPSHOT.jar
```

#### macOS / Linux (bash/zsh)

```bash
export DB_NAME="my_database"
export DB_USER="my_user"
export DB_PASS="my_secure_password"

./mvnw spring-boot:run
```

Or with the JAR:

```bash
export DB_NAME="my_database"
export DB_USER="my_user"
export DB_PASS="my_secure_password"

java -jar target/SpotHofra-0.0.1-SNAPSHOT.jar
```

### 4.3 Hard-coding values in `application.properties` (local only)

For quick local testing, you can directly put your database name and credentials in `application.properties`.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/my_database
spring.datasource.username=my_user
spring.datasource.password=my_secure_password
```

This is convenient for development, but avoid hard-coding real passwords for shared environments.

---

## 5. Getting the Source Code

If you are using Git, clone the repository:

```bash
git clone <your-repository-url>
cd SpotHofra
```

(Or open the existing folder `SpotHofra` directly in your IDE.)

---

## 6. Building the Project

You can build the project either with Maven directly or via the Maven Wrapper.

### 6.1 Using Maven Wrapper (recommended)

From the project root (`SpotHofra/`):

**On Windows (PowerShell / Command Prompt):**
```powershell
mvnw.cmd clean package
```

**On macOS / Linux:**
```bash
./mvnw clean package
```

### 6.2 Using Maven (if installed globally)

```bash
mvn clean package
```

This will:
- Download all required dependencies.
- Compile the source code.
- Run tests.
- Create an executable JAR file in the `target/` directory (e.g. `target/SpotHofra-0.0.1-SNAPSHOT.jar`).

---

## 7. Running the Application

You can run the application either directly via Maven (convenient for development) or by running the built JAR.

### 7.1 Run with Maven Wrapper

**On Windows:**
```powershell
mvnw.cmd spring-boot:run
```

**On macOS / Linux:**
```bash
./mvnw spring-boot:run
```

### 7.2 Run with Maven

```bash
mvn spring-boot:run
```

### 7.3 Run the Built JAR

After a successful build (see section 6), run the JAR from the project root:

```bash
java -jar target/SpotHofra-0.0.1-SNAPSHOT.jar
```

By default (if not changed in `application.properties`), the application will start on port `8080`:

- Base URL: http://localhost:8080

---

## 8. Configuration

Configuration is located in:

- `src/main/resources/application.properties`

Typical examples you might configure (these are examples only):

```properties
server.port=8080
spring.application.name=SpotHofra
# Database settings, logging, etc. can also be configured here
```

You can override properties via environment variables or command-line arguments using standard Spring Boot mechanisms.

---

## 9. Running Tests

Use Maven or the Maven Wrapper to run the test suite.

### 9.1 With Maven Wrapper

**On Windows:**
```powershell
mvnw.cmd test
```

**On macOS / Linux:**
```bash
./mvnw test
```

### 9.2 With Maven

```bash
mvn test
```

Test reports and compiled test classes are placed under `target/` (e.g. `target/surefire-reports/`).

---

## 10. Working with the Project in an IDE

1. Open IntelliJ IDEA (or your preferred IDE).
2. Choose **Open** and select the `SpotHofra` folder containing `pom.xml`.
3. Let the IDE import the Maven project and download dependencies.
4. You can run the application by locating `SpotHofraApplication` and running it as a Spring Boot application from within the IDE.

---

## 11. Summary of Main Tools Used

- **Java JDK** – programming language and runtime.
- **Spring Boot** – application framework for building stand‑alone, production‑ready Spring applications.
- **Maven** – build and dependency management.
- **Maven Wrapper (`mvnw`, `mvnw.cmd`)** – ensures a consistent Maven version without requiring a global install.
- **PostgreSQL** – relational database used by the application.
- **IntelliJ IDEA** (or other IDE) – for editing, building, and running the project.
- **Git** – for version control and collaboration.

This README should give you everything you need to set up the PostgreSQL database, configure the database name and credentials, and run SpotHofra locally. Adjust configuration and commands as needed for your specific environment or deployment setup.
