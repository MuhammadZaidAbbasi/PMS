-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: healthmanagement
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'Muhammad Somaan Khan','msomaan@gmail.com');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `app_id` varchar(10) NOT NULL,
  `doctor_id` varchar(8) NOT NULL,
  `patient_id` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `time` varchar(15) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `reason` longtext,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `app_id` (`app_id`),
  KEY `fk_appointments_doctor` (`doctor_id`),
  KEY `fk_appointments_patient` (`patient_id`),
  CONSTRAINT `fk_appointments_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`),
  CONSTRAINT `fk_appointments_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES ('A-1589','D-9935','P-2273','2025-05-23','10:00 - 11:00','Cancelled','shoulder pain '),('A-1927','D-9935','P-2588','2025-12-09','09:00-10:00','Approved','kidney pain'),('A-2197','D-5368','P-2588','2025-09-09','15:00-16:00','Cancelled','liver pain'),('A-2589','D-7998','P-2273','2025-05-21','14:00 - 15:00','Approved','ssssssddff'),('A-3235','D-7998','P-2588','2025-05-21','14:00 - 15:00','pending','hghgh'),('A-3519','D-5368','P-2588','2025-05-30','08:00 - 09:00','pending','cv'),('A-4218','D-7998','P-2588','2025-04-15','08:00-09:00','Approved','pain in left shouil;der '),('A-5673','D-9935','P-5225','2025-05-15','09:00 - 10:00','Approved','gjhdfjdf'),('A-5959','D-9935','P-2273','2025-06-06','13:00 - 14:00','Cancelled','just meeting'),('A-6639','D-7998','P-2588','2025-05-21','14:00 - 15:00','pending','nothing'),('A-7352','D-9935','P-5225','2025-05-15','12:00 - 13:00','Approved','wese he'),('A-8136','D-5368','P-2273','2025-05-22','15:00 - 16:00','Approved','Liver Pain from last 3 days'),('A-8239','D-5368','P-5225','2025-05-23','08:00 - 09:00','pending','ko'),('A-8813','D-9935','P-2588','2025-05-24','11:00 - 12:00','Approved','gfghfhjh'),('A-9373','D-5368','P-6525','2025-05-17','10:00 - 11:00','pending','yes'),('A-9581','D-9935','P-2273','2025-05-30','09:00 - 10:00','Rejected','knee pain from last 3 days');
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authorization`
--

