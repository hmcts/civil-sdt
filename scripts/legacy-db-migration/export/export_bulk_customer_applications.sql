SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'bulk_customer_applications.csv';

SELECT      bca.bulk_customer_applications_id || ','
         || bca.bulk_customer_id || ','
         || bca.target_application_id || ','
         || DECODE(bca.customer_application_id, NULL, '', '"' || REPLACE(bca.customer_application_id, '"', '""') || '"') || ','
         || bca.version_number
FROM     sdt_owner.bulk_customer_applications bca
ORDER BY bca.bulk_customer_applications_id;

SPOOL OFF;
