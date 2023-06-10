drop table if exists users;
drop table if exists votes;
drop table if exists submissions;
drop table if exists rounds;
drop table if exists songs;
drop table if exists artists;
drop table if exists artists_genres;

create table users (
	id varchar(255) not null,
	name varchar(255) not null,
	primary key (id)
);

create table votes (
	user_id varchar(255) not null,
	submission_id varchar(255) not null,
	comment mediumtext,
	weight int not null,
	primary key (user_id, submission_id)
);

create table submissions (
	id varchar(255) not null,
	user_id varchar(255) not null,
	spotify_uri varchar(255) not null,
	comment mediumtext,
	round_id varchar(255) not null,
	primary key (id)
);

create table rounds (
	id varchar(255) not null,
	name varchar(255) not null,
	description mediumtext,
	playlist_uri varchar(255) not null,
	league_id varchar(255) not null,
	primary key (id)
);

create table songs (
	spotify_uri varchar(255) not null,
	name varchar(255) not null,
	artist_id varchar(255) not null,
	duration_ms int not null,
	popularity_num int not null,
	primary key (spotify_uri)
);

create table artists (
	id varchar(255) not null,
	spotify_uri varchar(255) not null,
	name varchar(255) not null,
	popularity_num int not null,
	primary key (id)
);

create table artists_genres (
	artist_id varchar(255) not null,
	name varchar(255) not null,
	primary key (artist_id, name)
);
