package uk.gov.moj.sdt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class PurgeNativeCallFunction {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PurgeNativeCallFunction.class);

    public PurgeNativeCallFunction(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.setResultsMapCaseInsensitive(true);

        LOGGER.info("Recreating the Store Procedure...");
        jdbcTemplate.execute(SQL_STORED_PROCEDURE);

    }

    public void executePurgeStoredProc(Integer commitInterval) {
        LOGGER.info("STARTING: the Purge stored procedure with commit interval [{}]", commitInterval);

        /* Test Stored Procedure */
        LOGGER.info("Creating Store Procedure...");
        entityManager.createNativeQuery("call purge(?1)")
                .setParameter(1, commitInterval)
                .executeUpdate();

        LOGGER.info("COMPLETING: the Purge stored procedure.");
    }

    private static final String SQL_STORED_PROCEDURE =
            "DROP SEQUENCE IF EXISTS purge_job_audit_seq;\n" +
                    "DROP SEQUENCE IF EXISTS purge_job_audit_messages_seq;\n" +
                    "DROP TABLE IF EXISTS purge_job_audit CASCADE;\n" +
                    "DROP TABLE IF EXISTS purge_job_audit_messages CASCADE;\n" +
                    "\n" +
                    "CREATE TABLE purge_job_audit\n" +
                    "(purge_job_id NUMERIC UNIQUE NOT NULL,\n" +
                    "job_start_date TIMESTAMP(6),\n" +
                    "job_end_date TIMESTAMP(6),\n" +
                    "retention_date TIMESTAMP(6),\n" +
                    "count_of_error_logs_deleted NUMERIC,\n" +
                    "count_of_individual_requests_deleted NUMERIC,\n" +
                    "count_of_bulk_submissions_deleted NUMERIC,\n" +
                    "count_of_service_requests_deleted NUMERIC,\n" +
                    "success NUMERIC);\n" +
                    "\n" +
                    "CREATE TABLE purge_job_audit_messages\n" +
                    "(purge_job_message_id NUMERIC UNIQUE NOT NULL,\n" +
                    "purge_job_id NUMERIC REFERENCES purge_job_audit (purge_job_id),\n" +
                    "message_date TIMESTAMP(6),\n" +
                    "log_message VARCHAR(256));\n" +
                    "\n" +
                    "-- add indexes --\n" +
                    "CREATE UNIQUE INDEX purge_job_audit_pk ON purge_job_audit (purge_job_id);\n" +
                    "CREATE UNIQUE INDEX purge_job_audit_messages_pk ON purge_job_audit_messages (purge_job_message_id);\n" +
                    "\n" +
                    "-- add constraints --\n" +
                    "ALTER TABLE purge_job_audit ADD CONSTRAINT purge_job_audit_pk PRIMARY KEY USING INDEX purge_job_audit_pk;\n" +
                    "ALTER TABLE purge_job_audit_messages ADD CONSTRAINT purge_job_audit_messages_pk PRIMARY KEY USING INDEX purge_job_audit_messages_pk;\n" +
                    "\n" +
                    "CREATE SEQUENCE purge_job_audit_seq;\n" +
                    "CREATE SEQUENCE purge_job_audit_messages_seq;\n" +
                    "\n" +
                    "CREATE OR REPLACE PROCEDURE purge (i_CommitInterval IN INT) language plpgsql AS $$\n" +
                    "DECLARE i_retention_period VARCHAR(32);\n" +
                    "DECLARE t_retention_date TIMESTAMP;\n" +
                    "DECLARE t_job_start_date TIMESTAMP;\n" +
                    "DECLARE t_job_end_date TIMESTAMP;\n" +
                    "DECLARE i_purge_job_id NUMERIC;\n" +
                    "\n" +
                    "-- cursor1 service requests created before given retention date\n" +
                    "c1 CURSOR\n" +
                    "    FOR SELECT service_request_id\n" +
                    "        FROM service_requests\n" +
                    "        WHERE request_timestamp < t_retention_date;\n" +
                    "\n" +
                    "-- cursor2 bulk submissions with service requests created before given retention date\n" +
                    "c2 CURSOR\n" +
                    "    FOR SELECT bulk_submission_id\n" +
                    "        FROM bulk_submissions\n" +
                    "        WHERE service_request_id IN (\n" +
                    "            SELECT  service_request_id\n" +
                    "            FROM service_requests\n" +
                    "            WHERE request_timestamp < t_retention_date);\n" +
                    "\n" +
                    "-- cursor3 individual requests for bulk submissions\n" +
                    "--   with service requests created before given retention date\n" +
                    "c3 CURSOR\n" +
                    "    FOR SELECT individual_request_id,sdt_request_reference\n" +
                    "        FROM individual_requests\n" +
                    "        WHERE bulk_submission_id IN  (\n" +
                    "            SELECT bulk_submission_id\n" +
                    "            FROM bulk_submissions\n" +
                    "            WHERE service_request_id IN (\n" +
                    "                SELECT service_request_id\n" +
                    "                FROM service_requests\n" +
                    "                WHERE request_timestamp < t_retention_date));\n" +
                    "\n" +
                    "-- cursor4 error logs for individual requests for bulk submissions\n" +
                    "--   with service requests created before given retention date\n" +
                    "c4 CURSOR\n" +
                    "    FOR SELECT error_log_id\n" +
                    "        FROM error_logs\n" +
                    "        WHERE individual_request_id IN (\n" +
                    "            SELECT individual_request_id\n" +
                    "            FROM individual_requests\n" +
                    "            WHERE bulk_submission_id IN (\n" +
                    "                SELECT bulk_submission_id\n" +
                    "                FROM bulk_submissions\n" +
                    "                WHERE service_request_id IN (\n" +
                    "                    SELECT service_request_id\n" +
                    "                    FROM service_requests\n" +
                    "                    WHERE request_timestamp < t_retention_date)));\n" +
                    "\n" +
                    "n_iteration   INT;\n" +
                    "\n" +
                    "BEGIN\n" +
                    "\n" +
                    "    -- retention period\n" +
                    "\tSELECT parameter_value\n" +
                    "    INTO i_retention_period\n" +
                    "    FROM global_parameters\n" +
                    "    WHERE parameter_name = 'DATA_RETENTION_PERIOD';\n" +
                    "\n" +
                    "    -- job start date\n" +
                    "\tSELECT now()\n" +
                    "    INTO t_job_start_date;\n" +
                    "    RAISE NOTICE 'Job start date: %', t_job_start_date;\n" +
                    "\n" +
                    "    -- retention_date calculated using retention period and now\n" +
                    "\tSELECT t_job_start_date::DATE - (i_retention_period || ' DAY')::INTERVAL\n" +
                    "    INTO t_retention_date;\n" +
                    "    RAISE NOTICE 'Retention period date: %', t_retention_date;\n" +
                    "\n" +
                    "\t-- get job audit id\n" +
                    "\tSELECT nextval('purge_job_audit_seq')\n" +
                    "\tINTO i_purge_job_id;\n" +
                    "\n" +
                    "    RAISE NOTICE 'i_purge_job_id is %', i_purge_job_id;\n" +
                    "\n" +
                    "    -- create job audit\n" +
                    "\tINSERT INTO purge_job_audit\n" +
                    "  \t(purge_job_id, job_start_date, retention_date, success)\n" +
                    "  \tVALUES (i_purge_job_id, t_job_start_date, t_retention_date, 0);\n" +
                    "\n" +
                    "    RAISE NOTICE 'Purge retention period is % days, deleting all data prior to %', i_retention_period, t_retention_date;\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t\t\t'Purge retention period is ' || i_retention_period ||\n" +
                    "\t\t\t' days, deleting all data prior to ' || t_retention_date);\n" +
                    "\n" +
                    "    -- delete error logs\n" +
                    "    RAISE NOTICE 'Commencing deletion of error_logs records';\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t\t\t'Commencing deletion of error_logs records');\n" +
                    "\n" +
                    "\tn_iteration := 0;\n" +
                    "    FOR c4_rec IN c4 LOOP\n" +
                    "        DELETE FROM error_logs\n" +
                    "        WHERE error_log_id = c4_rec.error_log_id;\n" +
                    "\n" +
                    "\t    RAISE NOTICE 'Deleted error_logs : %', c4_rec.error_log_id;\n" +
                    "\n" +
                    "        n_iteration := n_iteration + 1;\n" +
                    "    END LOOP;\n" +
                    "\n" +
                    "    RAISE NOTICE '% records deleted from error_logs table.', n_iteration;\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t\t\tn_iteration || ' records deleted from error_logs table.');\n" +
                    "\n" +
                    "\t-- update count of deletions - error logs\n" +
                    "    UPDATE purge_job_audit\n" +
                    "  \tSET count_of_error_logs_deleted=n_iteration\n" +
                    "  \tWHERE purge_job_id=i_purge_job_id;\n" +
                    "\n" +
                    "    RAISE NOTICE 'Commencing deletion of individual_request records';\n" +
                    "\n" +
                    "    n_iteration := 0;\n" +
                    "    FOR c3_rec IN c3 LOOP\n" +
                    "        DELETE FROM individual_requests\n" +
                    "        WHERE individual_request_id = c3_rec.individual_request_id;\n" +
                    "\n" +
                    "        RAISE NOTICE 'Deleted individual_request : %', c3_rec.sdt_request_reference;\n" +
                    "\n" +
                    "\t    INSERT INTO purge_job_audit_messages\n" +
                    "\t\t(purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    \tVALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t\t\t\t'Deleted individual_request : ' || c3_rec.sdt_request_reference);\n" +
                    "\n" +
                    "\t    n_iteration := n_iteration + 1;\n" +
                    "    END LOOP;\n" +
                    "\n" +
                    "    RAISE NOTICE '% records deleted from individual_requests table.', n_iteration;\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t        n_iteration || ' records deleted from individual_requests table.');\n" +
                    "\n" +
                    "\t-- update count of deletions - error logs\n" +
                    "    UPDATE purge_job_audit\n" +
                    "  \tSET count_of_individual_requests_deleted =n_iteration\n" +
                    "  \tWHERE purge_job_id=i_purge_job_id;\n" +
                    "\n" +
                    "    RAISE NOTICE 'Commencing deletion of bulk_submissions records';\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t\t\t'Commencing deletion of bulk_submissions records');\n" +
                    "\n" +
                    "    n_iteration := 0;\n" +
                    "    FOR c2_rec IN c2 LOOP\n" +
                    "        DELETE FROM bulk_submissions\n" +
                    "        WHERE bulk_submission_id = c2_rec.bulk_submission_id;\n" +
                    "\n" +
                    "        RAISE NOTICE 'Deleted bulk_submissions : %', c2_rec.bulk_submission_id;\n" +
                    "\n" +
                    "        n_iteration := n_iteration + 1;\n" +
                    "    END LOOP;\n" +
                    "\n" +
                    "    RAISE NOTICE '% records deleted from bulk_submissions table.', n_iteration;\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t\t\tn_iteration || ' records deleted from bulk_submissions table.');\n" +
                    "\n" +
                    "\tUPDATE purge_job_audit\n" +
                    "  \tSET count_of_bulk_submissions_deleted=n_iteration\n" +
                    "  \tWHERE purge_job_id=i_purge_job_id;\n" +
                    "\n" +
                    "    RAISE NOTICE 'Commencing deletion of service_requests records';\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t        'Commencing deletion of service_requests records');\n" +
                    "\n" +
                    "    n_iteration := 0;\n" +
                    "    FOR c1_rec IN c1 LOOP\n" +
                    "        DELETE FROM service_requests\n" +
                    "        WHERE service_request_id = c1_rec.service_request_id;\n" +
                    "\n" +
                    "\t    RAISE NOTICE 'Deleted service_requests : %', c1_rec.service_request_id;\n" +
                    "\n" +
                    "        n_iteration := n_iteration + 1;\n" +
                    "    END LOOP;\n" +
                    "\n" +
                    "    RAISE NOTICE '% records deleted from service_requests table.', n_iteration;\n" +
                    "\n" +
                    "    INSERT INTO purge_job_audit_messages\n" +
                    "    (purge_job_message_id, purge_job_id, message_date,log_message)\n" +
                    "    VALUES (nextval('purge_job_audit_messages_seq'), i_purge_job_id, now(),\n" +
                    "\t\t\tn_iteration || ' records deleted from service_requests table.');\n" +
                    "\n" +
                    "\tUPDATE purge_job_audit\n" +
                    "  \tSET count_of_service_requests_deleted=n_iteration,job_end_date=now(), success=1\n" +
                    "  \tWHERE purge_job_id=i_purge_job_id;\n" +
                    "\n" +
                    "  END;\n" +
                    "$$";

}
