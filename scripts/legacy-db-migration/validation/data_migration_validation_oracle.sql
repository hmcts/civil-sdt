SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

-----------------------------------------
-- COMPARE ROW COUNTS
-----------------------------------------
SPOOL 'validation_row_counts_oracle.txt';
SELECT
(SELECT 'BULK_CUSTOMER_APPLICATIONS' || ',' || COUNT(*) FROM sdt_owner.bulk_customer_applications),
(SELECT 'BULK_CUSTOMERS' || ',' || COUNT(*) FROM sdt_owner.bulk_customers),
(SELECT 'BULK_SUBMISSIONS' || ',' || COUNT(*) FROM sdt_owner.bulk_submissions),
(SELECT 'ERROR_LOGS' || ',' || COUNT(*) FROM sdt_owner.error_logs),
(SELECT 'GLOBAL_PARAMETERS' || ',' || COUNT(*) FROM sdt_owner.global_parameters),
(SELECT 'INDIVIDUAL_REQUESTS' || ',' || COUNT(*) FROM sdt_owner.individual_requests),
(SELECT 'SERVICE_REQUESTS' || ',' || COUNT(*) FROM sdt_owner.service_requests),
(SELECT 'SERVICE_ROUTINGS' || ',' || COUNT(*) FROM sdt_owner.service_routings),
(SELECT 'SERVICE_TYPES' || ',' || COUNT(*) FROM sdt_owner.service_types),
(SELECT 'TARGET_APPLICATIONS' || ',' || COUNT(*) FROM sdt_owner.target_applications)
FROM DUAL;
SPOOL OFF;

-----------------------------------------
-- COMPARE ROW_COUNT FOR EACH TABLE JOIN
-----------------------------------------
-- Compare amount of error logs with each individual request
SPOOL 'validation_el_count_individual_req_oracle.csv';
SELECT
    i_r.individual_request_id || ',' ||
    COUNT(1)
FROM
    sdt_owner.error_logs          e_l,
    sdt_owner.individual_requests i_r
WHERE
    i_r.individual_request_id = e_l.individual_request_id
GROUP BY
    i_r.individual_request_id
ORDER BY
    i_r.individual_request_id;
SPOOL OFF;

-- Compare BULK_SUBMISSIONS.NUMBER_OF_REQUESTS with the actual counts in INDIVIDUAL_REQUESTS
SPOOL 'validation_ir_count_bulk_submission_oracle.csv';
SELECT
    b_s.sdt_bulk_reference || ',' ||
    b_s.number_of_requests || ',' ||
    COUNT(1) || ',' ||
    DECODE(b_s.number_of_requests, count(1), 't', 'f')
FROM
    sdt_owner.bulk_submissions    b_s,
    sdt_owner.individual_requests i_r
WHERE
    b_s.sdt_bulk_reference = i_r.sdt_bulk_reference
GROUP BY
    b_s.sdt_bulk_reference,
    b_s.number_of_requests
ORDER BY
    b_s.sdt_bulk_reference;
SPOOL OFF;

-- Compare amount of bulk submissions with each bulk customer
SPOOL 'validation_bs_count_bulk_customer_oracle.csv';
SELECT
    b_c.bulk_customer_id || ',' ||
    COUNT(1)
FROM
    sdt_owner.bulk_submissions b_s,
    sdt_owner.bulk_customers   b_c
WHERE
    b_c.bulk_customer_id = b_s.bulk_customer_id
GROUP BY
    b_c.bulk_customer_id
ORDER BY
    b_c.bulk_customer_id;
SPOOL OFF;

-- Compare amount of bulk customer applications with each bulk customer
SPOOL 'validation_bca_count_bulk_customer_oracle.csv';
SELECT
    b_c.bulk_customer_id || ',' ||
    COUNT(1)
FROM
    sdt_owner.bulk_customer_applications   b_c_a,
    sdt_owner.bulk_customers               b_c
