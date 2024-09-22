DROP DATABASE IF EXISTS workshops;
CREATE DATABASE IF NOT EXISTS workshops;

USE workshops;

DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS users(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, username VARCHAR(32) UNIQUE, password VARCHAR(32), address VARCHAR(254), card VARCHAR(20), nb_coupon INT);
CREATE TABLE IF NOT EXISTS coupon(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, code_coupon VARCHAR(20) UNIQUE, username VARCHAR(32), FOREIGN KEY (username) REFERENCES users(username));
INSERT INTO users(username,password,address,card, nb_coupon) VALUES ('admin','admin', '52 rue bayen', '4444 4444 4444 4444', 0);
INSERT INTO users(username,password,address,card, nb_coupon) VALUES ('user','user', '11 rue de la tour', '4242 4242 4242 4242', 0);

CREATE USER 'userdefault'@'%' IDENTIFIED BY 'password';
GRANT ALL ON workshops.* TO 'userdefault'@'%';

FLUSH PRIVILEGES;