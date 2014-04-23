#!/bin/bash
#
# needs 3 parameters db user, db password, db sid
#

export ORACLE_SID=$3
export ORACLE_HOME=/u01/app/oracle/product/10.2.0/db_1
$ORACLE_HOME/bin/sqlplus -s $1/$2 << LABEL1

set timing on
set serveroutput on
declare
  nResult NUMBER;
  nRetentionPeriod NUMBER;
  nCommitBatchSize NUMBER;
  vcDate  VARCHAR2(21);
begin
  nCommitBatchSize := 1000;

  SELECT TO_CHAR(SYSDATE,'dd-mon-yyyy hh24:mi:ss')
  INTO vcDate
  FROM DUAL;

  dbms_output.put_line('Processing commenced : '||vcDate);

  SELECT parameter_value
  INTO nRetentionPeriod
  FROM global_parameters
  WHERE parameter_name = 'DATA_RETENTION_PERIOD';

  dbms_output.put_line('Data_retention_period is set to : '||to_char(nRetentionPeriod) );

  vcDate := to_char( (trunc(sysdate) - nRetentionPeriod) ,'dd-mon-yyyy hh24:mi:ss');

  dbms_output.put_line('Purging data earlier than ' || vcDate);

  

  nResult:= purge_bulk_submissions(nRetentionPeriod,nCommitBatchSize);
  dbms_output.put_line('Deleted bulk_submissions : '||nResult);

  nResult:= purge_individual_requests(nRetentionPeriod,nCommitBatchSize);
  dbms_output.put_line('Deleted individual_requests : '||nResult);

  nResult:= purge_error_logs(nRetentionPeriod,nCommitBatchSize);
  dbms_output.put_line('Deleted error_logs : '||nResult);

  nResult:= purge_service_requests(nRetentionPeriod,nCommitBatchSize);
  dbms_output.put_line('Deleted service_requests : '||nResult);

  SELECT TO_CHAR(SYSDATE,'dd-mon-yyyy hh24:mi:ss')
  INTO vcDate
  FROM DUAL;

  dbms_output.put_line('Processing completed : '||vcDate);
end;
/
LABEL1