-----------------------------------------
-- COMPARE ROW COUNTS
-----------------------------------------
\copy (SELECT (SELECT 'BULK_CUSTOMER_APPLICATIONS' || ',' || COUNT(*) FROM public.bulk_customer_applications), (SELECT 'BULK_CUSTOMERS' || ',' || COUNT(*) FROM public.bulk_customers), (SELECT 'BULK_SUBMISSIONS' || ',' || COUNT(*) FROM public.bulk_submissions), (SELECT 'ERROR_LOGS' || ',' || COUNT(*) FROM public.error_logs), (SELECT 'GLOBAL_PARAMETERS' || ',' || COUNT(*) FROM public.global_parameters), (SELECT 'INDIVIDUAL_REQUESTS' || ',' || COUNT(*) FROM public.individual_requests), (SELECT 'SERVICE_REQUESTS' || ',' || COUNT(*) FROM public.service_requests), (SELECT 'SERVICE_ROUTINGS' || ',' || COUNT(*) FROM public.service_routings), (SELECT 'SERVICE_TYPES' || ',' || COUNT(*) FROM public.service_types), (SELECT 'TARGET_APPLICATIONS' || ',' || COUNT(*) FROM public.target_applications)) to 'validation_row_counts.txt';

-----------------------------------------
-- COMPARE ROW_COUNT FOR EACH TABLE JOIN
-----------------------------------------
-- Compare amount of error logs with each individual request
\copy (SELECT i_r.individual_request_id, COUNT(1) FROM public.error_logs e_l, public.individual_requests i_r WHERE i_r.individual_request_id = e_l.individual_request_id GROUP BY i_r.individual_request_id ORDER BY i_r.individual_request_id) to 'validation_el_count_individual_req.csv' csv;

-- Compare BULK_SUBMISSIONS.NUMBER_OF_REQUESTS with the actual counts in INDIVIDUAL_REQUESTS
\copy (SELECT b_s.sdt_bulk_reference, b_s.number_of_requests, COUNT(1), b_s.number_of_requests = COUNT(1) FROM public.bulk_submissions b_s, public.individual_requests i_r WHERE b_s.sdt_bulk_reference = i_r.sdt_bulk_reference GROUP BY b_s.sdt_bulk_reference, b_s.number_of_requests ORDER BY b_s.sdt_bulk_reference) to 'validation_ir_count_bulk_submission.csv' csv;

-- Compare amount of bulk submissions with each bulk customer
\copy (SELECT b_c.bulk_customer_id, COUNT(1) FROM public.bulk_submissions b_s, public.bulk_customers b_c WHERE b_c.bulk_customer_id = b_s.bulk_customer_id GROUP BY b_c.bulk_customer_id ORDER BY b_c.bulk_customer_id) to 'validation_bs_count_bulk_customer.csv' csv;

-- Compare amount of bulk customer applications with each bulk customer
\copy (SELECT b_c.bulk_customer_id, COUNT(1) FROM public.bulk_customer_applications b_c_a, public.bulk_customers b_c WHERE b_c.bulk_customer_id = b_c_a.bulk_customer_id GROUP BY b_c.bulk_customer_id ORDER BY b_c.bulk_customer_id) to 'validation_bca_count_bulk_customer.csv' csv;

-- Compare amount of bulk submissions with each service request
\copy (SELECT s_r.service_request_id, COUNT(1) FROM public.bulk_submissions b_s, public.service_requests s_r WHERE s_r.service_request_id = b_s.service_request_id GROUP BY s_r.service_request_id ORDER BY s_r.service_request_id) to 'validation_bs_count_service_request.csv' csv;

-- Compare amount of bulk customer applications with each target application
\copy (SELECT t_a.target_application_id, COUNT(1) FROM public.bulk_customer_applications b_c_a, public.target_applications t_a WHERE t_a.target_application_id = b_c_a.target_application_id GROUP BY t_a.target_application_id ORDER BY t_a.target_application_id) to 'validation_bca_count_target_app.csv' csv;

-- Compare amount of bulk submissions with each target application
\copy (SELECT t_a.target_application_id, COUNT(1) FROM public.bulk_submissions b_s, public.target_applications t_a WHERE t_a.target_application_id = b_s.target_application_id GROUP BY t_a.target_application_id ORDER BY t_a.target_application_id) to 'validation_bs_count_target_app.csv' csv;

-- Compare amount of service routings with each target application
\copy (SELECT t_a.target_application_id, COUNT(1) FROM public.service_routings s_r, public.target_applications t_a WHERE t_a.target_application_id = s_r.target_application_id GROUP BY t_a.target_application_id ORDER BY t_a.target_application_id) to 'validation_sr_count_target_app.csv' csv;