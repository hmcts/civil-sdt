CREATE OR REPLACE FUNCTION sdt_owner.purge (i_CommitInterval  IN INT) RETURNS VOID language plpgsql AS $$ 
DECLARE
--  fh1               utl_file.file_type;
--  DECLARE i_RetentionPeriod VARCHAR(32);
    DECLARE i_RetentionPeriod INT := 90;
  filename          VARCHAR(50);
  c1 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT service_request_id
               FROM service_requests
               WHERE EXTRACT(DAY FROM now() - request_timestamp) > i_RetentionPeriod;

  c2 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT bulk_submission_id
               FROM bulk_submissions
               WHERE service_request_id IN (
                   SELECT  service_request_id
                   FROM service_requests
                   WHERE EXTRACT(DAY FROM now() - request_timestamp) > i_RetentionPeriod);

  c3 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT individual_request_id,sdt_request_reference
               FROM individual_requests
               WHERE bulk_submission_id IN  (
                   SELECT bulk_submission_id
                   FROM bulk_submissions
                   WHERE service_request_id IN (
                       SELECT service_request_id
                       FROM service_requests
                       WHERE EXTRACT(DAY FROM now() - request_timestamp) > i_RetentionPeriod));

  c4 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT error_log_id
               FROM error_logs
               WHERE individual_request_id IN (
                   SELECT individual_request_id
                   FROM individual_requests
                   WHERE bulk_submission_id IN (
                       SELECT bulk_submission_id
                       FROM bulk_submissions
                       WHERE service_request_id IN (
                           SELECT service_request_id
                           FROM service_requests
                           WHERE EXTRACT(DAY FROM now() - request_timestamp) > i_RetentionPeriod)));


  nIteration   INT;
  BEGIN
    SELECT parameter_value
    INTO i_RetentionPeriod
    FROM global_parameters
    WHERE parameter_name = 'DATA_RETENTION_PERIOD';
--    filename := 'sdt_purge_log'||to_char(current_timestamp,'yyyymmddhh24miss')||'.log';
--    fh1 := UTL_FILE.FOPEN('SDT_PURGE_LOGDIR',filename,'W',255);
--     UTL_FILE.PUT_LINE(fh1,'Purge logfile generated on '
--                          || to_char(current_timestamp,'DD-MON-YYYY HH24:MI:SS'));
    RAISE NOTICE 'Purge logfile generated on %', TO_CHAR(current_timestamp,'DD-MON-YYYY HH24:MI:SS');
--     UTL_FILE.PUT_LINE(fh1,'Purge retention period applied is '
--                          || to_char(i_RetentionPeriod)
--                          || ' days, meaning we are deleting all data prior to '
--                          || to_char(TRUNC(CURRENT_TIMESTAMP) - i_RetentionPeriod,'DD-MON-YYYY HH24:MI:SS'));

-- RAISE NOTICE 'Purge retention period applied is % days, meaning we are deleting all data prior to %',
--                          i_RetentionPeriod,
--                          TO_CHAR(TRUNC(CURRENT_TIMESTAMP) - i_RetentionPeriod,'DD-MON-YYYY HH24:MI:SS');
    RAISE NOTICE 'Commencing deletion of error_logs records';
    nIteration := 0;
    FOR c4_rec IN c4(i_RetentionPeriod) LOOP
      DELETE FROM error_logs
      WHERE error_log_id = c4_rec.error_log_id;
      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from error_logs table.', nIteration;


    RAISE NOTICE 'Commencing deletion of individual_request records';
    nIteration := 0;
    FOR c3_rec IN c3(i_RetentionPeriod) LOOP
      DELETE FROM individual_requests
      WHERE individual_request_id = c3_rec.individual_request_id;
        RAISE NOTICE 'Deleted individual_request : %', c3_rec.sdt_request_reference;
      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from individual_requests table.', nIteration;

    RAISE NOTICE 'Commencing deletion of bulk_submissions records';
    nIteration := 0;
    FOR c2_rec IN c2(i_RetentionPeriod) LOOP
      DELETE FROM bulk_submissions
      WHERE bulk_submission_id = c2_rec.bulk_submission_id;

      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from bulk_submissions table.', nIteration;

    RAISE NOTICE 'Commencing deletion of service_requests records';
    nIteration := 0;
    FOR c1_rec IN c1(i_RetentionPeriod) LOOP
      DELETE FROM service_requests
      WHERE service_request_id = c1_rec.service_request_id;
      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from service_requests table.', nIteration;

--  utl_file.FCLOSE(fh1);
  END;
$$
