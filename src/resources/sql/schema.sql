DROP DATABASE client_schedule;
CREATE DATABASE client_schedule;
USE client_schedule;


SET FOREIGN_KEY_CHECKS = 0;
SET @tarray = NULL;
SELECT GROUP_CONCAT('`', table_name, '`') INTO @tarray
  FROM information_schema.tables WHERE table_schema = (SELECT DATABASE());
SELECT IFNULL(@tarray,'dummy') INTO @tarray;
SET @tarray = CONCAT('DROP TABLE IF EXISTS ', @tarray);
PREPARE stmt FROM @tarray;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
SET FOREIGN_KEY_CHECKS = 1;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `User_ID` int(10) NOT NULL AUTO_INCREMENT,
                         `User_Name` varchar(50) DEFAULT NULL,
                         `Password` text,
                         `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
                         `Created_By` varchar(50) DEFAULT NULL,
                         `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `Last_Updated_By` varchar(50) DEFAULT NULL,
                         PRIMARY KEY (`User_ID`),
                         UNIQUE KEY `User_Name` (`User_Name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts` (
                            `Contact_ID` int(10) NOT NULL AUTO_INCREMENT,
                            `Contact_Name` varchar(50) NOT NULL,
                            `Email` varchar(50) NOT NULL,
                            PRIMARY KEY (`Contact_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `countries`;
CREATE TABLE `countries` (
                             `Country_ID` int(10) NOT NULL AUTO_INCREMENT,
                             `Country` varchar(50) NOT NULL,
                             `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
                             `Created_By` varchar(50) DEFAULT NULL,
                             `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `Last_Updated_By` varchar(50) DEFAULT NULL,
                             PRIMARY KEY (`Country_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `first_level_divisions`;

CREATE TABLE `first_level_divisions` (
                                         `Division_ID` int(10) NOT NULL AUTO_INCREMENT,
                                         `Division` varchar(50) NOT NULL,
                                         `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
                                         `Created_By` varchar(50) DEFAULT NULL,
                                         `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         `Last_Updated_By` varchar(50) DEFAULT NULL,
                                         `COUNTRY_ID` int(11) NOT NULL,
                                         PRIMARY KEY (`Division_ID`),
                                         KEY `COUNTRY_ID` (`COUNTRY_ID`),
                                         CONSTRAINT `first_level_divisions_ibfk_1` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `countries` (`Country_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
                             `Customer_ID` int(10) NOT NULL AUTO_INCREMENT,
                             `Customer_Name` varchar(50) NOT NULL,
                             `Address` varchar(100) NOT NULL,
                             `Postal_Code` varchar(50) NOT NULL,
                             `Phone` varchar(50) NOT NULL,
                             `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
                             `Created_By` varchar(50) DEFAULT NULL,
                             `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `Last_Updated_By` varchar(50) DEFAULT NULL,
                             `Division_ID` int(10) NOT NULL,
                             PRIMARY KEY (`Customer_ID`),
                             KEY `Division_ID` (`Division_ID`),
                             CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`Division_ID`) REFERENCES `first_level_divisions` (`Division_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
DROP TABLE IF EXISTS `appointments`;

CREATE TABLE `appointments` (
                                `Appointment_ID` int(10) NOT NULL AUTO_INCREMENT,
                                `Title` varchar(50) NOT NULL,
                                `Description` varchar(50) DEFAULT NULL,
                                `Location` varchar(50) NOT NULL,
                                `Type` varchar(50) DEFAULT NULL,
                                `Start` datetime DEFAULT CURRENT_TIMESTAMP,
                                `End` datetime DEFAULT CURRENT_TIMESTAMP,
                                `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
                                `Created_By` varchar(50) DEFAULT NULL,
                                `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `Last_Updated_By` varchar(50) DEFAULT NULL,
                                `Customer_ID` int(10) DEFAULT NULL,
                                `User_ID` int(10) DEFAULT NULL,
                                `Contact_ID` int(10) DEFAULT NULL,
                                PRIMARY KEY (`Appointment_ID`),
                                KEY `Customer_ID` (`Customer_ID`),
                                KEY `User_ID` (`User_ID`),
                                KEY `Contact_ID` (`Contact_ID`),
                                CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`Customer_ID`) REFERENCES `customers` (`Customer_ID`),
                                CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`User_ID`) REFERENCES `users` (`User_ID`),
                                CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`Contact_ID`) REFERENCES `contacts` (`Contact_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

INSERT INTO users VALUES(1, 'test', 'test', NOW(), 'script', NOW(), 'script');
INSERT INTO users VALUES(2, 'admin', 'admin', NOW(), 'script', NOW(), 'script');

-- contacts

INSERT INTO contacts VALUES(1,	'Anika Costa', 'acoasta@company.com');
INSERT INTO contacts VALUES(2,	'Daniel Garcia',	'dgarcia@company.com');
INSERT INTO contacts VALUES(3,	'Li Lee',	'llee@company.com');

-- Counties

INSERT INTO countries VALUES(1,	'U.S',	NOW(), 'script', NOW(), 'script');
INSERT INTO countries VALUES(2,	'UK',	NOW(), 'script', NOW(), 'script');
INSERT INTO countries VALUES(3,	'Canada',	NOW(), 'script', NOW(), 'script');

-- First Level Divisions

INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alabama', 1, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alaska', 54, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Arizona', 02, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Arkansas', 03, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('California', 04, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Colorado', 05, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Connecticut', 06, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Delaware', 07, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('District of Columbia', 08, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Florida', 09, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Georgia', 10, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Hawaii', 52, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Idaho', 11, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Illinois', 12, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Indiana', 13, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Iowa', 14, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Kansas', 15, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Kentucky', 16, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Louisiana', 17, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Maine', 18, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Maryland', 19, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Massachusetts', 20, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Michigan', 21, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Minnesota', 22, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Mississippi', 23, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Missouri', 24, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Montana', 25, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nebraska', 26, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nevada', 27, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Hampshire', 28, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Jersey', 29, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Mexico', 30, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New York', 31, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('North Carolina', 32, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('North Dakota', 33, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Ohio', 34, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Oklahoma', 35, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Oregon', 36, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Pennsylvania', 37, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Rhode Island', 38, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('South Carolina', 39, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('South Dakota', 40, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Tennessee', 41, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Texas', 42, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Utah', 43, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Vermont', 44, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Virginia', 45, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Washington', 46, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('West Virginia', 47, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wisconsin', 48, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wyoming', 49, NOW(), 'script', NOW(), 'script', 1 );



INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Alberta', 61, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('British Columbia', 62, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Manitoba', 63, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('New Brunswick', 64, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Newfoundland and Labrador', 72, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Northwest Territories', 60, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nova Scotia', 65, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Nunavut', 70, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Ontario', 67, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Prince Edward Island', 66, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('QuÃ©bec', 68, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Saskatchewan', 69, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Yukon', 71, NOW(), 'script', NOW(), 'script', 3 );

INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('England', 101, NOW(), 'script', NOW(), 'script', 2 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Wales', 102, NOW(), 'script', NOW(), 'script', 2 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Scotland',103, NOW(), 'script', NOW(), 'script', 2 );
INSERT INTO first_level_divisions(Division, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By, COUNTRY_ID) VALUES('Northern Ireland', 104, NOW(), 'script', NOW(), 'script', 2 );

-- Customers

INSERT INTO customers VALUES(1, 'John Wilson', '1458 Boardwalk', '01291', '869-908-1875', NOW(), 'script', NOW(), 'script', 29);
INSERT INTO customers VALUES(2, 'Jill Anderson', '78 Wonder Road', 'AF19B', '11-445-910-2135', NOW(), 'script', NOW(), 'script', 103);
INSERT INTO customers VALUES(3, 'Brad Smith', '685 Walking Manor ', '28198', '874-916-2671', NOW(), 'script', NOW(), 'script', 60);

-- Appointments

INSERT INTO appointments VALUES(1, 'title', 'description', 'location', 'Planning Session', '2020-05-28 12:00:00', '2020-05-28 13:00:00', NOW(), 'script', NOW(), 'script', 1, 1, 3);
INSERT INTO appointments VALUES(2, 'title', 'description', 'location', 'De-Briefing', '2020-05-29 12:00:00', '2020-05-29 13:00:00', NOW(), 'script', NOW(), 'script', 2, 2, 2);