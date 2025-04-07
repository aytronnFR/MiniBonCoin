CREATE TABLE advertisement
(
    id          VARCHAR(255) NOT NULL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
    category_id VARCHAR(255),
    CONSTRAINT fk_advertisement_category FOREIGN KEY (category_id) REFERENCES category (id)
);