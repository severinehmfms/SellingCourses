-- ------------------------------------------------------------------------------
-- - Reconstruction de la base de données                                     ---
-- ------------------------------------------------------------------------------
DROP DATABASE IF EXISTS SellingCourses;
CREATE DATABASE SellingCourses;
USE SellingCourses;

-- -----------------------------------------------------------------------------
-- - Construction des TABLES						                     ---
-- -----------------------------------------------------------------------------

CREATE TABLE UserApp(
   login_app VARCHAR(50) PRIMARY KEY,
   password_app VARCHAR(50) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE Customer(
   customer_id INT PRIMARY KEY AUTO_INCREMENT,
   customer_name VARCHAR(50),
   customer_first_name VARCHAR(50),
   customer_mail VARCHAR(50),
   customer_adresse VARCHAR(50),
   customer_phone VARCHAR(10)
) ENGINE = InnoDB;

CREATE TABLE Training(
   training_id INT PRIMARY KEY AUTO_INCREMENT,
   training_name VARCHAR(50),
   training_description TEXT,
   training_length SMALLINT,
   remote_training BOOLEAN,
   training_price DECIMAL(10,2)
) ENGINE = InnoDB;

CREATE TABLE OrderApp(
   order_id INT PRIMARY KEY AUTO_INCREMENT,
   order_status SMALLINT NOT NULL,
   order_date DATETIME,
   total_amount DECIMAL(10,2),
   login_app VARCHAR(50) NOT NULL,
   customer_id INT NULL,
   KEY login_app (login_app),
   KEY customer_id (customer_id)
) ENGINE = InnoDB;

CREATE TABLE LineOrder(
   order_id INT NOT NULL,
   training_id INT NOT NULL,
   quantity SMALLINT,
   PRIMARY KEY(order_id, training_id),   
   KEY order_id (order_id),
   KEY training_id (training_id)
) ENGINE = InnoDB;

ALTER TABLE `OrderApp`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`login_app`) REFERENCES `UserApp` (`login_app`),
  ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`);
  
ALTER TABLE LineOrder
  ADD CONSTRAINT `lineorder_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `OrderApp` (`order_id`),
  ADD CONSTRAINT `lineorder_ibfk_2` FOREIGN KEY (`training_id`) REFERENCES `Training` (`training_id`);
  
-- ------------------------------------------------------------------------------
-- - Insertion des données 				                                      ---
-- ------------------------------------------------------------------------------
  
INSERT INTO `Training` (`training_id`, `training_name`, `training_description`, `training_length`, `remote_training`, `training_price` ) VALUES
(1, 'Java', 'Java SE 8 Syntaxe & POO', 20, False, 300.25),
(2, 'Java avancé', 'Exceptions, Fichiers, JDBC, Threads ...', 30, True, 200.50),
(3, 'Spring', 'Spring core, MVC, Security', 20, False, 500),
(4, 'PHP Frameworks', 'PHP, Symphony, Wordpress, Xoops', 20, True, 400),
(5, 'Python', 'Langage Python, PyCharm, POO', 15, False, 300.5),
(6, 'Python avancé', 'Exceptions, Fichiers, JDBC, Threads...', 20, True, 200.5),
(7, 'CISCO', 'Routeurs CISCO, formation réseaux', 10, False, 140.8),
(8, 'SQL', 'Bases de données MariaDb, MySql', 20, True, 150),
(9, 'GIT', 'Utilisation de Git, Github, Gitlab', 30, False, 235),
(10, 'Web', 'Partie frontend, Html, CSS, Javascript', 20, True, 345);

INSERT INTO `UserApp` (`login_app`, `password_app` ) VALUES
('myuser', 'test');