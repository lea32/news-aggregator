CREATE TABLE newssrc
(
  ID BIGINT IDENTITY PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) DEFAULT '',
  LOCATION VARCHAR(2048) DEFAULT ''
);

CREATE TABLE news
(
  ID BIGINT IDENTITY PRIMARY KEY NOT NULL,
  TITLE VARCHAR(1024),
  CONTENT VARCHAR(1024),
  APPEND_TIMESTAMP DATETIME,
  SOURCE_ID BIGINT NOT NULL,
  FOREIGN KEY (SOURCE_ID) REFERENCES newssrc(ID)
);