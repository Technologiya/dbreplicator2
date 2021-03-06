--Connections
insert into hikari_cp_settings 
(id_pool, driver, url, user, pass, max_pool_size, init_fail_fast, connection_timeout, idle_timeout, max_lifetime) values 
('source', 'org.h2.Driver', 'jdbc:h2:mem://localhost/~/source', 'sa', '', 10, false, 10000, 10000, 10000),
('error', 'org.h2.Driver', 'jdbc:h2:mem://localhost/~/source', 'sa', '', 10, false, 10000, 10000, 10000),
('dest', 'org.h2.Driver', 'jdbc:h2:mem://localhost/~/dest' , 'sa', '', 10, false, 10000, 10000, 10000);

--application_settings
insert into application_settings (key, value) values ('tp.threads', '10');
insert into application_settings (key, value) values ('stats.dest', 'error');
insert into application_settings (key, value) values ('error.dest', 'error');

--Runners Super Log
insert into runners (id_runner, source, target, description) 
values (1, 'source', 'source', 'description');
--Strategies Add Super Log
insert into strategies (id, className, param, isEnabled, priority, id_runner) 
values (1, 'ru.taximaxim.dbreplicator2.replica.strategies.superlog.Manager', 'key1=value1
key2=''value2''', true, 100, 1);

--Runners Task

--Runner Table 1
insert into runners (id_runner, source, target, description) 
values (3, 'source', 'dest', 'description');
--Strategy  Table 1
insert into strategies (id, className, param, isEnabled, priority, id_runner) 
values (3, 'ru.taximaxim.dbreplicator2.replica.strategies.replication.Generic', null, true, 100, 3);
insert into tables (name, id_runner, param) 
values ('T_TABLE', 3, 'ignoredCols=_STRING
requiredCols=ID,_INT,_BOOLEAN,_DATE,_TIME,_TIMESTAMP,_NOCOLOMN,_STRING');

-------

--Runner Table 2
insert into runners (id_runner, source, target, description) 
values (4, 'source', 'dest', 'description');
--Strategy  Table 2
insert into strategies (id, className, param, isEnabled, priority, id_runner) 
values (4, 'ru.taximaxim.dbreplicator2.replica.strategies.replication.Generic', null, true, 100, 4);
insert into tables (name, id_runner, param) 
values ('T_TABLE1', 4, 'ignoredCols=_STRING
requiredCols=ID,_INT,_BOOLEAN,_DATE,_TIME,_TIMESTAMP,_NOCOLOMN,_STRING');

--Runner CountWatchgdog
insert into runners (id_runner, source, target, description) 
values (7, 'source', 'source', 'ErrorsCountWatchgdogStrategy');
--Strategy  CountWatchgdog
insert into strategies (id, className, param, isEnabled, priority, id_runner) 
values (7, 'ru.taximaxim.dbreplicator2.replica.strategies.errors.CountWatchgdog', 'maxErrors=0
partEmail=10', true, 100, 7);
insert into strategies (id, className, param, isEnabled, priority, id_runner) 
values (10, 'ru.taximaxim.dbreplicator2.replica.strategies.errors.CountWatchgdog', null, true, 100, 7);