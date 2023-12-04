SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'service_requests.csv';

SELECT      sr.service_request_id || ','
         || ',' -- Exclude request_payload BLOB column
         || TO_CHAR(sr.request_timestamp, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || ',' -- Exclude response_payload BLOB column
         || TO_CHAR(sr.response_timestamp, 'YYYY-MM-DD HH24:MI:SS.FF') || ','
         || DECODE(sr.request_type, NULL, '', '"' || REPLACE(sr.request_type, '"', '""') || '"') || ','
         || DECODE(sr.sdt_customer_id, NULL, '', '"' || REPLACE(sr.sdt_customer_id, '"', '""') || '"') || ','
         || DECODE(sr.sdt_bulk_reference, NULL, '', '"' || REPLACE(sr.sdt_bulk_reference, '"', '""') || '"') || ','
         || DECODE(sr.server_host_name, NULL, '', '"' || REPLACE(sr.server_host_name, '"', '""') || '"') || ','
         || sr.version_number
FROM     sdt_owner.service_requests sr
ORDER BY sr.service_request_id;

SPOOL OFF;
