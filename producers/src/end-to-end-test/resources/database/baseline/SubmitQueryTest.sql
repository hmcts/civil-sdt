INSERT INTO BULK_CUSTOMERS (BULK_CUSTOMER_ID, SDT_CUSTOMER_ID, VERSION_NUMBER, READY_FOR_ALTERNATE_SERVICE)
VALUES(10711, 12345678, 0, true);

INSERT INTO BULK_CUSTOMER_APPLICATIONS (BULK_CUSTOMER_APPLICATIONS_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, CUSTOMER_APPLICATION_ID, VERSION_NUMBER)
VALUES(1, 10711, 1, '1', 0);

INSERT INTO BULK_CUSTOMERS (BULK_CUSTOMER_ID, SDT_CUSTOMER_ID, VERSION_NUMBER, READY_FOR_ALTERNATE_SERVICE)
VALUES(10712, 12345679, 0, true);

INSERT INTO BULK_CUSTOMER_APPLICATIONS (BULK_CUSTOMER_APPLICATIONS_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, CUSTOMER_APPLICATION_ID, VERSION_NUMBER)
VALUES(2, 10712, 1, 'MC0000299911', 0);

