DROP TABLE IF EXISTS users, items, bookings, comments CASCADE;

CREATE TABLE users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(512) NOT NULL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  user_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  available BOOLEAN NOT NULL,
  CONSTRAINT pk_item PRIMARY KEY (id),
  FOREIGN  KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE bookings (
   id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   status VARCHAR NOT NULL,
   booker_id BIGINT NOT NULL,
   item_id BIGINT NOT NULL,
   CONSTRAINT pk_booking PRIMARY KEY (id),
   FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE,
   FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);

CREATE TABLE comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR NOT NULL,
    item_id BIGINT NOT NULL,
    author_id INTEGER NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);