create database if not exists politics_app;

create table if not exists politics_app.user_cred (
	user_id bigint AUTO_INCREMENT,
	uname varchar(100),
	email varchar(100),
	pass_hash varchar(255),
	active tinyint,
	created_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
	PRIMARY KEY (user_id)
);

create table if not exists politics_app.city (
	city_id bigint,
	city_name varchar(100),
	PRIMARY KEY (city_id)
);

create table if not exists politics_app.user_images (
	image_id bigint AUTO_INCREMENT,
	created_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
	user_id bigint,
	PRIMARY KEY (image_id),
	FOREIGN KEY (user_id) REFERENCES politics_app.user_cred(user_id)
);

create table if not exists politics_app.issue (
	issue_id bigint AUTO_INCREMENT,
	city_id bigint,
	text varchar(500),
	image_id bigint,
	issue_level varchar(20),
	user_id bigint,
	issue_status varchar(10) default "open",
	created_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(), 
	PRIMARY KEY (issue_id),
	FOREIGN KEY (image_id) REFERENCES politics_app.user_images(image_id),
	FOREIGN KEY (city_id) REFERENCES politics_app.city(city_id),
	FOREIGN KEY (user_id) REFERENCES politics_app.user_cred(user_id)
);

create table if not exists politics_app.user_follows (
	user_id bigint, 
	issue_id bigint, 
	created_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(), 
	FOREIGN KEY (user_id) REFERENCES politics_app.user_cred(user_id),
	FOREIGN KEY (issue_id) REFERENCES politics_app.issue(issue_id)
);

