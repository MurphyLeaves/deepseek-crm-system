-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: crm_db
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
-- Current Database: `crm_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `crm_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `crm_db`;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `company_name` varchar(100) NOT NULL COMMENT '企业名称',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '企业地址',
  `industry` varchar(50) DEFAULT NULL COMMENT '所属行业',
  `employee_scale` varchar(30) DEFAULT NULL COMMENT '企业规模',
  `source` varchar(30) DEFAULT NULL COMMENT '客户来源',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注说明',
  `ai_priority_level` char(1) DEFAULT NULL COMMENT 'AI评估等级(A/B/C)',
  `ai_priority_score` int DEFAULT NULL COMMENT 'AI评估分数(1-10)',
  `ai_reason` varchar(500) DEFAULT NULL COMMENT 'AI评估理由',
  `ai_eval_time` datetime DEFAULT NULL COMMENT 'AI评估时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='B端客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (2,'深圳智汇制造有限公司','李娜','13800138002',NULL,NULL,'制造业','200-500人','展会',NULL,'B',6,'客户为制造业200-500人规模，属于中等体量，展会来源线索有一定意向但信息不全。当前无竞品及跟进细节，近期成交概率不明，建议先通过电话或邮件了解具体需求及预算，再确定是否提升优先级。','2026-07-25 20:57:27','2026-07-03 23:05:09','2026-07-03 23:05:09'),(4,'测试新增公司','李四','13900001111',NULL,NULL,'测试行业',NULL,NULL,NULL,'C',2,'当前建议：暂缓跟进，先补充基础信息。评估理由：客户信息严重缺失，包括企业规模、客户来源等关键字段均未提供，且备注为空，无法判断其潜在需求、预算匹配度或决策链。在B2B SaaS销售中，缺乏这些信息会导致无法制定有效的跟进策略，优先投入资源风险较高。建议先通过公开渠道或初步触达获取企业规模、行业痛点及来源渠道，再重新评估优先级。','2026-07-12 18:19:27','2026-07-05 11:29:32','2026-07-05 11:29:32'),(5,'张家屯教育集团','钟师傅','12580003987',NULL,NULL,'教育培训',NULL,NULL,NULL,'C',2,'客户信息严重不足：未提供企业规模、客户来源及任何跟进历史，无法评估客户体量、当前跟进情况和竞品动态。建议先通过渠道补充基础信息（如企业人数、当前使用工具、预算等），否则转化概率极低，暂列为低优先级跟进。','2026-07-25 20:56:39','2026-07-05 11:37:50','2026-07-05 11:37:50'),(7,'测试公司1','联系人1','10986996100',NULL,NULL,'金融业','10-50人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-22 23:35:45','2026-07-02 23:35:45'),(8,'测试公司2','联系人2','10436013566',NULL,NULL,'制造业','10-50人','展会',NULL,NULL,NULL,NULL,NULL,'2026-07-05 23:35:45','2026-07-01 23:35:45'),(9,'测试公司3','联系人3','10422110009',NULL,NULL,'零售业','500人以上','线上',NULL,'B',6,'该客户为零售业、500人以上规模，线上来源，具备一定潜力和体量，但缺少核心指标（如订单金额、近期成交概率、竞品情况等）的具体信息。当前建议：优先安排初步沟通，了解客户当前客服工单使用情况、痛点及预算，同时收集竞品动态，以进一步评估成交可能性。','2026-07-25 20:57:13','2026-07-08 23:35:45','2026-07-03 23:35:45'),(10,'测试公司4','联系人4','10856402103',NULL,NULL,'金融业','10人以下','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-15 23:35:45','2026-06-30 23:35:45'),(11,'测试公司5','联系人5','10009725544',NULL,NULL,'制造业','500人以上','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-19 23:35:45','2026-07-08 23:35:45'),(12,'测试公司6','联系人6','10401345135',NULL,NULL,'零售业','500人以上','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-15 23:35:45','2026-07-08 23:35:45'),(13,'测试公司7','联系人7','10846477838',NULL,NULL,'信息技术','500人以上','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-19 23:35:45','2026-07-04 23:35:45'),(14,'测试公司8','联系人8','10167929642',NULL,NULL,'金融业','10-50人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-09 23:35:45','2026-06-29 23:35:45'),(15,'测试公司9','联系人9','10599337137',NULL,NULL,'制造业','50-200人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-12 23:35:45','2026-07-01 23:35:45'),(16,'测试公司10','联系人10','10972940414',NULL,NULL,'金融业','50-200人','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-12 23:35:45','2026-07-08 23:35:45'),(17,'测试公司11','联系人11','10363267531',NULL,NULL,'零售业','500人以上','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-24 23:35:45','2026-07-06 23:35:45'),(18,'测试公司12','联系人12','10022368455',NULL,NULL,'制造业','10人以下','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-29 23:35:45','2026-07-08 23:35:45'),(19,'测试公司13','联系人13','10128359305',NULL,NULL,'金融业','200-500人','展会','','B',6,'客户为金融行业，预算和合规需求通常较强，企业规模200-500人属于中型体量，具备一定购买力。展会来源表明客户有主动了解意向，但暂无跟进记录和竞品信息，需尽快联系以明确具体需求、决策流程及预算情况，同时了解是否已有竞品接触。建议安排首轮电话或线上沟通，重点确认订单金额预期和近期成交概率，再决定是否升级优先级。','2026-07-25 20:57:13','2026-07-06 23:35:45','2026-07-03 23:35:45'),(20,'测试公司14','联系人14','10261723222',NULL,NULL,'零售业','10-50人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-28 23:35:45','2026-07-06 23:35:45'),(21,'测试公司15','联系人15','10321808375',NULL,NULL,'零售业','10人以下','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-28 23:35:45','2026-07-02 23:35:45'),(22,'测试公司16','联系人16','10399895736',NULL,NULL,'零售业','50-200人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-07-04 23:35:45','2026-07-03 23:35:45'),(23,'测试公司17','联系人17','10140209824',NULL,NULL,'信息技术','10-50人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-22 23:35:45','2026-07-01 23:35:45'),(24,'测试公司18','联系人18','10247729643',NULL,NULL,'零售业','200-500人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-11 23:35:45','2026-07-03 23:35:45'),(25,'测试公司19','联系人19','10879527757',NULL,NULL,'零售业','10-50人','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-29 23:35:45','2026-07-07 23:35:45'),(26,'测试公司20','联系人20','10888413270',NULL,NULL,'零售业','500人以上','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-14 23:35:45','2026-07-08 23:35:45'),(27,'测试公司21','联系人21','10695053887',NULL,NULL,'制造业','200-500人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-18 23:35:45','2026-07-08 23:35:45'),(28,'测试公司22','联系人22','10158866776',NULL,NULL,'金融业','200-500人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-16 23:35:45','2026-07-05 23:35:45'),(29,'测试公司23','联系人23','10474451189',NULL,NULL,'制造业','10-50人','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-30 23:35:45','2026-07-07 23:35:45'),(30,'测试公司24','联系人24','10053540426',NULL,NULL,'金融业','10-50人','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-20 23:35:45','2026-07-03 23:35:45'),(31,'测试公司25','联系人25','10892801205',NULL,NULL,'零售业','10-50人','展会',NULL,'C',3,'客户体量较小（10-50人），属于微型零售企业，客服工单需求通常较简单且预算有限；展会来源但无明确意向或关键人信息，近期成交概率低；订单金额预期较低，建议暂缓投入高成本跟进，可先通过邮件或标准化资料触达，观察反馈后再决定是否深入。','2026-07-25 20:57:25','2026-07-02 23:35:45','2026-07-02 23:35:45'),(32,'测试公司26','联系人26','10685652065',NULL,NULL,'制造业','10人以下','线上',NULL,'C',2,'客户企业规模仅10人以下，属于微型企业，通常缺乏专职客服团队或对工单软件需求较弱，订单金额预期极低。线上来源未提供进一步线索，近期成交概率低。建议暂缓主动跟进，可纳入长尾培育列表，通过自动化邮件或in-app消息保持品牌曝光，待客户体量增长或主动咨询时再重点跟进。','2026-07-25 20:57:25','2026-07-01 23:35:45','2026-07-08 23:35:45'),(33,'测试公司27','联系人27','10296102889',NULL,NULL,'制造业','10-50人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-25 23:35:45','2026-07-07 23:35:45'),(34,'测试公司28','联系人28','10228751279',NULL,NULL,'零售业','10-50人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-20 23:35:45','2026-07-03 23:35:45'),(35,'测试公司29','联系人29','10796786915',NULL,NULL,'制造业','50-200人','线上',NULL,'C',3,'客户为制造业中小企业（50-200人），体量中等偏小，客服团队规模有限，订单金额潜力一般。当前无竞品信息及近期跟进情况，客户来源为线上，缺乏主动意向信号。建议先通过初步沟通了解需求，无需投入高优先级资源，可列为长线培育或批量触达对象。','2026-07-25 20:57:25','2026-06-30 23:35:45','2026-07-07 23:35:45'),(36,'测试公司30','联系人30','10650254383',NULL,NULL,'零售业','200-500人','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-11 23:35:45','2026-06-30 23:35:45'),(37,'测试公司31','联系人31','10656828117',NULL,NULL,'金融业','10人以下','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-15 23:35:45','2026-07-07 23:35:45'),(38,'测试公司32','联系人32','10195959120',NULL,NULL,'金融业','200-500人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-10 23:35:45','2026-07-05 23:35:45'),(39,'趣然旗舰店','小埋','17708251614','781091791@qq.com','滨江海亮大厦','电子商务','50-200人','电话挖掘','1.存在竞品班牛，报价比我们贵一万。2.已经添加操作人和决策者。3.下周一开启试用，核心功能是ai辅助建单。流失，对方给了更高的报价，并且知名度更高','C',2,'客户已明确流失，选择了竞品班牛（报价更高但知名度更高），且原定试用计划已终止。当前无跟进价值，建议放弃投入资源，可作为后续竞品案例复盘或定期回访观察是否有转机。','2026-07-25 20:56:54','2026-07-06 23:35:45','2026-07-03 23:35:45'),(40,'测试公司34','联系人34','10711091586',NULL,NULL,'零售业','500人以上','推荐',NULL,NULL,NULL,NULL,NULL,'2026-06-27 23:35:45','2026-07-02 23:35:45'),(41,'测试公司35','联系人35','10108340806',NULL,NULL,'金融业','50-200人','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-14 23:35:45','2026-07-07 23:35:45'),(42,'测试公司36','联系人36','10145163915',NULL,NULL,'制造业','50-200人','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-28 23:35:45','2026-07-02 23:35:45'),(43,'测试公司37','联系人37','10229930923',NULL,NULL,'信息技术','10人以下','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-19 23:35:45','2026-07-02 23:35:45'),(44,'测试公司38','联系人38','10317319128',NULL,NULL,'金融业','10人以下','展会',NULL,NULL,NULL,NULL,NULL,'2026-07-03 23:35:45','2026-07-03 23:35:45'),(45,'测试公司39','联系人39','10087721710',NULL,NULL,'零售业','500人以上','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-19 23:35:45','2026-07-06 23:35:45'),(46,'测试公司40','联系人40','10356500360',NULL,NULL,'信息技术','10人以下','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-17 23:35:45','2026-07-07 23:35:45'),(47,'测试公司41','联系人41','10402333713',NULL,NULL,'金融业','10人以下','展会',NULL,NULL,NULL,NULL,NULL,'2026-07-08 23:35:45','2026-07-02 23:35:45'),(48,'测试公司42','联系人42','10321155727',NULL,NULL,'金融业','10人以下','展会',NULL,'C',2,'客户为10人以下的金融业企业，规模极小，通常缺乏专职客服团队，对工单软件需求弱；仅通过展会获取，无其他有效线索，近期成交概率极低。建议暂不投入资源，可列入长期培育名单，后续观察是否有规模增长或明确需求出现。','2026-07-25 20:57:25','2026-07-02 23:35:45','2026-07-02 23:35:45'),(49,'测试公司43','联系人43','10753140317',NULL,NULL,'金融业','10-50人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-21 23:35:45','2026-07-08 23:35:45'),(50,'测试公司44','联系人44','10306099440',NULL,NULL,'金融业','200-500人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-07-03 23:35:45','2026-07-04 23:35:45'),(51,'测试公司45','联系人45','10467929339',NULL,NULL,'信息技术','10人以下','展会',NULL,'C',3,'公司规模10人以下，客服团队和工单量预计有限，付费能力与订单金额偏低；客户来源为展会但备注无明确需求、预算、决策人或采购时间，且当前跟进情况及竞品信息缺失，近期成交概率较低。建议先低优先级培育，补充客服团队规模、工单量、痛点、预算和竞品信息，若无明确客服管理需求或扩张计划，暂不投入大量销售资源。','2026-10-22 20:47:55','2026-06-29 23:35:45','2026-06-30 23:35:45'),(52,'测试公司46','联系人46','10333268943',NULL,NULL,'信息技术','50-200人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-30 23:35:45','2026-06-29 23:35:45'),(53,'测试公司47','联系人47','10804399912',NULL,NULL,'信息技术','500人以上','展会',NULL,NULL,NULL,NULL,NULL,'2026-06-27 23:35:45','2026-07-01 23:35:45'),(54,'测试公司48','联系人48','10768960614',NULL,NULL,'金融业','10-50人','展会',NULL,NULL,NULL,NULL,NULL,'2026-07-03 23:35:45','2026-07-07 23:35:45'),(55,'测试公司49','联系人49','10416695475',NULL,NULL,'金融业','10-50人','线上',NULL,NULL,NULL,NULL,NULL,'2026-06-25 23:35:45','2026-07-08 23:35:45'),(56,'测试公司50','联系人50','10777375131',NULL,NULL,'零售业','200-500人','推荐',NULL,NULL,NULL,NULL,NULL,'2026-07-03 23:35:45','2026-07-05 23:35:45'),(57,'花果山水果运营有限公司','孙悟空','18000000000','88888888@tianting.com','西天','果蔬售卖','500人以上','客户推荐','展会获取。新兴企业，有自己的生产基地，目前面临电商转型','A',8,'客户为500人以上果蔬企业，规模较大且有自建基地，正处于电商转型期，客服需求可能快速增加；客户推荐叠加展会获取，信任基础较好，近期跟进价值较高。但竞品和具体预算尚不明确，建议优先安排深度沟通，结合电商场景输出客服工单解决方案，同时摸清竞品布局与决策流程，以加速推进。','2026-08-06 23:02:58','2026-08-06 23:02:47','2026-08-06 23:02:47');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_label`
--

DROP TABLE IF EXISTS `customer_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_label` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `label_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '标注时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_label` (`customer_id`,`label_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户-标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_label`
--

