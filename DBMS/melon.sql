-- MariaDB dump 10.17  Distrib 10.5.6-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: melon
-- ------------------------------------------------------
-- Server version	10.5.6-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `melon`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `melon` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `melon`;

--
-- Table structure for table `include`
--

DROP TABLE IF EXISTS `include`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `include` (
  `Snum` int(11) NOT NULL,
  `Pnum` int(11) NOT NULL,
  PRIMARY KEY (`Snum`,`Pnum`),
  KEY `Pnum` (`Pnum`),
  CONSTRAINT `include_ibfk_1` FOREIGN KEY (`Snum`) REFERENCES `song` (`Song_num`),
  CONSTRAINT `include_ibfk_2` FOREIGN KEY (`Pnum`) REFERENCES `playlist` (`Pl_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `include`
--

LOCK TABLES `include` WRITE;
/*!40000 ALTER TABLE `include` DISABLE KEYS */;
INSERT INTO `include` VALUES (123456,4),(123460,3),(123462,3),(123467,3),(123467,4),(123467,5),(123469,4),(123470,4),(123472,3),(123473,4);
/*!40000 ALTER TABLE `include` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `Name` varchar(15) NOT NULL,
  `Ssn` int(11) NOT NULL,
  `Age` int(11) NOT NULL,
  `Sex` varchar(5) NOT NULL,
  `Address` varchar(50) NOT NULL,
  `Phonenum` varchar(20) NOT NULL,
  PRIMARY KEY (`Ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES ('Junii',64878,21,'F','Seoul','010-5244-8453'),('Smith',254878,25,'M','Seoul','010-8745-5215'),('V',548651,26,'M','Seoul','010-5244-4534'),('Any',548712,30,'F','Seoul','010-1535-5265'),('Jin',784515,29,'M','Seoul','010-1287-1453'),('Jack',784516,28,'M','Tokyo','010-1421-1548'),('Joon',789551,26,'F','Seoul','010-5184-9654');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist` (
  `Title` varchar(50) NOT NULL,
  `Pl_num` int(11) NOT NULL,
  `Creater_id` varchar(15) NOT NULL,
  PRIMARY KEY (`Pl_num`),
  KEY `Creater_id` (`Creater_id`),
  CONSTRAINT `playlist_ibfk_1` FOREIGN KEY (`Creater_id`) REFERENCES `user` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` VALUES ('Driving',3,'ddfe5478'),('HeHe',4,'ddfe5478'),('MYMY',5,'nfe988');
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song` (
  `Enrolldate` date NOT NULL,
  `Length` int(11) NOT NULL,
  `Genre` varchar(20) DEFAULT NULL,
  `Title` varchar(50) NOT NULL,
  `Playnum` int(11) DEFAULT NULL,
  `Song_num` int(11) NOT NULL,
  `Singer` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Song_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song`
--

LOCK TABLES `song` WRITE;
/*!40000 ALTER TABLE `song` DISABLE KEYS */;
INSERT INTO `song` VALUES ('2020-11-26',158,'pop','dynamite',15678254,123456,'BTS'),('2019-05-25',170,'Ballad','How you like that',5252,123457,'BlackPink'),('2015-06-13',210,'Ballad','Snowman',56783,123460,'Jeongseunghwan'),('2015-07-09',300,'Ballad','Aloha',5888,123462,'Cool'),('2006-11-17',268,'k-pop','Babo',23549,123463,'BigBang'),('2009-06-13',248,'hiphop','Ballerino',78678,123464,'Leessang'),('2008-04-09',288,'k-pop','HeartBreaker',55678,123466,'G-dragon'),('2017-10-15',280,'k-pop','Fire',53786876,123467,'BTS'),('2017-02-07',254,'k-pop','Dance the night away',321345,123468,'Twice'),('2018-08-07',288,'k-pop','Kill this love',454654,123469,'BlackPink'),('2008-09-01',300,'Ballad','Yeosu Bambada',546515,123470,'Busker Busker'),('2018-05-09',315,'pop','2002',1,123471,'Anne'),('2019-08-18',350,'Ballad','Babo',1,123472,'Nilo'),('2017-11-19',300,'k-pop','DNA',1,123473,'BTS'),('2008-07-09',350,'k-pop','8282',1,123474,'Davichi');
/*!40000 ALTER TABLE `song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `Id` varchar(15) NOT NULL,
  `Passwd` varchar(15) NOT NULL,
  `Bdate` date DEFAULT NULL,
  `Sex` char(1) NOT NULL,
  `Name` varchar(15) NOT NULL,
  `Phonenum` varchar(20) NOT NULL,
  `Leftdays` int(11) NOT NULL,
  `Mgr_ssn` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Mgr_ssn` (`Mgr_ssn`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`Mgr_ssn`) REFERENCES `manager` (`Ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('ddfe5478','jf54891','2000-06-13','F','Juni','010-8787-5135',110,64878),('eijfe787','fjeij545','1987-11-27','M','Brian','010-8787-8888',28,64878),('iejw64','eijf789','1995-12-02','M','John','010-8244-4752',25,64878),('nfe988','eifje588','1988-08-04','F','Ken','010-8244-1548',84,548651);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-06 14:11:13
