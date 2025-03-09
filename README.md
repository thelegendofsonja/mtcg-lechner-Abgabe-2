Monster Trading Card Game (MTCG) - README

Link to the project:
https://github.com/thelegendofsonja/mtcg-lechner.git

Project Overview

This is an implementation of the Monster Trading Card Game (MTCG), built as a REST-based server in Java. The game allows users to collect, trade, and battle with cards. The server is implemented without an external HTTP framework, following the project specifications.

Features Implemented

User Management: Register, login, and manage users.

Card System: Cards with damage, element types (Fire, Water, Normal), and categories (Monster or Spell).

Deck Management: Users can configure decks with 4 cards for battles.

Trading System: Users can create and accept trading offers.

Battles: Users can engage in battles based on game logic.

Scoreboard & Stats: Tracks ELO rankings and user stats.

Security: Authentication via tokens to ensure only authorized users perform actions.

Database Integration: PostgreSQL for persistent data storage.

Custom Unique Feature: (Describe your unique feature here)

Installation & Setup

Requirements

Java 17+

PostgreSQL

Maven

Database Setup

Install PostgreSQL and create a database:

CREATE DATABASE mtcg;

Create necessary tables (see docs/database_schema.sql).

Update config.properties with your PostgreSQL credentials.

Build and Run the Server

Run the following commands:

mvn clean install
java -jar target/mtcg-server.jar

The server starts on port 10001.

API Endpoints

POST /users - Register a new user
POST /sessions - Login and get a token
GET /cards - View all cards owned
PUT /deck - Configure deck
POST /battles - Start a battle
GET /scoreboard - View leaderboard
POST /tradings - Create a trade offer
DELETE /tradings/{tradeId} - Cancel trade offer

For full API details, see docs/API_Documentation.md.

Running Tests

To validate the implementation, run:

mvn test

Additionally, use the provided MonsterTradingCards.exercise.curl.bat script to test API functionality.

Documentation

Protocol Document: Monster-Trading-Card-Game-(MTCG)-Protocol-Lechner.md

UML Diagram: mtcg-lechner-uml.png

Additional Notes

The server does not use an external HTTP framework.

Business logic follows the battle mechanics and trade requirements outlined in the specification.

Unique feature added: (Describe your unique feature here).