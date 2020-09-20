-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.4.8-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- musicplayer 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `musicplayer` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `musicplayer`;

-- 테이블 musicplayer.album 구조 내보내기
CREATE TABLE IF NOT EXISTS `album` (
  `Album_number` int(11) NOT NULL,
  `Album_title` varchar(20) NOT NULL,
  `Sell_date` date DEFAULT NULL,
  `Manager_num` int(11) NOT NULL,
  PRIMARY KEY (`Album_number`),
  KEY `Manager_num` (`Manager_num`),
  CONSTRAINT `album_ibfk_1` FOREIGN KEY (`Manager_num`) REFERENCES `manager` (`Manager_number`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.album:~8 rows (대략적) 내보내기
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
INSERT INTO `album` (`Album_number`, `Album_title`, `Sell_date`, `Manager_num`) VALUES
	(1, 'red diary2', '2018-05-24', 0),
	(2, 'speak now', '2010-01-01', 0),
	(3, 'two five', '2019-09-10', 1),
	(4, 'gumbupnam drama ost', '2018-07-02', 0),
	(5, 'twice in army', '2018-11-04', 1),
	(6, 'show_me_the_money6', '2018-09-09', 0),
	(7, 'coldplay remix', '2015-02-02', 0),
	(8, 'coldplay best', '2019-11-29', 0);
/*!40000 ALTER TABLE `album` ENABLE KEYS */;

-- 테이블 musicplayer.artist 구조 내보내기
CREATE TABLE IF NOT EXISTS `artist` (
  `Artist_number` int(11) NOT NULL,
  `Artist_name` varchar(20) NOT NULL,
  `Mg_number` int(11) NOT NULL,
  PRIMARY KEY (`Artist_number`),
  KEY `Mg_number` (`Mg_number`),
  CONSTRAINT `artist_ibfk_1` FOREIGN KEY (`Mg_number`) REFERENCES `manager` (`Manager_number`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.artist:~9 rows (대략적) 내보내기
/*!40000 ALTER TABLE `artist` DISABLE KEYS */;
INSERT INTO `artist` (`Artist_number`, `Artist_name`, `Mg_number`) VALUES
	(0, 'various artist', 0),
	(1, 'bol4', 0),
	(2, 'taylor swift', 0),
	(3, 'stella jang', 0),
	(4, 'cold play', 0),
	(5, 'hang zu', 0),
	(6, 'twice', 1),
	(7, 'zico', 0),
	(11, 'maroon5', 1);
/*!40000 ALTER TABLE `artist` ENABLE KEYS */;

-- 테이블 musicplayer.artist_genre 구조 내보내기
CREATE TABLE IF NOT EXISTS `artist_genre` (
  `Artist_num` int(11) NOT NULL,
  `Art_genre` varchar(10) NOT NULL,
  PRIMARY KEY (`Artist_num`,`Art_genre`),
  CONSTRAINT `artist_genre_ibfk_1` FOREIGN KEY (`Artist_num`) REFERENCES `artist` (`Artist_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.artist_genre:~13 rows (대략적) 내보내기
/*!40000 ALTER TABLE `artist_genre` DISABLE KEYS */;
INSERT INTO `artist_genre` (`Artist_num`, `Art_genre`) VALUES
	(0, 'ALL'),
	(1, 'BAND'),
	(1, 'INDI'),
	(2, 'POP'),
	(3, 'HIPHOP'),
	(3, 'INDI'),
	(4, 'BAND'),
	(4, 'POP'),
	(5, 'HIPHOP'),
	(6, 'DANCE'),
	(7, 'HIPHOP'),
	(11, 'BAND'),
	(11, 'POP');
/*!40000 ALTER TABLE `artist_genre` ENABLE KEYS */;

-- 테이블 musicplayer.manager 구조 내보내기
CREATE TABLE IF NOT EXISTS `manager` (
  `Manager_number` int(11) NOT NULL,
  `Manager_id` varchar(15) NOT NULL,
  `Manager_password` varchar(15) NOT NULL,
  `Manager_name` varchar(20) NOT NULL,
  `Manager_Address` varchar(30) DEFAULT NULL,
  `Phone_number` char(14) DEFAULT NULL,
  PRIMARY KEY (`Manager_number`),
  UNIQUE KEY `Manager_id` (`Manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.manager:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` (`Manager_number`, `Manager_id`, `Manager_password`, `Manager_name`, `Manager_Address`, `Phone_number`) VALUES
	(0, 'root', 'root', 'root', 'seoul', '010-0000-0000'),
	(1, 'root2', 'root2', 'root2', 'seoul', '010-0000-0001');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;

-- 테이블 musicplayer.music 구조 내보내기
CREATE TABLE IF NOT EXISTS `music` (
  `Music_number` int(11) NOT NULL,
  `Music_title` varchar(20) NOT NULL,
  `Track_number` int(11) DEFAULT NULL,
  `Mng_number` int(11) NOT NULL,
  `Album_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`Music_number`),
  KEY `Mng_number` (`Mng_number`),
  KEY `Album_num` (`Album_num`),
  CONSTRAINT `music_ibfk_1` FOREIGN KEY (`Mng_number`) REFERENCES `manager` (`Manager_number`) ON UPDATE CASCADE,
  CONSTRAINT `music_ibfk_2` FOREIGN KEY (`Album_num`) REFERENCES `album` (`Album_number`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.music:~24 rows (대략적) 내보내기
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` (`Music_number`, `Music_title`, `Track_number`, `Mng_number`, `Album_num`) VALUES
	(1, 'wind wind', 1, 0, 1),
	(2, 'travel', 2, 0, 1),
	(3, 'star light', 3, 0, 1),
	(4, 'hi teddy bear', 4, 0, 1),
	(5, 'clip', 5, 0, 1),
	(6, 'lonely', 6, 0, 1),
	(7, 'white rain', 1, 0, 4),
	(8, 'mine', 1, 0, 2),
	(9, 'sparks fly', 2, 0, 2),
	(10, 'back to december', 3, 0, 2),
	(11, 'under caffeine', NULL, 0, NULL),
	(12, 'under caffeine', NULL, 0, NULL),
	(13, 'work holic', 1, 1, 3),
	(14, '25', 2, 1, 3),
	(15, 'xx', 3, 1, 3),
	(17, 'what is love', 2, 1, 5),
	(18, 'signal', 3, 1, 5),
	(19, 'red sun', 1, 0, 6),
	(20, 'search', 2, 0, 6),
	(21, 'artist', NULL, 0, NULL),
	(22, 'vivala vida', 1, 0, 8),
	(23, 'fix you', 2, 0, 8),
	(24, 'hymn for the weekend', 1, 0, 7),
	(25, 'lost star', NULL, 0, NULL);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;

-- 테이블 musicplayer.music_genre 구조 내보내기
CREATE TABLE IF NOT EXISTS `music_genre` (
  `Music_num` int(11) NOT NULL,
  `M_genre` varchar(10) NOT NULL,
  PRIMARY KEY (`Music_num`,`M_genre`),
  CONSTRAINT `music_genre_ibfk_1` FOREIGN KEY (`Music_num`) REFERENCES `music` (`Music_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.music_genre:~35 rows (대략적) 내보내기
/*!40000 ALTER TABLE `music_genre` DISABLE KEYS */;
INSERT INTO `music_genre` (`Music_num`, `M_genre`) VALUES
	(1, 'INDI'),
	(2, 'INDI'),
	(3, 'BALLADE'),
	(3, 'INDI'),
	(4, 'INDI'),
	(5, 'INDI'),
	(6, 'BALLADE'),
	(6, 'INDI'),
	(7, 'BALLADE'),
	(7, 'HIPHOP'),
	(8, 'POP'),
	(9, 'DANCE'),
	(9, 'POP'),
	(10, 'BALLADE'),
	(10, 'POP'),
	(11, 'HIPHOP'),
	(11, 'INDI'),
	(12, 'BALLADE'),
	(12, 'POP'),
	(13, 'INDI'),
	(14, 'INDI'),
	(15, 'INDI'),
	(17, 'DANCE'),
	(18, 'DANCE'),
	(19, 'HIPHOP'),
	(20, 'HIPHOP'),
	(21, 'DANCE'),
	(21, 'HIPHOP'),
	(22, 'POP'),
	(23, 'BALLADE'),
	(23, 'POP'),
	(24, 'HIPHOP'),
	(24, 'POP'),
	(25, 'BLLADE'),
	(25, 'POP');
/*!40000 ALTER TABLE `music_genre` ENABLE KEYS */;

-- 테이블 musicplayer.perform 구조 내보내기
CREATE TABLE IF NOT EXISTS `perform` (
  `At_num` int(11) NOT NULL,
  `M_num` int(11) NOT NULL,
  PRIMARY KEY (`At_num`,`M_num`),
  KEY `M_num` (`M_num`),
  CONSTRAINT `perform_ibfk_1` FOREIGN KEY (`At_num`) REFERENCES `artist` (`Artist_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `perform_ibfk_2` FOREIGN KEY (`M_num`) REFERENCES `music` (`Music_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.perform:~26 rows (대략적) 내보내기
/*!40000 ALTER TABLE `perform` DISABLE KEYS */;
INSERT INTO `perform` (`At_num`, `M_num`) VALUES
	(1, 1),
	(1, 2),
	(1, 3),
	(1, 4),
	(1, 5),
	(1, 6),
	(1, 13),
	(1, 14),
	(1, 15),
	(2, 8),
	(2, 9),
	(2, 10),
	(3, 7),
	(3, 11),
	(4, 22),
	(4, 23),
	(4, 24),
	(5, 7),
	(5, 19),
	(5, 20),
	(6, 17),
	(6, 18),
	(7, 19),
	(7, 21),
	(11, 12),
	(11, 25);
/*!40000 ALTER TABLE `perform` ENABLE KEYS */;

-- 테이블 musicplayer.played 구조 내보내기
CREATE TABLE IF NOT EXISTS `played` (
  `Pl_number` int(11) NOT NULL,
  `Us_id` varchar(15) NOT NULL,
  `M_number` int(11) NOT NULL,
  `Play_number` int(11) NOT NULL,
  PRIMARY KEY (`Pl_number`,`M_number`,`Us_id`),
  KEY `Pl_number` (`Pl_number`,`Us_id`),
  KEY `M_number` (`M_number`),
  CONSTRAINT `played_ibfk_1` FOREIGN KEY (`Pl_number`, `Us_id`) REFERENCES `playlist` (`Playlist_number`, `U_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `played_ibfk_2` FOREIGN KEY (`M_number`) REFERENCES `music` (`Music_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.played:~23 rows (대략적) 내보내기
/*!40000 ALTER TABLE `played` DISABLE KEYS */;
INSERT INTO `played` (`Pl_number`, `Us_id`, `M_number`, `Play_number`) VALUES
	(1, 'test', 1, 7),
	(1, 'test1', 1, 1),
	(1, 'test', 2, 6),
	(1, 'test', 3, 5),
	(1, 'test1', 3, 2),
	(1, 'test', 4, 3),
	(1, 'test', 5, 2),
	(1, 'test1', 5, 3),
	(1, 'test', 6, 4),
	(1, 'test1', 7, 4),
	(1, 'test1', 9, 5),
	(1, 'test', 13, 8),
	(1, 'test', 14, 1),
	(1, 'test', 15, 9),
	(2, 'test', 1, 1),
	(2, 'test', 2, 2),
	(2, 'test', 3, 3),
	(2, 'test', 4, 4),
	(2, 'test', 5, 5),
	(2, 'test', 6, 6),
	(2, 'test', 7, 7),
	(2, 'test', 8, 8),
	(2, 'test', 9, 9);
/*!40000 ALTER TABLE `played` ENABLE KEYS */;

-- 테이블 musicplayer.player_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `player_user` (
  `User_id` varchar(15) NOT NULL,
  `User_password` varchar(15) NOT NULL,
  `User_name` varchar(20) NOT NULL,
  `Phone_number` char(14) DEFAULT NULL,
  `Sex` char(1) DEFAULT NULL,
  `User_address` varchar(30) DEFAULT NULL,
  `Birth_date` date DEFAULT NULL,
  `Payment_date` date DEFAULT NULL,
  `Expiry_date` date DEFAULT NULL,
  `Sub_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`User_id`),
  KEY `Sub_name` (`Sub_name`),
  CONSTRAINT `player_user_ibfk_1` FOREIGN KEY (`Sub_name`) REFERENCES `subscription` (`Subscription_name`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.player_user:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `player_user` DISABLE KEYS */;
INSERT INTO `player_user` (`User_id`, `User_password`, `User_name`, `Phone_number`, `Sex`, `User_address`, `Birth_date`, `Payment_date`, `Expiry_date`, `Sub_name`) VALUES
	('test', 'test', 'test', '010-9999-9999', 'M', 'some where in ', '1997-10-09', '2019-11-29', '2019-12-28', 'ALL FREE'),
	('test1', 'test1', 'test1', '010-9011-1234', 'F', 'busan', '2019-10-01', NULL, NULL, NULL);
/*!40000 ALTER TABLE `player_user` ENABLE KEYS */;

-- 테이블 musicplayer.playlist 구조 내보내기
CREATE TABLE IF NOT EXISTS `playlist` (
  `Playlist_number` int(11) NOT NULL,
  `Playlist_title` varchar(20) NOT NULL DEFAULT 'MY PLAY LIST',
  `U_id` varchar(15) NOT NULL,
  PRIMARY KEY (`Playlist_number`,`U_id`),
  KEY `U_id` (`U_id`),
  CONSTRAINT `playlist_ibfk_1` FOREIGN KEY (`U_id`) REFERENCES `player_user` (`User_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.playlist:~3 rows (대략적) 내보내기
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` (`Playlist_number`, `Playlist_title`, `U_id`) VALUES
	(1, 'BOL4 musics', 'test'),
	(1, 'My Play List', 'test1'),
	(2, 'My music', 'test');
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;

-- 테이블 musicplayer.released 구조 내보내기
CREATE TABLE IF NOT EXISTS `released` (
  `At_number` int(11) NOT NULL,
  `Ab_number` int(11) NOT NULL,
  PRIMARY KEY (`At_number`,`Ab_number`),
  KEY `Ab_number` (`Ab_number`),
  CONSTRAINT `released_ibfk_1` FOREIGN KEY (`At_number`) REFERENCES `artist` (`Artist_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `released_ibfk_2` FOREIGN KEY (`Ab_number`) REFERENCES `album` (`Album_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.released:~10 rows (대략적) 내보내기
/*!40000 ALTER TABLE `released` DISABLE KEYS */;
INSERT INTO `released` (`At_number`, `Ab_number`) VALUES
	(1, 1),
	(1, 3),
	(2, 2),
	(3, 4),
	(4, 7),
	(4, 8),
	(5, 4),
	(5, 6),
	(6, 5),
	(7, 6);
/*!40000 ALTER TABLE `released` ENABLE KEYS */;

-- 테이블 musicplayer.subscription 구조 내보내기
CREATE TABLE IF NOT EXISTS `subscription` (
  `Subscription_name` varchar(20) NOT NULL,
  `Price` int(11) NOT NULL,
  `Device_number` int(11) NOT NULL,
  PRIMARY KEY (`Subscription_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.subscription:~3 rows (대략적) 내보내기
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
INSERT INTO `subscription` (`Subscription_name`, `Price`, `Device_number`) VALUES
	('ALL FREE', 9000, 4),
	('basic streaming', 2000, 1),
	('couple streaming', 3000, 2);
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;

-- 테이블 musicplayer.subscr_device 구조 내보내기
CREATE TABLE IF NOT EXISTS `subscr_device` (
  `Subsc_name` varchar(20) NOT NULL,
  `Possible_device` varchar(20) NOT NULL,
  PRIMARY KEY (`Subsc_name`,`Possible_device`),
  CONSTRAINT `subscr_device_ibfk_1` FOREIGN KEY (`Subsc_name`) REFERENCES `subscription` (`Subscription_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 musicplayer.subscr_device:~6 rows (대략적) 내보내기
/*!40000 ALTER TABLE `subscr_device` DISABLE KEYS */;
INSERT INTO `subscr_device` (`Subsc_name`, `Possible_device`) VALUES
	('ALL FREE', 'PC'),
	('ALL FREE', 'PHONE'),
	('ALL FREE', 'TABLET'),
	('basic streaming', 'PHONE'),
	('couple streaming', 'PHONE'),
	('couple streaming', 'TABLET');
/*!40000 ALTER TABLE `subscr_device` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
