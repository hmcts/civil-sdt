TRUNCATE TABLE BULK_CUSTOMERS CASCADE;
TRUNCATE TABLE TARGET_APPLICATIONS CASCADE;
TRUNCATE TABLE BULK_SUBMISSIONS CASCADE;
TRUNCATE TABLE INDIVIDUAL_REQUESTS CASCADE;
TRUNCATE TABLE SERVICE_TYPES CASCADE;
TRUNCATE TABLE SERVICE_ROUTINGS CASCADE;
TRUNCATE TABLE BULK_CUSTOMER_APPLICATIONS CASCADE;

insert into BULK_CUSTOMERS (BULK_CUSTOMER_ID, SDT_CUSTOMER_ID, VERSION_NUMBER)
values (10711, 2, 0);

insert into TARGET_APPLICATIONS (TARGET_APPLICATION_ID, TARGET_APPLICATION_CODE, TARGET_APPLICATION_NAME, VERSION_NUMBER)
values (10713, '1104', '2', 0);

insert into BULK_SUBMISSIONS (BULK_SUBMISSION_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, SDT_BULK_REFERENCE, CUSTOMER_REFERENCE,
                              CREATED_DATE, NUMBER_OF_REQUESTS, BULK_SUBMISSION_STATUS, BULK_PAYLOAD, VERSION_NUMBER)
values (14, 10711, 10713, 'MCOL-10012013010101-100099999', 'REF1', now()::DATE - 1 , 4, 'Validated', 'payload', 0);

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE, LINE_NUMBER,
                                 SDT_REQUEST_REFERENCE, CREATED_DATE, UPDATED_DATE, FORWARDING_ATTEMPTS, TARGET_APPLICATION_RESPONSE, REQUEST_TYPE,
                                 DEAD_LETTER, VERSION_NUMBER)
values (5, 14, 'REF1-4', 'Accepted', 'MCOL-10012013010101-100099999', 4,
        'MCOL-10012013010101-100099999-0000004', now()::DATE - 1, now()::DATE - 1, 1, 'responded', 'mcolJudgment',
        'N', 0);

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE, LINE_NUMBER,
                                 SDT_REQUEST_REFERENCE, CREATED_DATE, UPDATED_DATE, FORWARDING_ATTEMPTS, TARGET_APPLICATION_RESPONSE, REQUEST_TYPE,
                                 DEAD_LETTER, VERSION_NUMBER)
values (6, 14, 'REF1-3', 'Initially Accepted', 'MCOL-10012013010101-100099999', 3,
        'MCOL-10012013010101-100099999-0000003', now()::DATE - 1, now()::DATE - 1, 1, null, 'mcolJudgment',
        'N', 0);

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE, LINE_NUMBER,
                                 SDT_REQUEST_REFERENCE, CREATED_DATE, UPDATED_DATE, FORWARDING_ATTEMPTS, TARGET_APPLICATION_RESPONSE, REQUEST_TYPE,
                                 DEAD_LETTER, VERSION_NUMBER)
values (7, 14, 'REF1-2', 'Forwarded', 'MCOL-10012013010101-100099999', 2,
        'MCOL-10012013010101-100099999-0000002', now()::DATE - 1, now()::DATE - 1, 1, null, 'mcolJudgment',
        'N', 0);

insert into INDIVIDUAL_REQUESTS (INDIVIDUAL_REQUEST_ID, BULK_SUBMISSION_ID, CUSTOMER_REQUEST_REF, REQUEST_STATUS, SDT_BULK_REFERENCE, LINE_NUMBER,
                                 SDT_REQUEST_REFERENCE, CREATED_DATE, UPDATED_DATE, FORWARDING_ATTEMPTS, TARGET_APPLICATION_RESPONSE, REQUEST_TYPE,
                                 DEAD_LETTER, VERSION_NUMBER)
values (8, 14, 'REF1-1', 'Received', 'MCOL-10012013010101-100099999', 1,
        'MCOL-10012013010101-100099999-0000001', now()::DATE - 1, now()::DATE - 1, 1, null, 'mcolJudgment',
        'N', 0);

insert into SERVICE_TYPES (SERVICE_TYPE_ID, SERVICE_TYPE_NAME, SERVICE_TYPE_STATUS, SERVICE_TYPE_DESCRIPTION, VERSION_NUMBER)
values (1, '11043', '2', '0', 0);

insert into SERVICE_ROUTINGS (SERVICE_ROUTINGS_ID, SERVICE_TYPE_ID, TARGET_APPLICATION_ID, WEB_SERVICE_ENDPOINT, VERSION_NUMBER)
values (1, 1, 10713, '2', 0);

insert into BULK_CUSTOMER_APPLICATIONS (BULK_CUSTOMER_APPLICATIONS_ID, BULK_CUSTOMER_ID, TARGET_APPLICATION_ID, CUSTOMER_APPLICATION_ID, VERSION_NUMBER)
values (1, 10711, 10713, 1, 0);

