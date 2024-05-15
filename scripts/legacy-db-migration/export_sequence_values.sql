SET LINESIZE 1000;
SET TRIMSPOOL ON;
SET HEADING OFF;
SET PAGESIZE 0;
SET FEEDBACK OFF;
SET ECHO OFF;

SPOOL 'data/update_sequence_values.sql';

SELECT   'ALTER SEQUENCE ' || s.sequence_name || ' RESTART WITH ' || s.last_number || ';'
FROM     all_sequences s
WHERE    s.sequence_owner = 'SDT_OWNER'
ORDER BY s.sequence_name;

SPOOL OFF;
