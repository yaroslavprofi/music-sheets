CREATE DATABASE IF NOT EXISTS sheets_db;

USE sheets_db;

CREATE TABLE IF NOT EXISTS posts
(
    id         BIGINT AUTO_INCREMENT               NOT NULL,
    name       TEXT                                NOT NULL,
    instrument TEXT                                NOT NULL,
    difficulty TEXT                                NOT NULL,
    comment    TEXT                                NOT NULL,
    file       MEDIUMBLOB                          NOT NULL,
    date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);