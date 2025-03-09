# Monster Trading Card Game (MTCG) Protocol Document

## Project Overview

This document describes the design and development decisions for the Monster Trading Card Game (MTCG) backend server, implemented in Java as a REST-based HTTP server. The goal of the server is to provide an API that supports user registration, login, card management, and future functionalities like deck management, battles, and card trading.

**Link to GitHub repository:** [GitHub MTCG Repository](https://github.com/thelegendofsonja/mtcg-lechner.git)

------

## 1. App Design Decisions

### 1.1 Server Architecture

- The MTCG server is designed as a custom-built HTTP server to ensure a deeper understanding of HTTP request parsing and response handling, without relying on external frameworks like Spring.

- The server listens on a specified port (10001) and processes incoming requests, delegating them to appropriate handlers based on the request path.

  

### 1.2 REST API Design

- The initial focus is on user-related actions: **user registration** and **login**. These are implemented using the `/users` and `/sessions` endpoints, respectively.

- **Future API Extensions**: The server has been designed in a modular fashion to accommodate additional endpoints for managing cards (`/cards`), decks (`/deck`), battles (`/battles`), and trading (`/tradings`).

- **Routing**: The `ClientHandler` class acts as a router, delegating incoming requests to dedicated handler classes (e.g., `UserHandler` for registration, `SessionHandler` for login).

  

### 1.3 Modularity with Handlers

- Each REST API endpoint is handled by a dedicated class, allowing for clean separation of concerns. This modular approach ensures easier scalability and maintenance.

- Handlers Implemented:

  - `SessionHandler`: Handles login requests.

  - `UserHandler`: Handles user registration.

  - Future handlers such as `CardHandler`, `DeckHandler`, `BattleHandler`, and `TradingHandler` are placeholders for card and game-related functionality.

    

### 1.4 User Management

- The `UserManager` class is responsible for managing user data, including registration and authentication.

- For the intermediate hand-in, an in-memory `HashMap` is used to store user credentials. This will be replaced with a PostgreSQL-based persistence solution in the final submission.

  

### 1.5 Security Decisions

- **Simple Token-based Security**: When a user logs in, a simple token is generated and returned to the client. The token format follows the structure `{username}-mtcgToken`. This token is not cryptographically secure, but it serves as a basic mechanism for associating user sessions with a client. The token will be validated in future endpoints that require authentication, such as card management and trading.

------

## 2. Project Structure

```
src/
├── main/
│   └── java/
│       └── game/
│           ├── model/
│           │   ├── Card.java
│           │   ├── Deck.java
│           │   ├── Package.java
│           │   ├── User.java
│           ├── restAPI/
│           │   ├── HttpServer.java
│           │   ├── ClientHandler.java
│           │   ├── UserManager.java
│           │   └── handlers/
│           │       ├── SessionHandler.java
│           │       ├── UserHandler.java
│           │       ├── CardHandler.java
│           │       ├── DeckHandler.java
│           │       ├── BattleHandler.java
│           │       └── TradingHandler.java
├── pom.xml
├── Protocol.md
```

The application is structured into several key packages, each serving a distinct role in the overall functionality of the Monster Trading Card Game (MTCG) server. Here's an outline of the structure with descriptions of each component:

1. **Model Package (`game.model`)**
   - Contains the core data models for the game, defining the entities used throughout the application:
     - **`Card.java`**: Represents a card with attributes like name, damage, and element type (e.g., fire, water, normal).
     - **`Deck.java`**: Represents a user's deck, containing 4 selected cards used for battles.
     - **`Package.java`**: Represents a package of cards (each package contains 5 cards) that users can acquire.
     - **`User.java`**: Represents a user in the system, containing credentials (username and password), coins for purchasing packages, a stack of owned cards, and a deck for battling.
2. **REST API Package (`game.restAPI`)**
   - Manages the server functionality and request handling logic for the game. It is responsible for handling client requests and routing them to the appropriate handlers:
     - **`HttpServer.java`**: The entry point of the application. Starts the HTTP server on port 10001, listens for incoming connections, and delegates them to the `ClientHandler`.
     - **`ClientHandler.java`**: Routes incoming HTTP requests to the appropriate handlers based on the request path (e.g., `/sessions`, `/users`).
     - **`UserManager.java`**: Handles user-related functionality, such as registration, authentication, and managing user data.
3. **Handlers Package (`game.restAPI.handlers`)**
   - Contains specific handler classes responsible for handling various REST API endpoints. Each handler is designed to process specific types of requests:
     - **`SessionHandler.java`**: Handles login requests and generates simple user tokens (e.g., `{username}-mtcgToken`) for session management.
     - **`UserHandler.java`**: Manages user registration requests, processing new user data and storing it in the `UserManager`.
     - **`CardHandler.java`**: Placeholder for handling card-related actions (such as retrieving or managing cards).
     - **`DeckHandler.java`**: Placeholder for managing user decks (e.g., selecting cards for battle).
     - **`BattleHandler.java`**: Placeholder for handling battles between users, where two users' decks compete in a card battle.
     - **`TradingHandler.java`**: Placeholder for handling card trading between users.
4. **Build and Dependency Management (`pom.xml`)**
   - The `pom.xml` file manages the project dependencies and build configurations. It includes dependencies for:
     - **Jackson**: Used for JSON parsing in request and response bodies.
     - **PostgreSQL**: The future database driver for persistent data storage (not yet used in the current implementation).
     - **JUnit**: Included for unit testing.

---

## 3. Class Diagrams

The following UML diagram represents the key classes in the project, including the server, handlers, and model classes.

![mtcg-lechner-uml](mtcg-lechner-uml.png)