drop database if exists sql_utilities;
create database if not exists sql_utilities;
use sql_utilities;

create table if not exists users(
	email_user varchar(100) not null,
	key_user int,
	active boolean,
	creationDate date not null,
	lastAccessDate datetime not null,
	documentType char(15) not null,
	documentNumber int,
	primary key (documentType,documentNumber)
); 
