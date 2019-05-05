-- MySQL dump 10.13  Distrib 5.7.21, for Win64 (x86_64)
--
-- Host: localhost    Database: miaosha
-- ------------------------------------------------------
-- Server version	5.7.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(16) DEFAULT NULL,
  `goods_title` varchar(64) DEFAULT NULL,
  `goods_img` varchar(64) DEFAULT NULL,
  `goods_detail` longtext,
  `goods_price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `goods_stock` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` VALUES (1,'iphoneX','Apple iphoneX(A1865) 64GB 银色 移动联通电信手机','/img/iphonex.png','Apple iphoneX(A1865) 64GB 银色 移动联通电信手机',8765.00,1000),(2,'华为mate9','华为mate9 4GB+64GB 银色 移动联通电信手机 双卡双待','/img/meta10.png','华为mate9 4GB+64GB 银色 移动联通电信手机 双卡双待',3212.00,10000);
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miaosha_goods`
--

DROP TABLE IF EXISTS `miaosha_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `miaosha_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) DEFAULT NULL,
  `miaosha_price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `stock_count` int(11) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miaosha_goods`
--

LOCK TABLES `miaosha_goods` WRITE;
/*!40000 ALTER TABLE `miaosha_goods` DISABLE KEYS */;
INSERT INTO `miaosha_goods` VALUES (1,1,0.01,7,'2019-03-01 18:22:47','2019-03-02 11:22:57'),(2,2,0.01,9,'2019-01-12 06:23:48','2019-01-15 11:23:53');
/*!40000 ALTER TABLE `miaosha_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miaosha_order`
--

DROP TABLE IF EXISTS `miaosha_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `miaosha_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miaosha_order`
--

LOCK TABLES `miaosha_order` WRITE;
/*!40000 ALTER TABLE `miaosha_order` DISABLE KEYS */;
INSERT INTO `miaosha_order` VALUES (11,15914437906,16,1);
/*!40000 ALTER TABLE `miaosha_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miaosha_user`
--

DROP TABLE IF EXISTS `miaosha_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `miaosha_user` (
  `id` bigint(20) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(32) DEFAULT NULL,
  `salt` varchar(10) DEFAULT NULL,
  `head` varchar(128) DEFAULT NULL,
  `register_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `login_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miaosha_user`
--

LOCK TABLES `miaosha_user` WRITE;
/*!40000 ALTER TABLE `miaosha_user` DISABLE KEYS */;
INSERT INTO `miaosha_user` VALUES (15914437906,'xiaozhou','6e0a7fe692684372437c9e508508990d','1a2b3c4d',NULL,'2018-12-25 10:52:06','2018-12-25 10:52:13',1);
/*!40000 ALTER TABLE `miaosha_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `delivery_addr_id` bigint(20) DEFAULT NULL,
  `goods_name` varchar(16) DEFAULT NULL,
  `goods_count` int(11) DEFAULT NULL,
  `goods_price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `order_channel` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `create_date` datetime DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info`
--

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
INSERT INTO `order_info` VALUES (16,15914437906,1,NULL,'iphoneX',1,0.01,1,0,'2019-01-14 09:50:25',NULL);
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'zzj'),(2,'222');
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

-- Dump completed on 2019-05-05 10:29:53
