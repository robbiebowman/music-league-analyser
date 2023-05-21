drop table if exists users;
drop table if exists votes;
drop table if exists submissions;
drop table if exists rounds;

create table users (
	id varchar(255) not null,
	name varchar(255) not null
);

create table votes (
	user_id varchar(255) not null,
	submission_id varchar(255) not null,
	comment mediumtext,
	weight int not null
);

create table submissions (
	id varchar(255) not null,
	user_id varchar(255) not null,
	spotify_uri varchar(255) not null,
	comment mediumtext,
	round_id varchar(255) not null
);

create table rounds (
	id varchar(255) not null,
	name varchar(255) not null,
	description mediumtext,
	playlist_uri varchar(255) not null
);