DROP TABLE IF EXISTS `authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authorization` (
  `user_id` varchar(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorization`
--

LOCK TABLES `authorization` WRITE;
/*!40000 ALTER TABLE `authorization` DISABLE KEYS */;
INSERT INTO `authorization` VALUES ('D-9572','aali.bsds24seecs@seecs.edu.pk','seecs@123'),('P-3255','ahmed@gmail.com','00000000'),('P-6525','ali@gmail.com','11111111'),('D-9935','doc@gmail.com','0000'),('1','msomaan@gmail.com','somaan'),('P-2273','pat@gmail.com','qwerty'),('P-5225','patient@gmail.com','1212'),('D-5368','saim@12gmail.com','1234abcd'),('P-2588','saim12abc@gmail.com','1111'),('D-7998','taha)234@gmail.com','abbasibrother');
/*!40000 ALTER TABLE `authorization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_messages`
--

DROP TABLE IF EXISTS `chat_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `message_text` text,
  `receiver_id` varchar(8) DEFAULT NULL,
  `sender_id` varchar(8) DEFAULT NULL,
  `seen` tinyint(1) DEFAULT '0',
  `sent_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_messages`
--

LOCK TABLES `chat_messages` WRITE;
/*!40000 ALTER TABLE `chat_messages` DISABLE KEYS */;
INSERT INTO `chat_messages` VALUES (1,'hello','P-2588','D-5368',1,'2025-05-12 20:55:28'),(2,'hello bhai','P-2588','D-5368',1,'2025-05-12 21:01:43'),(3,'how are you','P-2588','D-5368',1,'2025-05-12 21:02:08'),(4,'ni am doing well','D-5368','P-2588',0,'2025-05-13 10:35:38'),(5,'hello zaid from saim','D-9935','P-2588',0,'2025-05-13 12:28:13'),(6,'hi patient','P-2588  ','D-9935',0,'2025-05-13 12:55:47'),(7,'hello doctor','D-9935  ','P-2588',0,'2025-05-13 12:57:26'),(8,'did you got the messagef','D-5368','P-2588',0,'2025-05-13 13:12:35'),(9,'yes','P-2588','D-5368',1,'2025-05-13 13:14:23'),(10,'ok','D-5368','P-2588',0,'2025-05-13 18:22:15'),(11,'ok','P-5225','D-9935',0,'2025-05-14 09:22:44'),(12,'hi','P-2588','D-5368',0,'2025-05-14 09:31:47'),(13,'yes plz','P-5225','D-9935',0,'2025-05-14 09:41:12');
/*!40000 ALTER TABLE `chat_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `doctor_id` varchar(8) NOT NULL,
  `name` varchar(40) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `contact` varchar(15) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `experience` varchar(15) DEFAULT NULL,
  `specialization` varchar(30) DEFAULT NULL,
  `registration_date` date DEFAULT NULL,
  PRIMARY KEY (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES ('D-5368','Saim Abbasi','1994-10-28','0332-2825312','saim@12gmail.com','8 years','Oncologist','2025-05-10'),('D-7998','Taha Abbasi','2015-01-10','0333-7134519','taha)234@gmail.com','10 years','Dermatologist','2025-04-29'),('D-9572','Ahmed Ali','2005-08-25','0331-2701585','aali.bsds24seecs@seecs.edu.pk','10 year','Physiotherapist','2025-05-07'),('D-9935','Muhammad Zaid  Abbasi','2006-01-13','0331-2707515','doc@gmail.com','6 year','physiotherapy','2025-04-29');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_availability`
--

DROP TABLE IF EXISTS `doctor_availability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_availability` (
  `doctor_id` varchar(8) DEFAULT NULL,
  `time` varchar(15) DEFAULT NULL,
  `date` date DEFAULT NULL,
  KEY `fk_availability_doctor` (`doctor_id`),
  CONSTRAINT `fk_availability_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_availability`
--

LOCK TABLES `doctor_availability` WRITE;
/*!40000 ALTER TABLE `doctor_availability` DISABLE KEYS */;
INSERT INTO `doctor_availability` VALUES ('D-9935','08:00 - 09:00','2025-05-18'),('D-9935','17:00 - 18:00','2025-05-31'),('D-5368','11:00 - 12:00','2025-05-23'),('D-5368','10:00 - 11:00','2025-05-17'),('D-5368','08:00 - 09:00','2025-05-23'),('D-5368','10:00 - 11:00','2025-05-23'),('D-5368','14:00 - 15:00','2025-05-23'),('D-5368','13:00 - 14:00','2025-05-21');
/*!40000 ALTER TABLE `doctor_availability` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_patient_assignment`
--

DROP TABLE IF EXISTS `doctor_patient_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_patient_assignment` (
  `AssignmentID` int NOT NULL AUTO_INCREMENT,
  `doctor_id` varchar(8) NOT NULL,
  `patient_id` varchar(8) NOT NULL,
  `app_id` varchar(8) NOT NULL,
  `AssignedDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`AssignmentID`),
  UNIQUE KEY `app_id` (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_patient_assignment`
--

LOCK TABLES `doctor_patient_assignment` WRITE;
/*!40000 ALTER TABLE `doctor_patient_assignment` DISABLE KEYS */;
INSERT INTO `doctor_patient_assignment` VALUES (3,'D-5368','P-2588','A-2197','2025-05-13 10:51:10'),(4,'D-9935','P-2588','A-1927','2025-05-13 12:22:12'),(5,'D-9935','P-2588','A-8813','2025-05-13 12:22:50'),(9,'D-9935','P-2273','A-1589','2025-05-13 17:14:54'),(10,'D-9935','P-2273','A-5959','2025-05-13 18:27:28'),(11,'D-9935','P-5225','A-5673','2025-05-13 21:08:55'),(12,'D-9935','P-5225','A-7352','2025-05-13 21:09:15'),(13,'D-7998','P-2273','A-2589','2025-05-14 09:27:25'),(14,'D-7998','P-2588','A-4218','2025-05-14 09:27:36'),(15,'D-5368','P-2273','A-8136','2025-05-14 09:30:23');
/*!40000 ALTER TABLE `doctor_patient_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `doctor_id` varchar(8) NOT NULL,
  `patient_id` varchar(10) DEFAULT NULL,
  `message` longtext,
  KEY `fk_feedback_doctor` (`doctor_id`),
  KEY `fk_feedback_patient` (`patient_id`),
  CONSTRAINT `fk_feedback_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`),
  CONSTRAINT `fk_feedback_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES ('D-9935','P-2588','recovering fast keep eating healthy food'),('D-9935','P-2588','recovering fast'),('D-9935','P-2273','need to meet in person'),('D-9935','P-2273','sfsdgfdg'),('D-7998','P-2273','hgjhbjhbjh'),('D-5368','P-2273','jkgmuklkliu;');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logs` (
  `timestamp` datetime DEFAULT NULL,
  `log` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs`
--

LOCK TABLES `logs` WRITE;
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `patient_id` varchar(10) NOT NULL,
  `name` varchar(25) NOT NULL,
  `Dob` date DEFAULT NULL,
  `contact` varchar(15) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `blood_group` varchar(5) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `registration_date` date DEFAULT NULL,
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES ('P-2273','Abdullah Shah','2007-05-24','0331-3337415','pat@gmail.com','O+','Male','2025-05-10'),('P-2588','Saim Abbasi','2024-10-23','12-3456','saim12abc@gmail.com','A+','Male','2025-05-01'),('P-3255','Ahmed','2004-07-21','0331-1529658','ahmed@gmail.com','A+','Male','2025-05-17'),('P-5225','Ayesha','2012-11-16','30','patient@gmail.com','B-','female','2025-05-01'),('P-6525','Ali','2007-05-29','0332-2512565','ali@gmail.com','B+','Male','2025-05-16');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `prescription_id` varchar(10) NOT NULL,
  `patient_id` varchar(8) NOT NULL,
  `doctor_id` varchar(8) NOT NULL,
  `medicine_name` varchar(100) NOT NULL,
  `dosage` varchar(100) NOT NULL,
  `schedule` varchar(100) NOT NULL,
  `date_issued` date NOT NULL,
  PRIMARY KEY (`prescription_id`),
  KEY `fk_prescription_patient` (`patient_id`),
  KEY `fk_prescription_doctor` (`doctor_id`),
  CONSTRAINT `fk_prescription_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`),
  CONSTRAINT `fk_prescription_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES ('Med-3695','P-2588','D-9935','Panadol','2 tablets','3 times a day','2025-05-13'),('Med-5731','P-2273','D-7998','jbjbmn','65656','knkjn','2025-05-14'),('Med-7655','P-2273','D-9935','adad','ada','adfaffsf','2025-05-14'),('Med-8981','P-2273','D-5368','jkm,.n.','nlkkm',',mknlk','2025-05-14');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vitals`
--

DROP TABLE IF EXISTS `vitals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vitals` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `temperature` double DEFAULT NULL,
  `systolic` double DEFAULT NULL,
  `diastolic` double DEFAULT NULL,
  `heart_rate` double DEFAULT NULL,
  `oxygen` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vitals` (`patient_id`),
  CONSTRAINT `fk_vitals` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vitals`
--

LOCK TABLES `vitals` WRITE;
/*!40000 ALTER TABLE `vitals` DISABLE KEYS */;
INSERT INTO `vitals` VALUES (3,'P-2273','2025-05-10 23:22:59',100.2,22,120,12,85),(4,'P-2273','2025-05-10 23:23:45',95,120,80,22,95),(5,'P-2273','2025-05-10 23:24:00',411,11,11,11,225),(6,'P-2273','2025-05-11 00:12:32',36,120,80,72,95),(7,'P-2273','2025-05-11 00:12:57',120,44,52,117,58),(8,'P-2273','2025-05-11 02:22:37',21,121,100,15,45),(9,'P-2273','2025-05-12 02:20:05',35,115,75,70,95),(10,'P-2588','2025-05-12 15:01:09',36.5,120,80,76,97),(11,'P-2588','2025-05-12 15:01:42',39,110,85,95,88),(12,'P-2588','2025-05-12 15:15:38',55,75,110,85,35),(13,'P-2588','2025-05-12 15:17:12',35.5,120,80,76,92),(14,'P-2273','2025-05-12 20:54:18',12,10,11,12,15),(15,'P-5225','2025-05-14 01:33:37',35.5,120,110,95,95),(16,'P-5225','2025-05-14 01:33:37',30,90,80,72,75),(17,'P-5225','2025-05-14 01:33:37',20,30,55,25,37),(18,'P-5225','2025-05-14 01:33:37',50,175,85,55,80),(19,'P-5225','2025-05-14 02:11:03',35.5,120,110,95,95),(20,'P-5225','2025-05-14 02:11:03',30,90,80,72,75),(21,'P-5225','2025-05-14 02:11:03',20,30,55,25,37),(22,'P-5225','2025-05-14 02:11:03',50,175,85,55,80),(23,'P-5225','2025-05-14 14:19:31',15,120,50,25,12),(24,'P-5225','2025-05-14 14:19:52',35.5,120,110,95,95),(25,'P-5225','2025-05-14 14:19:52',30,90,80,72,75),(26,'P-5225','2025-05-14 14:19:52',20,30,55,25,37),(27,'P-5225','2025-05-14 14:19:52',50,175,85,55,80);
/*!40000 ALTER TABLE `vitals` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-17  4:43:22
