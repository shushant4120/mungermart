Vegetable Loyalty SaaS - Starter

See src/main/resources/application.properties for DB config.

## Requirements

- Java 21 (Temurin / OpenJDK) is required to build and run this project. Ensure your `java -version` shows a 21.x LTS distribution before building.

Example:

```
java -version
openjdk version "21.0.x" 2025-... LTS
```

If you use the Maven wrapper (`mvnw.cmd` / `mvnw`), it will use the JDK on your PATH. On Windows you can check and set JAVA_HOME as needed.

## Features

- Multi-tenant architecture (MongoDB, tenant-aware queries)
- Role-based access control (Admin, Merchant, Customer)
- Product Listing Page (PLP) and Product Detail Page (PDP) APIs
- Customer registration and details APIs
- Tenant registration and details APIs
- Purchase API (with points and redemption logic)
- JWT-based authentication (login API)
- Global exception handling
- Test coverage (unit and integration tests)
- Modern package structure (product, customer, security, multitenant, etc.)

## API Endpoints

### Authentication
- `POST /api/v1/auth/login` — Login, returns JWT token

### Customer APIs
- `POST /api/v1/customers/create-customer` — Register customer
- `GET /api/v1/customers/list-customers` — List all customers
- `GET /api/v1/customers/get-customer-by-card/{cardId}` — Get customer by card ID

### Product APIs
- `GET /api/v1/products/list-products` — List products (PLP)
- `GET /api/v1/products/get-product/{id}` — Get product details (PDP)
- `POST /api/v1/products/create-product` — Create product (Admin/Merchant)
- `PUT /api/v1/products/update-product/{id}` — Update product (Admin/Merchant)
- `DELETE /api/v1/products/delete-product/{id}` — Delete product (Admin)

### Purchase APIs
- `POST /api/v1/purchases/create-purchase` — Create purchase, redeem points

### Tenant APIs
- `POST /api/v1/tenants/register-tenant` — Register tenant/shop
- `GET /api/v1/tenants/{id}` — Get tenant details

## Security
- JWT authentication for login and protected endpoints
- Role-based access enforced via custom annotations/interceptors

## Error Handling
- Centralized error responses via `GlobalExceptionHandler`

## Testing
- Unit tests for services
- Integration tests for repositories

## How to Run
1. Install Java 21 and MongoDB
2. Configure `application.properties` with your MongoDB URI
3. Build and run with `./mvnw spring-boot:run`
4. Use the listed API endpoints with Postman or curl

## Recommendations
- Use `BigDecimal` for monetary values in future
- Expand DTOs for request/response objects
- Add more integration tests and security filters

## Using Jasypt to protect DB password

This project is configured to use Jasypt to encrypt the MongoDB password so it is not stored in plaintext in `application.properties`.

1) Add a master password as an environment variable before starting the app:

Windows (cmd.exe):
```
set JASYPT_ENCRYPTOR_PASSWORD=yourMasterPassword
```

Linux/macOS:
```
export JASYPT_ENCRYPTOR_PASSWORD=yourMasterPassword
```

2) Create an encrypted password using Jasypt CLI or a small Java helper. Example using the Jasypt CLI JAR:

```
# download jasypt jar or use one you have
java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="gpFKdX6JGKHj89nG" password=yourMasterPassword algorithm=PBEWithMD5AndDES
```

The CLI prints the encrypted value. Put the returned ciphertext into `application.properties` as:

```
spring.data.mongodb.password=ENC(theEncryptedTextHere)
```

3) Start the application. Jasypt starter will automatically decrypt any `ENC(...)` properties using the `JASYPT_ENCRYPTOR_PASSWORD` value.

Notes:
- For production do not commit the encrypted password or master password in source control. Store the master password in a secure secret store or CI/CD secret.
- If you prefer, you can provide the master password via JVM system property: `-Djasypt.encryptor.password=yourMasterPassword`.

---
For more details, see source code and Postman collection in the repo.
