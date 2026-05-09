DROP DATABASE IF EXISTS retrotrade;
CREATE DATABASE retrotrade;

USE retrotrade;

CREATE TABLE user (
  id               VARCHAR(255) PRIMARY KEY,
  username         VARCHAR(255) NOT NULL UNIQUE,
  email    		   VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category  VARCHAR(255) NOT NULL,
    estimated_value DECIMAL(10,2) NOT NULL,

    seller_id VARCHAR(255) NOT NULL,

    CONSTRAINT FK_ITEM_SELLER_ID
        FOREIGN KEY (seller_id)
        REFERENCES user(id)
        ON DELETE CASCADE
);