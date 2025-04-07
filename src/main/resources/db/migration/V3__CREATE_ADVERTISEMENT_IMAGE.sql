CREATE TABLE advertisement_image
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    advertisement_id VARCHAR(255),
    image            BYTEA,
    CONSTRAINT fk_advertisement_image_advertisement FOREIGN KEY (advertisement_id) REFERENCES advertisement (id)
);