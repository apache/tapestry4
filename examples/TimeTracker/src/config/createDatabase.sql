create table projects (
	project_id		integer 		not null generated always as identity(start with 100, increment by 1),
	name			varchar(40)		not null,
);

insert into projects(name) values ('Code Zeitgeist');
insert into projects(name) values ('Zooland Systems');
insert into projects(name) values ('Weedasher Industries');