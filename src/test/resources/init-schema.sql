DROP TABLE IF EXISTS user;
CREATE table  user(
  id int(11) unsigned not null auto_increment,
  name  VARCHAR(64) not null default '',
  password VARCHAR(128) not null default '',
  salt varchar(32) not null default '',
  head_url varchar(256) not null default '',
  primary key (id),
  unique key (name)
)ENGINE =InnoDB default charset = utf8;

DROP table if exists news;
create table news(
  id int(11) unsigned not null auto_increment,
  title varchar(128) not null default '',
  link varchar(256) NOT NULL DEFAULT '',
  image varchar(256) NOT NULL DEFAULT '',
  like_count int NOT NULL,
  comment_count int NOT NULL,
  created_date datetime NOT NULL,
  user_id int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS login_ticket;
CREATE TABLE login_ticket(
  id int not null auto_increment,
  user_id int not null ,
  expired DATETIME not null,
  ticket VARCHAR(45) not null ,
  status int null default 0,
  primary key (id),
  unique index ticket_UNIQUE (ticket asc)
);

DROP TABLE IF EXISTS comment;
CREATE TABLE comment(
  id int NOT NULL AUTO_INCREMENT,
  comment text not null ,
  user_id int not null ,
  entity_id int not null ,
  entity_type int not null ,
  created_date DATETIME not null ,
  status int not null default 0,
  primary key (id),
  INDEX entity_index (entity_id ASC ,entity_type asc)
)engine=InnoDB DEFAULT CHARSET =utf8;

