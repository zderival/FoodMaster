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

| Feature                 |    Before |  After |    Improvement |
| ----------------------- | --------: | -----: | -------------: |
| Recipe detail caching   |   1240 ms | 434 ms | **65% faster** |
| Gemini response caching | 23,798 ms | 505 ms | **98% faster** |

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

---

# Architecture Highlights

* Stateless JWT authentication with Spring Security (no server-side sessions)
* DTO separation between external API responses and internal domain models
* Global exception handling using a centralized `GlobalExceptionHandler` with typed custom exceptions
* Spring Cache with `@Lazy` self-injection to resolve Spring AOP proxy limitations
* Custom `LLMCacheKeyGenerator` using normalized, sorted composite cache keys for deterministic LLM caching
* Priority Queue–based recommendation engine that scores recipes using nutrition-goal-specific formulas
* Stack-based recipe history that stores each user's 10 most recently viewed recipes with database-level deduplication
* Nested JPA entity relationships (`MealPlan → WeekPlan → DayPlan`) implemented using `@OneToMany` and `@JoinColumn`

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
