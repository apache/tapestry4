create table projects (
	project_id		integer 		not null generated always as identity(start with 100, increment by 1),
	name			varchar(40)		not null
);

alter table projects add constraint projects_pk
primary key(project_id);

alter table projects add constraint name_unique 
unique (name);

insert into projects(name) values ('Code Zeitgeist');
insert into projects(name) values ('Zooland Systems');
insert into projects(name) values ('Weedasher Industries');

create table tasks (
	task_id			integer			not null generated always as identity(start with 100, increment by 1),
	project_id		integer			not null,
	start_dt		timestamp		not null,
	end_dt			timestamp		not null,
	descr_txt		varchar(200)	not null
);

alter table tasks add constraint tasks_fk
Foreign Key(project_id) references projects (project_id);
