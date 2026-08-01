# FoodMaster

A backend REST API for personalized recipe generation, nutrition tracking, and AI-powered meal planning built with **Java 21** and **Spring Boot**. FoodMaster combines traditional recipe search with intelligent recommendations, LLM-powered recipe generation, and nutrition-aware meal planning while emphasizing secure authentication, clean architecture, and performance optimization.

---

# Features

## Guest Features

* Search recipes by ingredients with optional filters (cuisine, diet, allergies, and preferences)
* View complete recipe details, including ingredients, instructions, and nutrition information

## Authenticated Features

* Secure JWT-based registration and login
* Save and manage favorite recipes
* AI-powered recipe generation using Gemini when Spoonacular returns no matching recipes
* Create and manage personalized nutrition profiles (diet, allergies, preferences, and fitness goals)
* Receive personalized recipe recommendations using a Priority Queue–based ranking algorithm
* View the 10 most recently viewed recipes
* Generate AI-powered weekly meal plans with support for regenerating individual days or meals

---

# Performance

| Feature                                | Before    | After    | Improvement       |
| --------------------------------------- | --------: | -------: | -----------------: |
| Recipe detail caching (single request)  | 1240 ms   | 434 ms   | **65% faster**      |
| Gemini response caching                 | 23,798 ms | 505 ms   | **98% faster**      |
| Recipe detail caching (under load, k6)  | 1.78 s    | 5.52 ms  | **~99.7% faster**   |

Load-tested with **k6**: 20 concurrent virtual users hitting a cached recipe endpoint sustained **~4,000+ requests/sec at p95 ~11ms with a 0% failure rate** across 40,000+ requests.

---

# Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Security + JWT** — Stateless authentication
* **Spring Cache** — Response caching
* **JPA (Hibernate)** — ORM and persistence
* **MySQL** — Relational database
* **Spoonacular API** — Recipe search and recommendations
* **Gemini API** — AI recipe generation and meal planning
* **Docker & Docker Compose** — Containerized deployment
* **Lombok** — Boilerplate reduction
* **JUnit 5 + MockMvc** — Integration testing against a real Spring context and security filter chain
* **k6** — Load and concurrency testing
* **GitHub Actions** — Continuous integration

---

# Architecture Highlights

* Stateless JWT authentication with Spring Security (no server-side sessions)
* DTO separation between external API responses and internal domain models
* Global exception handling using a centralized `GlobalExceptionHandler` with typed custom exceptions, including graceful handling of upstream Spoonacular rate-limit errors (mapped to `503` instead of a generic `500`)
* Spring Cache with `@Lazy` self-injection to resolve Spring AOP proxy limitations
* Custom `LLMCacheKeyGenerator` using normalized, sorted composite cache keys for deterministic LLM caching
* Priority Queue–based recommendation engine that scores recipes using nutrition-goal-specific formulas
* Stack-based recipe history that stores each user's 10 most recently viewed recipes with database-level deduplication
* Nested JPA entity relationships (`MealPlan → WeekPlan → DayPlan`) implemented using `@OneToMany` and `@JoinColumn`
* Custom `AuthenticationEntryPoint` to ensure unauthenticated requests correctly return `401 Unauthorized` rather than Spring Security's default `403 Forbidden`

---

# Testing

## Integration Testing

FoodMaster is covered by integration tests built with **MockMvc** and **`@SpringBootTest`**, exercising the full Spring context (real security filter chain, real database, `@Transactional` rollback after each test) rather than isolated unit mocks. Coverage includes:

* Unauthenticated access to protected endpoints correctly returns `401`
* Invalid input (e.g. an invalid nutrition goal) correctly returns `400` via `GlobalExceptionHandler`
* Duplicate user registration correctly returns `409`

## Load Testing

Load testing with **k6** was used to validate performance under concurrency and to surface bugs that unit/integration tests didn't catch. Full methodology and results are documented in [`LOAD_TESTING.md`](./LOAD_TESTING.md), including:

* Baseline vs. cached response times for recipe lookups
* Concurrent load against cached and uncached endpoints
* Full recommendation-engine path under load (real user, real saved recipe, real Spoonacular calls)

### Bugs Found Through Testing

Load and integration testing surfaced and led to fixes for three real production bugs:

1. **Missing 401 entry point** — unauthenticated requests were falling through to a default `403` instead of `401`; fixed with a custom `AuthenticationEntryPoint`.
2. **Null-pointer on optional filters** — `RecipeService.searchRecipes` threw an NPE when `allergies` was omitted from a search request; fixed with a null-safe fallback.
3. **Unhandled upstream rate limiting** — a Spoonacular `429` (rate limit) was falling through to a generic exception handler and returning `500` under concurrent load (97% failure rate in testing); fixed by explicitly catching `HttpClientErrorException` and returning a proper `503` with a clear message. Re-tested at ~12,000 requests with a 0.00% failure rate.

---

# REST API Endpoints

## Authentication

* `POST /auth/register`
* `POST /auth/login`

## Recipes

* `POST /recipes/search` — Search recipes using ingredients and optional filters
* `GET /recipes/{id}` — Retrieve recipe details (automatically records history for authenticated users)

## Saved Recipes

* `POST /saved/save`
* `DELETE /saved/remove`
* `GET /saved/getRecipes`

## Nutrition Profile

* `POST /profile/create`
* `GET /profile/get`
* `PUT /profile/update`
* `DELETE /profile/delete`

## Recommendations

* `GET /recommendations/getRecommendations`

## Recipe History

* `GET /history/recipes`

## Meal Planner

* `POST /mealplanner/createPlanner?weeks={1-4}`
* `GET /mealplanner/getPlanner`
* `PUT /mealplanner/updateDay`
* `PUT /mealplanner/updateMeal`

---

# Continuous Integration

Every push and pull request runs the integration test suite via **GitHub Actions** (`.github/workflows/tests.yml`), using a MySQL 8 service container with a health check, JDK 21, and secrets injected for the required API keys. k6 load tests are intentionally excluded from CI to avoid burning real third-party API quota on every push — they're run manually and documented in `LOAD_TESTING.md`.

---

# Running with Docker

## Prerequisites

* Docker Desktop installed and running

## 1. Clone the repository

```bash
git clone <repository-url>
cd FoodMaster
```

## 2. Create a `.env` file in the project root

```env
MYSQL_USERNAME=root
MYSQL_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
SPOONACULAR_API_KEY=your_spoonacular_api_key
GEMINI_API_KEY=your_gemini_api_key
```

## 3. Start the application

```bash
docker compose up --build
```

The API will be available at:

```
http://localhost:8080
```

---

# Running Without Docker

1. Start a MySQL instance.
2. Create a database named `food_master`.
3. Configure your database credentials and API keys in `application.properties` (or environment variables).
4. Run the application using IntelliJ IDEA or:

```bash
mvn spring-boot:run
```