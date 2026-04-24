# 🔔 Notify API - Multi-Channel Notification Gateway

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-API_Client-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Twilio](https://img.shields.io/badge/Twilio-F22F46?style=for-the-badge&logo=twilio&logoColor=white)
![Mailtrap](https://img.shields.io/badge/Mailtrap-SMTP-1A73E8?style=for-the-badge&logo=minutemailer&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

**Notify** is a robust, enterprise-grade RESTful API that works as a **multi-channel notification gateway**. Through a single unified contract, clients can trigger **E-mail (Mailtrap API)**, **SMS (Twilio)** and **WhatsApp (Twilio Business API)** deliveries — with authentication, rate limiting, idempotency and observability baked in from day one.

## The Strategy Pattern at the core

The architectural heart of Notify is the classic **Strategy Design Pattern**, combined with Spring's Dependency Injection to deliver a **zero-coupling, runtime-pluggable** notification layer.

Every delivery channel (E-mail, SMS, WhatsApp, and any future one) implements the same contract:

```java
public interface NotificationStrategy {
    boolean supports(NotificationChannel channel);
    void send(NotificationModel notification);
}
```

A single factory, receiving **the full list of strategies** via Spring's DI container, resolves the correct implementation at runtime — **no `if/else`, no `switch`, no coupling**:

```java
@Component
@RequiredArgsConstructor
public class NotificationStrategyFactory {
    private final List<NotificationStrategy> strategies;

    public NotificationStrategy getStrategy(NotificationChannel channel) {
        return strategies.stream()
                .filter(s -> s.supports(channel))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException(
                    "Nenhum provedor configurado para o canal " + channel + "."));
    }
}
```

**What this gives us:**

- **A truly pluggable architecture** — adding a new channel (Telegram, Slack, Push) means writing one new class annotated with `@Component`. The controller, the factory, and every other piece of the system remain completely untouched.
- **Full polymorphism, zero conditionals** — the controller doesn't know Twilio exists. It doesn't know Mailtrap exists. It just asks the factory for "the strategy for this channel" and calls `.send()`.
- **Testability by design** — each strategy is an isolated, mockable bean. Unit tests don't need the entire infrastructure up.

This is the difference between a project and a **well-engineered** project.

## Architecture & Technical Decisions

Beyond the Strategy pattern, the project was built around these pillars:

- **Stateless Security:** Authentication via **JWT (JSON Web Token)** using `Auth0 java-jwt` integrated with `Spring Security`, carrying *claims* (user role and identity) inside the token itself to avoid database round-trips on every protected request. A custom `OncePerRequestFilter` intercepts the `Authorization` header, validates the token, and populates the `SecurityContext` — returning a standardized 401 JSON payload on failure.
- **Rate Limiting with Redis:** Distributed rate limiting (**5 requests per minute per user**) implemented on top of **Redis** and wired through a Spring MVC `HandlerInterceptor`. The interceptor reads the JWT principal, delegates the decision to a `RateLimiterService`, and returns **HTTP 429** with a standardized JSON body when the limit is exceeded. Applied specifically to the **e-mail (Mailtrap) channel** to protect it from abuse. Because the counter lives in Redis, the limit is consistent even when the API is scaled horizontally across multiple pods.
- **Global Exception Handling:** A central `@RestControllerAdvice` maps each domain exception to the correct HTTP status and a **standardized JSON error payload** (`timestamp`, `status`, `error`, `message`, `path`). `ResourceNotFoundException` → `404`, `BusinessRuleException` → `409 Conflict`, unhandled `Exception` → `500` with a safe, user-friendly message. **No stack trace ever leaks** to the client.
- **External Integrations:** **Spring Cloud OpenFeign** is the backbone for HTTP integrations — declarative, strongly-typed Feign Clients call the **Mailtrap REST API** for e-mail delivery, keeping the code clean, testable, and free of boilerplate `RestTemplate`/`WebClient` plumbing. **Twilio SDK** handles SMS and WhatsApp dispatching through its own idiomatic client.
- **NoSQL Persistence:** **MongoDB** as the primary datastore — great fit for the flexible, semi-structured nature of notification payloads and delivery logs.
- **Input Validation:** `Bean Validation` (`@Valid`, `@NotBlank`, `@Email`) shields the system against malformed payloads before they reach the service layer.
- **Live Documentation:** Interactive and testable documentation powered by **Swagger / OpenAPI 3 (springdoc)**, including *Authorize* button for JWT tokens, request/response schemas and example payloads.
- **Containerization:** The full stack (API + MongoDB + Redis) runs with **Docker Compose** — one command and you're up.

## How to Run the Project

The application is fully "Dockerized". You don't need to have Java, MongoDB, or Redis installed on your local machine; the container will do all the heavy lifting.

1. Clone the repository:

```bash
git clone https://github.com/GGhiaroni/notify.git
cd notify
```

2. Configure the environment variables (create a `.env` file at the project root):

```env
# JWT
JWT_SECRET=your-jwt-secret

# Twilio
TWILIO_ACCOUNT_SID=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
TWILIO_AUTH_TOKEN=your-twilio-auth-token
TWILIO_PHONE_NUMBER=+15555555555
TWILIO_WHATSAPP_FROM=whatsapp:+14155238886

# Mailtrap (SMTP)
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your-mailtrap-username
MAIL_PASSWORD=your-mailtrap-password
```

3. Spin up the infrastructure and the API with a single command:

```bash
docker-compose up -d --build
```

4. Access the Swagger documentation in your browser:

```text
http://localhost:8080/swagger-ui.html
```

## Main Endpoints

| Method | Endpoint                       | Description                                                              |
|--------|--------------------------------|--------------------------------------------------------------------------|
| POST   | `/notify/auth/register`        | Account creation with integrity constraints for duplicate e-mails.       |
| POST   | `/notify/auth/login`           | Authentication and JWT Token generation.                                 |
| POST   | `/notify/send/email`           | Dispatches an e-mail via Mailtrap API (OpenFeign). *Protected by rate limiter.*   |
| POST   | `/notify/send/sms`             | Dispatches an SMS via Twilio.                                            |
| POST   | `/notify/send/whatsapp`        | Dispatches a WhatsApp message via Twilio Business API.                   |
| GET    | `/notify/history`              | Returns the notification history of the authenticated user (paginated).  |

> 🔒 All `/notify/send/**` endpoints require a valid JWT token.

## Delivery Proofs

The notification pipeline was validated end-to-end across the three channels — and, because of the Strategy pattern, each one was a drop-in implementation with zero changes to the controller layer:

- ✅ **E-mail** delivered via the Mailtrap HTTP API (`EmailStrategy` + OpenFeign)
- ✅ **SMS** delivered via Twilio to a real device (`SmsStrategy`)
- ✅ **WhatsApp** delivered via Twilio Business API to a real device (`WhatsAppStrategy`)

## Project Structure

```
notify/
├── src/main/java/com/gabrieltiziano/notify/
│   ├── controller/        # REST endpoints
│   ├── strategy/          # Strategy pattern core
│   │   ├── NotificationStrategy.java          # contract
│   │   ├── NotificationStrategyFactory.java   # resolver
│   │   ├── EmailStrategy.java
│   │   ├── SmsStrategy.java
│   │   └── WhatsAppStrategy.java
│   ├── security/          # JWT filter, SecurityConfig, TokenService
│   ├── service/           # RateLimiterService and others
│   ├── interceptor/       # RateLimitInterceptor
│   ├── exception/         # GlobalExceptionHandler (@RestControllerAdvice)
│   ├── client/            # Feign clients
│   ├── model/             # Domain models & DTOs
│   └── repository/        # MongoDB repositories
├── docker-compose.yml
└── pom.xml
```

---

*Developed by [Gabriel Tiziano](https://www.linkedin.com/in/gabrieltiziano/) with 🤍 && ☕️*