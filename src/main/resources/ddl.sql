create table if not exists swimmer (
  id long primary key auto_increment,
  name varchar(24) not null
);
create index if not exists swimmer_name_idx ON swimmer(name);

create table if not exists session (
  id long primary key auto_increment,
  swimmer_id long references swimmer(id),
  weight int not null,
  weight_unit char(2) not null,
  laps int not null,
  lap_distance int not null,
  lap_unit varchar(6) not null,
  style varchar(9) not null,
  kickboard boolean not null,
  fins boolean not null,
  minutes int not null,
  seconds int not null,
  calories int not null,
  datetime bigint not null
);
create index if not exists session_datetime_idx ON session(datetime);