LOCK TABLES `customer_label` WRITE;
/*!40000 ALTER TABLE `customer_label` DISABLE KEYS */;
INSERT INTO `customer_label` VALUES (1,4,1,'2026-07-07 22:31:31'),(2,4,2,'2026-07-07 22:31:31'),(3,4,3,'2026-07-07 22:31:31'),(4,5,1,'2026-07-07 23:02:15'),(7,2,5,'2026-07-07 23:02:51'),(8,5,2,'2026-07-08 21:51:31'),(12,5,6,'2026-07-08 21:51:31'),(18,5,10,'2026-07-08 22:34:43'),(23,4,4,'2026-07-08 22:41:02'),(24,4,9,'2026-07-08 22:41:02'),(25,4,10,'2026-07-08 22:41:02'),(26,4,6,'2026-07-08 23:24:40'),(58,52,12,'2026-07-06 23:35:45'),(59,22,3,'2026-07-07 23:35:45'),(60,14,3,'2026-07-08 23:35:45'),(61,7,1,'2026-07-08 23:35:45'),(62,19,2,'2026-07-06 23:35:45'),(63,21,1,'2026-07-08 23:35:45'),(64,37,8,'2026-07-08 23:35:45'),(65,20,11,'2026-07-08 23:35:45'),(66,4,12,'2026-07-07 23:35:45'),(67,15,10,'2026-07-06 23:35:45'),(68,29,7,'2026-07-07 23:35:45'),(69,10,10,'2026-07-07 23:35:45'),(70,38,11,'2026-07-06 23:35:45'),(71,18,9,'2026-07-08 23:35:45'),(72,25,10,'2026-07-08 23:35:45'),(73,50,6,'2026-07-08 23:35:45'),(74,13,5,'2026-07-06 23:35:45'),(75,43,9,'2026-07-06 23:35:45'),(76,35,3,'2026-07-06 23:35:45'),(77,36,8,'2026-07-07 23:35:45'),(78,33,1,'2026-07-06 23:35:45'),(79,31,5,'2026-07-06 23:35:45'),(89,37,4,'2026-07-08 23:35:48'),(90,21,4,'2026-07-07 23:35:48'),(91,23,3,'2026-07-07 23:35:48'),(92,25,5,'2026-07-08 23:35:48'),(93,36,10,'2026-07-07 23:35:48'),(94,17,2,'2026-07-08 23:35:48'),(95,22,9,'2026-07-08 23:35:48'),(96,27,12,'2026-07-07 23:35:48'),(97,15,9,'2026-07-08 23:35:48'),(98,19,11,'2026-07-07 23:35:48'),(99,48,2,'2026-07-07 23:35:48'),(100,19,5,'2026-07-12 18:09:12'),(101,19,8,'2026-07-12 18:09:12'),(102,5,8,'2026-07-12 18:22:03');
/*!40000 ALTER TABLE `customer_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `label` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` varchar(30) NOT NULL COMMENT '标签名称（唯一）',
  `category` varchar(30) NOT NULL COMMENT '标签分类（如：行业、规模、意向等）',
  `color` varchar(20) DEFAULT '#409EFF' COMMENT '标签颜色（16进制色值）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
INSERT INTO `label` VALUES (1,'信息技术','行业分类','#409EFF','2026-07-06 22:22:27'),(2,'制造业','行业分类','#67C23A','2026-07-06 22:22:27'),(3,'金融业','行业分类','#E6A23C','2026-07-06 22:22:27'),(4,'零售业','行业分类','#F56C6C','2026-07-06 22:22:27'),(5,'高意向','意向程度','#F56C6C','2026-07-06 22:22:27'),(6,'中意向','意向程度','#E6A23C','2026-07-06 22:22:27'),(7,'低意向','意向程度','#909399','2026-07-06 22:22:27'),(8,'大客户','客户等级','#409EFF','2026-07-06 22:22:27'),(9,'VIP','客户等级','#E6A23C','2026-07-06 22:22:27'),(10,'已成交','跟进阶段','#67C23A','2026-07-06 22:22:27'),(11,'洽谈中','跟进阶段','#409EFF','2026-07-06 22:22:27'),(12,'线索','跟进阶段','#909399','2026-07-06 22:22:27');
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'ai_eval_prompt','\"你是一位资深的B2Bsaas软件销售管理专家，销售的产品是客服工单软件，客服群体主要是客服主管，评分主要反馈跟进价值，分为客户体量、当前跟进情况、竞品情况（核心指标订单金额，近期成交概率）。请根据以下客户信息评估该客户的跟进优先级，其中reason要根据目前状况写出当前建议和评估理由。请严格按照JSON格式返回，包含priorityLevel(值为A/B/C)、priorityScore(值为1-10的整数)和reason(评估理由)。\\n客户信息：\\n公司名称：{companyName}\\n所属行业：{industry}\\n企业规模：{employeeScale}\\n客户来源：{source}\\n备注：{remark}\"','2026-07-12 15:51:23');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码（加密后）',
  `role` varchar(20) DEFAULT 'user' COMMENT '角色：admin/user',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','$2a$10$HwvJk4uMffuUcMnUrja5Ker8NenGhqvlJLU9M1bhWX11hLsaRvuHq','admin','管理员','2026-06-28 15:07:20','2026-10-22 20:50:53');
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

-- Dump completed on 2026-10-22 21:31:58
