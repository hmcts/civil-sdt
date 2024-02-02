SET LINESIZE 2000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'error_logs.csv';

SELECT      el.error_log_id || ','
         || el.individual_request_id || ','
         || DECODE(el.error_code, NULL, '', '"' || REPLACE(el.error_code, '"', '""') || '"') || ','
         || TO_CHAR(el.created_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || TO_CHAR(el.updated_date, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || el.version_number || ','
         || DECODE(el.error_text, NULL, '', '"' || REPLACE(el.error_text, '"', '""') || '"')
FROM     sdt_owner.error_logs el
ORDER BY el.error_log_id;

SPOOL OFF;
