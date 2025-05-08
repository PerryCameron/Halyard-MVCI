CREATE TABLE `form_settings` (
  `form_id` varchar(60) NOT NULL,
  `PORT` int,
  `LINK` varchar(200),
  `form_url` varchar(255) NOT NULL,
  `selected_year` int,
  PRIMARY KEY (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ECSC_SQL.form_settings
(
    form_id  varchar(60)  not null primary key,
    PORT     int,
    LINK     varchar(200),
    form_url varchar(255) not null,
    selected_year int
);

#   next table

CREATE TABLE `form_email_auth` (
  `HOST` varchar(100),
  `PORT` int,
  `USER` varchar(100) NOT NULL,
  `PASS` varchar(100),
  `PROTOCOL` varchar(20),
  `SMTP_AUTH` tinyint(1),
  `TTLS` tinyint(1),
  `DEBUG` tinyint(1),
  `id` int NOT NULL,
  `email` varchar(255),
  PRIMARY KEY (`USER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ECSC_SQL.form_email_auth
(
    HOST      varchar(100),
    PORT      int,
    USER      varchar(100) primary key,
    PASS      varchar(100),
    PROTOCOL  varchar(20),
    SMTP_AUTH boolean,
    TTLS      boolean,
    DEBUG     boolean,
    id int NOT NULL,
    email varchar(255)
);

#   next table

CREATE TABLE `officer` (
  `O_ID` int NOT NULL auto_increment,
  `P_ID` int NOT NULL,
  `BOARD_YEAR` int,
  `OFF_TYPE` varchar(20),
  `OFF_YEAR` int,
  PRIMARY KEY (`O_ID`),
  FOREIGN KEY (`P_ID`) REFERENCES `person`(`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create table ECSC_SQL.officer
(
    O_ID       INTEGER NOT NULL auto_increment primary key,
    P_ID       INTEGER NOT NULL,
    BOARD_YEAR INTEGER NULL,
    OFF_TYPE   varchar(20),
    OFF_YEAR   INTEGER NULL, # This maintains the record forever
    foreign key (P_ID) references person (P_ID),
    unique (P_ID, OFF_YEAR, OFF_TYPE)
);

#   next table

CREATE TABLE `wait_list` (
  `MS_ID` int NOT NULL,
  `SLIP_WAIT` tinyint(1),
  `KAYAK_RACK_WAIT` tinyint(1),
  `SHED_WAIT` tinyint(1),
  `WANT_SUBLEASE` tinyint(1),
  `WANT_RELEASE` tinyint(1),
  `WANT_SLIP_CHANGE` tinyint(1),
  PRIMARY KEY (`MS_ID`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fee` (
  `FEE_ID` int NOT NULL auto_increment,
  `FIELD_NAME` varchar(40),
  `FIELD_VALUE` decimal(10,2),
  `DB_INVOICE_ID` int NOT NULL,
  `FEE_YEAR` int NOT NULL,
  `Description` varchar(40),
  `DEFAULT_FEE` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`FEE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `memo` (
  `MEMO_ID` int NOT NULL auto_increment,
  `MS_ID` int NOT NULL,
  `MEMO_DATE` date NOT NULL,
  `MEMO` varchar(2000),
  `INVOICE_ID` int,
  `CATEGORY` varchar(20) NOT NULL,
  `boat_id` int,
  PRIMARY KEY (`MEMO_ID`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `db_table_changes` (
  `id` int NOT NULL,
  `db_updates_id` int NOT NULL,
  `table_changed` varchar(50),
  `table_insert` int NOT NULL,
  `table_delete` int NOT NULL,
  `table_update` int NOT NULL,
  `change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `changed_by` varchar(100),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`db_updates_id`) REFERENCES `db_updates`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `membership` (
  `MS_ID` int NOT NULL auto_increment,
  `P_ID` int NOT NULL,
  `JOIN_DATE` date,
  `MEM_TYPE` varchar(4),
  `ADDRESS` varchar(40),
  `CITY` varchar(20),
  `STATE` varchar(4),
  `ZIP` varchar(15),
  PRIMARY KEY (`MS_ID`),
  UNIQUE KEY (`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `board_positions` (
  `id` int NOT NULL,
  `position` varchar(50) NOT NULL,
  `identifier` varchar(5) NOT NULL,
  `list_order` int NOT NULL,
  `is_officer` tinyint(1) NOT NULL,
  `is_chair` tinyint(1) NOT NULL,
  `is_assistant_chair` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`position`),
  UNIQUE KEY (`identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `stats_view` (
  `STAT_ID` int NOT NULL DEFAULT 0,
  `FISCAL_YEAR` int,
  `REGULAR` bigint NOT NULL DEFAULT 0,
  `FAMILY` bigint NOT NULL DEFAULT 0,
  `SOCIAL` bigint NOT NULL DEFAULT 0,
  `LAKE_ASSOCIATES` bigint NOT NULL DEFAULT 0,
  `LIFE_MEMBERS` bigint NOT NULL DEFAULT 0,
  `STUDENT` bigint NOT NULL DEFAULT 0,
  `RACE_FELLOWS` bigint NOT NULL DEFAULT 0,
  `NEW_MEMBERS` bigint NOT NULL DEFAULT 0,
  `RETURN_MEMBERS` bigint NOT NULL DEFAULT 0,
  `NON_RENEW` decimal(23,0),
  `ACTIVE_MEMBERSHIPS` decimal(25,0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `db_roster_radio_selection` (
  `ID` int NOT NULL auto_increment,
  `LABEL` varchar(40) NOT NULL,
  `METHOD_NAME` varchar(100) NOT NULL,
  `LIST_ORDER` int NOT NULL,
  `LIST` int NOT NULL,
  `selected` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `form_hash_request` (
  `FORM_HASH_ID` int NOT NULL auto_increment,
  `REQ_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `PRI_MEM` varchar(120),
  `LINK` varchar(120),
  `MSID` int NOT NULL,
  `MAILED_TO` varchar(120),
  PRIMARY KEY (`FORM_HASH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `stats` (
  `STAT_ID` int NOT NULL auto_increment,
  `FISCAL_YEAR` int NOT NULL,
  `ACTIVE_MEMBERSHIPS` int,
  `NON_RENEW` int,
  `RETURN_MEMBERS` int,
  `NEW_MEMBERS` int,
  `SECONDARY_MEMBERS` int,
  `DEPENDANTS` int,
  `NUMBER_OF_BOATS` int,
  `FAMILY` int,
  `REGULAR` int,
  `SOCIAL` int,
  `LAKE_ASSOCIATES` int,
  `LIFE_MEMBERS` int,
  `RACE_FELLOWS` int,
  `STUDENT` int,
  `DEPOSITS` decimal(13,2),
  `INITIATION` decimal(13,2),
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `payment` (
  `PAY_ID` int NOT NULL auto_increment,
  `INVOICE_ID` int NOT NULL,
  `CHECK_NUMBER` varchar(20),
  `PAYMENT_TYPE` varchar(4) NOT NULL,
  `PAYMENT_DATE` date NOT NULL,
  `AMOUNT` decimal(10,2) NOT NULL,
  `DEPOSIT_ID` int NOT NULL,
  PRIMARY KEY (`PAY_ID`),
  FOREIGN KEY (`INVOICE_ID`) REFERENCES `invoice`(`ID`),
  FOREIGN KEY (`DEPOSIT_ID`) REFERENCES `deposit`(`DEPOSIT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `id_change` (
  `CHANGE_ID` int NOT NULL auto_increment,
  `ID_YEAR` int NOT NULL,
  `CHANGED` tinyint(1),
  PRIMARY KEY (`CHANGE_ID`),
  UNIQUE KEY (`ID_YEAR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `db_boat_list_radio_selection` (
  `ID` int NOT NULL auto_increment,
  `LABEL` varchar(40) NOT NULL,
  `METHOD_NAME` varchar(2000),
  `LIST_ORDER` int,
  `LIST` int DEFAULT 1,
  `SELECTED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `email` (
  `EMAIL_ID` int NOT NULL auto_increment,
  `P_ID` int NOT NULL,
  `PRIMARY_USE` tinyint(1),
  `EMAIL` varchar(60),
  `EMAIL_LISTED` tinyint(1),
  PRIMARY KEY (`EMAIL_ID`),
  FOREIGN KEY (`P_ID`) REFERENCES `person`(`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `db_roster_settings` (
  `ID` int NOT NULL auto_increment,
  `name` varchar(30) NOT NULL,
  `pojo_name` varchar(30) NOT NULL,
  `data_type` varchar(30) NOT NULL,
  `field_name` varchar(30) NOT NULL,
  `getter` varchar(50) NOT NULL,
  `searchable` tinyint(1) DEFAULT 0,
  `exportable` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `form_msid_hash` (
  `HASH_ID` int NOT NULL auto_increment,
  `HASH` bigint NOT NULL,
  `MS_ID` int NOT NULL,
  PRIMARY KEY (`HASH_ID`),
  UNIQUE KEY (`HASH`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `db_invoice` (
  `id` int NOT NULL,
  `FISCAL_YEAR` int NOT NULL,
  `FIELD_NAME` varchar(30) NOT NULL,
  `widget_type` varchar(30) NOT NULL,
  `width` double,
  `Invoice_order` int NOT NULL,
  `multiplied` tinyint(1) NOT NULL,
  `price_editable` tinyint(1) NOT NULL,
  `is_credit` tinyint(1) NOT NULL,
  `max_qty` int NOT NULL,
  `auto_populate` tinyint(1) NOT NULL DEFAULT 0,
  `is_itemized` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `membership_id` (
  `MID` int NOT NULL auto_increment,
  `FISCAL_YEAR` int NOT NULL,
  `MS_ID` int NOT NULL,
  `MEMBERSHIP_ID` int,
  `RENEW` tinyint(1),
  `MEM_TYPE` varchar(4),
  `SELECTED` tinyint(1),
  `LATE_RENEW` tinyint(1),
  PRIMARY KEY (`MID`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `boat_owner` (
  `MS_ID` int NOT NULL,
  `BOAT_ID` int NOT NULL,
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`),
  FOREIGN KEY (`BOAT_ID`) REFERENCES `boat`(`BOAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `users` (
  `id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `boat` (
  `BOAT_ID` int NOT NULL auto_increment,
  `MANUFACTURER` varchar(40),
  `MANUFACTURE_YEAR` varchar(5),
  `REGISTRATION_NUM` varchar(30),
  `MODEL` varchar(40),
  `BOAT_NAME` varchar(30),
  `SAIL_NUMBER` varchar(20),
  `HAS_TRAILER` tinyint(1),
  `LENGTH` varchar(20),
  `WEIGHT` varchar(20),
  `KEEL` varchar(4),
  `PHRF` int,
  `DRAFT` varchar(20),
  `BEAM` varchar(20),
  `LWL` varchar(20),
  `AUX` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`BOAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `db_updates` (
  `ID` int NOT NULL,
  `SQL_CREATION_DATE` datetime,
  `IS_CLOSED` tinyint(1) NOT NULL,
  `DB_SIZE` double,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `form_request` (
  `FORM_ID` int NOT NULL auto_increment,
  `REQ_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `PRI_MEM` varchar(50),
  `MSID` int NOT NULL,
  `SUCCESS` tinyint(1),
  PRIMARY KEY (`FORM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `invoice_item` (
  `ID` int NOT NULL auto_increment,
  `INVOICE_ID` int NOT NULL,
  `MS_ID` int NOT NULL,
  `FISCAL_YEAR` int,
  `FIELD_NAME` varchar(50) NOT NULL,
  `IS_CREDIT` tinyint(1) NOT NULL,
  `VALUE` decimal(10,2),
  `QTY` int,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`INVOICE_ID`) REFERENCES `invoice`(`ID`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `slip` (
  `SLIP_ID` int NOT NULL auto_increment,
  `MS_ID` int,
  `SLIP_NUM` varchar(4) NOT NULL,
  `SUBLEASED_TO` int,
  `ALT_TEXT` varchar(20),
  PRIMARY KEY (`SLIP_ID`),
  UNIQUE KEY (`MS_ID`),
  UNIQUE KEY (`MS_ID`),
  UNIQUE KEY (`SLIP_NUM`),
  UNIQUE KEY (`SUBLEASED_TO`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `phone` (
  `PHONE_ID` int NOT NULL auto_increment,
  `P_ID` int NOT NULL,
  `PHONE` varchar(30),
  `PHONE_TYPE` varchar(30),
  `PHONE_LISTED` tinyint(1),
  PRIMARY KEY (`PHONE_ID`),
  FOREIGN KEY (`P_ID`) REFERENCES `person`(`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `api_key` (
  `API_ID` int NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `APIKEY` varchar(50) NOT NULL,
  `ts` timestamp DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY (`API_ID`),
  UNIQUE KEY (`NAME`),
  UNIQUE KEY (`APIKEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `person` (
  `P_ID` int NOT NULL auto_increment,
  `MS_ID` int,
  `MEMBER_TYPE` int,
  `F_NAME` varchar(20),
  `L_NAME` varchar(20),
  `BIRTHDAY` date,
  `OCCUPATION` varchar(50),
  `BUSINESS` varchar(50),
  `IS_ACTIVE` tinyint(1),
  `PICTURE` blob,
  `NICK_NAME` varchar(30),
  `OLD_MSID` int,
  PRIMARY KEY (`P_ID`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `awards` (
  `AWARD_ID` int NOT NULL auto_increment,
  `P_ID` int NOT NULL,
  `AWARD_YEAR` varchar(10) NOT NULL,
  `AWARD_TYPE` varchar(10) NOT NULL,
  PRIMARY KEY (`AWARD_ID`),
  FOREIGN KEY (`P_ID`) REFERENCES `person`(`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `boat_photos` (
  `ID` int NOT NULL auto_increment,
  `BOAT_ID` int NOT NULL,
  `upload_date` datetime,
  `filename` varchar(200) NOT NULL,
  `file_number` int NOT NULL,
  `default_image` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`BOAT_ID`) REFERENCES `boat`(`BOAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `deposit` (
  `DEPOSIT_ID` int NOT NULL auto_increment,
  `DEPOSIT_DATE` date NOT NULL,
  `FISCAL_YEAR` int NOT NULL,
  `BATCH` int NOT NULL,
  PRIMARY KEY (`DEPOSIT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `db_boat_list_settings` (
  `ID` int NOT NULL auto_increment,
  `name` varchar(30) NOT NULL,
  `pojo_field_name` varchar(30) NOT NULL,
  `control_type` varchar(30) NOT NULL,
  `field_name` varchar(30) NOT NULL,
  `getter` varchar(50) NOT NULL,
  `searchable` tinyint(1) DEFAULT 0,
  `exportable` tinyint(1) DEFAULT 0,
  `visible` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `invoice` (
  `ID` int NOT NULL auto_increment,
  `MS_ID` int NOT NULL,
  `FISCAL_YEAR` int,
  `PAID` decimal(10,2),
  `TOTAL` decimal(10,2),
  `CREDIT` decimal(10,2),
  `BALANCE` decimal(10,2),
  `BATCH` int,
  `COMMITTED` tinyint(1),
  `CLOSED` tinyint(1),
  `SUPPLEMENTAL` tinyint(1),
  `MAX_CREDIT` decimal(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`MS_ID`) REFERENCES `membership`(`MS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user` (
  `id` int NOT NULL,
  `active` bit(1) NOT NULL,
  `password` varchar(255),
  `roles` varchar(255),
  `username` varchar(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `winter_storage` (
  `WS_ID` int NOT NULL auto_increment,
  `BOAT_ID` int NOT NULL,
  `FISCAL_YEAR` int NOT NULL,
  PRIMARY KEY (`WS_ID`),
  FOREIGN KEY (`BOAT_ID`) REFERENCES `boat`(`BOAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

