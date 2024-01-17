SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'individual_requests.csv';

SELECT      ir.individual_request_id || ','
         || ir.bulk_submission_id || ','
         || DECODE(ir.customer_request_ref, NULL, '', '"' || REPLACE(ir.customer_request_ref, '"', '""') || '"') || ','
         || DECODE(ir.request_status, NULL, '', '"' || REPLACE(ir.request_status, '"', '""') || '"') || ','
         || DECODE(ir.sdt_bulk_reference, NULL, '', '"' || REPLACE(ir.sdt_bulk_reference, '"', '""') || '"') || ','
         || ir.line_number || ','
         || DECODE(ir.sdt_request_reference, NULL, '', '"' || REPLACE(ir.sdt_request_reference, '"', '""') || '"') || ','
         || TO_CHAR(ir.created_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || TO_CHAR(ir.updated_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || TO_CHAR(ir.completed_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || ir.forwarding_attempts || ','
         || DECODE(ir.dead_letter, NULL, '', '"' || REPLACE(ir.dead_letter, '"', '""') || '"') || ','
         || DECODE(ir.internal_system_error, NULL, '', '"' || REPLACE(REPLACE(ir.internal_system_error, CHR(10), ' '), '"', '""') || '"') || ','
         || DECODE(ir.request_type, NULL, '', '"' || REPLACE(ir.request_type, '"', '""') || '"') || ','
         || ir.version_number || ','
         || ',' -- Exclude individual_payload BLOB column
         || ',' -- Exclude target_application_response BLOB column
         || (SELECT MAX(el.error_log_id) FROM sdt_owner.error_logs el WHERE el.individual_request_id = ir.individual_request_id) || ','
         -- Null for new issued_date_column
FROM     sdt_owner.individual_requests ir
ORDER BY ir.individual_request_id;

SPOOL OFF;
