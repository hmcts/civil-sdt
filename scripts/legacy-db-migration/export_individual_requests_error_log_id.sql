SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'data/update_individual_requests_error_log_id.sql';

SELECT   'UPDATE individual_requests SET error_log_id = ' ||
         MAX(el.error_log_id) ||
         ' WHERE individual_request_id = ' ||
         ir.individual_request_id ||
         ';'
FROM     sdt_owner.individual_requests ir
,        sdt_owner.error_logs el
WHERE    ir.individual_request_id = el.individual_request_id
GROUP BY ir.individual_request_id;

SPOOL OFF;
