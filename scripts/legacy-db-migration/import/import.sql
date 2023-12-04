SET search_path TO public;
\copy bulk_customers from 'bulk_customers.csv' delimiter ',' csv;
\copy bulk_customer_applications from 'bulk_customer_applications.csv' delimiter ',' csv;
\copy service_requests from 'service_requests.csv' delimiter ',' csv;
\copy bulk_submissions from 'bulk_submissions.csv' delimiter ',' csv;
\copy individual_requests from 'individual_requests.csv' delimiter ',' csv;
\copy error_logs from 'error_logs.csv' delimiter ',' csv;