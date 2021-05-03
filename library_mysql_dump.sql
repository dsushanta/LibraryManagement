-- MySQL dump 10.13  Distrib 8.0.23, for osx10.15 (x86_64)
--
-- Host: localhost    Database: LIBRARY_MANAGEMENT
-- ------------------------------------------------------
-- Server version	8.0.23

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
-- Table structure for table `LI_BOOK_COPIES`
--

DROP TABLE IF EXISTS `LI_BOOK_COPIES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LI_BOOK_COPIES` (
  `CopyId` int NOT NULL AUTO_INCREMENT,
  `BookId` int NOT NULL,
  `IsIssued` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CopyId`),
  KEY `BookId` (`BookId`),
  CONSTRAINT `LI_BOOK_COPIES_ibfk_1` FOREIGN KEY (`BookId`) REFERENCES `LI_BOOKS` (`BookId`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LI_BOOK_COPIES`
--

LOCK TABLES `LI_BOOK_COPIES` WRITE;
/*!40000 ALTER TABLE `LI_BOOK_COPIES` DISABLE KEYS */;
INSERT INTO `LI_BOOK_COPIES` VALUES (3,2,0),(4,3,1),(5,4,1),(6,5,0),(7,6,1),(8,7,0),(9,8,0),(10,9,1),(11,10,0),(12,11,0),(15,14,1),(18,15,1),(19,16,1),(20,17,1),(21,17,1),(22,18,0),(23,17,1),(24,19,0),(38,4,1),(39,32,1),(40,32,1),(41,33,1),(42,33,0),(43,33,0),(44,33,1),(45,19,0),(46,34,0),(47,35,0),(48,36,0);
/*!40000 ALTER TABLE `LI_BOOK_COPIES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LI_BOOK_LIFE_CYCLE`
--

DROP TABLE IF EXISTS `LI_BOOK_LIFE_CYCLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LI_BOOK_LIFE_CYCLE` (
  `BookIssueId` int NOT NULL AUTO_INCREMENT,
  `CopyId` int NOT NULL,
  `BookId` int NOT NULL,
  `UserName` varchar(255) NOT NULL,
  `IsReturned` tinyint(1) NOT NULL DEFAULT '0',
  `IsRenewed` tinyint(1) NOT NULL DEFAULT '0',
  `IssueDate` date NOT NULL,
  `ExpectedReturnDate` date NOT NULL,
  `ActualReturnDate` date DEFAULT NULL,
  `RenewDate` date DEFAULT NULL,
  `Fine` int NOT NULL DEFAULT '0',
  `FineCleared` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`BookIssueId`),
  KEY `CopyId` (`CopyId`),
  KEY `BookId` (`BookId`),
  KEY `UserName` (`UserName`),
  CONSTRAINT `LI_BOOK_LIFE_CYCLE_ibfk_1` FOREIGN KEY (`CopyId`) REFERENCES `LI_BOOK_COPIES` (`CopyId`),
  CONSTRAINT `LI_BOOK_LIFE_CYCLE_ibfk_2` FOREIGN KEY (`BookId`) REFERENCES `LI_BOOKS` (`BookId`),
  CONSTRAINT `LI_BOOK_LIFE_CYCLE_ibfk_3` FOREIGN KEY (`UserName`) REFERENCES `LI_USERS` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LI_BOOK_LIFE_CYCLE`
--

LOCK TABLES `LI_BOOK_LIFE_CYCLE` WRITE;
/*!40000 ALTER TABLE `LI_BOOK_LIFE_CYCLE` DISABLE KEYS */;
INSERT INTO `LI_BOOK_LIFE_CYCLE` VALUES (1,20,17,'chandler',1,1,'2019-08-13','2019-08-08','2019-08-13','2019-08-13',200,1),(2,19,16,'chandler',1,0,'2019-08-13','2019-08-08','2019-08-13',NULL,100,1),(3,20,17,'ross',0,0,'2019-08-13','2019-08-08',NULL,NULL,0,1),(4,3,2,'chandler',1,1,'2019-08-13','2019-08-08','2019-08-14','2019-08-13',220,1),(5,11,10,'sushantaD',1,0,'2019-08-14','2019-08-09','2019-08-16',NULL,140,1),(6,24,19,'sushantaD',1,1,'2019-08-14','2019-08-10','2019-08-16','2019-08-15',240,1),(7,12,11,'sushantaD',1,1,'2019-08-15','2019-08-10','2019-08-16','2019-08-15',220,1),(8,18,15,'sushantaD',1,1,'2019-08-15','2019-08-11','2019-08-16','2019-08-16',220,1),(9,22,18,'sushantaD',1,1,'2019-08-15','2019-08-11','2019-08-16','2019-08-16',220,1),(10,8,7,'chandler',1,1,'2019-08-15','2019-08-10','2019-08-16','2019-08-15',220,1),(11,3,2,'udrocks',1,0,'2019-08-15','2019-08-10','2019-08-16',NULL,120,1),(12,15,14,'udrocks',1,1,'2019-08-15','2019-08-11','2019-08-16','2019-08-16',220,1),(13,5,4,'udrocks',0,0,'2019-08-15','2019-08-10',NULL,NULL,0,1),(14,19,16,'udrocks',0,1,'2019-08-15','2019-08-11',NULL,'2019-08-16',120,1),(15,21,17,'chandler',1,1,'2019-08-15','2019-08-10','2019-08-16','2019-08-15',220,1),(16,23,17,'chandler',1,1,'2019-08-15','2019-08-11','2019-08-16','2019-08-16',220,1),(17,18,15,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(18,7,6,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(19,4,3,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(20,4,3,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(21,24,19,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(22,22,18,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(23,4,3,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(24,6,5,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(25,22,18,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(26,24,19,'sushantaD',1,1,'2019-08-16','2019-08-11','2019-08-17','2019-08-16',220,1),(27,4,3,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(28,21,17,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(29,6,5,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-16',NULL,100,1),(30,22,18,'sushantaD',1,0,'2019-08-16','2019-08-11','2019-08-18',NULL,140,1),(32,18,15,'sushantaD',1,1,'2019-08-16','2019-08-11','2019-08-17','2019-08-16',140,1),(33,6,5,'sushantaD',1,1,'2019-08-16','2019-08-15','2019-08-17','2019-08-16',50,1),(34,4,3,'sushantaD',1,0,'2019-08-16','2019-08-15','2019-08-16',NULL,20,1),(37,38,4,'chandler',0,0,'2019-08-16','2019-08-11',NULL,NULL,0,1),(38,39,32,'chandler',0,0,'2019-08-16','2019-08-11',NULL,NULL,0,1),(39,10,9,'chandler',1,0,'2019-08-17','2019-08-12','2019-08-17',NULL,100,1),(40,41,33,'chandler',0,0,'2019-08-17','2019-08-12',NULL,NULL,0,1),(41,42,33,'chandler',1,0,'2019-08-17','2019-08-12','2019-08-17',NULL,100,1),(42,43,33,'udrocks',1,0,'2019-08-17','2019-08-12','2019-08-17',NULL,100,1),(43,44,33,'udrocks',0,0,'2019-08-17','2019-08-12',NULL,NULL,0,1),(44,7,6,'sushantaD',1,1,'2019-08-17','2019-08-13','2019-08-19','2019-08-18',240,0),(45,6,5,'sushantaD',1,0,'2019-08-17','2019-08-12','2019-08-18',NULL,120,1),(46,18,15,'chandler',0,0,'2019-08-17','2019-08-12',NULL,NULL,0,1),(47,3,2,'sushantaD',1,1,'2019-08-17','2019-08-13','2019-08-19','2019-08-18',240,0),(48,40,32,'chandler',0,0,'2019-08-17','2019-08-12',NULL,NULL,0,1),(49,4,3,'udrocks',0,0,'2019-08-17','2019-08-12',NULL,NULL,0,1),(50,21,17,'anirband',0,0,'2019-08-17','2019-08-12',NULL,NULL,0,1),(51,15,14,'anirband',0,0,'2019-08-17','2019-08-12',NULL,NULL,0,1),(52,10,9,'sushantaD',0,1,'2019-08-18','2019-08-13',NULL,'2019-08-18',100,1),(53,11,10,'sushantaD',1,0,'2019-08-18','2019-08-13','2019-08-19',NULL,120,0),(54,8,7,'sushantaD',1,0,'2019-08-18','2019-08-13','2019-08-19',NULL,120,0),(55,42,33,'phoebe',1,0,'2019-08-19','2019-08-14','2021-02-21',NULL,11140,0),(56,7,6,'joey',1,0,'2021-02-21','2021-02-16','2021-02-21',NULL,100,1),(57,7,6,'joey',0,0,'2021-02-21','2021-02-16',NULL,NULL,0,1),(58,23,17,'joey',0,0,'2021-02-21','2021-02-16',NULL,NULL,0,1);
/*!40000 ALTER TABLE `LI_BOOK_LIFE_CYCLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LI_BOOKS`
--

DROP TABLE IF EXISTS `LI_BOOKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LI_BOOKS` (
  `BookId` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) NOT NULL,
  `Description` text NOT NULL,
  `Author` varchar(255) NOT NULL,
  `Genre` varchar(255) NOT NULL,
  `Likes` int DEFAULT NULL,
  `Available` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`BookId`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LI_BOOKS`
--

LOCK TABLES `LI_BOOKS` WRITE;
/*!40000 ALTER TABLE `LI_BOOKS` DISABLE KEYS */;
INSERT INTO `LI_BOOKS` VALUES (1,'Man-Eaters of Kumaon','Man-Eaters of Kumaon is a 1944 book written by hunter-naturalist Jim Corbett','Jim Corbett','Adventure',NULL,1),(2,'Three Men in a Boat','Three Men in a Boat (To Say Nothing of the Dog),[Note 1] published in 1889,[1] is a humorous account by English writer Jerome K. Jerome of a two-week boating holiday on the Thames from Kingston upon Thames to Oxford and back to Kingston. The book was initially intended to be a serious travel guide,[2] with accounts of local history along the route, but the humorous elements took over to the point where the serious and somewhat sentimental passages seem a distraction to the comic novel. One of the most praised things about Three Men in a Boat is how undated it appears to modern readers – the jokes have been praised as fresh and witty','Jerome Klapka Jerome','Comedy',NULL,1),(3,'A Confederacy of Dunces','A Confederacy of Dunces is a picaresque novel by American novelist John Kennedy Toole which reached publication in 1980, eleven years after Toole\'s suicide','John Kennedy Toole','Comedy',NULL,0),(4,'The Jungle Book','The Jungle Book (1894) is a collection of stories by the English author Rudyard Kipling. Most of the characters are animals such as Shere Khan the tiger and Baloo the bear, though a principal character is the boy or man-cub Mowgli, who is raised in the jungle by wolves. The stories are set in a forest in India; one place mentioned repeatedly is Seonee (Seoni), in the central state of Madhya Pradesh','Rudyard Kipling','Adventure',NULL,0),(5,'The Da Vinci Code','The Da Vinci Code is a 2003 mystery thriller novel by Dan Brown. It is Brown\'s second novel to include the character Robert Langdon: the first was his 2000 novel Angels & Demons. The Da Vinci Code follows symbologist Robert Langdon and cryptologist Sophie Neveu after a murder in the Louvre Museum in Paris causes them to become involved in a battle between the Priory of Sion and Opus Dei over the possibility of Jesus Christ having been a companion to Mary Magdalene','Dan Brown','Adventure',NULL,1),(6,'The Immortals of Meluha','The Immortals of Meluha is the first novel of the Shiva trilogy series by Amish Tripathi. The story is set in the land of Meluha and starts with the arrival of the Shiva. The Meluhans believe that Shiva is their fabled saviour Neelkanth. Shiva decides to help the Meluhans in their war against the Chandravanshis, who had joined forces with a cursed Nagas; however, during his journey and the fight that ensues, Shiva learns how his choices actually reflect who he aspires to be and how they lead to dire consequences','Amish Tripathi','Fiction',NULL,0),(7,'The Secret of the Nagas','The Secret of the Nagas is the second novel of the Shiva trilogy series by the Indian author Amish Tripathi. The story takes place in the imaginary land of Meluha and narrates how the inhabitants of that land are saved from their wars by a nomad named Shiva. It begins from where its predecessor, The Immortals of Meluha, left off, with Shiva trying to save Sati from the invading Naga. Later Shiva takes his troop of soldiers and travels far east to the land of Branga, where he wishes to find a clue to reach the Naga people. Shiva also learns that Sati\'s first child is still alive, as well as her twin sister. His journey ultimately leads him to the Naga capital of Panchavati, where he finds a surprise waiting for him','Amish Tripathi','Fiction',NULL,1),(8,'The Oath of the Vayuputras','The Oath of the Vayuputras is a 2013 novel by Indian author Amish Tripathi and the final book in his Shiva trilogy. The book was released on 27 February 2013, through Westland Press and completes the mythical story about an imaginary land Meluha and how its inhabitants were saved by a nomad named Shiva. Starting from where the previous installment left off, Shiva discovers that Somras is the true evil in The Oath of the Vayuputras. Shiva then declares a holy war on those who seek to continue to use it, mainly the Emperors Daksha and Dilipa, who are being controlled by the sage Bhrigu. The battle rages on and Shiva travels to the land of Pariha to consult with Vayuputras, a legendary tribe. By the time he returns, the war has ended with Sati, his wife, being murdered. An enraged Shiva destroys the capital of Meluha and Somras is wiped out of history. The story concludes with Shiva and his associates being popularized as Gods for their deeds and accomplishments','Amish Tripathi','Fiction',NULL,1),(9,'Chitrangada','‘Chitrangada’, a dance drama, originally composed by Nobel laureate Gurudeb Rabindranath Tagore in 1892, is based on the love life of Manipur’s princess Chitrangada and Arjun, the third Pandava of the epic Mahabharata, and it documents the emotional journey of Chitrangada as she is awakened by her irresistible passion for the love of her life, Arjun. According to Mahabharata, Arjuna, the brave warrior travelled the length and breadth of India during his term of exile, and it was during his wanderings that he had a brief sojourn in ancient Manipur, a mystic kingdom renowned for its natural beauty. There, he met Chitrangada, the daughter of the king of Manipur and eventually married him on the preconditioned premise that he would never take away either Chitrangada or their children from her maiden kingdom of Manipur. The couple later had a son named Babruvahana','Rabindranath Tagore','Drama',NULL,0),(10,'Macbeth','Macbeth (full title The Tragedy of Macbeth) is a tragedy by William Shakespeare; it is thought to have been first performed in 1606. It dramatises the damaging physical and psychological effects of political ambition on those who seek power for its own sake. Of all the plays that Shakespeare wrote during the reign of James I, who was patron of Shakespeare\'s acting company, Macbeth most clearly reflects the playwright\'s relationship with his sovereign. It was first published in the Folio of 1623, possibly from a prompt book, and is Shakespeare\'s shortest tragedy.','William Shakespeare','Drama',NULL,1),(11,'The Haunting of Hill House','The Haunting of Hill House is a 1959 gothic horror novel by American author Shirley Jackson. A finalist for the National Book Award and considered one of the best literary ghost stories published during the 20th century,it has been made into two feature films and a play, and is the basis of a Netflix series. Jackson\'s novel relies on terror rather than horror to elicit emotion in the reader, using complex relationships between the mysterious events in the house and the characters’ psyches','Shirley Jackson','Horror',NULL,1),(12,'Dracula','Dracula is an 1897 Gothic horror novel by Irish author Bram Stoker. It introduced the character of Count Dracula, and established many conventions of subsequent vampire fantasy. The novel tells the story of Dracula\'s attempt to move from Transylvania to England so that he may find new blood and spread the undead curse, and of the battle between Dracula and a small group of men and a woman led by Professor Abraham Van Helsing.','Bram Stoker','Horror',NULL,1),(14,'The Lord of the Rings','The Lord of the Rings is an epic high fantasy novel written by English author and scholar J. R. R. Tolkien. The story began as a sequel to Tolkien\'s 1937 fantasy novel The Hobbit, but eventually developed into a much larger work. Written in stages between 1937 and 1949, The Lord of the Rings is one of the best-selling novels ever written, with over 150 million copies sold.The title of the novel refers to the story\'s main antagonist, the Dark Lord Sauron,[a] who had in an earlier age created the One Ring to rule the other Rings of Power as the ultimate weapon in his campaign to conquer and rule all of Middle-earth. From quiet beginnings in the Shire, a hobbit land not unlike the English countryside, the story ranges across Middle-earth, following the course of the War of the Ring through the eyes of its characters, most notably the hobbits Frodo Baggins, Sam, Merry and Pippin.','J. R. R. Tolkien','Fantasy',NULL,0),(15,'Beyond the last blue mountain','An exhaustive and unforgettable portrait of India\'s greatest and most respected industrialist. Written with J.R.D. Tata\'s co-operation, this superb biography tells the J.R.D. story from his birth to 1993, the year in which he died in Switzerland. The book is divided into four parts: Part I deals with the early years, from J.R.D\'s birth in France in 1904 to his accession to the chairmanship of Tatas, India\'s largest industrial conglomerate, at the age of thirty-four; Part II looks at his forty-six years in Indian aviation (the lasting passion of J.R.D\'s life) which led to the initiation of the Indian aviation industry and its development into one of India\'s success stories; Part III illuminates his half-century-long stint as the outstanding personality of Indian industry; and Part IV unearths hitherto unknown details about the private man and the public figure, including glimpses of his long friendships with such people as Jawaharlal Nehru, Mahatma Gandhi, Indira Gandhi and his association with celebrities in India and abroad','R M Lala','Biography',NULL,0),(16,'Mein Kampf','Mein Kampf (German: [ma??n kampf], My Struggle or My Fight) is a 1925 autobiographical manifesto by Nazi Party leader Adolf Hitler. The work describes the process by which Hitler became antisemitic and outlines his political ideology and future plans for Germany. Volume 1 of Mein Kampf was published in 1925 and Volume 2 in 1926. The book was edited firstly by Emil Maurice, then by Hitler\'s deputy Rudolf Hess.Hitler began Mein Kampf while imprisoned for what he considered to be \'political crimes\' following his failed Putsch in Munich in November 1923. Although Hitler received many visitors initially, he soon devoted himself entirely to the book. As he continued, Hitler realized that it would have to be a two-volume work, with the first volume scheduled for release in early 1925. The governor of Landsberg noted at the time that \'he [Hitler] hopes the book will run into many editions, thus enabling him to fulfill his financial obligations and to defray the expenses incurred at the time of his trial. After slow initial sales, the book was a bestseller in Germany after Hitler\'s rise to power in 1933. After Hitler\'s death, copyright of Mein Kampf passed to the state government of Bavaria, which refused to allow any copying or printing of the book in Germany. In 2016, following the expiration of the copyright held by the Bavarian state government, Mein Kampf was republished in Germany for the first time since 1945, which prompted public debate and divided reactions from Jewish groups','Adolf Hitler','Biography',NULL,0),(17,'A Game of Thrones','A Game of Thrones is the first novel in A Song of Ice and Fire, a series of fantasy novels by the American author George R. R. Martin. It was first published on August 1, 1996. The novel won the 1997 Locus Award and was nominated for both the 1997 Nebula Award and the 1997 World Fantasy Award. The novella Blood of the Dragon, comprising the Daenerys Targaryen chapters from the novel, won the 1997 Hugo Award for Best Novella.','A Game of Thrones','Fantasy',NULL,0),(18,'Think & Grow Rich','Think and Grow Rich is probably the most important financial book you can ever hope to read. Inspiring generations of readers since the time it was first published in 1937, it has been used by millions of business leaders around the world to create a concrete plan for success that, when followed, never fails.','Napoleon Hill','Self-Help',NULL,1),(19,'Power of your Subconscious Mind','This book can bring to your notice the innate power that the sub-conscious holds. We have some traits which seem like habits, but in reality these are those traits which are directly controlled by the sub-conscious mind, vis-à-vis your habits or your routine can be changed if you can control and direct your sub-conscious mind positively. To be able to control this \'mind power\' and use it to improve the quality of your life is no walk in the park. This is where this book acts as a guide and allows you to decipher the depths of the sub-conscious.','Joseph Murthy','Self-Help',NULL,1),(32,'qqqqqqqqqqqqqq','qqqqqqqqqqqqqqq','qqqqqqqqqqqqqqqq','xyzxyzxyzxyz',NULL,0),(33,'pppp','pppp','pppp','pppp',NULL,1),(34,'test-book-1','test test test','test-author-1','test',NULL,1),(35,'test-book-2','test test test','test-author-2','test',NULL,1),(36,'test-book-3','test test test','test-author-3','test',NULL,1);
/*!40000 ALTER TABLE `LI_BOOKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LI_CONFIG`
--

DROP TABLE IF EXISTS `LI_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LI_CONFIG` (
  `NoOfDaysAUserCanKeepABook` int NOT NULL DEFAULT '0',
  `FinePerDay` int NOT NULL DEFAULT '0',
  `NoOfBooksPerUser` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LI_CONFIG`
--

LOCK TABLES `LI_CONFIG` WRITE;
/*!40000 ALTER TABLE `LI_CONFIG` DISABLE KEYS */;
INSERT INTO `LI_CONFIG` VALUES (-5,20,5);
/*!40000 ALTER TABLE `LI_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LI_USERS`
--

DROP TABLE IF EXISTS `LI_USERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LI_USERS` (
  `UserName` varchar(255) NOT NULL,
  `FirstName` varchar(255) NOT NULL,
  `LastName` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Pwd` varchar(255) NOT NULL,
  `FavGenre` varchar(255) DEFAULT NULL,
  `Fine` int DEFAULT '0',
  `BookCount` int DEFAULT '0',
  PRIMARY KEY (`UserName`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LI_USERS`
--

LOCK TABLES `LI_USERS` WRITE;
/*!40000 ALTER TABLE `LI_USERS` DISABLE KEYS */;
INSERT INTO `LI_USERS` VALUES ('admin','Library','Admin','library.admin@library.com','00299a408dc3498a3cd7bae6db588f3324654d76',NULL,0,0),('anirband','Anirban','Das','anirban@gmail.com','00299a408dc3498a3cd7bae6db588f3324654d76','Fiction',0,2),('chandler','Chandler','Bing','chandler.bing@library.com','00299a408dc3498a3cd7bae6db588f3324654d76','Comedy',0,5),('ironman','Tony','Stark','ironman@gmail.com','00299a408dc3498a3cd7bae6db588f3324654d76','Adventure',0,0),('joey','Joey','Tribbiani','joey.tribbiani@library.com','00299a408dc3498a3cd7bae6db588f3324654d76','Fiction',0,2),('monica','Monica','Geller','monica.geller@library.com','00299a408dc3498a3cd7bae6db588f3324654d76','Drama',0,0),('phoebe','Phoebe','Buffay','phoebe.buffay@library.com','00299a408dc3498a3cd7bae6db588f3324654d76','Horror',11140,0),('rachel','Rachel','Green','rachel.green@library.com','00299a408dc3498a3cd7bae6db588f3324654d76','Fantasy',0,0),('ross','Ross','Geller','ross.geller@library.com','00299a408dc3498a3cd7bae6db588f3324654d76','Biography',0,1),('sushantaD','Sushanta','Dasgupta','sd@gmail.com','00299a408dc3498a3cd7bae6db588f3324654d76','Horror',720,1),('udrocks','Urmisha','Das','ud@gmail.com','9d062bafff17ba8b9a1215c4c51485134d509d91','Fiction',0,4);
/*!40000 ALTER TABLE `LI_USERS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-01 22:09:51