WHERE
    b_c.bulk_customer_id = b_c_a.bulk_customer_id
GROUP BY
    b_c.bulk_customer_id
ORDER BY
    b_c.bulk_customer_id;
SPOOL OFF;

-- Compare amount of bulk submissions with each service request
SPOOL 'validation_bs_count_service_request_oracle.csv';
SELECT
    s_r.service_request_id || ',' ||
    COUNT(1)
FROM
    sdt_owner.bulk_submissions b_s,
    sdt_owner.service_requests s_r
WHERE
    s_r.service_request_id = b_s.service_request_id
GROUP BY
    s_r.service_request_id
ORDER BY
    s_r.service_request_id;
SPOOL OFF;

-- Compare amount of bulk customer applications with each target application
SPOOL 'validation_bca_count_target_app_oracle.csv';
SELECT
    t_a.target_application_id || ',' ||
    COUNT(1)
FROM
    sdt_owner.bulk_customer_applications   b_c_a,
    sdt_owner.target_applications          t_a
WHERE
    t_a.target_application_id = b_c_a.target_application_id
GROUP BY
    t_a.target_application_id
ORDER BY
    t_a.target_application_id;
SPOOL OFF;

-- Compare amount of bulk submissions with each target application
SPOOL 'validation_bs_count_target_app_oracle.csv';
SELECT
    t_a.target_application_id || ',' ||
    COUNT(1)
FROM
    sdt_owner.bulk_submissions     b_s,
    sdt_owner.target_applications  t_a
WHERE
    t_a.target_application_id = b_s.target_application_id
GROUP BY
    t_a.target_application_id
ORDER BY
    t_a.target_application_id;
SPOOL OFF;

-- Compare amount of service routings with each target application
SPOOL 'validation_sr_count_target_app_oracle.csv';
SELECT
    t_a.target_application_id || ',' ||
    COUNT(1)
FROM
    sdt_owner.service_routings     s_r,
    sdt_owner.target_applications  t_a
WHERE
    t_a.target_application_id = s_r.target_application_id
GROUP BY
    t_a.target_application_id
ORDER BY
    t_a.target_application_id;
SPOOL OFF;

-----------------------------------------
-- COMPARE ERROR LOG IDS
-----------------------------------------
SPOOL 'validation_ir_error_log_id_oracle.csv';
SELECT
    ir.individual_request_id || ',' ||
    MAX(el.error_log_id)
FROM
    sdt_owner.individual_requests ir,
    sdt_owner.error_logs el
WHERE
    ir.individual_request_id = el.individual_request_id
GROUP BY
    ir.individual_request_id
ORDER BY
    ir.individual_request_id;
SPOOL OFF;

-----------------------------------------
-- COMPARE SEQUENCE VALUES
-----------------------------------------
SPOOL 'validation_sequences_oracle.txt';
SELECT
(SELECT 'BULK_CUST_APP_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'BULK_CUST_APP_SEQ'),
(SELECT 'BULK_CUST_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'BULK_CUST_SEQ'),
(SELECT 'BULK_SUB_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'BULK_SUB_SEQ'),
(SELECT 'ERR_LOG_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'ERR_LOG_SEQ'),
(SELECT 'ERR_MESG_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'ERR_MESG_SEQ'),
(SELECT 'GLB_PAR_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'GLB_PAR_SEQ'),
(SELECT 'IND_REQ_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'IND_REQ_SEQ'),
(SELECT 'SDT_REF_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'SDT_REF_SEQ'),
(SELECT 'SER_ROU_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'SER_ROU_SEQ'),
(SELECT 'SER_TYP_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'SER_TYP_SEQ'),
(SELECT 'SRV_REQ_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'SRV_REQ_SEQ'),
(SELECT 'TAR_APP_SEQ' || ',' || s.last_number FROM all_sequences s WHERE s.sequence_owner = 'SDT_OWNER' AND s.sequence_name = 'TAR_APP_SEQ')
FROM DUAL;
SPOOL OFF;
