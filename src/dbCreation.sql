DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Packages CASCADE;
DROP TABLE IF EXISTS Stats CASCADE;
DROP TABLE IF EXISTS Cards CASCADE;
DROP TABLE IF EXISTS Session CASCADE;
DROP TABLE IF EXISTS Decks CASCADE;
DROP TABLE IF EXISTS Trade CASCADE;

-- Users table (includes name, bio, image, coins, elo, wins, losses)
CREATE TABLE Users (
                       username VARCHAR(255) PRIMARY KEY,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(255),
                       bio VARCHAR(255),
                       image VARCHAR(255),
                       coins INT NOT NULL DEFAULT 20,
                       elo INT NOT NULL DEFAULT 100,
                       wins INT NOT NULL DEFAULT 0,
                       losses INT NOT NULL DEFAULT 0
);

CREATE TABLE Packages (
                          id SERIAL PRIMARY KEY,
                          card_1 VARCHAR(255) NOT NULL,
                          card_2 VARCHAR(255) NOT NULL,
                          card_3 VARCHAR(255) NOT NULL,
                          card_4 VARCHAR(255) NOT NULL,
                          card_5 VARCHAR(255) NOT NULL,
                          bought BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE Stats (
                       username VARCHAR(255) NOT NULL,
                       games_played INT DEFAULT 0 NOT NULL,
                       games_won INT DEFAULT 0 NOT NULL,
                       games_lost INT DEFAULT 0 NOT NULL,
                       elo INT DEFAULT 100 NOT NULL,
                       FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE Cards (
                        card_id UUID PRIMARY KEY,
                        username VARCHAR(255),
                        package_id INT REFERENCES packages(id),
                        name VARCHAR(255) NOT NULL,
                        damage FLOAT NOT NULL,
                        monster_type BOOLEAN NOT NULL,
                        element_type VARCHAR(50) NOT NULL,
                        enhancement VARCHAR(50) NOT NULL,
                        locked BOOLEAN
);

CREATE TABLE Session (
                        username VARCHAR(255) NOT NULL,
                        token VARCHAR(255) NOT NULL,
                        FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE Decks (
                       username VARCHAR(255) NOT NULL UNIQUE,
                       card1_id UUID,
                       card2_id UUID,
                       card3_id UUID,
                       card4_id UUID
);

CREATE TABLE Trade (
                       trade_id VARCHAR(255) NOT NULL,
                       card_id VARCHAR(255) NOT NULL,
                       type VARCHAR(50) NOT NULL,
                       minDmg INT,
                       element VARCHAR(50)
);
