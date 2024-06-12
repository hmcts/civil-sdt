TRUNCATE TABLE BULK_CUSTOMERS CASCADE;
TRUNCATE TABLE TARGET_APPLICATIONS CASCADE;
TRUNCATE TABLE BULK_SUBMISSIONS CASCADE;
TRUNCATE TABLE INDIVIDUAL_REQUESTS CASCADE;
TRUNCATE TABLE SERVICE_TYPES CASCADE;
TRUNCATE TABLE SERVICE_ROUTINGS CASCADE;
TRUNCATE TABLE BULK_CUSTOMER_APPLICATIONS CASCADE;

insert into BULK_CUSTOMERS (BULK_CUSTOMER_ID, SDT_CUSTOMER_ID, VERSION_NUMBER) values (10711, 2, 0);

insert into TARGET_APPLICATIONS (TARGET_APPLICATION_ID, TARGET_APPLICATION_CODE, TARGET_APPLICATION_NAME, VERSION_NUMBER) values (10713, '1104', '2', 0);

insert into BULK_SUBMISSIONS (BULK_SUBMISSION_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, SDT_BULK_REFERENCE, CUSTOMER_REFERENCE,
                              CREATED_DATE, NUMBER_OF_REQUESTS, BULK_SUBMISSION_STATUS, BULK_PAYLOAD, VERSION_NUMBER)
values (10711, 10711, 10713, 'SDT_BULK_2', '10711', now()::DATE - 1 ,9, 'SUBMITTED', 'MTIzNDU=', 0);

insert into BULK_SUBMISSIONS (BULK_SUBMISSION_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, SDT_BULK_REFERENCE, CUSTOMER_REFERENCE,
                              CREATED_DATE, NUMBER_OF_REQUESTS, BULK_SUBMISSION_STATUS, BULK_PAYLOAD, VERSION_NUMBER)
values (10710, 10711, 10713, 'SDT_BULK_1', '10711', now()::DATE - 1 , 1, 'SUBMITTED', 'MTIzNDU=', 0);

insert into BULK_SUBMISSIONS (BULK_SUBMISSION_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, SDT_BULK_REFERENCE, CUSTOMER_REFERENCE,
                              CREATED_DATE, NUMBER_OF_REQUESTS, BULK_SUBMISSION_STATUS, BULK_PAYLOAD, VERSION_NUMBER)
values (10712, 10711, 10713, 'SDT_BULK_3', '10711', now()::DATE - 1 , 2, 'SUBMITTED', 'MTIzNDU=', 0);

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10714, 10710, '10711', 'Accepted', 'SDT_BULK_1', 'SDT_REQ_TEST_1', 0, 'MTIzNDU=', now()::DATE - 1, now()::DATE - 1, 1, 0, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10715, 10711, '10711', 'Accepted', 'SDT_BULK_2', 'SDT_REQ_TEST_21', 0, 'MTIzNDU=', now()::DATE - 1, null, 1, 0, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10716, 10711, '10711', 'Accepted', 'SDT_BULK_2', 'SDT_REQ_TEST_22', 0, 'MTIzNDU=', now()::DATE - 1, now() - Interval '13 hour', 2, 0, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10717, 10711, '10711', 'Accepted', 'SDT_BULK_2', 'SDT_REQ_TEST_23', 0, 'MTIzNDU=', now()::DATE - 1, now() - Interval '11 hour', 3, 0, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10718, 10711, '10711', 'Received', 'SDT_BULK_2', 'SDT_REQ_TEST_24', 0, 'MTIzNDU=', now()::DATE - 1, null, 4, 1, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10719, 10711, '10711', 'Received', 'SDT_BULK_2', 'SDT_REQ_TEST_25', 0, 'MTIzNDU=', now()::DATE - 1, now() - Interval '13 hour', 5, 3, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10720, 10711, '10711', 'Received', 'SDT_BULK_2', 'SDT_REQ_TEST_26', 0, 'MTIzNDU=', now()::DATE - 1, now() - Interval '11 hour', 6, 3, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10721, 10711, '10711', 'Forwarded', 'SDT_BULK_2', 'SDT_REQ_TEST_27', 0, 'MTIzNDU=', now()::DATE - 1, null, 7, 1, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10722, 10711, '10711', 'Forwarded', 'SDT_BULK_2', 'SDT_REQ_TEST_28', 0, 'MTIzNDU=', now()::DATE - 1, now() - Interval '13 hour', 8, 2, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10723, 10711, '10711', 'Forwarded', 'SDT_BULK_2', 'SDT_REQ_TEST_29', 0, 'MTIzNDU=', now()::DATE - 1, now() - Interval '11 hour', 9, 2, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10724, 10712, '10711', 'Forwarded', 'SDT_BULK_3', 'SDT_REQ_TEST_31', 0, 'MTIzNDU=', now()::DATE - 1, now(), 1, 3, 'N');

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE,
                                 SDT_REQUEST_REFERENCE, VERSION_NUMBER, INDIVIDUAL_PAYLOAD, CREATED_DATE, UPDATED_DATE, LINE_NUMBER,
                                 FORWARDING_ATTEMPTS, DEAD_LETTER)
values (10725, 10712, '10711', 'Forwarded', 'SDT_BULK_3', 'SDT_REQ_TEST_32', 0, 'MTIzNDU=', now()::DATE - 1, now(), 2, 3, 'Y');

insert into SERVICE_TYPES (SERVICE_TYPE_ID, SERVICE_TYPE_NAME, SERVICE_TYPE_STATUS, SERVICE_TYPE_DESCRIPTION, VERSION_NUMBER)
values (1, '11043', '2', '0', 0);

insert into SERVICE_ROUTINGS (SERVICE_ROUTINGS_ID, SERVICE_TYPE_ID, TARGET_APPLICATION_ID, WEB_SERVICE_ENDPOINT, VERSION_NUMBER) values (1, 1, 10713, '2', 0);

insert into BULK_CUSTOMER_APPLICATIONS (BULK_CUSTOMER_APPLICATIONS_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, CUSTOMER_APPLICATION_ID, VERSION_NUMBER)
values (1, 10711, 10713, 1, 0);
