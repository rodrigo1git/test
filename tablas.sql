CREATE EXTENSION IF NOT EXISTS vector;

DROP TABLE IF EXISTS liked_post CASCADE;
DROP TABLE IF EXISTS post_comment CASCADE;
DROP TABLE IF EXISTS follows CASCADE;
DROP TABLE IF EXISTS post CASCADE;
DROP TABLE IF EXISTS post_category CASCADE;
DROP TABLE IF EXISTS profile CASCADE;

CREATE TABLE profile (
    id SERIAL,
    name VARCHAR(40) NOT NULL,
    second_name VARCHAR(40),
    last_name VARCHAR(40) NOT NULL,
    public_name VARCHAR(60) NOT NULL,
    email VARCHAR(60) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    photo TEXT,
    user_embedding vector(768),
    like_count BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_profile PRIMARY KEY(id)
);

CREATE TABLE follows (
    id_follower INTEGER NOT NULL,
    id_followed INTEGER NOT NULL,
    since DATE NOT NULL,
    CONSTRAINT pk_follows PRIMARY KEY(id_follower, id_followed),
    CONSTRAINT fk_follower FOREIGN KEY(id_follower) REFERENCES profile(id) ON DELETE CASCADE,
    CONSTRAINT fk_followed FOREIGN KEY(id_followed) REFERENCES profile(id) ON DELETE CASCADE
);

CREATE TABLE post_category (
    category_id SERIAL,
    name VARCHAR(40) NOT NULL,
    description TEXT,
    embedding vector(768),
    CONSTRAINT pk_post_category PRIMARY KEY(category_id)
);

CREATE TABLE post (
    post_id SERIAL,
    post_title TEXT NOT NULL,
    post_body TEXT NOT NULL,
    post_date DATE NOT NULL,
    image_url TEXT,
    profile_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    embedding vector(768),
    like_count BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_post PRIMARY KEY(post_id),
    CONSTRAINT fk_post_category FOREIGN KEY(category_id) REFERENCES post_category(category_id),
    CONSTRAINT fk_user_id FOREIGN KEY(profile_id) REFERENCES profile(id)
);

CREATE TABLE post_comment (
    id SERIAL,
    body TEXT NOT NULL,
    image_url TEXT,
    comment_date DATE NOT NULL,
    post_id INTEGER NOT NULL,
    profile_id INTEGER NOT NULL,
    CONSTRAINT pk_comment_id PRIMARY KEY(id),
    CONSTRAINT fk_post_comment_profile FOREIGN KEY(profile_id) REFERENCES profile(id),
    CONSTRAINT fk_post_id FOREIGN KEY(post_id) REFERENCES post(post_id)
);

CREATE TABLE liked_post (
    post_id INTEGER NOT NULL,
    profile_id INTEGER NOT NULL,
    liked_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_liked_post PRIMARY KEY (post_id, profile_id),
    CONSTRAINT fk_liked_post_post FOREIGN KEY (post_id) REFERENCES post(post_id),
    CONSTRAINT fk_liked_post_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

CREATE INDEX idx_liked_post_profile ON liked_post(profile_id);