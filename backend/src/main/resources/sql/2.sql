CREATE SEQUENCE user_id_seq START WITH 4001;
CREATE TABLE users (
  user_id BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
  user_name VARCHAR(255) NOT NULL,
  create_time BIGINT NOT NULL,
  login_psw VARCHAR(255) NOT NULL
);
ALTER SEQUENCE user_id_seq OWNED BY users.user_id;


CREATE SEQUENCE box_id_seq START WITH 100001;
CREATE TABLE boxs (
  box_id BIGINT PRIMARY KEY DEFAULT nextval('box_id_seq'),
  box_name VARCHAR(255) not NULL,
  box_mac VARCHAR(63) NOT NULL,
  create_time BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  group_id BIGINT NOT NULL DEFAULT 0,
  rssi_set int NOT NULL,
  x FLOAT ,
  y FLOAT
);
ALTER SEQUENCE box_id_seq OWNED BY boxs.box_id;

CREATE SEQUENCE group_id_seq START WITH 7001;
create table groups (
  group_id BIGINT PRIMARY KEY DEFAULT nextval('group_id_seq'),
  group_name VARCHAR(255) NOT NULL,
  user_id BIGINT NOT NULL,
  create_time BIGINT NOT NULL,
  duration_length BIGINT NOT NULL,
  map VARCHAR(255) ,
  scala FLOAT ,
  width FLOAT ,
  height FLOAT
);
ALTER SEQUENCE group_id_seq OWNED BY groups.group_id;


create table count_detail(
  id bigserial PRIMARY KEY,
  group_id VARCHAR(255) not null,
  timestamp BIGINT not NULL ,
  count int not null
);

CREATE TABLE user_action (
  id BIGSERIAL PRIMARY KEY ,
  cilent_mac VARCHAR(255) NOT NULL ,
  group_id BIGINT NOT NULL ,
  in_time BIGINT NOT NULL ,
  out_time BIGINT
);

CREATE TABLE staff_mac (
  id BIGSERIAL PRIMARY KEY ,
  mac VARCHAR(255) NOT NULL ,
  group_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL
);

--
-- CREATE TABLE brand(
--   id SERIAL PRIMARY KEY ,
--   name VARCHAR(255) NOT NULL DEFAULT ''
-- );