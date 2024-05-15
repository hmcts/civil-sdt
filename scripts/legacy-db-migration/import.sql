BEGIN;

\i 'data/BULK_CUSTOMERS_output.sql'
\i 'data/BULK_CUSTOMER_APPLICATIONS_output.sql'
\i 'data/SERVICE_REQUESTS_output.sql'
\i 'data/BULK_SUBMISSIONS_output.sql'
\i 'data/INDIVIDUAL_REQUESTS_output.sql'
\i 'data/ERROR_LOGS_output.sql'
\i 'data/update_individual_requests_error_log_id.sql'
\i 'data/update_sequence_values.sql'

COMMIT;
