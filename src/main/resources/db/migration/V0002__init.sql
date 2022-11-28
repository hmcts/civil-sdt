DROP TABLE IF EXISTS sdt_owner.purge_job_audit;
DROP TABLE IF EXISTS sdt_owner.purge_job_audit_messages;

CREATE TABLE sdt_owner.purge_job_audit
(purge_job_id SERIAL PRIMARY KEY,
job_start_date TIMESTAMP(6),
job_end_date TIMESTAMP(6),
retention_date TIMESTAMP(6),
count_of_error_logs NUMERIC,
count_of_individual_requests NUMERIC,
count_of_bulk_submissions NUMERIC,
count_of_service_requests NUMERIC,
success NUMERIC);

CREATE TABLE sdt_owner.purge_job_audit_messages
(purge_job_message_id SERIAL PRIMARY KEY,
purge_job_id NUMERIC,
created_date TIMESTAMP(6),
log_message VARCHAR(256));
