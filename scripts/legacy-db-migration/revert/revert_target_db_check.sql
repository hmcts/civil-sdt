-- Display number of rows in tables
SELECT 'bulk_customers' AS "table_name", COUNT(*) FROM bulk_customers;
SELECT 'bulk_customer_applications' AS "table_name", COUNT(*) FROM bulk_customer_applications;
SELECT 'service_requests' AS "table_name", COUNT(*) FROM service_requests;
SELECT 'bulk_submissions' AS "table_name", COUNT(*) FROM bulk_submissions;
SELECT 'individual_requests' AS "table_name", COUNT(*) FROM individual_requests;
SELECT 'error_logs' AS "table_name", COUNT(*) FROM error_logs;

-- Display sequence values
SELECT 'BULK_CUST_APP_SEQ' AS "sequence_name", last_value FROM BULK_CUST_APP_SEQ;
SELECT 'BULK_CUST_SEQ' AS "sequence_name", last_value FROM BULK_CUST_SEQ;
SELECT 'BULK_SUB_SEQ' AS "sequence_name", last_value FROM BULK_SUB_SEQ;
SELECT 'ERR_LOG_SEQ' AS "sequence_name", last_value FROM ERR_LOG_SEQ;
SELECT 'ERR_MESG_SEQ' AS "sequence_name", last_value FROM ERR_MESG_SEQ;
SELECT 'GLB_PAR_SEQ' AS "sequence_name", last_value FROM GLB_PAR_SEQ;
SELECT 'IND_REQ_SEQ' AS "sequence_name", last_value FROM IND_REQ_SEQ;
SELECT 'SDT_REF_SEQ' AS "sequence_name", last_value FROM SDT_REF_SEQ;
SELECT 'SER_ROU_SEQ' AS "sequence_name", last_value FROM SER_ROU_SEQ;
SELECT 'SER_TYP_SEQ' AS "sequence_name", last_value FROM SER_TYP_SEQ;
SELECT 'SRV_REQ_SEQ' AS "sequence_name", last_value FROM SRV_REQ_SEQ;
SELECT 'TAR_APP_SEQ' AS "sequence_name", last_value FROM TAR_APP_SEQ;
