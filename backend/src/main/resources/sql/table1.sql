create table groups (
  group_id bigserial PRIMARY key,
  group_name VARCHAR(255) not NULL

);
ALTER TABLE public.groups ADD create_time BIGINT DEFAULT 0 NULL;

CREATE SEQUENCE box_id_seq START WITH 1100001;
CREATE TABLE boxs (
  box_id BIGINT PRIMARY KEY DEFAULT nextval('box_id_seq'),
  box_name VARCHAR(255) not NULL,
  box_mac VARCHAR(63) NOT NULL,
  create_time BIGINT NOT NULL,
  group_id bigint NOT NULL DEFAULT 0
);
ALTER SEQUENCE box_id_seq OWNED BY boxs.box_id;

create table group_box_map(
  group_id BIGINT PRIMARY KEY NOT NULL ,
  box_id BIGINT NOT NULL DEFAULT 0
);

create table count_detail(
  id bigserial PRIMARY KEY,
  group_id VARCHAR(255) not null,
  timestamp BIGINT not NULL ,
  count int not null
);

CREATE TABLE count_history (
  id BIGSERIAL PRIMARY KEY ,
  group_id VARCHAR(255) NOT NULL ,
  timestamp BIGINT NOT NULL ,
  count int NOT NULL
);

CREATE TABLE count_by_day(
  id BIGSERIAL PRIMARY KEY ,
  group_id VARCHAR(255) not NULL ,
  date varchar(255) NOT NULL,
  count int NOT NULL
);


CREATE TABLE resident_by_day(
  group_id VARCHAR(255) ,
  time_point VARCHAR(63) not NULL ,
  duration Int NOT NULL,
  num Int NOT NULL
);
CREATE INDEX rbdidx ON public.resident_by_day (group_id, time_point);

create TABLE new_old_ratio(
 id bigserial PRIMARY KEY ,
 group_id VARCHAR(255) not null,
 date VARCHAR(255) not null,
 old_num int not null,
 new_num int not null
);

create table visit_frequency(
  id BIGSERIAL PRIMARY KEY ,
  group_id VARCHAR(255) NOT NULL ,
  date VARCHAR(255) NOT NULL ,
  frequency int NOT NULL ,
  size int NOT NULL
);
create index on visit_frequency(group_id, date);
create index on count_by_day(group_id, timestamp);
create index on count_detail(group_id,timestamp);
create index on count_history(group_id, timestamp);

CREATE TABLE oui(
  key VARCHAR(63) PRIMARY KEY ,
  organization VARCHAR(255) not NULL DEFAULT ''
);

CREATE TABLE org_brand_map(
  org VARCHAR(255) PRIMARY KEY,
  brand_id INT NOT NULL DEFAULT 0
);

CREATE TABLE brand(
  id SERIAL PRIMARY KEY ,
  name VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE brand_by_day(
  id BIGSERIAL PRIMARY KEY ,
  group_id VARCHAR(255) NOT NULL,
  time_point VARCHAR(63) NOT NULL,
  brand_id BIGINT NOT NULL DEFAULT 0,
  num Int NOT NULL DEFAULT 0);
CREATE INDEX bydidx ON public.brand_by_day (group_id, time_point);