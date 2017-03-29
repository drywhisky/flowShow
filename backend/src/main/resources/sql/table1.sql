CREATE SEQUENCE user_id_seq START WITH 1000 INCREMENT BY 1;
CREATE TABLE "public"."user" (
  "id" BIGINT DEFAULT nextval('user_id_seq'::regclass) NOT NULL,
  CONSTRAINT "user_pkey" PRIMARY KEY ("id"),
  nickname VARCHAR(255) NOT NULL,
  head_img VARCHAR(255),
  sex INTEGER NOT NULL ,
  login_psw VARCHAR(255) NOT NULL,
  create_time BIGINT NOT NULL
);


CREATE SEQUENCE chat_room_id_seq START WITH 5000 INCREMENT BY 1;
CREATE TABLE "public"."chat_room" (
  "id" BIGINT DEFAULT nextval('chat_room_id_seq'::regclass) NOT NULL,
  CONSTRAINT "chat_room_pkey" PRIMARY KEY ("id"),
  room_name VARCHAR(255) NOT NULL,
  state INTEGER DEFAULT 1 NOT NULL,
  create_time BIGINT NOT NULL
);

CREATE SEQUENCE stay_record_id_seq START WITH 10000 INCREMENT BY 1;
CREATE TABLE "public"."stay_record"
(
  "id" BIGINT DEFAULT nextval('stay_record_id_seq'::regclass) NOT NULL,
  CONSTRAINT "stay_record_pkey" PRIMARY KEY ("id"),
  user_id BIGINT NOT NULL,
  room_id BIGINT NOT NULL,
  begin_time BIGINT NOT NULL,
  end_time BIGINT NOT NULL
);


CREATE SEQUENCE chat_log_id_seq START WITH 50000 INCREMENT BY 1;
CREATE TABLE "public"."chat_log" (
  "id" BIGINT DEFAULT nextval('chat_log_id_seq'::regclass) NOT NULL,
  CONSTRAINT "chat_log_pkey" PRIMARY KEY ("id"),
  room_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  user_nick_name VARCHAR(255) NOT NULL,
  content VARCHAR(255) NOT NULL,
  create_time BIGINT NOT NULL
);
CREATE INDEX clm_index ON chat_log (room_id);
