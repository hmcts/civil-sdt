SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'bulk_submissions.csv';

SELECT      bs.bulk_submission_id || ','
         || bs.bulk_customer_id || ','
         || bs.target_application_id || ','
         || bs.service_request_id || ','
         || DECODE(bs.sdt_bulk_reference, NULL, '', '"' || REPLACE(bs.sdt_bulk_reference, '"', '""') || '"') || ','
         || DECODE(bs.customer_reference, NULL, '', '"' || REPLACE(bs.customer_reference, '"', '""') || '"') || ','
         || TO_CHAR(bs.created_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || bs.number_of_requests || ','
         || DECODE( bs.bulk_submission_status, NULL, '', '"' || REPLACE( bs.bulk_submission_status, '"', '""') || '"') || ','
         || TO_CHAR(bs.completed_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || TO_CHAR(bs.updated_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || DECODE(bs.error_code, NULL, '', '"' || REPLACE(bs.error_code, '"', '""') || '"') || ','
         || DECODE(bs.error_text, NULL, '', '"' || REPLACE(bs.error_text, '"', '""') || '"') || ','
         || bs.version_number || ','
         || '"<not-imported />"' -- Exclude bulk_payload BLOB column but use a default value to prevent not null constraint failure
FROM     sdt_owner.bulk_submissions bs
ORDER BY bs.bulk_submission_id;

SPOOL OFF;
