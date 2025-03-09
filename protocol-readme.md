
# Protocol – MTCG Semester Project

## Overview

This project is a Java implementation of a backend server for the Monster Trading Card Game (MTCG). It is built without any frameworks and follows the given requirements, including HTTP handling, token-based user authentication, persistence using PostgreSQL, and core game functionality like packages, decks, trading, and stats.

The system is structured as a RESTful API and can be tested using curl or automated scripts. The project was developed individually as part of the 3rd semester, and this was the first time working seriously with Java.

## App Design (Structure and Decisions)

The application uses a custom server setup without HTTP helper libraries or frameworks. The structure is organized into the following packages:

- `restAPI/`: Handles incoming connections and routes HTTP requests using `HttpServer`, `ClientHandler`, and `Router`.
- `controller/`: Contains logic for different features such as user handling, stats, and trading.
- `repository/`: Responsible for interacting with the PostgreSQL database using raw SQL queries.
- `model/`: Contains classes for core entities like `User`, `Card`, `Deck`, `Package`, and `Stats`.

Routing is handled manually using pattern matching in a custom `Router` class. User authentication is done using simple token-based security. The format is: `Authorization: Basic <username>-mtcgToken`.

User stats are automatically initialized upon registration so that `/stats` and `/score` always return consistent values.

## Lessons Learned

- Building an HTTP server from scratch was much harder than expected.
- Understanding and working with HTTP headers and routing logic was a key challenge.
- Using PostgreSQL without an ORM made SQL injection and query building important topics.
- Writing tests helped catch errors early and made it easier to restructure code safely.
- Keeping code modular (controllers, repositories) helps organize larger projects.

## Unit Testing Decisions

To ensure reliability, a focused testing approach was used:

- 20 unit tests were written using JUnit 5.
- They cover important features like:
  - User registration, login, and authentication
  - Package creation and card assignment
  - Deck rules like size limits and no duplicates
  - Stats initialization and ELO sorting
  - Secure profile editing and trading logic

The tests also include edge cases like:

- Duplicate usernames
- Invalid login or token access
- Trying to buy when no packages are available
- Invalid deck formats

Database operations were not mocked, but the tests focus on checking the core application logic.
 Additionally, integration testing was done using a curl script that automatically runs through major endpoints to make sure everything works together as expected.

## Unique Feature

As a small extension, user stats are initialized automatically during registration. This avoids having to manually add stats later, and ensures `/stats` and `/score` always return valid data.

Also, the project checks that users can only access and update their own profile using tokens, which improves security and logic separation.

## Tracked Time

| Task                            | Hours |
|---------------------------------|-------|
| HTTP Server and Routing         | 8     |
| User Registration and Login     | 6     |
| Database Integration            | 6     |
| Package and Card Features       | 6     |
| Deck Handling                   | 5     |
| Trading Logic                   | 6     |
| Stats and Scoreboard            | 4     |
| Unit and Integration Tests      | 8     |
| Documentation and Cleanup       | 4     |
| **Total**                       | **around 50 to 55 hours** |

## Git Repository

https://github.com/thelegendofsonja/mtcg-lechner-Abgabe-2.git
