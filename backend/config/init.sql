DROP DATABASE IF EXISTS retrotrade;
CREATE DATABASE retrotrade;

USE retrotrade;

CREATE TABLE user (
  id               VARCHAR(255) PRIMARY KEY,
  registered_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
  fullName         VARCHAR(255) NOT NULL,
  username         VARCHAR(255) NOT NULL UNIQUE,
  email    		   VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category  VARCHAR(255) NOT NULL,
    estimated_value DECIMAL(10,2) NOT NULL,
    icon_char VARCHAR(50) DEFAULT NULL,
    photo LONGBLOB NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT "ACTIVE",

    seller_id VARCHAR(255) NOT NULL,

    CONSTRAINT FK_ITEM_SELLER_ID
        FOREIGN KEY (seller_id)
        REFERENCES user(id)
        ON DELETE CASCADE
);

CREATE TABLE item_view (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    viewed_at TIMESTAMP NOT NULL 
        DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP,

    user_id VARCHAR(255) NOT NULL,
    item_id INT NOT NULL,

    -- Prevent duplicates per user/item
    CONSTRAINT UK_USER_ITEM UNIQUE (user_id, item_id),

    CONSTRAINT FK_ITEM_VIEW_USER_ID
        FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE,

    CONSTRAINT FK_ITEM_VIEW_ITEM_ID
        FOREIGN KEY (item_id) REFERENCES item(id)
        ON DELETE CASCADE
);


CREATE TABLE trade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'Pending',

	item_id INT NOT NULL,
    requester_id VARCHAR(255) NOT NULL,
    receiver_id VARCHAR(255) NOT NULL,
    
    CONSTRAINT FK_TRADE_ITEM_ID
        FOREIGN KEY (item_id)
        REFERENCES item(id)
        ON DELETE CASCADE,

    CONSTRAINT FK_TRADE_REQUESTER_ID
        FOREIGN KEY (requester_id)
        REFERENCES user(id)
        ON DELETE CASCADE,

    CONSTRAINT FK_TRADE_RECEIVER_ID
        FOREIGN KEY (receiver_id)
        REFERENCES user(id)
        ON DELETE CASCADE
);