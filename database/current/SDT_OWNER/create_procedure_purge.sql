CREATE OR REPLACE FUNCTION sdt_owner.purge (i_CommitInterval  IN INT) RETURNS VOID language plpgsql AS $$ 
DECLARE
--  fh1               utl_file.file_type;
DECLARE i_RetentionPeriod VARCHAR(32);
DECLARE t_retention_date TIMESTAMP;
DECLARE t_job_start_date TIMESTAMP;
DECLARE t_job_end_date TIMESTAMP;
DECLARE i_purge_job_id NUMERIC;

  c1 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT service_request_id
               FROM sdt_owner.service_requests
               WHERE request_timestamp < t_retention_date;

  c2 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT bulk_submission_id
               FROM sdt_owner.bulk_submissions
               WHERE service_request_id IN (
                   SELECT  service_request_id
                   FROM sdt_owner.service_requests
                   WHERE request_timestamp < t_retention_date);

  c3 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT individual_request_id,sdt_request_reference
               FROM sdt_owner.individual_requests
               WHERE bulk_submission_id IN  (
                   SELECT bulk_submission_id
                   FROM sdt_owner.bulk_submissions
                   WHERE service_request_id IN (
                       SELECT service_request_id
                       FROM sdt_owner.service_requests
                       WHERE request_timestamp < t_retention_date));

  c4 CURSOR (nParam DOUBLE PRECISION)
            FOR SELECT error_log_id
               FROM sdt_owner.error_logs
               WHERE individual_request_id IN (
                   SELECT individual_request_id
                   FROM sdt_owner.individual_requests
                   WHERE bulk_submission_id IN (
                       SELECT bulk_submission_id
                       FROM sdt_owner.bulk_submissions
                       WHERE service_request_id IN (
                           SELECT service_request_id
                           FROM sdt_owner.service_requests
                           WHERE request_timestamp < t_retention_date)));

  nIteration   INT;
  BEGIN

	SELECT parameter_value
    INTO i_RetentionPeriod
    FROM sdt_owner.global_parameters
    WHERE parameter_name = 'DATA_RETENTION_PERIOD';

	SELECT now()
    INTO t_job_start_date;
    RAISE NOTICE 'Job start date: %', t_job_start_date;

	SELECT t_job_start_date::TIMESTAMP - (i_RetentionPeriod || ' DAY')::INTERVAL
    INTO t_retention_date;
    RAISE NOTICE 'Retention period date: %', t_retention_date;

	INSERT INTO sdt_owner.purge_job_audit
  	( job_start_date, success)
  	VALUES (t_job_start_date, 0);

	SELECT max(purge_job_id)
    INTO i_purge_job_id
    FROM sdt_owner.purge_job_audit;


    RAISE NOTICE 'Purge retention period is % days, deleting all data prior to %',
                          i_RetentionPeriod,
                          t_retention_date;
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), 'Purge retention period is ' || i_RetentionPeriod ||
			' days, deleting all data prior to ' || t_retention_date);

    RAISE NOTICE 'Commencing deletion of error_logs records';
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), 'Commencing deletion of error_logs records');

    nIteration := 0;
    FOR c4_rec IN c4(i_RetentionPeriod) LOOP
      DELETE FROM sdt_owner.error_logs
      WHERE error_log_id = c4_rec.error_log_id;
      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from error_logs table.', nIteration;
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), nIteration || ' records deleted from error_logs table.');
    UPDATE sdt_owner.purge_job_audit
  	SET count_of_error_logs=nIteration
  	WHERE purge_job_id=i_purge_job_id;

    RAISE NOTICE 'Commencing deletion of individual_request records';
    nIteration := 0;
    FOR c3_rec IN c3(i_RetentionPeriod) LOOP
      DELETE FROM sdt_owner.individual_requests
      WHERE individual_request_id = c3_rec.individual_request_id;
      RAISE NOTICE 'Deleted individual_request : %', c3_rec.sdt_request_reference;
      INSERT INTO sdt_owner.purge_job_audit_messages
      (purge_job_id, created_date,log_message)
      VALUES (i_purge_job_id, now(), 'Deleted individual_request : ' || c3_rec.sdt_request_reference);

      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from individual_requests table.', nIteration;
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), nIteration || ' records deleted from individual_requests table.');
    UPDATE sdt_owner.purge_job_audit
  	SET count_of_individual_requests=nIteration
  	WHERE purge_job_id=i_purge_job_id;

    RAISE NOTICE 'Commencing deletion of bulk_submissions records';
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), 'Commencing deletion of bulk_submissions records');

    nIteration := 0;
    FOR c2_rec IN c2(i_RetentionPeriod) LOOP
      DELETE FROM sdt_owner.bulk_submissions
      WHERE bulk_submission_id = c2_rec.bulk_submission_id;

      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from bulk_submissions table.', nIteration;
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), nIteration || ' records deleted from bulk_submissions table.');
    UPDATE sdt_owner.purge_job_audit
  	SET count_of_bulk_submissions=nIteration
  	WHERE purge_job_id=i_purge_job_id;

    RAISE NOTICE 'Commencing deletion of service_requests records';
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), 'Commencing deletion of service_requests records');

    nIteration := 0;
    FOR c1_rec IN c1(i_RetentionPeriod) LOOP
      DELETE FROM sdt_owner.service_requests
      WHERE service_request_id = c1_rec.service_request_id;
      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        /* COMMIT; */
      END IF;
    END LOOP;
    /* COMMIT; */
    RAISE NOTICE '% records deleted from service_requests table.', nIteration;
    INSERT INTO sdt_owner.purge_job_audit_messages
    (purge_job_id, created_date,log_message)
    VALUES (i_purge_job_id, now(), nIteration || ' records deleted from service_requests table.');
    UPDATE sdt_owner.purge_job_audit
  	SET count_of_service_requests=nIteration
  	WHERE purge_job_id=i_purge_job_id;

	UPDATE sdt_owner.purge_job_audit
  	SET job_end_date=now(), success=1
  	WHERE purge_job_id=i_purge_job_id;

  END;
$$
