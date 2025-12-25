CREATE TABLE IF NOT EXISTS `fake_festival` (
  `ID` int(11) NOT NULL,
  `TITLE` varchar(100) NOT NULL,
  `ORIGIN` varchar(100) NOT NULL,
  `CONTENT` varchar(1000) NOT NULL,
  `IMAGE` varchar(50) NOT NULL,
  `STARTDATE` date NOT NULL,
  `ENDDATE` date NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
