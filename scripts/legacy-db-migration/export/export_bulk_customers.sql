SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'bulk_customers.csv';

SELECT      bc.bulk_customer_id || ','
         || bc.sdt_customer_id || ','
         || bc.version_number || ','
         || 'false' -- false for new ready_for_alternate_service column
FROM     sdt_owner.bulk_customers bc
ORDER BY bc.bulk_customer_id;

SPOOL OFF;
