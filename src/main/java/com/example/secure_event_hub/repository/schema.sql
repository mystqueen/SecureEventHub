CREATE TYPE role_enum AS ENUM ('ADMIN', 'USER');

CREATE TABLE "user" (
                        user_id VARCHAR(255) NOT NULL PRIMARY KEY,
                        userName VARCHAR(255),
                        email VARCHAR(255),
                        password VARCHAR(255),
                        role role_enum
);

CREATE TABLE event (
                       event_id VARCHAR(255) NOT NULL PRIMARY KEY,
                       event_type VARCHAR(255),
                       description VARCHAR(255),
                       event_date TIMESTAMP,
                       event_organiser VARCHAR(255),
                       CONSTRAINT fk_event_organiser FOREIGN KEY (event_organiser) REFERENCES "user"(user_id)
);

CREATE TABLE registration (
                              registration_id VARCHAR(255) NOT NULL PRIMARY KEY,
                              user_id VARCHAR(255),
                              event_id VARCHAR(255),
                              status VARCHAR(255),
                              registration_time TIMESTAMP,
                              CONSTRAINT fk_registration_user FOREIGN KEY (user_id) REFERENCES "user"(user_id),
                              CONSTRAINT fk_registration_event FOREIGN KEY (event_id) REFERENCES event(event_id)
);