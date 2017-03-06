CREATE SCHEMA `ippr`;
COMMIT;

CREATE SCHEMA `ippr_security` ;
COMMIT;

CREATE SCHEMA `ippr_communicator` ;
COMMIT;


CREATE USER 'ippr'@'localhost' IDENTIFIED BY 'Pa$$w0rd';
GRANT ALL PRIVILEGES ON * . * TO 'ippr'@'localhost';
FLUSH PRIVILEGES;

