create table if not exists swimmer (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(24) NOT NULL
);

create table if not exists session (
  id BIGSERIAL PRIMARY KEY,
  swimmer_id BIGINT REFERENCES swimmer(id),
  weight INT NOT NULL,
  weight_unit CHAR(2) NOT NULL,
  laps INT NOT NULL,
  lap_distance INT NOT NULL,
  lap_unit VARCHAR(6) NOT NULL,
  style VARCHAR(9) NOT NULL,
  kickboard BOOLEAN NOT NULL,
  fins BOOLEAN NOT NULL,
  minutes INT NOT NULL,
  seconds INT NOT NULL,
  calories INT NOT NULL,
  datetime BIGINT NOT NULL
);