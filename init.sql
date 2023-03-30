CREATE USER postgres SUPERUSER;
--CREATE DATABASE postgres WITH OWNER postgres;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    provider VARCHAR(50),
    provider_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE book_info (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    user_id INTEGER REFERENCES users(id) NOT NULL,
    pages INTEGER NOT NULL,
    description TEXT,
    author VARCHAR(255),
    type VARCHAR(50),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE book_status (
    id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES book_info(id),
    read_pages INTEGER,
    started_reading_at TIMESTAMP,
    completed_at TIMESTAMP,
    status VARCHAR(20),
    notes TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);