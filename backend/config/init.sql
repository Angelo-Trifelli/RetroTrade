DROP DATABASE IF EXISTS retrotrade;
CREATE DATABASE retrotrade;

USE retrotrade;

CREATE TABLE user (
  id               VARCHAR(255) PRIMARY KEY,
  username         VARCHAR(255) NOT NULL UNIQUE,
  email    		   VARCHAR(255) NOT NULL UNIQUE
);