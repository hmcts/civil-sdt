TRUNCATE TABLE BULK_CUSTOMERS CASCADE;
TRUNCATE TABLE TARGET_APPLICATIONS CASCADE;
TRUNCATE TABLE BULK_CUSTOMER_APPLICATIONS CASCADE;

INSERT INTO BULK_CUSTOMERS (BULK_CUSTOMER_ID, SDT_CUSTOMER_ID, VERSION_NUMBER, READY_FOR_ALTERNATE_SERVICE)
VALUES(10711, 2, 0, false);

INSERT INTO TARGET_APPLICATIONS (TARGET_APPLICATION_ID, TARGET_APPLICATION_CODE, TARGET_APPLICATION_NAME, VERSION_NUMBER)
VALUES(10713, 1104, 2, 0);

INSERT INTO BULK_CUSTOMER_APPLICATIONS (BULK_CUSTOMER_APPLICATIONS_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, CUSTOMER_APPLICATION_ID, VERSION_NUMBER)
VALUES(1, 10711, 10713, '1', 0);
