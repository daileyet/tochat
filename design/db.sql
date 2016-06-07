delimiter $$

CREATE DATABASE `tochat` /*!40100 DEFAULT CHARACTER SET utf8 */$$

delimiter $$

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(500) DEFAULT NULL,
  `alias` varchar(500) DEFAULT NULL,
  `lastip` varchar(200) DEFAULT NULL,
  `createon` varchar(50) DEFAULT NULL,
  `lock` varchar(3) DEFAULT 'N',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE `channel` (
  `id` varchar(50) NOT NULL,
  `name` varchar(500) NOT NULL,
  `owner` int(11) DEFAULT NULL,
  `active` varchar(3) DEFAULT 'Y',
  `createon` varchar(50) DEFAULT NULL,
  `period` float DEFAULT NULL,
  `desc` varchar(4000) DEFAULT NULL,
  `starton` varchar(50) DEFAULT NULL,
  `securitycode` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE `message` (
  `id` varchar(50) NOT NULL,
  `type` varchar(10) NOT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `channel` varchar(50) DEFAULT NULL,
  `from` int(11) DEFAULT NULL,
  `to` int(11) DEFAULT NULL,
  `timestamp` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

