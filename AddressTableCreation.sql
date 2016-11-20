drop database if exists addressBook;
CREATE database if not exists addressBook;

use addressBook;

CREATE TABLE IF NOT EXISTS `Addresses` (
 `addressID` int NOT NULL AUTO_INCREMENT,
 `firstName` varchar(50) NOT NULL,
 `lastName` varchar(50) NOT NULL,
 `street` varchar(50) NOT NULL,
 `cityID` int NOT NULL,
 `stateID` int NOT NULL,
 `zip` varchar(5) NOT NULL,
 PRIMARY KEY (`addressID`)
);

CREATE TABLE IF NOT EXISTS `States` (
 `stateID` int NOT NULL AUTO_INCREMENT,
 `stateName` varchar(50) NOT NULL UNIQUE,
 `stateInitials` varchar(2) NOT NULL UNIQUE,
 PRIMARY KEY (`stateID`)
);

CREATE TABLE IF NOT EXISTS `Cities` (
 `cityID` int NOT NULL AUTO_INCREMENT,
 `cityName` varchar(50) NOT NULL,
 `stateID` int NOT NULL,
 PRIMARY KEY (`cityID`)
);

ALTER TABLE `Addresses` 
 ADD CONSTRAINT FOREIGN KEY (`cityID`) REFERENCES `Cities` (`cityID`);

ALTER TABLE `Addresses` 
 ADD CONSTRAINT FOREIGN KEY (`stateID`) REFERENCES `States` (`stateID`);

ALTER TABLE `Cities` 
 ADD CONSTRAINT FOREIGN KEY (`stateID`) REFERENCES `States` (`stateID`),
 ADD UNIQUE `cityState` (`cityName`, `stateID`);


insert into States (stateName, stateInitials) values 
("Alabama", "AL"),
("Alaska", "AK"),
("Arizona", "AZ"),
("Arkansas", "AR"),
("California", "CA"),
("Colorado", "CO"),
("Connecticut", "CT"),
("Delaware", "DE"),
("Florida", "FL"),
("Georgia", "GA"),
("Hawaii", "HI"),
("Idaho", "ID"),
("Illinois", "IL"),
("Indiana", "IN"),
("Iowa", "IA"),
("Kansas", "KS"),
("Kentucky", "KY"),
("Louisiana", "LA"),
("Maine", "ME"),
("Maryland", "MD"),
("Massachusetts", "MA"),
("Michigan", "MI"),
("Minnesota", "MN"),
("Mississippi", "MS"),
("Missouri", "MO"),
("Montana", "MT"),
("Nebraska", "NE"),
("Nevada", "NV"),
("New Hampshire", "NH"),
("New Jersey", "NJ"),
("New Mexico", "NM"),
("New York", "NY"),
("North Carolina", "NC"),
("North Dakota", "ND"),
("Ohio", "OH"),
("Oklahoma", "OK"),
("Oregon", "OR"),
("Pennsylvania", "PA"),
("Rhode Island", "RI"),
("South Carolina", "SC"),
("South Dakota", "SD"),
("Tennessee", "TN"),
("Texas", "TX"),
("Utah", "UT"),
("Vermont", "VT"),
("Virginia", "VA"),
("Washington", "WA"),
("West Virginia", "WV"),
("Wisconsin", "WI"),
("Wyoming", "WY"),
("District of Columbia", "DC"),
("American Samoa", "AS"),
("Guam", "GU"),
("Northern Marianara Islands", "MP"),
("Puerto Rico", "PR"),
("US Virgin Islands", "VI");