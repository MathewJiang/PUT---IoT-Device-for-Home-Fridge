-- ECE496 PUTS Project Core Database
-- Stores grocery purchase/consumption records and fridge sensor data.
-- uid stands for id per package unit (bag/box/bottle/...)
-- std_name is standarized name of a grocery item (apple/milk2%/miso sauce/...)

DROP DATABASE IF EXISTS putsDB;
CREATE DATABASE IF NOT EXISTS putsDB;
USE putsDB;

DROP TABLE IF EXISTS 
		units,
		brands,
		vendors,
		statuses,
		actions,
		categories,
		std_names,
		purchase_history,
		comsumption_history,
		void_items,
		grocery_storage,
		barcodes;



CREATE TABLE content_units (
    unit 	VARCHAR(30)  	NOT NULL,
    PRIMARY KEY (unit)
);

CREATE TABLE package_units (
	unit 	VARCHAR(30) 	NOT NULL,
	PRIMARY KEY (unit)
);

CREATE TABLE brands (
    brand_name 		VARCHAR(30)  	NOT NULL,
    PRIMARY KEY (brand_name)
);

CREATE TABLE vendors (
    vendor_name 	VARCHAR(30)  	NOT NULL,
    PRIMARY KEY (vendor_name)
);

CREATE TABLE statuses (
    status   	VARCHAR(30)     NOT NULL,
    PRIMARY KEY (status)
);

CREATE TABLE actions (
	user_action		VARCHAR(30)	NOT NULL,
	PRIMARY KEY (user_action)
);

CREATE TABLE categories (
	category	VARCHAR(30)	NOT NULL,
	PRIMARY KEY (category)
);

CREATE TABLE std_names (
	std_name 		VARCHAR(30)	NOT NULL,
	category 		VARCHAR(30)	NOT NULL,
	FOREIGN KEY (category) REFERENCES categories (category)	ON DELETE CASCADE,
	PRIMARY KEY (std_name)
);

CREATE TABLE purchase_history (
	uid					INT				NOT NULL AUTO_INCREMENT,
	std_name 			VARCHAR(30)		NOT NULL,
	vendor 				VARCHAR(30)		NOT NULL,
	brand 		 		VARCHAR(30)		NOT NULL,
	content_quantity 	INT				NOT NULL,
	content_unit 		VARCHAR(30)		NOT NULL,
	is_packaged			CHAR(1)			NOT NULL CHECK (is_packaged IN ('T', 'F')),	-- T/F
	package_unit		VARCHAR(30)		NOT NULL,
	purchase_date		DATE 			NOT NULL,
	expiry_date			DATE 			NOT NULL,
	FOREIGN KEY (std_name) REFERENCES std_names (std_name)		ON DELETE CASCADE,
	FOREIGN KEY (vendor) REFERENCES vendors (vendor_name)		ON DELETE CASCADE,
	FOREIGN KEY (brand) REFERENCES brands (brand_name)			ON DELETE CASCADE,
	FOREIGN KEY (content_unit) REFERENCES content_units (unit)	ON DELETE CASCADE,
	FOREIGN KEY (package_unit) REFERENCES package_units (unit)	ON DELETE CASCADE,
	PRIMARY KEY (uid)
);

CREATE TABLE consumption_history (
	opid				INT 			NOT NULL AUTO_INCREMENT,
	uid					INT				NOT NULL,
	std_name 			VARCHAR(30)		NOT NULL,
	consumed_quantity 	INT				NOT NULL CHECK (consumed_quantity > 0),
	remaining_quantity	INT 			NOT NULL CHECK (remaining_quantity >= 0),
	action 				VARCHAR(30)		NOT NULL,
	time_stamp			DATETIME 		NOT NULL,
	FOREIGN KEY (uid) REFERENCES purchase_history (uid)			ON DELETE CASCADE,
	FOREIGN KEY (std_name) REFERENCES std_names (std_name)		ON DELETE CASCADE,
	FOREIGN KEY (action) REFERENCES actions (user_action)		ON DELETE CASCADE,
	PRIMARY KEY (opid)
);

CREATE TABLE void_items (
	uid					INT				NOT NULL,
	std_name 			VARCHAR(30)		NOT NULL,
	void_status			VARCHAR(30) 	NOT NULL,
	time_stamp			DATETIME 		NOT NULL,
	FOREIGN KEY (uid) REFERENCES purchase_history (uid)			ON DELETE CASCADE,
	FOREIGN KEY (std_name) REFERENCES std_names (std_name)		ON DELETE CASCADE,
	FOREIGN KEY (void_status) REFERENCES statuses(status) 		ON DELETE CASCADE,
	PRIMARY KEY (uid)
); 

CREATE TABLE grocery_storage (
	uid					INT				NOT NULL,
	std_name 			VARCHAR(30)		NOT NULL,
	content_quantity	INT 			NOT NULL CHECK (content_quantity > 0),
	content_unit 		VARCHAR(30) 	NOT NULL,
	last_updated		DATETIME 		NOT NULL,
	purchased_date		DATE 			NOT NULL,
	expiry_date			DATE 			NOT NULL,
	status 				VARCHAR(30)		NOT NULL,
	FOREIGN KEY (uid) REFERENCES purchase_history (uid)			ON DELETE CASCADE,
	FOREIGN KEY (std_name) REFERENCES std_names (std_name)		ON DELETE CASCADE,
	FOREIGN KEY (content_unit) REFERENCES content_units (unit)	ON DELETE CASCADE,
	FOREIGN KEY (status) REFERENCES statuses(status) 			ON DELETE CASCADE,
	PRIMARY KEY (uid)
);

CREATE TABLE barcodes (
	barcode 			VARCHAR(30)	NOT NULL,
	brand 				VARCHAR(30)	NOT NULL,
	std_name 			VARCHAR(30)	NOT NULL,
	content_quantity 	VARCHAR(30)	NOT NULL CHECK (content_quantity > 0),
	content_unit 		VARCHAR(30) NOT NULL,
	is_packaged			CHAR(1)		NOT NULL CHECK (is_packaged IN ('T', 'F')),	-- T/F
	package_unit 		VARCHAR(30) NOT NULL,
	FOREIGN KEY (std_name) REFERENCES std_names (std_name)		ON DELETE CASCADE,
	FOREIGN KEY (brand) REFERENCES brands (brand_name)			ON DELETE CASCADE,
	FOREIGN KEY (content_unit) REFERENCES content_units (unit)	ON DELETE CASCADE,
	FOREIGN KEY (package_unit) REFERENCES package_units (unit)	ON DELETE CASCADE,
	PRIMARY KEY (barcode)
);

CREATE TABLE temperature_data (
	rid				INT			NOT NULL AUTO_INCREMENT,
	time			DATETIME	NOT NULL,
	temperature 	FLOAT		NOT NULL,
	PRIMARY KEY (rid)
);

source putsDB_testData.sql; 
