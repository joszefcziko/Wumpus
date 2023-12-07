

#Táblák
DROP DATABASE IF EXISTS wumpus;
CREATE DATABASE wumpus CHARACTER SET utf8 COLLATE utf8_hungarian_ci;
USE wumpus;

CREATE TABLE users (
	id int primary key not null AUTO_INCREMENT, 
    name varchar(30),
	passw varchar(30),
	hiscore int
);

CREATE TABLE map(
	id int primary key not null AUTO_INCREMENT, 
	userid int,
    name varchar(30),
	size int,
	herocol varchar(1),
    herodir varchar(1),
	herorow int,
	data varchar(400),
	FOREIGN KEY (userid) REFERENCES users(id)
);

CREATE TABLE state(
id int primary key not null AUTO_INCREMENT, 
	userid int,
    name varchar(30),
	size int,
	herocol varchar(1),
	herodir varchar(1),
	herorow int,
	arrows int,
	steps int,
	data varchar(400),
	FOREIGN KEY (userid) REFERENCES users(id)
);

#Táblák feltöltése

insert into users (name,passw,hiscore) values
('User', '',9),
('Anonymus', 'user',9);

insert into map (userid, name, size, herocol, herorow, herodir, data) values
(2, 'TEST', 6, 'B', 5, 'EAST', 'WWWWWWW___PWWUGP_WW____WW__P_WWWWWWW');


#Függvényel

/*---------------------------*/

DELIMITER $
CREATE PROCEDURE `LoadMap`(IN `map_name` varchar(30), IN `user_name` varchar(30)) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER 

SELECT map.id, users.name, map.name, map.size, map.herocol, map.herorow, map.data
FROM map
LEFT JOIN (SELECT id, name FROM users) AS users
ON users.id = map.userid
	WHERE UPPER(map.name) LIKE CONCAT('%',UPPER(map_name),'%')
AND UPPER(users.name) = UPPER(user_name)
$

/*---------------------------*/

DELIMITER $
CREATE PROCEDURE `LoadState`(IN `state_name` varchar(30)) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER 

SELECT id, userid, name, size, herocol, herorow, arrows, steps, dat
	FROM `state` 
	WHERE UPPER(name) LIKE CONCAT('%',UPPER(state_name),'%')
$

