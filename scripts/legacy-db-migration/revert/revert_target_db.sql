-- Delete imported data
DELETE FROM error_logs;
DELETE FROM individual_requests;
DELETE FROM bulk_submissions;
DELETE FROM service_requests;
DELETE FROM bulk_customer_applications;
DELETE FROM bulk_customers;

-- Reset sequences
ALTER SEQUENCE BULK_CUST_APP_SEQ RESTART WITH 1;
ALTER SEQUENCE BULK_CUST_SEQ RESTART WITH 1;
ALTER SEQUENCE BULK_SUB_SEQ RESTART WITH 1;
ALTER SEQUENCE ERR_LOG_SEQ RESTART WITH 1;
ALTER SEQUENCE IND_REQ_SEQ RESTART WITH 1;
ALTER SEQUENCE SDT_REF_SEQ RESTART WITH 1;
ALTER SEQUENCE SRV_REQ_SEQ RESTART WITH 1;
