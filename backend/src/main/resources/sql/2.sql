CREATE SEQUENCE box_id_seq START WITH 1100001;
CREATE TABLE boxs (
  box_id BIGINT PRIMARY KEY DEFAULT nextval('box_id_seq'),
  box_name VARCHAR(255) not NULL,
  box_mac VARCHAR(63) NOT NULL,
  create_time BIGINT NOT NULL,
  group_id bigint NOT NULL DEFAULT 0,
  rssiSet int NOT NULL
);
ALTER SEQUENCE box_id_seq OWNED BY boxs.box_id;

create table groups (
  group_id bigserial PRIMARY key,
  group_name VARCHAR(255) NOT NULL,
  create_time BIGINT NOT NULL,
  duration_length BIGINT NOT NULL
);

CREATE TABLE brand(
  id SERIAL PRIMARY KEY ,
  name VARCHAR(255) NOT NULL DEFAULT ''
);
