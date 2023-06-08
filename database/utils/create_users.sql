------------------------------------------------
-- Create each of the schemas used by SDT
------------------------------------------------

SET ECHO ON

CREATE USER PUBLIC IDENTIFIED BY PUBLIC DEFAULT TABLESPACE users TEMPORARY TABLESPACE temp;
GRANT CONNECT,RESOURCE TO PUBLIC;
GRANT INHERIT PRIVILEGES ON USER SYSTEM TO PUBLIC;
ALTER USER PUBLIC QUOTA UNLIMITED ON USERS;

