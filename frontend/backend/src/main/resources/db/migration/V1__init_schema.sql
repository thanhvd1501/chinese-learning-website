-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table (managed by Keycloak, but we'll sync basic info)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    keycloak_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(100),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    avatar_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Textbooks/Courses table
CREATE TABLE textbooks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    version VARCHAR(10) NOT NULL CHECK (version IN ('PB3', 'NEW', 'OLD')),
    publication_year INTEGER NOT NULL,
    pdf_url TEXT,
    cover_image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses (Hán 1-6) table
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    textbook_id BIGINT REFERENCES textbooks(id),
    level VARCHAR(10) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lessons INTEGER DEFAULT 0,
    duration VARCHAR(50),
    difficulty VARCHAR(20) NOT NULL CHECK (difficulty IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT')),
    cover_image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vocabulary table
CREATE TABLE vocabulary (
    id BIGSERIAL PRIMARY KEY,
    hanzi VARCHAR(255) NOT NULL,
    pinyin VARCHAR(255) NOT NULL,
    nghia TEXT NOT NULL,
    example TEXT,
    variant VARCHAR(20) NOT NULL CHECK (variant IN ('SIMPLIFIED', 'TRADITIONAL', 'BOTH')),
    hsk_level INTEGER CHECK (hsk_level BETWEEN 1 AND 6),
    frequency_rank INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vocabulary tags (many-to-many relationship)
CREATE TABLE vocab_tags (
    vocab_id BIGINT REFERENCES vocabulary(id) ON DELETE CASCADE,
    tag VARCHAR(100) NOT NULL,
    PRIMARY KEY (vocab_id, tag)
);

-- Grammar topics table
CREATE TABLE grammar_topics (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    content TEXT,
    level VARCHAR(20) NOT NULL CHECK (level IN ('BASIC', 'MEDIUM', 'ADVANCED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Radicals table
CREATE TABLE radicals (
    id BIGSERIAL PRIMARY KEY,
    hanzi VARCHAR(10) NOT NULL UNIQUE,
    strokes INTEGER NOT NULL,
    meaning VARCHAR(255),
    pronunciation VARCHAR(50),
    frequency_rank INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User learning progress
CREATE TABLE user_learning_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    vocab_id BIGINT REFERENCES vocabulary(id) ON DELETE CASCADE,
    mastery_level INTEGER DEFAULT 0 CHECK (mastery_level BETWEEN 0 AND 100),
    last_reviewed TIMESTAMP,
    review_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, vocab_id)
);

-- AI Chat messages (for grammar checking)
CREATE TABLE ai_chat_messages (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    response TEXT,
    message_type VARCHAR(20) DEFAULT 'grammar_check',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Daily AI usage limits
CREATE TABLE user_ai_usage (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    usage_date DATE NOT NULL,
    message_count INTEGER DEFAULT 0,
    daily_limit INTEGER DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, usage_date)
);

-- Indexes for performance
CREATE INDEX idx_users_keycloak_id ON users(keycloak_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_vocabulary_hanzi ON vocabulary(hanzi);
CREATE INDEX idx_vocabulary_variant ON vocabulary(variant);
CREATE INDEX idx_vocabulary_hsk_level ON vocabulary(hsk_level);
CREATE INDEX idx_vocab_tags_vocab_id ON vocab_tags(vocab_id);
CREATE INDEX idx_user_learning_progress_user_id ON user_learning_progress(user_id);
CREATE INDEX idx_user_learning_progress_vocab_id ON user_learning_progress(vocab_id);
CREATE INDEX idx_ai_chat_messages_user_id ON ai_chat_messages(user_id);
CREATE INDEX idx_ai_chat_messages_created_at ON ai_chat_messages(created_at);
CREATE INDEX idx_user_ai_usage_user_id ON user_ai_usage(user_id);
CREATE INDEX idx_user_ai_usage_date ON user_ai_usage(usage_date);

