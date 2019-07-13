CREATE TABLE IF NOT EXISTS `employee_relationships` (
    `employee` varchar(200) NOT NULL,
    `supervisor` varchar(200) NOT NULL,
    PRIMARY KEY (`employee`)
);