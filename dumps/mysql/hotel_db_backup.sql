-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: localhost    Database: hotel_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `bill_id` int NOT NULL AUTO_INCREMENT,
  `reservation_id` int NOT NULL,
  `opened_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `closed_at` timestamp NULL DEFAULT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `is_paid` tinyint(1) NOT NULL DEFAULT '0',
  `bill_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `discount_amount` decimal(38,2) NOT NULL,
  `room_charge` decimal(38,2) NOT NULL,
  `tax_amount` decimal(38,2) NOT NULL,
  PRIMARY KEY (`bill_id`),
  UNIQUE KEY `reservation_id` (`reservation_id`),
  KEY `idx_bill_reservation` (`reservation_id`),
  KEY `idx_bill_opened` (`opened_at`),
  KEY `idx_bill_status` (`closed_at`),
  CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (1,1,'2026-05-05 10:30:00','2026-05-07 11:00:00',160.00,0,NULL,0.00,0.00,0.00),(2,2,'2026-05-06 11:00:00','2026-05-09 10:00:00',360.00,0,NULL,0.00,0.00,0.00),(3,3,'2026-05-07 12:00:00','2026-05-10 10:00:00',330.00,0,NULL,0.00,0.00,0.00),(4,4,'2026-05-08 13:00:00','2026-05-12 10:00:00',800.00,0,NULL,0.00,0.00,0.00),(5,5,'2026-05-09 14:00:00','2026-05-13 10:00:00',1120.00,0,NULL,0.00,0.00,0.00),(6,6,'2026-05-10 15:00:00',NULL,400.00,0,NULL,0.00,0.00,0.00),(7,7,'2026-05-11 16:00:00','2026-05-14 10:00:00',360.00,0,NULL,0.00,0.00,0.00),(8,8,'2026-05-12 17:00:00','2026-05-16 10:00:00',440.00,0,NULL,0.00,0.00,0.00),(9,9,'2026-05-13 18:00:00','2026-05-17 10:00:00',800.00,0,NULL,0.00,0.00,0.00),(10,10,'2026-05-14 19:00:00','2026-05-18 10:00:00',1120.00,0,NULL,0.00,0.00,0.00),(11,11,'2026-05-15 20:00:00','2026-05-19 10:00:00',320.00,0,NULL,0.00,0.00,0.00),(12,12,'2026-05-16 21:00:00','2026-05-19 10:00:00',360.00,0,NULL,0.00,0.00,0.00),(13,13,'2026-05-17 22:00:00','2026-05-20 10:00:00',330.00,0,NULL,0.00,0.00,0.00),(14,14,'2026-05-18 08:00:00','2026-05-22 10:00:00',800.00,0,NULL,0.00,0.00,0.00),(15,15,'2026-05-19 09:00:00','2026-05-23 10:00:00',1120.00,0,NULL,0.00,0.00,0.00),(16,16,'2026-05-20 10:00:00',NULL,320.00,0,NULL,0.00,0.00,0.00),(17,17,'2026-05-21 11:00:00','2026-05-24 10:00:00',360.00,0,NULL,0.00,0.00,0.00),(18,18,'2026-05-22 12:00:00','2026-05-25 10:00:00',330.00,0,NULL,0.00,0.00,0.00),(19,19,'2026-05-23 13:00:00','2026-05-27 10:00:00',800.00,0,NULL,0.00,0.00,0.00),(20,20,'2026-05-24 14:00:00','2026-05-28 10:00:00',1120.00,0,NULL,0.00,0.00,0.00),(21,21,'2026-05-25 15:00:00','2026-05-29 10:00:00',320.00,0,NULL,0.00,0.00,0.00),(22,22,'2026-05-26 16:00:00','2026-05-29 10:00:00',360.00,0,NULL,0.00,0.00,0.00),(23,23,'2026-05-27 17:00:00','2026-05-30 10:00:00',330.00,0,NULL,0.00,0.00,0.00),(24,24,'2026-05-28 18:00:00','2026-06-01 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(25,25,'2026-05-29 19:00:00','2026-06-02 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(26,26,'2026-05-30 20:00:00','2026-06-03 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(27,27,'2026-05-31 21:00:00','2026-06-03 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(28,28,'2026-06-01 22:00:00','2026-06-04 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(29,29,'2026-06-02 08:00:00','2026-06-06 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(30,30,'2026-06-03 09:00:00','2026-06-07 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(31,31,'2026-06-04 10:00:00','2026-06-08 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(32,32,'2026-06-05 11:00:00','2026-06-08 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(33,33,'2026-06-06 12:00:00','2026-06-09 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(34,34,'2026-06-07 13:00:00','2026-06-11 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(35,35,'2026-06-08 14:00:00','2026-06-12 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(36,36,'2026-06-09 15:00:00','2026-06-13 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(37,37,'2026-06-10 16:00:00','2026-06-13 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(38,38,'2026-06-11 17:00:00','2026-06-14 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(39,39,'2026-06-12 18:00:00','2026-06-16 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(40,40,'2026-06-13 19:00:00','2026-06-17 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(41,41,'2026-06-14 20:00:00','2026-06-18 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(42,42,'2026-06-15 21:00:00','2026-06-18 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(43,43,'2026-06-16 22:00:00','2026-06-19 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(44,44,'2026-06-17 08:00:00','2026-06-21 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(45,45,'2026-06-18 09:00:00','2026-06-22 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(46,46,'2026-06-19 10:00:00','2026-06-23 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(47,47,'2026-06-20 11:00:00','2026-06-23 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(48,48,'2026-06-21 12:00:00','2026-06-24 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(49,49,'2026-06-22 13:00:00','2026-06-26 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(50,50,'2026-06-23 14:00:00','2026-06-27 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(51,51,'2026-06-24 15:00:00','2026-06-28 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(52,52,'2026-06-25 16:00:00','2026-06-28 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(53,53,'2026-06-26 17:00:00','2026-06-29 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(54,54,'2026-06-27 18:00:00','2026-07-01 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(55,55,'2026-06-28 19:00:00','2026-07-02 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(56,56,'2026-07-01 20:00:00','2026-07-05 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(57,57,'2026-07-02 21:00:00','2026-07-05 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(58,58,'2026-07-03 22:00:00','2026-07-06 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(59,59,'2026-07-04 08:00:00','2026-07-08 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(60,60,'2026-07-05 09:00:00','2026-07-09 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(61,61,'2026-07-06 10:00:00','2026-07-10 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(62,62,'2026-07-07 11:00:00','2026-07-10 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(63,63,'2026-07-08 12:00:00','2026-07-11 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(64,64,'2026-07-09 13:00:00','2026-07-13 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(65,65,'2026-07-10 14:00:00','2026-07-14 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(66,66,'2026-07-11 15:00:00','2026-07-15 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(67,67,'2026-07-12 16:00:00','2026-07-15 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(68,68,'2026-07-13 17:00:00','2026-07-16 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(69,69,'2026-07-14 18:00:00','2026-07-18 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(70,70,'2026-07-15 19:00:00','2026-07-19 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(71,71,'2026-07-16 20:00:00','2026-07-20 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(72,72,'2026-07-17 21:00:00','2026-07-20 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(73,73,'2026-07-18 22:00:00','2026-07-21 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(74,74,'2026-07-19 08:00:00','2026-07-23 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(75,75,'2026-07-20 09:00:00','2026-07-24 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(76,76,'2026-07-21 10:00:00','2026-07-25 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(77,77,'2026-07-22 11:00:00','2026-07-25 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(78,78,'2026-07-23 12:00:00','2026-07-26 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(79,79,'2026-07-24 13:00:00','2026-07-28 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(80,80,'2026-07-25 14:00:00','2026-07-29 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(81,81,'2026-08-01 15:00:00','2026-08-05 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(82,82,'2026-08-02 16:00:00','2026-08-05 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(83,83,'2026-08-03 17:00:00','2026-08-06 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(84,84,'2026-08-04 18:00:00','2026-08-08 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(85,85,'2026-08-05 19:00:00','2026-08-09 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(86,86,'2026-08-06 20:00:00','2026-08-10 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(87,87,'2026-08-07 21:00:00','2026-08-10 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(88,88,'2026-08-08 22:00:00','2026-08-11 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(89,89,'2026-08-09 08:00:00','2026-08-13 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(90,90,'2026-08-10 09:00:00','2026-08-14 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(91,91,'2026-08-11 10:00:00','2026-08-15 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(92,92,'2026-08-12 11:00:00','2026-08-15 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(93,93,'2026-08-13 12:00:00','2026-08-16 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(94,94,'2026-08-14 13:00:00','2026-08-18 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(95,95,'2026-08-15 14:00:00','2026-08-19 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(96,96,'2026-08-16 15:00:00','2026-08-20 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(97,97,'2026-08-17 16:00:00','2026-08-20 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(98,98,'2026-08-18 17:00:00','2026-08-21 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(99,99,'2026-08-19 18:00:00','2026-08-23 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(100,100,'2026-08-20 19:00:00','2026-08-24 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(101,101,'2026-08-21 20:00:00','2026-08-25 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(102,102,'2026-08-22 21:00:00','2026-08-25 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(103,103,'2026-08-23 22:00:00','2026-08-26 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(104,104,'2026-08-24 08:00:00','2026-08-28 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(105,105,'2026-08-25 09:00:00','2026-08-29 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(106,106,'2026-09-01 10:00:00','2026-09-05 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(107,107,'2026-09-02 11:00:00','2026-09-05 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(108,108,'2026-09-03 12:00:00','2026-09-06 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(109,109,'2026-09-04 13:00:00','2026-09-08 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(110,110,'2026-09-05 14:00:00','2026-09-09 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(111,111,'2026-09-06 15:00:00','2026-09-10 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(112,112,'2026-09-07 16:00:00','2026-09-10 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(113,113,'2026-09-08 17:00:00','2026-09-11 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(114,114,'2026-09-09 18:00:00','2026-09-13 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(115,115,'2026-09-10 19:00:00','2026-09-14 10:00:00',1680.00,0,NULL,0.00,0.00,0.00),(116,116,'2026-09-11 20:00:00','2026-09-15 10:00:00',480.00,0,NULL,0.00,0.00,0.00),(117,117,'2026-09-12 21:00:00','2026-09-15 10:00:00',540.00,0,NULL,0.00,0.00,0.00),(118,118,'2026-09-13 22:00:00','2026-09-16 10:00:00',510.00,0,NULL,0.00,0.00,0.00),(119,119,'2026-09-14 08:00:00','2026-09-18 10:00:00',1200.00,0,NULL,0.00,0.00,0.00),(120,120,'2026-09-15 09:00:00','2026-09-19 10:00:00',1680.00,0,NULL,0.00,0.00,0.00);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_item`
--

DROP TABLE IF EXISTS `bill_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_item` (
  `bill_item_id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` int NOT NULL,
  `item_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `unit_price` decimal(38,2) NOT NULL,
  `line_total` decimal(38,2) NOT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `posted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`bill_item_id`),
  KEY `idx_bill_item_bill` (`bill_id`),
  KEY `idx_bill_item_type` (`item_type`),
  KEY `idx_bill_item_posted` (`posted_at`),
  CONSTRAINT `bill_item_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_item`
--

LOCK TABLES `bill_item` WRITE;
/*!40000 ALTER TABLE `bill_item` DISABLE KEYS */;
INSERT INTO `bill_item` VALUES (1,1,'ROOM_CHARGE','Single Room - 2 nights',2,80.00,160.00,NULL,'2026-05-07 11:00:00'),(2,2,'ROOM_CHARGE','Double Room - 3 nights',3,120.00,360.00,NULL,'2026-05-09 10:00:00'),(3,3,'ROOM_CHARGE','Twin Room - 3 nights',3,110.00,330.00,NULL,'2026-05-10 10:00:00'),(4,4,'ROOM_CHARGE','Suite - 4 nights',4,200.00,800.00,NULL,'2026-05-12 10:00:00'),(5,5,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,280.00,1120.00,NULL,'2026-05-13 10:00:00'),(6,6,'ROOM_CHARGE','Single Room - 5 nights',5,80.00,400.00,NULL,'2026-05-15 10:00:00'),(7,7,'ROOM_CHARGE','Double Room - 3 nights',3,120.00,360.00,NULL,'2026-05-14 10:00:00'),(8,8,'ROOM_CHARGE','Twin Room - 4 nights',4,110.00,440.00,NULL,'2026-05-16 10:00:00'),(9,9,'ROOM_CHARGE','Suite - 4 nights',4,200.00,800.00,NULL,'2026-05-17 10:00:00'),(10,10,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,280.00,1120.00,NULL,'2026-05-18 10:00:00'),(11,11,'EXTRA_SERVICE','Room Service Breakfast',4,25.00,100.00,NULL,'2026-05-15 12:00:00'),(12,12,'EXTRA_SERVICE','Spa Treatment - Massage',1,100.00,100.00,NULL,'2026-05-17 18:00:00'),(13,13,'EXTRA_SERVICE','Airport Transfer',2,50.00,100.00,NULL,'2026-05-18 08:00:00'),(14,14,'ROOM_CHARGE','Suite - 4 nights',4,200.00,800.00,NULL,'2026-05-22 10:00:00'),(15,15,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,280.00,1120.00,NULL,'2026-05-23 10:00:00'),(16,16,'EXTRA_SERVICE','Gym Access',5,15.00,75.00,NULL,'2026-05-24 06:00:00'),(17,17,'ROOM_CHARGE','Double Room - 3 nights',3,120.00,360.00,NULL,'2026-05-24 10:00:00'),(18,18,'ROOM_CHARGE','Twin Room - 3 nights',3,110.00,330.00,NULL,'2026-05-25 10:00:00'),(19,19,'ROOM_CHARGE','Suite - 4 nights',4,200.00,800.00,NULL,'2026-05-27 10:00:00'),(20,20,'EXTRA_SERVICE','Wine Tasting',2,75.00,150.00,NULL,'2026-05-27 19:00:00'),(21,21,'EXTRA_SERVICE','City Tour',1,120.00,120.00,NULL,'2026-05-26 14:00:00'),(22,22,'EXTRA_SERVICE','Early Breakfast',3,20.00,60.00,NULL,'2026-05-28 07:00:00'),(23,23,'ROOM_CHARGE','Twin Room - 3 nights',3,110.00,330.00,NULL,'2026-05-30 10:00:00'),(24,24,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-06-01 10:00:00'),(25,25,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-06-02 10:00:00'),(26,26,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-06-03 10:00:00'),(27,27,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-06-03 10:00:00'),(28,28,'ROOM_CHARGE','Twin Room - 3 nights',3,170.00,510.00,NULL,'2026-06-04 10:00:00'),(29,29,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-06-06 10:00:00'),(30,30,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-06-07 10:00:00'),(31,31,'EXTRA_SERVICE','Late Checkout',1,30.00,30.00,NULL,'2026-06-08 12:00:00'),(32,32,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-06-08 10:00:00'),(33,33,'ROOM_CHARGE','Twin Room - 3 nights',3,170.00,510.00,NULL,'2026-06-09 10:00:00'),(34,34,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-06-11 10:00:00'),(35,35,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-06-12 10:00:00'),(36,36,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-06-13 10:00:00'),(37,37,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-06-13 10:00:00'),(38,38,'EXTRA_SERVICE','Room Service Lunch',1,35.00,35.00,NULL,'2026-06-13 12:00:00'),(39,39,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-06-16 10:00:00'),(40,40,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-06-17 10:00:00'),(41,41,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-06-18 10:00:00'),(42,42,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-06-18 10:00:00'),(43,43,'EXTRA_SERVICE','Spa Treatment - Facial',1,80.00,80.00,NULL,'2026-06-18 15:00:00'),(44,44,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-06-21 10:00:00'),(45,45,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-06-22 10:00:00'),(46,46,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-06-23 10:00:00'),(47,47,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-06-23 10:00:00'),(48,48,'EXTRA_SERVICE','Room Service Dinner',2,45.00,90.00,NULL,'2026-06-23 18:00:00'),(49,49,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-06-26 10:00:00'),(50,50,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-06-27 10:00:00'),(51,51,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-06-28 10:00:00'),(52,52,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-06-28 10:00:00'),(53,53,'EXTRA_SERVICE','Mini Bar Restocking',2,10.00,20.00,NULL,'2026-06-28 20:00:00'),(54,54,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-07-01 10:00:00'),(55,55,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-07-02 10:00:00'),(56,56,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-07-05 10:00:00'),(57,57,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-07-05 10:00:00'),(58,58,'EXTRA_SERVICE','Airport Transfer',1,50.00,50.00,NULL,'2026-07-05 12:00:00'),(59,59,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-07-08 10:00:00'),(60,60,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-07-09 10:00:00'),(61,61,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-07-10 10:00:00'),(62,62,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-07-10 10:00:00'),(63,63,'EXTRA_SERVICE','Gym Access',3,15.00,45.00,NULL,'2026-07-10 06:00:00'),(64,64,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-07-13 10:00:00'),(65,65,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-07-14 10:00:00'),(66,66,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-07-15 10:00:00'),(67,67,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-07-15 10:00:00'),(68,68,'EXTRA_SERVICE','Late Checkout',2,30.00,60.00,NULL,'2026-07-15 12:00:00'),(69,69,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-07-18 10:00:00'),(70,70,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-07-19 10:00:00'),(71,71,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-07-20 10:00:00'),(72,72,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-07-20 10:00:00'),(73,73,'EXTRA_SERVICE','Early Breakfast',2,20.00,40.00,NULL,'2026-07-20 07:00:00'),(74,74,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-07-23 10:00:00'),(75,75,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-07-24 10:00:00'),(76,76,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-07-25 10:00:00'),(77,77,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-07-25 10:00:00'),(78,78,'EXTRA_SERVICE','Wine Tasting',1,75.00,75.00,NULL,'2026-07-25 19:00:00'),(79,79,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-07-28 10:00:00'),(80,80,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-07-29 10:00:00'),(81,81,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-08-05 10:00:00'),(82,82,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-08-05 10:00:00'),(83,83,'EXTRA_SERVICE','City Tour',1,120.00,120.00,NULL,'2026-08-05 10:00:00'),(84,84,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-08-08 10:00:00'),(85,85,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-08-09 10:00:00'),(86,86,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-08-10 10:00:00'),(87,87,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-08-10 10:00:00'),(88,88,'EXTRA_SERVICE','Spa Treatment - Massage',1,100.00,100.00,NULL,'2026-08-10 14:00:00'),(89,89,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-08-13 10:00:00'),(90,90,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-08-14 10:00:00'),(91,91,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-08-15 10:00:00'),(92,92,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-08-15 10:00:00'),(93,93,'EXTRA_SERVICE','Room Service Breakfast',2,25.00,50.00,NULL,'2026-08-15 07:00:00'),(94,94,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-08-18 10:00:00'),(95,95,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-08-19 10:00:00'),(96,96,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-08-20 10:00:00'),(97,97,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-08-20 10:00:00'),(98,98,'EXTRA_SERVICE','Gym Access',4,15.00,60.00,NULL,'2026-08-20 06:00:00'),(99,99,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-08-23 10:00:00'),(100,100,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-08-24 10:00:00'),(101,101,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-08-25 10:00:00'),(102,102,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-08-25 10:00:00'),(103,103,'EXTRA_SERVICE','Late Checkout',1,30.00,30.00,NULL,'2026-08-25 12:00:00'),(104,104,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-08-28 10:00:00'),(105,105,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-08-29 10:00:00'),(106,106,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-09-05 10:00:00'),(107,107,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-09-05 10:00:00'),(108,108,'EXTRA_SERVICE','Early Breakfast',3,20.00,60.00,NULL,'2026-09-05 07:00:00'),(109,109,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-09-08 10:00:00'),(110,110,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-09-09 10:00:00'),(111,111,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-09-10 10:00:00'),(112,112,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-09-10 10:00:00'),(113,113,'EXTRA_SERVICE','Airport Transfer',2,50.00,100.00,NULL,'2026-09-11 12:00:00'),(114,114,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-09-13 10:00:00'),(115,115,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-09-14 10:00:00'),(116,116,'ROOM_CHARGE','Single Room - 4 nights',4,120.00,480.00,NULL,'2026-09-15 10:00:00'),(117,117,'ROOM_CHARGE','Double Room - 3 nights',3,180.00,540.00,NULL,'2026-09-15 10:00:00'),(118,118,'EXTRA_SERVICE','Wine Tasting',1,75.00,75.00,NULL,'2026-09-15 19:00:00'),(119,119,'ROOM_CHARGE','Suite - 4 nights',4,300.00,1200.00,NULL,'2026-09-18 10:00:00'),(120,120,'ROOM_CHARGE','Deluxe Suite - 4 nights',4,420.00,1680.00,NULL,'2026-09-19 10:00:00');
/*!40000 ALTER TABLE `bill_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cleaner`
--

DROP TABLE IF EXISTS `cleaner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cleaner` (
  `cleaner_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`cleaner_id`),
  KEY `idx_cleaner_active` (`active`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cleaner`
--

LOCK TABLES `cleaner` WRITE;
/*!40000 ALTER TABLE `cleaner` DISABLE KEYS */;
INSERT INTO `cleaner` VALUES (1,'Maria','Garcia','555-0101',1),(2,'John','Smith','555-0102',1),(3,'Lisa','Chen','555-0103',1),(4,'Ahmed','Hassan','555-0104',1),(5,'Elena','Rodriguez','555-0105',1),(6,'Michael','Johnson','555-0106',1),(7,'Sofia','Petrov','555-0107',1),(8,'Carlos','Martinez','555-0108',1),(9,'Anna','Kowalski','555-0109',1),(10,'David','Kim','555-0110',1),(11,'Roberto','Sanchez','555-0111',1),(12,'Patricia','Kim','555-0112',1),(13,'Marcus','Lee','555-0113',1),(14,'Angela','Brown','555-0114',1),(15,'Steven','Davis','555-0115',1),(16,'Elizabeth','Miller','555-0116',1),(17,'Thomas','Wilson','555-0117',1),(18,'Donna','Moore','555-0118',1),(19,'Christopher','Taylor','555-0119',1),(20,'Carol','Anderson','555-0120',1),(21,'Donald','Thomas','555-0121',1),(22,'Susan','Jackson','555-0122',1),(23,'Matthew','White','555-0123',1),(24,'Dorothy','Harris','555-0124',1),(25,'Mark','Martin','555-0125',1),(26,'Lisa','Thompson','555-0126',1),(27,'Donald','Garcia','555-0127',1),(28,'Barbara','Martinez','555-0128',1),(29,'Steven','Robinson','555-0129',1),(30,'Mary','Clark','555-0130',1),(31,'Paul','Rodriguez','555-0131',1),(32,'Patricia','Lewis','555-0132',1),(33,'Andrew','Lee','555-0133',1),(34,'Linda','Walker','555-0134',1),(35,'Joshua','Hall','555-0135',1),(36,'Barbara','Allen','555-0136',1),(37,'Kenneth','Young','555-0137',1),(38,'Mary','King','555-0138',1),(39,'Kevin','Wright','555-0139',1),(40,'Susan','Lopez','555-0140',1),(41,'Brian','Hill','555-0141',1),(42,'Karen','Scott','555-0142',1),(43,'Edward','Green','555-0143',1),(44,'Donna','Adams','555-0144',1),(45,'Ronald','Nelson','555-0145',1),(46,'Carol','Carter','555-0146',1),(47,'Timothy','Mitchell','555-0147',1),(48,'Sandra','Perez','555-0148',1),(49,'Jason','Roberts','555-0149',1),(50,'Mary','Phillips','555-0150',1),(51,'Jeffrey','Campbell','555-0151',1),(52,'Sandra','Parker','555-0152',1),(53,'Ryan','Evans','555-0153',1),(54,'Ashley','Edwards','555-0154',1),(55,'Jacob','Collins','555-0155',1),(56,'Kimberly','Reeves','555-0156',1),(57,'Gary','Morris','555-0157',1),(58,'Donna','Murphy','555-0158',1),(59,'Nicholas','Cook','555-0159',1),(60,'Michelle','Morgan','555-0160',1),(61,'Eric','Peterson','555-0161',1),(62,'Dorothy','Gray','555-0162',1),(63,'Jonathan','Ramirez','555-0163',1),(64,'Nancy','James','555-0164',1),(65,'Stephen','Watson','555-0165',1),(66,'Lisa','Brooks','555-0166',1),(67,'Larry','Chavez','555-0167',1),(68,'Betty','Wood','555-0168',1),(69,'Justin','Mendoza','555-0169',1),(70,'Margaret','Parks','555-0170',1),(71,'Scott','Bennett','555-0171',1),(72,'Sandra','Cruz','555-0172',1),(73,'Brandon','Porter','555-0173',1),(74,'Ashley','Howell','555-0174',1),(75,'Benjamin','Curry','555-0175',1),(76,'Kimberly','Stokes','555-0176',1),(77,'Samuel','Ng','555-0177',1),(78,'Emily','Tate','555-0178',1),(79,'Frank','Lamb','555-0179',1),(80,'Isabella','Harper','555-0180',1),(81,'Gregory','Hunter','555-0181',1),(82,'Sophia','Mckinney','555-0182',1),(83,'Alexander','Lucas','555-0183',1),(84,'Charlotte','Nichols','555-0184',1),(85,'Patrick','Deleon','555-0185',1),(86,'Mia','Dorsey','555-0186',1),(87,'Jack','Osborne','555-0187',1),(88,'Amelia','Ochoa','555-0188',1),(89,'Dennis','Jacobson','555-0189',1),(90,'Harper','Mock','555-0190',1),(91,'Evelyn','Hunt','555-0191',1),(92,'Nathan','Barnes','555-0192',1),(93,'Chloe','Ross','555-0193',1),(94,'Logan','Henderson','555-0194',1),(95,'Zoey','Coleman','555-0195',1),(96,'Mason','Jenkins','555-0196',1),(97,'Lily','Perry','555-0197',1),(98,'Ethan','Powell','555-0198',1),(99,'Hannah','Long','555-0199',1),(100,'Owen','Patterson','555-0200',1);
/*!40000 ALTER TABLE `cleaner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extra_service`
--

DROP TABLE IF EXISTS `extra_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extra_service` (
  `extra_service_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unit_price` decimal(38,2) NOT NULL,
  `price_unit` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`extra_service_id`),
  UNIQUE KEY `name` (`name`),
  KEY `idx_extra_service_active` (`active`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extra_service`
--

LOCK TABLES `extra_service` WRITE;
/*!40000 ALTER TABLE `extra_service` DISABLE KEYS */;
INSERT INTO `extra_service` VALUES (1,'Room Service Breakfast',25.00,'per meal',1),(2,'Room Service Lunch',35.00,'per meal',1),(3,'Room Service Dinner',45.00,'per meal',1),(4,'Spa Treatment - Massage',100.00,'per session',1),(5,'Spa Treatment - Facial',80.00,'per session',1),(6,'Airport Transfer',50.00,'per trip',1),(7,'City Tour',120.00,'per person',1),(8,'Wine Tasting',75.00,'per person',1),(9,'Gym Access',15.00,'per day',1),(10,'Late Checkout',30.00,'per hour',1),(11,'Early Breakfast',20.00,'per person',1),(12,'Mini Bar Restocking',10.00,'per item',1),(13,'Laundry Service',20.00,'per load',1),(14,'Ironing Service',15.00,'per shirt',1),(15,'Dry Cleaning',25.00,'per item',1),(16,'Baby Sitting',20.00,'per hour',1),(17,'Pet Care',30.00,'per day',1),(18,'Wake-up Call',5.00,'per call',1),(19,'Business Center Access',10.00,'per hour',1),(20,'Parking Fee',15.00,'per day',1),(21,'Pet Accommodation',50.00,'per night',1),(22,'Bike Rental',20.00,'per day',1),(23,'Car Rental',80.00,'per day',1),(24,'Concierge Service',25.00,'per request',1),(25,'Housekeeping',50.00,'per hour',1),(26,'Room Service Setup',30.00,'per meal',1),(27,'Breakfast Buffet',35.00,'per person',1),(28,'Lunch Buffet',40.00,'per person',1),(29,'Dinner Buffet',50.00,'per person',1),(30,'Minibar Service',15.00,'per item',1),(31,'Wake Up Coffee',8.00,'per person',1),(32,'Newspaper Delivery',3.00,'per day',1),(33,'WiFi Premium',10.00,'per day',1),(34,'Phone Calls',2.00,'per minute',1),(35,'Video On Demand',8.00,'per movie',1),(36,'Fitness Training',60.00,'per session',1),(37,'Yoga Class',25.00,'per class',1),(38,'Swimming Lessons',40.00,'per lesson',1),(39,'Tennis Coaching',50.00,'per hour',1),(40,'Golf Coaching',100.00,'per hour',1),(41,'Photography Service',200.00,'per hour',1),(42,'DJ Service',300.00,'per event',1),(43,'Event Planning',150.00,'per hour',1),(44,'Catering Service',100.00,'per person',1),(45,'Bar Service',50.00,'per hour',1),(46,'Sommelier Service',75.00,'per hour',1),(47,'Wedding Planning',500.00,'per event',1),(48,'Anniversary Package',200.00,'per package',1),(49,'Honeymoon Package',300.00,'per night',1),(50,'Romance Package',150.00,'per night',1),(51,'Adventure Tour',150.00,'per person',1),(52,'Cultural Tour',120.00,'per person',1),(53,'Cooking Class',80.00,'per class',1),(54,'Wine Tasting Extra',60.00,'per person',1),(55,'Spa Day Package',300.00,'per day',1),(56,'Beauty Consultation',50.00,'per hour',1),(57,'Haircut Service',40.00,'per cut',1),(58,'Manicure Service',30.00,'per service',1),(59,'Pedicure Service',40.00,'per service',1),(60,'Facial Service',80.00,'per service',1),(61,'Body Wrap',100.00,'per wrap',1),(62,'Hot Stone Massage',120.00,'per session',1),(63,'Aromatherapy',90.00,'per session',1),(64,'Swedish Massage',110.00,'per session',1),(65,'Deep Tissue Massage',130.00,'per session',1),(66,'Thai Massage',100.00,'per session',1),(67,'Couples Massage',200.00,'per session',1),(68,'Medical Consultation',100.00,'per visit',1),(69,'Pharmacy Service',25.00,'per delivery',1),(70,'Doctor On Call',150.00,'per visit',1),(71,'Dental Service',100.00,'per visit',1),(72,'Vet Service',80.00,'per visit',1),(73,'Pet Grooming',60.00,'per session',1),(74,'Car Wash',30.00,'per wash',1),(75,'Car Detailing',100.00,'per detail',1),(76,'Valeting Service',40.00,'per service',1),(77,'Luggage Storage',10.00,'per day',1),(78,'Safe Deposit',5.00,'per day',1),(79,'Room Change Service',50.00,'per change',1),(80,'Express Checkout',20.00,'per service',1),(81,'Late Checkout Extended',50.00,'per hour',1),(82,'Early Check-in',30.00,'per service',1),(83,'Connecting Room',100.00,'per night',1),(84,'Room Upgrade',75.00,'per night',1),(85,'Complimentary Breakfast',40.00,'per person',1),(86,'Complimentary Lunch',45.00,'per person',1),(87,'Complimentary Dinner',60.00,'per person',1),(88,'Welcome Basket',50.00,'per basket',1),(89,'Turndown Service',20.00,'per night',1),(90,'Chocolate Pillow',10.00,'per pillow',1),(91,'Fresh Flowers',35.00,'per arrangement',1),(92,'Candle Light Dinner',100.00,'per couple',1),(93,'Valentine Package',250.00,'per night',1),(94,'Christmas Package',200.00,'per night',1),(95,'New Year Package',250.00,'per night',1),(96,'Summer Package',180.00,'per night',1),(97,'Easter Package',150.00,'per package',1),(98,'Birthday Package',200.00,'per event',1),(99,'Graduation Package',300.00,'per event',1),(100,'Family Package',400.00,'per package',1);
/*!40000 ALTER TABLE `extra_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guest`
--

DROP TABLE IF EXISTS `guest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guest` (
  `guest_id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `credit_card_last4` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`guest_id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_guest_email` (`email`),
  KEY `idx_guest_name` (`last_name`,`first_name`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest`
--

LOCK TABLES `guest` WRITE;
/*!40000 ALTER TABLE `guest` DISABLE KEYS */;
INSERT INTO `guest` VALUES (1,'John','Doe','john.doe@example.com','555-1001','1234'),(2,'Jane','Smith','jane.smith@example.com','555-1002','5678'),(3,'Bob','Johnson','bob.johnson@example.com','555-1003','9012'),(4,'Alice','Williams','alice.williams@example.com','555-1004','3456'),(5,'Charlie','Brown','charlie.brown@example.com','555-1005','7890'),(6,'Diana','Davis','diana.davis@example.com','555-1006','2345'),(7,'Edward','Miller','edward.miller@example.com','555-1007','6789'),(8,'Fiona','Wilson','fiona.wilson@example.com','555-1008','0123'),(9,'George','Moore','george.moore@example.com','555-1009','4567'),(10,'Hannah','Taylor','hannah.taylor@example.com','555-1010','8901'),(11,'Ian','Anderson','ian.anderson@example.com','555-1011','2345'),(12,'Jessica','Thomas','jessica.thomas@example.com','555-1012','6789'),(13,'Kevin','Jackson','kevin.jackson@example.com','555-1013','0123'),(14,'Laura','White','laura.white@example.com','555-1014','4567'),(15,'Michael','Harris','michael.harris@example.com','555-1015','8901'),(16,'Nicole','Martin','nicole.martin@example.com','555-1016','2345'),(17,'Oscar','Thompson','oscar.thompson@example.com','555-1017','6789'),(18,'Patricia','Garcia','patricia.garcia@example.com','555-1018','0123'),(19,'Quincy','Martinez','quincy.martinez@example.com','555-1019','4567'),(20,'Rachel','Robinson','rachel.robinson@example.com','555-1020','8901'),(21,'Samuel','Clark','samuel.clark@example.com','555-1021','2345'),(22,'Tanya','Rodriguez','tanya.rodriguez@example.com','555-1022','6789'),(23,'Ulysses','Lewis','ulysses.lewis@example.com','555-1023','0123'),(24,'Vanessa','Lee','vanessa.lee@example.com','555-1024','4567'),(25,'William','Walker','william.walker@example.com','555-1025','8901'),(26,'Xena','Hall','xena.hall@example.com','555-1026','2345'),(27,'Yuri','Allen','yuri.allen@example.com','555-1027','6789'),(28,'Zara','Young','zara.young@example.com','555-1028','0123'),(29,'Aaron','King','aaron.king@example.com','555-1029','4567'),(30,'Bella','Wright','bella.wright@example.com','555-1030','8901'),(31,'Calvin','Lopez','calvin.lopez@example.com','555-1031','2345'),(32,'Daisy','Hill','daisy.hill@example.com','555-1032','6789'),(33,'Ethan','Scott','ethan.scott@example.com','555-1033','0123'),(34,'Faye','Green','faye.green@example.com','555-1034','4567'),(35,'Gregory','Adams','gregory.adams@example.com','555-1035','8901'),(36,'Harper','Nelson','harper.nelson@example.com','555-1036','2345'),(37,'Isaac','Carter','isaac.carter@example.com','555-1037','6789'),(38,'Jasmine','Mitchell','jasmine.mitchell@example.com','555-1038','0123'),(39,'Kevin','Perez','kevin.perez@example.com','555-1039','4567'),(40,'Luna','Roberts','luna.roberts@example.com','555-1040','8901'),(41,'Marcus','Phillips','marcus.phillips@example.com','555-1041','2345'),(42,'Natalie','Campbell','natalie.campbell@example.com','555-1042','6789'),(43,'Oliver','Parker','oliver.parker@example.com','555-1043','0123'),(44,'Piper','Evans','piper.evans@example.com','555-1044','4567'),(45,'Quinton','Edwards','quinton.edwards@example.com','555-1045','8901'),(46,'Ruby','Collins','ruby.collins@example.com','555-1046','2345'),(47,'Sebastian','Reeves','sebastian.reeves@example.com','555-1047','6789'),(48,'Sophia','Morris','sophia.morris@example.com','555-1048','0123'),(49,'Tristan','Murphy','tristan.murphy@example.com','555-1049','4567'),(50,'Unity','Cook','unity.cook@example.com','555-1050','8901'),(51,'Victor','Morgan','victor.morgan@example.com','555-1051','2345'),(52,'Willa','Peterson','willa.peterson@example.com','555-1052','6789'),(53,'Xavier','Gray','xavier.gray@example.com','555-1053','0123'),(54,'Yolanda','Ramirez','yolanda.ramirez@example.com','555-1054','4567'),(55,'Zachary','James','zachary.james@example.com','555-1055','8901'),(56,'Amelia','Watson','amelia.watson@example.com','555-1056','2345'),(57,'Benjamin','Brooks','benjamin.brooks@example.com','555-1057','6789'),(58,'Charlotte','Chavez','charlotte.chavez@example.com','555-1058','0123'),(59,'Daniel','Wood','daniel.wood@example.com','555-1059','4567'),(60,'Emma','Mendoza','emma.mendoza@example.com','555-1060','8901'),(61,'Fredrick','Parks','fredrick.parks@example.com','555-1061','2345'),(62,'Grace','Bennett','grace.bennett@example.com','555-1062','6789'),(63,'Henry','Cruz','henry.cruz@example.com','555-1063','0123'),(64,'Iris','Porter','iris.porter@example.com','555-1064','4567'),(65,'Jacob','Howell','jacob.howell@example.com','555-1065','8901'),(66,'Katherine','Curry','katherine.curry@example.com','555-1066','2345'),(67,'Liam','Stokes','liam.stokes@example.com','555-1067','6789'),(68,'Mia','Ng','mia.ng@example.com','555-1068','0123'),(69,'Nathan','Tate','nathan.tate@example.com','555-1069','4567'),(70,'Olivia','Lamb','olivia.lamb@example.com','555-1070','8901'),(71,'Paul','Harper','paul.harper@example.com','555-1071','2345'),(72,'Quinn','Hunter','quinn.hunter@example.com','555-1072','6789'),(73,'Riley','Mckinney','riley.mckinney@example.com','555-1073','0123'),(74,'Sophie','Lucas','sophie.lucas@example.com','555-1074','4567'),(75,'Thomas','Nichols','thomas.nichols@example.com','555-1075','8901'),(76,'Ursula','Deleon','ursula.deleon@example.com','555-1076','2345'),(77,'Vincent','Dorsey','vincent.dorsey@example.com','555-1077','6789'),(78,'Wendy','Osborne','wendy.osborne@example.com','555-1078','0123'),(79,'Xander','Ochoa','xander.ochoa@example.com','555-1079','4567'),(80,'Yasmine','Jacobson','yasmine.jacobson@example.com','555-1080','8901'),(81,'Zacharias','Mock','zacharias.mock@example.com','555-1081','2345'),(82,'Abigail','Knowles','abigail.knowles@example.com','555-1082','6789'),(83,'Adrian','Moss','adrian.moss@example.com','555-1083','0123'),(84,'Aurora','Minor','aurora.minor@example.com','555-1084','4567'),(85,'Axel','Munoz','axel.munoz@example.com','555-1085','8901'),(86,'Brenda','Nickerson','brenda.nickerson@example.com','555-1086','2345'),(87,'Brennan','Oliver','brennan.oliver@example.com','555-1087','6789'),(88,'Brianna','Nieves','brianna.nieves@example.com','555-1088','0123'),(89,'Britney','Navarro','britney.navarro@example.com','555-1089','4567'),(90,'Brooke','Norris','brooke.norris@example.com','555-1090','8901'),(91,'Bryant','Nolan','bryant.nolan@example.com','555-1091','2345'),(92,'Carly','Odonnell','carly.odonnell@example.com','555-1092','6789'),(93,'Cecilia','Nader','cecilia.nader@example.com','555-1093','0123'),(94,'Celeste','Oconnor','celeste.oconnor@example.com','555-1094','4567'),(95,'Cesar','Ogden','cesar.ogden@example.com','555-1095','8901'),(96,'Chad','Pacheco','chad.pacheco@example.com','555-1096','2345'),(97,'Chandler','Pack','chandler.pack@example.com','555-1097','6789'),(98,'Chantelle','Page','chantelle.page@example.com','555-1098','0123'),(99,'Chelsea','Palmer','chelsea.palmer@example.com','555-1099','4567'),(100,'Chester','Parish','chester.parish@example.com','555-1100','8901'),(101,'Cheyenne','Parrish','cheyenne.parrish@example.com','555-1101','2345'),(102,'Chloe','Parsons','chloe.parsons@example.com','555-1102','6789'),(103,'Chris','Patton','chris.patton@example.com','555-1103','0123'),(104,'Christa','Paul','christa.paul@example.com','555-1104','4567'),(105,'Christelle','Payne','christelle.payne@example.com','555-1105','8901'),(106,'Christian','Pearson','christian.pearson@example.com','555-1106','2345'),(107,'Christina','Peck','christina.peck@example.com','555-1107','6789'),(108,'Christine','Pedersen','christine.pedersen@example.com','555-1108','0123'),(109,'Christopher','Pena','christopher.pena@example.com','555-1109','4567'),(110,'Christy','Penley','christy.penley@example.com','555-1110','8901'),(111,'Chrystal','Pennell','chrystal.pennell@example.com','555-1111','2345'),(112,'Cindy','Pennington','cindy.pennington@example.com','555-1112','6789'),(113,'Claire','Penniston','claire.penniston@example.com','555-1113','0123'),(114,'Clarence','Penny','clarence.penny@example.com','555-1114','4567'),(115,'Clark','Pensinger','clark.pensinger@example.com','555-1115','8901'),(116,'Claude','Penson','claude.penson@example.com','555-1116','2345'),(117,'Claudia','Perales','claudia.perales@example.com','555-1117','6789'),(118,'Clay','Peralta','clay.peralta@example.com','555-1118','0123'),(119,'Clayton','Perault','clayton.perault@example.com','555-1119','4567'),(120,'Cleora','Perazoult','cleora.perazoult@example.com','555-1120','8901'),(121,'Cleveland','Perberg','cleveland.perberg@example.com','555-1121','2345'),(122,'Clifford','PerchÃ©','clifford.perche@example.com','555-1122','6789'),(123,'Clifton','Percy','clifton.percy@example.com','555-1123','0123'),(124,'Clint','Perdue','clint.perdue@example.com','555-1124','4567'),(125,'Clinton','Perelman','clinton.perelman@example.com','555-1125','8901'),(126,'Clive','Perendo','clive.perendo@example.com','555-1126','2345'),(127,'Cloyd','Perera','cloyd.perera@example.com','555-1127','6789'),(128,'Clyde','Perez','clyde.perez@example.com','555-1128','0123'),(129,'Cody','Perham','cody.perham@example.com','555-1129','4567'),(130,'Cole','Perhard','cole.perhard@example.com','555-1130','8901'),(131,'Coleman','Perrault','coleman.perrault@example.com','555-1131','2345'),(132,'Colin','Perrecht','colin.perrecht@example.com','555-1132','6789'),(133,'Collin','Perrier','collin.perrier@example.com','555-1133','0123'),(134,'Colton','Perring','colton.perring@example.com','555-1134','4567'),(135,'Comer','Perris','comer.perris@example.com','555-1135','8901'),(136,'Conan','Perry','conan.perry@example.com','555-1136','2345'),(137,'Concepcion','Pershing','concepcion.pershing@example.com','555-1137','6789'),(138,'Conner','Persichine','conner.persichine@example.com','555-1138','0123'),(139,'Connie','Persing','connie.persing@example.com','555-1139','4567'),(140,'Connor','Persinger','connor.persinger@example.com','555-1140','8901'),(141,'Conor','Persky','conor.persky@example.com','555-1141','2345'),(142,'Conrad','Persone','conrad.persone@example.com','555-1142','6789'),(143,'Constance','Personne','constance.personne@example.com','555-1143','0123'),(144,'Constantine','Perth','constantine.perth@example.com','555-1144','4567'),(145,'Consuela','Pertue','consuela.pertue@example.com','555-1145','8901'),(146,'Consulo','Perugia','consulo.perugia@example.com','555-1146','2345'),(147,'Consuelo','Perusal','consuelo.perusal@example.com','555-1147','6789'),(148,'Consuella','Peruse','consuella.peruse@example.com','555-1148','0123'),(149,'Consuela','Pervuis','consuela.pervuis@example.com','555-1149','4567'),(150,'Consultoria','Peruvin','consultoria.peruvin@example.com','555-1150','8901');
/*!40000 ALTER TABLE `guest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_item`
--

DROP TABLE IF EXISTS `inventory_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_item` (
  `inventory_item_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unit_price` decimal(38,2) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`inventory_item_id`),
  UNIQUE KEY `name` (`name`),
  KEY `idx_inventory_active` (`active`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_item`
--

LOCK TABLES `inventory_item` WRITE;
/*!40000 ALTER TABLE `inventory_item` DISABLE KEYS */;
INSERT INTO `inventory_item` VALUES (1,'Bed Sheets Premium',25.00,1),(2,'Pillowcase Set',15.00,1),(3,'Towels Bath',12.00,1),(4,'Towels Hand',8.00,1),(5,'Towels Face Cloth',5.00,1),(6,'Shampoo Bottle',3.50,1),(7,'Conditioner Bottle',3.50,1),(8,'Soap Bar',2.00,1),(9,'Lotion Bottle',4.00,1),(10,'Toilet Paper Roll',1.50,1),(11,'Cleaning Supplies Spray',5.00,1),(12,'Vacuum Bag',8.00,1),(13,'Light Bulb LED',10.00,1),(14,'WiFi Router',150.00,1),(15,'Mattress Protector',40.00,1),(16,'Pillow Standard',20.00,1),(17,'Pillow Deluxe',35.00,1),(18,'Comforter',50.00,1),(19,'Mattress Pad',45.00,1),(20,'Duvet Cover',30.00,1),(21,'Pillow Cases',10.00,1),(22,'Blanket Thermal',25.00,1),(23,'Blanket Fleece',20.00,1),(24,'Curtains',40.00,1),(25,'Blackout Curtains',60.00,1),(26,'Window Shade',25.00,1),(27,'Bathroom Mirror',35.00,1),(28,'Shower Curtain',20.00,1),(29,'Bath Mat',15.00,1),(30,'Hand Towel Rack',30.00,1),(31,'Toilet Paper Holder',15.00,1),(32,'Soap Dispenser',20.00,1),(33,'Toothbrush Holder',10.00,1),(34,'Trash Can',25.00,1),(35,'Wastebasket',20.00,1),(36,'Desk Lamp',45.00,1),(37,'Bedside Lamp',40.00,1),(38,'Floor Lamp',60.00,1),(39,'Overhead Light',80.00,1),(40,'Night Light',15.00,1),(41,'Alarm Clock',20.00,1),(42,'Phone',50.00,1),(43,'Television',300.00,1),(44,'Remote Control',25.00,1),(45,'Thermostat',100.00,1),(46,'Air Conditioner Filter',15.00,1),(47,'Heating Element',35.00,1),(48,'Door Lock',75.00,1),(49,'Safe Box',150.00,1),(50,'Coat Hanger',5.00,1),(51,'Closet Rod',30.00,1),(52,'Curtain Rod',40.00,1),(53,'Picture Frame',25.00,1),(54,'Wall Art',50.00,1),(55,'Mirror',60.00,1),(56,'Bed Frame',200.00,1),(57,'Mattress',400.00,1),(58,'Box Spring',150.00,1),(59,'Nightstand',75.00,1),(60,'Desk',120.00,1),(61,'Chair',100.00,1),(62,'Sofa',300.00,1),(63,'Coffee Table',80.00,1),(64,'Side Table',60.00,1),(65,'Bookshelf',100.00,1),(66,'Cabinet',150.00,1),(67,'Dresser',180.00,1),(68,'Wardrobe',200.00,1),(69,'Shelving Unit',120.00,1),(70,'Laundry Basket',30.00,1),(71,'Vacuum Cleaner',250.00,1),(72,'Mop',20.00,1),(73,'Broom',15.00,1),(74,'Dustpan',10.00,1),(75,'Duster',8.00,1),(76,'Cleaning Cloth',3.00,1),(77,'Sponge',2.00,1),(78,'Dish Brush',5.00,1),(79,'Plunger',15.00,1),(80,'Air Freshener',8.00,1),(81,'Candle',12.00,1),(82,'Essential Oil Diffuser',40.00,1),(83,'Humidifier',80.00,1),(84,'Fan',60.00,1),(85,'Space Heater',70.00,1),(86,'Extension Cord',20.00,1),(87,'Power Outlet',15.00,1),(88,'Surge Protector',25.00,1),(89,'USB Charger',15.00,1),(90,'Phone Charger',20.00,1),(91,'Laptop Charger',80.00,1),(92,'Hair Dryer',35.00,1),(93,'Iron',40.00,1),(94,'Ironing Board',50.00,1),(95,'Garment Rack',45.00,1),(96,'Shoe Rack',30.00,1),(97,'Umbrella Stand',25.00,1),(98,'Coat Rack',40.00,1),(99,'Key Holder',10.00,1),(100,'Luggage Rack',50.00,1),(101,'Suitcase Stand',35.00,1);
/*!40000 ALTER TABLE `inventory_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservation_id` int NOT NULL AUTO_INCREMENT,
  `reference_no` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `nights` int NOT NULL,
  `num_guests` int NOT NULL,
  `room_type_id` int NOT NULL,
  `assigned_room_id` int DEFAULT NULL,
  `booked_rate_id` int NOT NULL,
  `booked_nightly_price` decimal(38,2) NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CONFIRMED',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `guest_id` bigint DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  PRIMARY KEY (`reservation_id`),
  UNIQUE KEY `reference_no` (`reference_no`),
  KEY `room_type_id` (`room_type_id`),
  KEY `assigned_room_id` (`assigned_room_id`),
  KEY `booked_rate_id` (`booked_rate_id`),
  KEY `idx_reservation_dates` (`check_in_date`,`check_out_date`),
  KEY `idx_reservation_status` (`status`),
  KEY `idx_reservation_guest` (`guest_id`),
  KEY `idx_reservation_room` (`room_id`),
  KEY `idx_reservation_reference` (`reference_no`),
  KEY `idx_reservation_created` (`created_at`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE RESTRICT,
  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`assigned_room_id`) REFERENCES `room` (`room_id`) ON DELETE SET NULL,
  CONSTRAINT `reservation_ibfk_3` FOREIGN KEY (`booked_rate_id`) REFERENCES `season_rate` (`rate_id`) ON DELETE RESTRICT,
  CONSTRAINT `reservation_ibfk_4` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guest_id`) ON DELETE CASCADE,
  CONSTRAINT `reservation_ibfk_5` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,'RES001','2026-05-05','2026-05-07',2,1,1,1,1,80.00,'CONFIRMED','2026-05-04 10:00:00',1,1),(2,'RES002','2026-05-06','2026-05-09',3,2,2,11,3,120.00,'CONFIRMED','2026-05-04 11:00:00',2,11),(3,'RES003','2026-05-07','2026-05-10',3,2,3,21,5,110.00,'CONFIRMED','2026-05-04 12:00:00',3,21),(4,'RES004','2026-05-08','2026-05-12',4,4,4,31,7,200.00,'CONFIRMED','2026-05-04 13:00:00',4,31),(5,'RES005','2026-05-09','2026-05-13',4,4,5,36,9,280.00,'CONFIRMED','2026-05-04 14:00:00',5,36),(6,'RES006','2026-05-10','2026-05-15',5,1,1,2,1,80.00,'CONFIRMED','2026-05-04 15:00:00',6,2),(7,'RES007','2026-05-11','2026-05-14',3,2,2,12,3,120.00,'CONFIRMED','2026-05-04 16:00:00',7,12),(8,'RES008','2026-05-12','2026-05-16',4,2,3,22,5,110.00,'CONFIRMED','2026-05-04 17:00:00',8,22),(9,'RES009','2026-05-13','2026-05-17',4,4,4,32,7,200.00,'CONFIRMED','2026-05-04 18:00:00',9,32),(10,'RES010','2026-05-14','2026-05-18',4,4,5,37,9,280.00,'CONFIRMED','2026-05-04 19:00:00',10,37),(11,'RES011','2026-05-15','2026-05-19',4,1,1,3,1,80.00,'CONFIRMED','2026-05-04 20:00:00',11,3),(12,'RES012','2026-05-16','2026-05-19',3,2,2,13,3,120.00,'CONFIRMED','2026-05-04 21:00:00',12,13),(13,'RES013','2026-05-17','2026-05-20',3,2,3,23,5,110.00,'CONFIRMED','2026-05-04 22:00:00',13,23),(14,'RES014','2026-05-18','2026-05-22',4,4,4,33,7,200.00,'CONFIRMED','2026-05-05 08:00:00',14,33),(15,'RES015','2026-05-19','2026-05-23',4,4,5,38,9,280.00,'CONFIRMED','2026-05-05 09:00:00',15,38),(16,'RES016','2026-05-20','2026-05-24',4,1,1,4,1,80.00,'CANCELLED','2026-05-05 10:00:00',16,4),(17,'RES017','2026-05-21','2026-05-24',3,2,2,14,3,120.00,'CONFIRMED','2026-05-05 11:00:00',17,14),(18,'RES018','2026-05-22','2026-05-25',3,2,3,24,5,110.00,'CONFIRMED','2026-05-05 12:00:00',18,24),(19,'RES019','2026-05-23','2026-05-27',4,4,4,34,7,200.00,'CONFIRMED','2026-05-05 13:00:00',19,34),(20,'RES020','2026-05-24','2026-05-28',4,4,5,39,9,280.00,'CONFIRMED','2026-05-05 14:00:00',20,39),(21,'RES021','2026-05-25','2026-05-29',4,1,1,5,1,80.00,'CONFIRMED','2026-05-05 15:00:00',21,5),(22,'RES022','2026-05-26','2026-05-29',3,2,2,15,3,120.00,'CONFIRMED','2026-05-05 16:00:00',22,15),(23,'RES023','2026-05-27','2026-05-30',3,2,3,25,5,110.00,'CONFIRMED','2026-05-05 17:00:00',23,25),(24,'RES024','2026-05-28','2026-06-01',4,4,4,35,8,300.00,'CONFIRMED','2026-05-05 18:00:00',24,35),(25,'RES025','2026-05-29','2026-06-02',4,4,5,40,10,420.00,'CONFIRMED','2026-05-05 19:00:00',25,40),(26,'RES026','2026-05-30','2026-06-03',4,1,1,1,2,120.00,'CONFIRMED','2026-05-05 20:00:00',26,1),(27,'RES027','2026-05-31','2026-06-03',3,2,2,16,4,180.00,'CONFIRMED','2026-05-05 21:00:00',27,16),(28,'RES028','2026-06-01','2026-06-04',3,2,3,26,6,170.00,'CONFIRMED','2026-05-05 22:00:00',28,26),(29,'RES029','2026-06-02','2026-06-06',4,4,4,31,8,300.00,'CONFIRMED','2026-05-06 08:00:00',29,31),(30,'RES030','2026-06-03','2026-06-07',4,4,5,36,10,420.00,'CONFIRMED','2026-05-06 09:00:00',30,36),(31,'RES031','2026-06-04','2026-06-08',4,1,1,2,2,120.00,'CONFIRMED','2026-05-06 10:00:00',31,2),(32,'RES032','2026-06-05','2026-06-08',3,2,2,11,4,180.00,'CONFIRMED','2026-05-06 11:00:00',32,11),(33,'RES033','2026-06-06','2026-06-09',3,2,3,21,6,170.00,'CONFIRMED','2026-05-06 12:00:00',33,21),(34,'RES034','2026-06-07','2026-06-11',4,4,4,32,8,300.00,'CONFIRMED','2026-05-06 13:00:00',34,32),(35,'RES035','2026-06-08','2026-06-12',4,4,5,37,10,420.00,'CONFIRMED','2026-05-06 14:00:00',35,37),(36,'RES036','2026-06-09','2026-06-13',4,1,1,3,2,120.00,'CONFIRMED','2026-05-06 15:00:00',36,3),(37,'RES037','2026-06-10','2026-06-13',3,2,2,12,4,180.00,'CONFIRMED','2026-05-06 16:00:00',37,12),(38,'RES038','2026-06-11','2026-06-14',3,2,3,22,6,170.00,'CONFIRMED','2026-05-06 17:00:00',38,22),(39,'RES039','2026-06-12','2026-06-16',4,4,4,33,8,300.00,'CONFIRMED','2026-05-06 18:00:00',39,33),(40,'RES040','2026-06-13','2026-06-17',4,4,5,38,10,420.00,'CONFIRMED','2026-05-06 19:00:00',40,38),(41,'RES041','2026-06-14','2026-06-18',4,1,1,4,2,120.00,'CONFIRMED','2026-05-06 20:00:00',41,4),(42,'RES042','2026-06-15','2026-06-18',3,2,2,13,4,180.00,'CONFIRMED','2026-05-06 21:00:00',42,13),(43,'RES043','2026-06-16','2026-06-19',3,2,3,23,6,170.00,'CONFIRMED','2026-05-06 22:00:00',43,23),(44,'RES044','2026-06-17','2026-06-21',4,4,4,34,8,300.00,'CONFIRMED','2026-05-07 08:00:00',44,34),(45,'RES045','2026-06-18','2026-06-22',4,4,5,39,10,420.00,'CONFIRMED','2026-05-07 09:00:00',45,39),(46,'RES046','2026-06-19','2026-06-23',4,1,1,5,2,120.00,'CONFIRMED','2026-05-07 10:00:00',46,5),(47,'RES047','2026-06-20','2026-06-23',3,2,2,14,4,180.00,'CONFIRMED','2026-05-07 11:00:00',47,14),(48,'RES048','2026-06-21','2026-06-24',3,2,3,24,6,170.00,'CONFIRMED','2026-05-07 12:00:00',48,24),(49,'RES049','2026-06-22','2026-06-26',4,4,4,35,8,300.00,'CONFIRMED','2026-05-07 13:00:00',49,35),(50,'RES050','2026-06-23','2026-06-27',4,4,5,40,10,420.00,'CONFIRMED','2026-05-07 14:00:00',50,40),(51,'RES051','2026-06-24','2026-06-28',4,1,1,1,2,120.00,'CHECKED_OUT','2026-05-07 15:00:00',51,1),(52,'RES052','2026-06-25','2026-06-28',3,2,2,15,4,180.00,'CONFIRMED','2026-05-07 16:00:00',52,15),(53,'RES053','2026-06-26','2026-06-29',3,2,3,25,6,170.00,'CONFIRMED','2026-05-07 17:00:00',53,25),(54,'RES054','2026-06-27','2026-07-01',4,4,4,31,8,300.00,'CONFIRMED','2026-05-07 18:00:00',54,31),(55,'RES055','2026-06-28','2026-07-02',4,4,5,36,10,420.00,'CONFIRMED','2026-05-07 19:00:00',55,36),(56,'RES056','2026-07-01','2026-07-05',4,1,1,6,2,120.00,'CONFIRMED','2026-05-08 10:00:00',56,6),(57,'RES057','2026-07-02','2026-07-05',3,2,2,16,4,180.00,'CONFIRMED','2026-05-08 11:00:00',57,16),(58,'RES058','2026-07-03','2026-07-06',3,2,3,26,6,170.00,'CONFIRMED','2026-05-08 12:00:00',58,26),(59,'RES059','2026-07-04','2026-07-08',4,4,4,32,8,300.00,'CONFIRMED','2026-05-08 13:00:00',59,32),(60,'RES060','2026-07-05','2026-07-09',4,4,5,37,10,420.00,'CONFIRMED','2026-05-08 14:00:00',60,37),(61,'RES061','2026-07-06','2026-07-10',4,1,1,7,2,120.00,'CONFIRMED','2026-05-08 15:00:00',61,7),(62,'RES062','2026-07-07','2026-07-10',3,2,2,17,4,180.00,'CONFIRMED','2026-05-08 16:00:00',62,17),(63,'RES063','2026-07-08','2026-07-11',3,2,3,27,6,170.00,'CONFIRMED','2026-05-08 17:00:00',63,27),(64,'RES064','2026-07-09','2026-07-13',4,4,4,33,8,300.00,'CONFIRMED','2026-05-08 18:00:00',64,33),(65,'RES065','2026-07-10','2026-07-14',4,4,5,38,10,420.00,'CONFIRMED','2026-05-08 19:00:00',65,38),(66,'RES066','2026-07-11','2026-07-15',4,1,1,8,2,120.00,'CONFIRMED','2026-05-09 10:00:00',66,8),(67,'RES067','2026-07-12','2026-07-15',3,2,2,18,4,180.00,'CONFIRMED','2026-05-09 11:00:00',67,18),(68,'RES068','2026-07-13','2026-07-16',3,2,3,28,6,170.00,'CONFIRMED','2026-05-09 12:00:00',68,28),(69,'RES069','2026-07-14','2026-07-18',4,4,4,34,8,300.00,'CONFIRMED','2026-05-09 13:00:00',69,34),(70,'RES070','2026-07-15','2026-07-19',4,4,5,39,10,420.00,'CONFIRMED','2026-05-09 14:00:00',70,39),(71,'RES071','2026-07-16','2026-07-20',4,1,1,9,2,120.00,'CONFIRMED','2026-05-09 15:00:00',71,9),(72,'RES072','2026-07-17','2026-07-20',3,2,2,19,4,180.00,'CONFIRMED','2026-05-09 16:00:00',72,19),(73,'RES073','2026-07-18','2026-07-21',3,2,3,29,6,170.00,'CONFIRMED','2026-05-09 17:00:00',73,29),(74,'RES074','2026-07-19','2026-07-23',4,4,4,35,8,300.00,'CONFIRMED','2026-05-09 18:00:00',74,35),(75,'RES075','2026-07-20','2026-07-24',4,4,5,40,10,420.00,'CONFIRMED','2026-05-09 19:00:00',75,40),(76,'RES076','2026-07-21','2026-07-25',4,1,1,10,2,120.00,'CONFIRMED','2026-05-10 10:00:00',76,10),(77,'RES077','2026-07-22','2026-07-25',3,2,2,20,4,180.00,'CONFIRMED','2026-05-10 11:00:00',77,20),(78,'RES078','2026-07-23','2026-07-26',3,2,3,30,6,170.00,'CONFIRMED','2026-05-10 12:00:00',78,30),(79,'RES079','2026-07-24','2026-07-28',4,4,4,31,8,300.00,'CONFIRMED','2026-05-10 13:00:00',79,31),(80,'RES080','2026-07-25','2026-07-29',4,4,5,36,10,420.00,'CONFIRMED','2026-05-10 14:00:00',80,36),(81,'RES081','2026-08-01','2026-08-05',4,1,1,1,2,120.00,'CONFIRMED','2026-05-10 15:00:00',81,1),(82,'RES082','2026-08-02','2026-08-05',3,2,2,11,4,180.00,'CONFIRMED','2026-05-10 16:00:00',82,11),(83,'RES083','2026-08-03','2026-08-06',3,2,3,21,6,170.00,'CONFIRMED','2026-05-10 17:00:00',83,21),(84,'RES084','2026-08-04','2026-08-08',4,4,4,32,8,300.00,'CONFIRMED','2026-05-10 18:00:00',84,32),(85,'RES085','2026-08-05','2026-08-09',4,4,5,37,10,420.00,'CONFIRMED','2026-05-10 19:00:00',85,37),(86,'RES086','2026-08-06','2026-08-10',4,1,1,2,2,120.00,'CONFIRMED','2026-05-11 10:00:00',86,2),(87,'RES087','2026-08-07','2026-08-10',3,2,2,12,4,180.00,'CONFIRMED','2026-05-11 11:00:00',87,12),(88,'RES088','2026-08-08','2026-08-11',3,2,3,22,6,170.00,'CONFIRMED','2026-05-11 12:00:00',88,22),(89,'RES089','2026-08-09','2026-08-13',4,4,4,33,8,300.00,'CONFIRMED','2026-05-11 13:00:00',89,33),(90,'RES090','2026-08-10','2026-08-14',4,4,5,38,10,420.00,'CONFIRMED','2026-05-11 14:00:00',90,38),(91,'RES091','2026-08-11','2026-08-15',4,1,1,3,2,120.00,'CONFIRMED','2026-05-11 15:00:00',91,3),(92,'RES092','2026-08-12','2026-08-15',3,2,2,13,4,180.00,'CONFIRMED','2026-05-11 16:00:00',92,13),(93,'RES093','2026-08-13','2026-08-16',3,2,3,23,6,170.00,'CONFIRMED','2026-05-11 17:00:00',93,23),(94,'RES094','2026-08-14','2026-08-18',4,4,4,34,8,300.00,'CONFIRMED','2026-05-11 18:00:00',94,34),(95,'RES095','2026-08-15','2026-08-19',4,4,5,39,10,420.00,'CONFIRMED','2026-05-11 19:00:00',95,39),(96,'RES096','2026-08-16','2026-08-20',4,1,1,4,2,120.00,'CONFIRMED','2026-05-12 10:00:00',96,4),(97,'RES097','2026-08-17','2026-08-20',3,2,2,14,4,180.00,'CONFIRMED','2026-05-12 11:00:00',97,14),(98,'RES098','2026-08-18','2026-08-21',3,2,3,24,6,170.00,'CONFIRMED','2026-05-12 12:00:00',98,24),(99,'RES099','2026-08-19','2026-08-23',4,4,4,35,8,300.00,'CONFIRMED','2026-05-12 13:00:00',99,35),(100,'RES100','2026-08-20','2026-08-24',4,4,5,40,10,420.00,'CONFIRMED','2026-05-12 14:00:00',100,40),(101,'RES101','2026-08-21','2026-08-25',4,1,1,5,2,120.00,'CONFIRMED','2026-05-12 15:00:00',101,5),(102,'RES102','2026-08-22','2026-08-25',3,2,2,15,4,180.00,'CONFIRMED','2026-05-12 16:00:00',102,15),(103,'RES103','2026-08-23','2026-08-26',3,2,3,25,6,170.00,'CONFIRMED','2026-05-12 17:00:00',103,25),(104,'RES104','2026-08-24','2026-08-28',4,4,4,31,8,300.00,'CONFIRMED','2026-05-12 18:00:00',104,31),(105,'RES105','2026-08-25','2026-08-29',4,4,5,36,10,420.00,'CONFIRMED','2026-05-12 19:00:00',105,36),(106,'RES106','2026-09-01','2026-09-05',4,1,1,6,2,120.00,'CONFIRMED','2026-05-13 10:00:00',106,6),(107,'RES107','2026-09-02','2026-09-05',3,2,2,16,4,180.00,'CONFIRMED','2026-05-13 11:00:00',107,16),(108,'RES108','2026-09-03','2026-09-06',3,2,3,26,6,170.00,'CONFIRMED','2026-05-13 12:00:00',108,26),(109,'RES109','2026-09-04','2026-09-08',4,4,4,32,8,300.00,'CONFIRMED','2026-05-13 13:00:00',109,32),(110,'RES110','2026-09-05','2026-09-09',4,4,5,37,10,420.00,'CONFIRMED','2026-05-13 14:00:00',110,37),(111,'RES111','2026-09-06','2026-09-10',4,1,1,7,2,120.00,'CONFIRMED','2026-05-13 15:00:00',111,7),(112,'RES112','2026-09-07','2026-09-10',3,2,2,17,4,180.00,'CONFIRMED','2026-05-13 16:00:00',112,17),(113,'RES113','2026-09-08','2026-09-11',3,2,3,27,6,170.00,'CONFIRMED','2026-05-13 17:00:00',113,27),(114,'RES114','2026-09-09','2026-09-13',4,4,4,33,8,300.00,'CONFIRMED','2026-05-13 18:00:00',114,33),(115,'RES115','2026-09-10','2026-09-14',4,4,5,38,10,420.00,'CONFIRMED','2026-05-13 19:00:00',115,38),(116,'RES116','2026-09-11','2026-09-15',4,1,1,8,2,120.00,'CONFIRMED','2026-05-14 10:00:00',116,8),(117,'RES117','2026-09-12','2026-09-15',3,2,2,18,4,180.00,'CONFIRMED','2026-05-14 11:00:00',117,18),(118,'RES118','2026-09-13','2026-09-16',3,2,3,28,6,170.00,'CONFIRMED','2026-05-14 12:00:00',118,28),(119,'RES119','2026-09-14','2026-09-18',4,4,4,34,8,300.00,'CONFIRMED','2026-05-14 13:00:00',119,34),(120,'RES120','2026-09-15','2026-09-19',4,4,5,39,10,420.00,'CONFIRMED','2026-05-14 14:00:00',120,39);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_AfterCheckout` AFTER UPDATE ON `reservation` FOR EACH ROW BEGIN
    IF NEW.status = 'Checked Out' AND OLD.status != 'Checked Out' THEN
        UPDATE room
        SET clean_status = 'Dirty', room_status = 'Vacant'
        WHERE room_id = NEW.room_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_RoomStatusUpdate` AFTER UPDATE ON `reservation` FOR EACH ROW BEGIN
    IF NEW.status = 'CONFIRMED' AND OLD.status != 'CONFIRMED' THEN
        UPDATE room
        SET room_status = 'OCCUPIED', occupied = 1
        WHERE room_id = NEW.room_id;
    ELSEIF NEW.status = 'CANCELLED' THEN
        UPDATE room
        SET room_status = 'AVAILABLE', occupied = 0
        WHERE room_id = NEW.room_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `reservation_guest`
--

DROP TABLE IF EXISTS `reservation_guest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_guest` (
  `reservation_id` int NOT NULL,
  `guest_id` bigint NOT NULL,
  `is_primary` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`reservation_id`,`guest_id`),
  KEY `idx_res_guest_guest` (`guest_id`),
  CONSTRAINT `reservation_guest_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`) ON DELETE CASCADE,
  CONSTRAINT `reservation_guest_ibfk_2` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guest_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_guest`
--

LOCK TABLES `reservation_guest` WRITE;
/*!40000 ALTER TABLE `reservation_guest` DISABLE KEYS */;
INSERT INTO `reservation_guest` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,1),(5,5,1),(6,6,1),(7,7,1),(8,8,1),(9,9,1),(10,10,1),(11,11,1),(12,12,1),(13,13,1),(14,14,1),(15,15,1),(16,16,1),(17,17,1),(18,18,1),(19,19,1),(20,20,1),(21,21,1),(22,22,1),(23,23,1),(24,24,1),(25,25,1),(26,26,1),(27,27,1),(28,28,1),(29,29,1),(30,30,1),(31,31,1),(32,32,1),(33,33,1),(34,34,1),(35,35,1),(36,36,1),(37,37,1),(38,38,1),(39,39,1),(40,40,1),(41,41,1),(42,42,1),(43,43,1),(44,44,1),(45,45,1),(46,46,1),(47,47,1),(48,48,1),(49,49,1),(50,50,1),(51,51,1),(52,52,1),(53,53,1),(54,54,1),(55,55,1),(56,56,1),(57,57,1),(58,58,1),(59,59,1),(60,60,1),(61,61,1),(62,62,1),(63,63,1),(64,64,1),(65,65,1),(66,66,1),(67,67,1),(68,68,1),(69,69,1),(70,70,1),(71,71,1),(72,72,1),(73,73,1),(74,74,1),(75,75,1),(76,76,1),(77,77,1),(78,78,1),(79,79,1),(80,80,1),(81,81,1),(82,82,1),(83,83,1),(84,84,1),(85,85,1),(86,86,1),(87,87,1),(88,88,1),(89,89,1),(90,90,1),(91,91,1),(92,92,1),(93,93,1),(94,94,1),(95,95,1),(96,96,1),(97,97,1),(98,98,1),(99,99,1),(100,100,1),(101,101,1),(102,102,1),(103,103,1),(104,104,1),(105,105,1),(106,106,1),(107,107,1),(108,108,1),(109,109,1),(110,110,1),(111,111,1),(112,112,1),(113,113,1),(114,114,1),(115,115,1),(116,116,1),(117,117,1),(118,118,1),(119,119,1),(120,120,1);
/*!40000 ALTER TABLE `reservation_guest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `room_number` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `room_type_id` int NOT NULL,
  `room_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `clean_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `occupied` bit(1) NOT NULL DEFAULT b'0',
  `type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `room_number` (`room_number`),
  KEY `room_type_id` (`room_type_id`),
  KEY `idx_room_status` (`room_status`),
  KEY `idx_room_clean_status` (`clean_status`),
  KEY `idx_room_number` (`room_number`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'101',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(2,'102',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(3,'103',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(4,'104',1,'OCCUPIED','CLEAN',_binary '','Single'),(5,'105',1,'MAINTENANCE','DIRTY',_binary '\0','Single'),(6,'106',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(7,'107',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(8,'108',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(9,'109',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(10,'110',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(11,'201',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(12,'202',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(13,'203',2,'OCCUPIED','CLEAN',_binary '','Double'),(14,'204',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(15,'205',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(16,'206',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(17,'207',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(18,'208',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(19,'209',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(20,'210',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(21,'301',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(22,'302',3,'OCCUPIED','CLEAN',_binary '','Twin'),(23,'303',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(24,'304',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(25,'305',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(26,'306',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(27,'307',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(28,'308',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(29,'309',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(30,'310',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(31,'401',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(32,'402',4,'OCCUPIED','CLEAN',_binary '','Suite'),(33,'403',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(34,'404',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(35,'405',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(36,'501',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe Suite'),(37,'502',5,'OCCUPIED','CLEAN',_binary '','Deluxe Suite'),(38,'503',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe Suite'),(39,'504',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe Suite'),(40,'505',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe Suite'),(41,'601',6,'AVAILABLE','CLEAN',_binary '\0','Penthouse'),(42,'602',6,'OCCUPIED','CLEAN',_binary '','Penthouse'),(43,'603',6,'AVAILABLE','CLEAN',_binary '\0','Penthouse'),(44,'604',6,'AVAILABLE','CLEAN',_binary '\0','Penthouse'),(45,'605',6,'AVAILABLE','CLEAN',_binary '\0','Penthouse'),(46,'46',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(47,'47',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(48,'48',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(49,'49',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(50,'50',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(51,'51',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(52,'52',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(53,'53',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(54,'54',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(55,'55',1,'AVAILABLE','CLEAN',_binary '\0','Single'),(56,'56',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(57,'57',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(58,'58',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(59,'59',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(60,'60',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(61,'61',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(62,'62',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(63,'63',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(64,'64',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(65,'65',2,'AVAILABLE','CLEAN',_binary '\0','Double'),(66,'66',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(67,'67',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(68,'68',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(69,'69',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(70,'70',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(71,'71',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(72,'72',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(73,'73',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(74,'74',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(75,'75',3,'AVAILABLE','CLEAN',_binary '\0','Twin'),(76,'76',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(77,'77',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(78,'78',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(79,'79',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(80,'80',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(81,'81',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(82,'82',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(83,'83',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(84,'84',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(85,'85',4,'AVAILABLE','CLEAN',_binary '\0','Suite'),(86,'86',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(87,'87',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(88,'88',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(89,'89',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(90,'90',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(91,'91',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(92,'92',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(93,'93',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(94,'94',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(95,'95',5,'AVAILABLE','CLEAN',_binary '\0','Deluxe'),(96,'96',6,'AVAILABLE','CLEAN',_binary '\0','Studio'),(97,'97',6,'AVAILABLE','CLEAN',_binary '\0','Studio'),(98,'98',6,'AVAILABLE','CLEAN',_binary '\0','Studio'),(99,'99',6,'AVAILABLE','CLEAN',_binary '\0','Studio'),(100,'100',6,'AVAILABLE','CLEAN',_binary '\0','Studio');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_cleaning_assignment`
--

DROP TABLE IF EXISTS `room_cleaning_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_cleaning_assignment` (
  `task_id` int NOT NULL,
  `cleaner_id` int NOT NULL,
  `assigned_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`,`cleaner_id`),
  KEY `idx_assignment_cleaner` (`cleaner_id`),
  CONSTRAINT `room_cleaning_assignment_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `room_cleaning_task` (`task_id`) ON DELETE CASCADE,
  CONSTRAINT `room_cleaning_assignment_ibfk_2` FOREIGN KEY (`cleaner_id`) REFERENCES `cleaner` (`cleaner_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_cleaning_assignment`
--

LOCK TABLES `room_cleaning_assignment` WRITE;
/*!40000 ALTER TABLE `room_cleaning_assignment` DISABLE KEYS */;
INSERT INTO `room_cleaning_assignment` VALUES (1,1,'2026-05-07 11:30:00'),(2,2,'2026-05-08 10:00:00'),(3,3,'2026-05-09 09:00:00'),(4,4,'2026-05-10 08:00:00'),(5,5,'2026-05-11 10:00:00'),(6,1,'2026-05-12 12:00:00'),(7,2,'2026-05-13 11:00:00'),(8,3,'2026-05-14 10:00:00'),(9,4,'2026-05-15 09:00:00'),(10,5,'2026-05-16 08:00:00'),(11,1,'2026-05-17 13:00:00'),(12,2,'2026-05-18 12:00:00'),(13,3,'2026-05-19 11:00:00'),(14,4,'2026-05-20 10:00:00'),(15,5,'2026-05-21 09:00:00'),(16,1,'2026-05-22 14:00:00'),(17,2,'2026-05-23 13:00:00'),(18,3,'2026-05-24 12:00:00'),(19,4,'2026-05-25 11:00:00'),(20,5,'2026-05-26 10:00:00'),(21,1,'2026-05-27 15:00:00'),(22,2,'2026-05-28 14:00:00'),(23,3,'2026-05-29 13:00:00'),(24,4,'2026-05-30 12:00:00'),(25,5,'2026-05-31 11:00:00'),(26,1,'2026-06-01 16:00:00'),(27,2,'2026-06-02 15:00:00'),(28,3,'2026-06-03 14:00:00'),(29,4,'2026-06-04 13:00:00'),(30,5,'2026-06-05 12:00:00'),(31,1,'2026-06-06 11:00:00'),(32,2,'2026-06-07 10:00:00'),(33,3,'2026-06-08 09:00:00'),(34,4,'2026-06-09 08:00:00'),(35,5,'2026-06-10 10:00:00'),(36,1,'2026-06-11 12:00:00'),(37,2,'2026-06-12 11:00:00'),(38,3,'2026-06-13 10:00:00'),(39,4,'2026-06-14 09:00:00'),(40,5,'2026-06-15 08:00:00'),(41,1,'2026-06-16 13:00:00'),(42,2,'2026-06-17 12:00:00'),(43,3,'2026-06-18 11:00:00'),(44,4,'2026-06-19 10:00:00'),(45,5,'2026-06-20 09:00:00'),(46,1,'2026-06-21 14:00:00'),(47,2,'2026-06-22 13:00:00'),(48,3,'2026-06-23 12:00:00'),(49,4,'2026-06-24 11:00:00'),(50,5,'2026-06-25 10:00:00'),(51,1,'2026-06-26 15:00:00'),(52,2,'2026-06-27 14:00:00'),(53,3,'2026-06-28 13:00:00'),(54,4,'2026-06-29 12:00:00'),(55,5,'2026-06-30 11:00:00'),(56,1,'2026-07-01 16:00:00'),(57,2,'2026-07-02 15:00:00'),(58,3,'2026-07-03 14:00:00'),(59,4,'2026-07-04 13:00:00'),(60,5,'2026-07-05 12:00:00'),(61,1,'2026-07-06 11:00:00'),(62,2,'2026-07-07 10:00:00'),(63,3,'2026-07-08 09:00:00'),(64,4,'2026-07-09 08:00:00'),(65,5,'2026-07-10 10:00:00'),(66,1,'2026-07-11 12:00:00'),(67,2,'2026-07-12 11:00:00'),(68,3,'2026-07-13 10:00:00'),(69,4,'2026-07-14 09:00:00'),(70,5,'2026-07-15 08:00:00'),(71,1,'2026-07-16 13:00:00'),(72,2,'2026-07-17 12:00:00'),(73,3,'2026-07-18 11:00:00'),(74,4,'2026-07-19 10:00:00'),(75,5,'2026-07-20 09:00:00'),(76,1,'2026-07-21 14:00:00'),(77,2,'2026-07-22 13:00:00'),(78,3,'2026-07-23 12:00:00'),(79,4,'2026-07-24 11:00:00'),(80,5,'2026-07-25 10:00:00'),(81,1,'2026-07-26 15:00:00'),(82,2,'2026-07-27 14:00:00'),(83,3,'2026-07-28 13:00:00'),(84,4,'2026-07-29 12:00:00'),(85,5,'2026-07-30 11:00:00'),(86,1,'2026-08-01 16:00:00'),(87,2,'2026-08-02 15:00:00'),(88,3,'2026-08-03 14:00:00'),(89,4,'2026-08-04 13:00:00'),(90,5,'2026-08-05 12:00:00'),(91,1,'2026-08-06 11:00:00'),(92,2,'2026-08-07 10:00:00'),(93,3,'2026-08-08 09:00:00'),(94,4,'2026-08-09 08:00:00'),(95,5,'2026-08-10 10:00:00'),(96,1,'2026-08-11 12:00:00'),(97,2,'2026-08-12 11:00:00'),(98,3,'2026-08-13 10:00:00'),(99,4,'2026-08-14 09:00:00'),(100,5,'2026-08-15 08:00:00');
/*!40000 ALTER TABLE `room_cleaning_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_cleaning_task`
--

DROP TABLE IF EXISTS `room_cleaning_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_cleaning_task` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `room_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `task_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `note` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  KEY `idx_cleaning_task_room` (`room_id`),
  KEY `idx_cleaning_task_status` (`task_status`),
  CONSTRAINT `room_cleaning_task_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_cleaning_task`
--

LOCK TABLES `room_cleaning_task` WRITE;
/*!40000 ALTER TABLE `room_cleaning_task` DISABLE KEYS */;
INSERT INTO `room_cleaning_task` VALUES (1,1,'2026-05-07 11:30:00','COMPLETED','Room cleaned after checkout'),(2,2,'2026-05-08 10:00:00','PENDING','Waiting for cleaner'),(3,3,'2026-05-09 09:00:00','IN_PROGRESS','Currently being cleaned'),(4,4,'2026-05-10 08:00:00','COMPLETED','Room cleaned and ready'),(5,5,'2026-05-11 10:00:00','COMPLETED','Maintenance cleaning'),(6,6,'2026-05-12 12:00:00','COMPLETED','Room cleaned after checkout'),(7,7,'2026-05-13 11:00:00','IN_PROGRESS','Currently being cleaned'),(8,8,'2026-05-14 10:00:00','COMPLETED','Room cleaned'),(9,9,'2026-05-15 09:00:00','PENDING','Waiting for cleaner'),(10,10,'2026-05-16 08:00:00','COMPLETED','Room cleaned'),(11,11,'2026-05-17 13:00:00','COMPLETED','Room cleaned after checkout'),(12,12,'2026-05-18 12:00:00','COMPLETED','Room cleaned'),(13,13,'2026-05-19 11:00:00','IN_PROGRESS','Currently being cleaned'),(14,14,'2026-05-20 10:00:00','PENDING','Waiting for cleaner'),(15,15,'2026-05-21 09:00:00','COMPLETED','Room cleaned'),(16,16,'2026-05-22 14:00:00','COMPLETED','Room cleaned after checkout'),(17,17,'2026-05-23 13:00:00','COMPLETED','Room cleaned'),(18,18,'2026-05-24 12:00:00','IN_PROGRESS','Currently being cleaned'),(19,19,'2026-05-25 11:00:00','PENDING','Waiting for cleaner'),(20,20,'2026-05-26 10:00:00','COMPLETED','Room cleaned'),(21,21,'2026-05-27 15:00:00','COMPLETED','Room cleaned after checkout'),(22,22,'2026-05-28 14:00:00','COMPLETED','Room cleaned'),(23,23,'2026-05-29 13:00:00','IN_PROGRESS','Currently being cleaned'),(24,24,'2026-05-30 12:00:00','PENDING','Waiting for cleaner'),(25,25,'2026-05-31 11:00:00','COMPLETED','Room cleaned'),(26,26,'2026-06-01 16:00:00','COMPLETED','Room cleaned after checkout'),(27,27,'2026-06-02 15:00:00','COMPLETED','Room cleaned'),(28,28,'2026-06-03 14:00:00','IN_PROGRESS','Currently being cleaned'),(29,29,'2026-06-04 13:00:00','PENDING','Waiting for cleaner'),(30,30,'2026-06-05 12:00:00','COMPLETED','Room cleaned'),(31,1,'2026-06-06 11:00:00','COMPLETED','Room cleaned after checkout'),(32,2,'2026-06-07 10:00:00','PENDING','Waiting for cleaner'),(33,3,'2026-06-08 09:00:00','IN_PROGRESS','Currently being cleaned'),(34,4,'2026-06-09 08:00:00','COMPLETED','Room cleaned and ready'),(35,5,'2026-06-10 10:00:00','COMPLETED','Maintenance cleaning'),(36,6,'2026-06-11 12:00:00','COMPLETED','Room cleaned after checkout'),(37,7,'2026-06-12 11:00:00','IN_PROGRESS','Currently being cleaned'),(38,8,'2026-06-13 10:00:00','COMPLETED','Room cleaned'),(39,9,'2026-06-14 09:00:00','PENDING','Waiting for cleaner'),(40,10,'2026-06-15 08:00:00','COMPLETED','Room cleaned'),(41,11,'2026-06-16 13:00:00','COMPLETED','Room cleaned after checkout'),(42,12,'2026-06-17 12:00:00','COMPLETED','Room cleaned'),(43,13,'2026-06-18 11:00:00','IN_PROGRESS','Currently being cleaned'),(44,14,'2026-06-19 10:00:00','PENDING','Waiting for cleaner'),(45,15,'2026-06-20 09:00:00','COMPLETED','Room cleaned'),(46,16,'2026-06-21 14:00:00','COMPLETED','Room cleaned after checkout'),(47,17,'2026-06-22 13:00:00','COMPLETED','Room cleaned'),(48,18,'2026-06-23 12:00:00','IN_PROGRESS','Currently being cleaned'),(49,19,'2026-06-24 11:00:00','PENDING','Waiting for cleaner'),(50,20,'2026-06-25 10:00:00','COMPLETED','Room cleaned'),(51,21,'2026-06-26 15:00:00','COMPLETED','Room cleaned after checkout'),(52,22,'2026-06-27 14:00:00','COMPLETED','Room cleaned'),(53,23,'2026-06-28 13:00:00','IN_PROGRESS','Currently being cleaned'),(54,24,'2026-06-29 12:00:00','PENDING','Waiting for cleaner'),(55,25,'2026-06-30 11:00:00','COMPLETED','Room cleaned'),(56,26,'2026-07-01 16:00:00','COMPLETED','Room cleaned after checkout'),(57,27,'2026-07-02 15:00:00','COMPLETED','Room cleaned'),(58,28,'2026-07-03 14:00:00','IN_PROGRESS','Currently being cleaned'),(59,29,'2026-07-04 13:00:00','PENDING','Waiting for cleaner'),(60,30,'2026-07-05 12:00:00','COMPLETED','Room cleaned'),(61,1,'2026-07-06 11:00:00','COMPLETED','Room cleaned after checkout'),(62,2,'2026-07-07 10:00:00','PENDING','Waiting for cleaner'),(63,3,'2026-07-08 09:00:00','IN_PROGRESS','Currently being cleaned'),(64,4,'2026-07-09 08:00:00','COMPLETED','Room cleaned and ready'),(65,5,'2026-07-10 10:00:00','COMPLETED','Maintenance cleaning'),(66,6,'2026-07-11 12:00:00','COMPLETED','Room cleaned after checkout'),(67,7,'2026-07-12 11:00:00','IN_PROGRESS','Currently being cleaned'),(68,8,'2026-07-13 10:00:00','COMPLETED','Room cleaned'),(69,9,'2026-07-14 09:00:00','PENDING','Waiting for cleaner'),(70,10,'2026-07-15 08:00:00','COMPLETED','Room cleaned'),(71,11,'2026-07-16 13:00:00','COMPLETED','Room cleaned after checkout'),(72,12,'2026-07-17 12:00:00','COMPLETED','Room cleaned'),(73,13,'2026-07-18 11:00:00','IN_PROGRESS','Currently being cleaned'),(74,14,'2026-07-19 10:00:00','PENDING','Waiting for cleaner'),(75,15,'2026-07-20 09:00:00','COMPLETED','Room cleaned'),(76,16,'2026-08-01 14:00:00','COMPLETED','Room cleaned after checkout'),(77,17,'2026-08-02 13:00:00','COMPLETED','Room cleaned'),(78,18,'2026-08-03 12:00:00','IN_PROGRESS','Currently being cleaned'),(79,19,'2026-08-04 11:00:00','PENDING','Waiting for cleaner'),(80,20,'2026-08-05 10:00:00','COMPLETED','Room cleaned'),(81,21,'2026-08-06 15:00:00','COMPLETED','Room cleaned after checkout'),(82,22,'2026-08-07 14:00:00','COMPLETED','Room cleaned'),(83,23,'2026-08-08 13:00:00','IN_PROGRESS','Currently being cleaned'),(84,24,'2026-08-09 12:00:00','PENDING','Waiting for cleaner'),(85,25,'2026-08-10 11:00:00','COMPLETED','Room cleaned'),(86,26,'2026-08-11 13:00:00','COMPLETED','Room cleaned after checkout'),(87,27,'2026-08-12 12:00:00','COMPLETED','Room cleaned'),(88,28,'2026-08-13 11:00:00','IN_PROGRESS','Currently being cleaned'),(89,29,'2026-08-14 10:00:00','PENDING','Waiting for cleaner'),(90,30,'2026-08-15 09:00:00','COMPLETED','Room cleaned'),(91,31,'2026-08-16 14:00:00','COMPLETED','Maintenance cleaning'),(92,32,'2026-08-17 13:00:00','COMPLETED','Room cleaned'),(93,33,'2026-08-18 12:00:00','IN_PROGRESS','Currently being cleaned'),(94,34,'2026-08-19 11:00:00','PENDING','Waiting for cleaner'),(95,35,'2026-08-20 10:00:00','COMPLETED','Room cleaned'),(96,36,'2026-08-21 15:00:00','COMPLETED','Deep cleaning'),(97,37,'2026-08-22 14:00:00','COMPLETED','Room cleaned'),(98,38,'2026-08-23 13:00:00','IN_PROGRESS','Currently being cleaned'),(99,39,'2026-08-24 12:00:00','PENDING','Waiting for cleaner'),(100,40,'2026-08-25 11:00:00','COMPLETED','Room cleaned');
/*!40000 ALTER TABLE `room_cleaning_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type` (
  `room_type_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `max_occupancy` int NOT NULL,
  PRIMARY KEY (`room_type_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type`
--

LOCK TABLES `room_type` WRITE;
/*!40000 ALTER TABLE `room_type` DISABLE KEYS */;
INSERT INTO `room_type` VALUES (1,'Single',1),(2,'Double',2),(3,'Twin',2),(4,'Suite',4),(5,'Deluxe Suite',4),(6,'Penthouse',6);
/*!40000 ALTER TABLE `room_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season_rate`
--

DROP TABLE IF EXISTS `season_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `season_rate` (
  `rate_id` int NOT NULL AUTO_INCREMENT,
  `room_type_id` int NOT NULL,
  `season` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price_per_night` decimal(38,2) NOT NULL,
  `valid_from` date NOT NULL,
  `valid_to` date NOT NULL,
  PRIMARY KEY (`rate_id`),
  UNIQUE KEY `uk_season_rate` (`room_type_id`,`season`,`valid_from`,`valid_to`),
  KEY `idx_season_rate_room_type` (`room_type_id`),
  KEY `idx_season_rate_dates` (`valid_from`,`valid_to`),
  CONSTRAINT `season_rate_ibfk_1` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `season_rate`
--

LOCK TABLES `season_rate` WRITE;
/*!40000 ALTER TABLE `season_rate` DISABLE KEYS */;
INSERT INTO `season_rate` VALUES (1,1,'Low',80.00,'2026-01-01','2026-05-31'),(2,1,'High',120.00,'2026-06-01','2026-12-31'),(3,2,'Low',120.00,'2026-01-01','2026-05-31'),(4,2,'High',180.00,'2026-06-01','2026-12-31'),(5,3,'Low',110.00,'2026-01-01','2026-05-31'),(6,3,'High',170.00,'2026-06-01','2026-12-31'),(7,4,'Low',200.00,'2026-01-01','2026-05-31'),(8,4,'High',300.00,'2026-06-01','2026-12-31'),(9,5,'Low',280.00,'2026-01-01','2026-05-31'),(10,5,'High',420.00,'2026-06-01','2026-12-31'),(11,6,'Low',500.00,'2026-01-01','2026-05-31'),(12,6,'High',750.00,'2026-06-01','2026-12-31'),(13,1,'Early Summer',92.00,'2026-06-01','2026-06-15'),(14,1,'Mid Summer',135.00,'2026-06-16','2026-07-31'),(15,1,'Late Summer',125.00,'2026-08-01','2026-08-31'),(16,2,'Early Fall',132.00,'2026-09-01','2026-09-30'),(17,2,'Mid Fall',114.00,'2026-10-01','2026-10-31'),(18,2,'Late Fall',108.00,'2026-11-01','2026-11-30'),(19,3,'Early Winter',154.00,'2026-12-01','2026-12-15'),(20,3,'Christmas',176.00,'2026-12-16','2026-12-31'),(21,3,'New Year',171.00,'2027-01-01','2027-01-03'),(22,4,'Winter Regular',95.00,'2027-01-04','2027-02-28'),(23,4,'Spring Early',125.00,'2027-03-01','2027-03-31'),(24,4,'Spring Peak',150.00,'2027-04-01','2027-04-30'),(25,5,'Spring Late',230.00,'2027-05-01','2027-05-31'),(26,1,'Summer Intro',96.00,'2026-05-20','2026-05-31'),(27,2,'Peak Season',180.00,'2026-07-15','2026-08-15'),(28,3,'Holiday Season',159.50,'2026-11-15','2026-12-31'),(29,4,'Weekend Premium',240.00,'2026-06-01','2026-09-30'),(30,5,'Weekday Discount',170.00,'2026-06-01','2026-09-30'),(31,1,'Business Week',72.00,'2026-01-01','2026-12-31'),(32,2,'Conference Season',132.00,'2026-04-01','2026-04-30'),(33,3,'Wedding Season',148.50,'2026-05-01','2026-10-31'),(34,4,'Group Rate',160.00,'2026-01-01','2026-12-31'),(35,5,'Family Vacation',230.00,'2026-07-01','2026-08-31'),(36,1,'School Break',125.00,'2026-12-01','2027-01-15'),(37,2,'Easter Holiday',156.00,'2026-03-20','2026-04-10'),(38,3,'Thanksgiving',220.00,'2026-11-15','2026-11-30'),(39,4,'Valentine Special',290.00,'2026-02-01','2026-02-28'),(40,5,'Mother Day',297.00,'2026-05-01','2026-05-15'),(41,1,'Father Day',108.00,'2026-06-01','2026-06-15'),(42,2,'Labor Day',150.00,'2026-08-28','2026-09-10'),(43,3,'Back to School',121.00,'2026-08-01','2026-08-31'),(44,4,'Halloween',220.00,'2026-10-25','2026-10-31'),(45,5,'Cyber Monday',255.00,'2026-11-25','2026-11-27'),(46,1,'Black Friday',68.00,'2026-11-23','2026-11-24'),(47,2,'Off Season',96.00,'2026-02-01','2026-04-30'),(48,3,'Shoulder Season',114.50,'2026-05-01','2026-05-31'),(49,4,'Peak Summer',290.00,'2026-06-15','2026-08-31'),(50,5,'Autumn Leaves',230.00,'2026-09-15','2026-10-31'),(51,1,'Winter Wonderland',100.00,'2026-12-01','2027-01-31'),(52,2,'Spring Blossom',132.00,'2027-03-15','2027-04-30'),(53,3,'Festival Time',143.00,'2026-07-01','2026-07-31'),(54,4,'Concert Season',250.00,'2026-06-01','2026-09-30'),(55,5,'Exhibition',299.00,'2026-01-01','2026-12-31'),(56,1,'Art Fair',96.00,'2026-04-01','2026-04-30'),(57,2,'Music Festival',162.00,'2026-07-01','2026-07-31'),(58,3,'Food Festival',137.50,'2026-10-01','2026-10-31'),(59,4,'Wine Festival',260.00,'2026-09-01','2026-09-30'),(60,5,'Sports Event',280.00,'2026-06-01','2026-08-31'),(61,1,'Marathon Week',117.00,'2026-10-15','2026-10-31'),(62,2,'Championship',189.00,'2026-08-01','2026-08-31'),(63,3,'Tournament',148.50,'2026-09-01','2026-09-30'),(64,4,'Game Day',250.00,'2026-06-01','2026-09-30'),(65,5,'Trade Show',220.00,'2026-03-01','2026-03-31'),(66,1,'Convention',91.00,'2026-04-01','2026-04-30'),(67,2,'Seminar',121.00,'2026-05-01','2026-05-31'),(68,3,'Workshop',115.50,'2026-02-01','2026-02-28'),(69,4,'Meeting',190.00,'2026-01-01','2026-12-31'),(70,5,'Corporate Retreat',287.50,'2026-05-01','2026-09-30'),(71,1,'Team Building',88.00,'2026-04-01','2026-10-31'),(72,2,'Incentive Travel',150.00,'2026-06-01','2026-08-31'),(73,3,'Executive Offsite',220.00,'2026-03-01','2026-10-31'),(74,4,'Sales Conference',260.00,'2026-05-01','2026-05-31'),(75,5,'Product Launch',297.00,'2026-06-01','2026-06-30'),(76,1,'Annual Meeting',91.00,'2026-04-01','2026-04-30'),(77,2,'Quarterly Review',126.00,'2026-01-01','2026-12-31'),(78,6,'New Year',185.00,'2026-12-28','2027-01-02'),(79,6,'Spring Break',138.00,'2026-03-15','2026-04-05'),(80,6,'Summer Promo',155.00,'2026-07-01','2026-07-31'),(81,6,'Conference Rate',125.00,'2026-04-01','2026-11-30'),(82,6,'Winter Special',112.00,'2026-01-01','2026-02-28'),(83,6,'Autumn Rate',130.00,'2026-09-01','2026-11-30'),(84,1,'Holiday Extended',105.00,'2026-12-20','2027-01-05'),(85,2,'Spring Extended',138.00,'2026-03-01','2026-03-31'),(86,3,'Summer Start',130.00,'2026-05-25','2026-06-15'),(87,4,'Autumn Peak',255.00,'2026-10-01','2026-11-15'),(88,5,'Winter Holiday',195.00,'2026-12-01','2026-12-31'),(89,1,'New Year Extended',120.00,'2026-12-30','2027-01-02'),(90,2,'Chinese New Year',165.00,'2026-02-07','2026-02-14'),(91,3,'St Patrick Day',145.00,'2026-03-14','2026-03-17'),(92,4,'Pride Month',245.00,'2026-06-01','2026-06-30'),(93,5,'Independence Week',295.00,'2026-07-01','2026-07-07'),(94,1,'Tax Season',85.00,'2026-04-01','2026-04-30'),(95,2,'Summer Finale',140.00,'2026-08-20','2026-09-05'),(96,3,'Fall Foliage',126.50,'2026-09-06','2026-09-30'),(97,4,'Pre-Christmas',235.00,'2026-12-01','2026-12-15'),(98,5,'New Year Eve',300.00,'2026-12-28','2026-12-31'),(99,6,'Summer Peak',160.00,'2026-06-15','2026-08-31'),(100,6,'Holiday Season',172.00,'2026-11-15','2026-12-31'),(101,6,'Business Rate',100.00,'2026-01-01','2026-12-31');
/*!40000 ALTER TABLE `season_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ADMIN','CLEANER','STAFF') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (1,'cleaner1','$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2','CLEANER'),(2,'cleaner2','$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2','CLEANER'),(3,'cleaner3','$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2','CLEANER'),(4,'cleaner4','$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2','CLEANER'),(5,'cleaner5','$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ee9O6oLxV9tIUHm2','CLEANER'),(6,'admin','$2a$10$xJP/qUH87WqjwaCmgRqbNu1gCPCKDRjrnOlLByfAlnvPp1vlg99.K','ADMIN'),(7,'staff','$2a$10$2aWj5X7lt8T0VEUto4XngO5XqMmaRodHmu.KWfZPmG3fj6sZU0C8K','STAFF'),(8,'cleaner6','$2a$10$tjQH63kSvoQxnE3cwzislO4Ggpt.pr0o2Olc9O6lZhNJ8MpaArIP.','CLEANER'),(9,'cleaner7','$2a$10$MSS.dvJKwuZEmvjBY/LJyeHyHHwf9egWXuYAEMqD85p5UlXzx7CiW','CLEANER'),(10,'cleaner8','$2a$10$217C6dPD1WoDTD7N6gdtfu4C4560.pXkoJAEM3ajaJceZZ.W.wCSq','CLEANER'),(11,'cleaner9','$2a$10$4qw5wQArBCov0.5s437wsuo6tE9VfI4fGXm5iDcuwnljy6yZgek6.','CLEANER'),(12,'cleaner10','$2a$10$HXwHrYG9l65BiBrhmLpZ9O1wFmaFuU.2TjY2rfdYFxyX.iqcgKPtO','CLEANER'),(13,'cleaner11','$2a$10$Vi8yQNzq2Zg7uCGAu3qCbuR.pK9ZgVkjNYVOqyMaW29rjyJQRhswe','CLEANER'),(14,'cleaner12','$2a$10$95EoyL1IYspabH1d9yg63u15j2eTcrAg/A9yOFYcIJ3irx2shSiZe','CLEANER'),(15,'cleaner13','$2a$10$Yf8/SeqpyA.Es5PjOMok1Ojf1J75DB9Kie0BDlC2.6r1gMiopWZYC','CLEANER'),(16,'cleaner14','$2a$10$PNYpAb0fFlQSffKqTVREF.jl.gKrd.nN4QQmjrJ1ZNjpzNHz7qL72','CLEANER'),(17,'cleaner15','$2a$10$RdExeUmS.OknPamOqQtqd.xDssOMxQG0H5qcuTPr1wvw2BSISAKma','CLEANER'),(18,'cleaner16','$2a$10$og7/BlURSJliTHulWkGtMu1t85LUCBZEhDiRnBW7iftRQTuZKDWTy','CLEANER'),(19,'cleaner17','$2a$10$uZdj6ezmUVtr1NO3PlKzP.Y8NZYBIqyPVYBKeaHMo7nL0flJImhFq','CLEANER'),(20,'cleaner18','$2a$10$NyCxuAAeIKYiMI4rRu6wPe.oXnMSMkATsvaWjIUw4KMzJVrOqx9OO','CLEANER'),(21,'cleaner19','$2a$10$3vFDIK2SyV28zgOYqDiFzuk4cSbmh418.kPVjTNVmd/Uzph87AMWu','CLEANER'),(22,'cleaner20','$2a$10$DM73m42sTrSb.B4tjfXpaeucdzsmqp.VfLlmB9aX36mzuYC.WboT.','CLEANER');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vw_BillDetails`
--

DROP TABLE IF EXISTS `vw_BillDetails`;
/*!50001 DROP VIEW IF EXISTS `vw_BillDetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_BillDetails` AS SELECT 
 1 AS `bill_id`,
 1 AS `reservation_id`,
 1 AS `reference_no`,
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `opened_at`,
 1 AS `closed_at`,
 1 AS `total_amount`,
 1 AS `is_paid`,
 1 AS `line_item_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_HousekeepingList`
--

DROP TABLE IF EXISTS `vw_HousekeepingList`;
/*!50001 DROP VIEW IF EXISTS `vw_HousekeepingList`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_HousekeepingList` AS SELECT 
 1 AS `room_id`,
 1 AS `room_number`,
 1 AS `room_type`,
 1 AS `clean_status`,
 1 AS `occupancy`,
 1 AS `room_type_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_ReservationDetails`
--

DROP TABLE IF EXISTS `vw_ReservationDetails`;
/*!50001 DROP VIEW IF EXISTS `vw_ReservationDetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_ReservationDetails` AS SELECT 
 1 AS `reservation_id`,
 1 AS `reference_no`,
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `email`,
 1 AS `phone`,
 1 AS `room_type`,
 1 AS `room_number`,
 1 AS `check_in_date`,
 1 AS `check_out_date`,
 1 AS `nights`,
 1 AS `status`,
 1 AS `price_per_night`,
 1 AS `estimated_room_charge`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_BillDetails`
--

/*!50001 DROP VIEW IF EXISTS `vw_BillDetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_BillDetails` AS select `b`.`bill_id` AS `bill_id`,`b`.`reservation_id` AS `reservation_id`,`res`.`reference_no` AS `reference_no`,`g`.`first_name` AS `first_name`,`g`.`last_name` AS `last_name`,`b`.`opened_at` AS `opened_at`,`b`.`closed_at` AS `closed_at`,`b`.`total_amount` AS `total_amount`,`b`.`is_paid` AS `is_paid`,count(`bi`.`bill_item_id`) AS `line_item_count` from (((`bill` `b` join `reservation` `res` on((`b`.`reservation_id` = `res`.`reservation_id`))) join `guest` `g` on((`res`.`guest_id` = `g`.`guest_id`))) left join `bill_item` `bi` on((`b`.`bill_id` = `bi`.`bill_id`))) group by `b`.`bill_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_HousekeepingList`
--

/*!50001 DROP VIEW IF EXISTS `vw_HousekeepingList`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_HousekeepingList` AS select `r`.`room_id` AS `room_id`,`r`.`room_number` AS `room_number`,`rt`.`name` AS `room_type`,`r`.`clean_status` AS `clean_status`,`r`.`room_status` AS `occupancy`,`r`.`room_type_id` AS `room_type_id` from (`room` `r` join `room_type` `rt` on((`r`.`room_type_id` = `rt`.`room_type_id`))) where (`r`.`clean_status` in ('Dirty','Inspected')) order by `r`.`clean_status` desc,`r`.`room_number` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_ReservationDetails`
--

/*!50001 DROP VIEW IF EXISTS `vw_ReservationDetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_ReservationDetails` AS select `res`.`reservation_id` AS `reservation_id`,`res`.`reference_no` AS `reference_no`,`g`.`first_name` AS `first_name`,`g`.`last_name` AS `last_name`,`g`.`email` AS `email`,`g`.`phone` AS `phone`,`rt`.`name` AS `room_type`,`r`.`room_number` AS `room_number`,`res`.`check_in_date` AS `check_in_date`,`res`.`check_out_date` AS `check_out_date`,`res`.`nights` AS `nights`,`res`.`status` AS `status`,`sr`.`price_per_night` AS `price_per_night`,(`res`.`nights` * `sr`.`price_per_night`) AS `estimated_room_charge` from ((((`reservation` `res` join `guest` `g` on((`res`.`guest_id` = `g`.`guest_id`))) join `room_type` `rt` on((`res`.`room_type_id` = `rt`.`room_type_id`))) left join `room` `r` on((`res`.`room_id` = `r`.`room_id`))) left join `season_rate` `sr` on((`res`.`booked_rate_id` = `sr`.`rate_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-09  1:21:17
