INSERT INTO bulk_customers
(bulk_customer_id, sdt_customer_id, version_number, ready_for_alternate_service)
VALUES
(100, 10000001, 0, false);

INSERT INTO bulk_customer_applications
(bulk_customer_applications_id, bulk_customer_id, target_application_id, customer_application_id, version_number)
VALUES
(200, 100, 1, 'MC0000000001', 0);

-- Service request for a bulk submission with six individual requests.  Individual requests have
-- all permitted request_status values and no target application responses.
INSERT INTO service_requests
(service_request_id, request_payload, request_timestamp, response_payload, response_timestamp,
 request_type, sdt_customer_id, sdt_bulk_reference, server_host_name, version_number)
VALUES
(300, 'requestPayload1', now(), 'responsePayload1', now(),
 'mcolJudgment', 10000001, 'MCOL-20240627140000-000000001', 'localhost', 0);

INSERT INTO bulk_submissions
(bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 customer_reference, created_date, number_of_requests, bulk_submission_status, completed_date,
 updated_date, error_code, error_text, version_number, bulk_payload)
VALUES
(400, 100, 1, 300, 'MCOL-20240627140000-000000001',
 'CustRef1', now(), 6, 'Validated', null,
 null, null, null, 0, 'bulkPayload1');

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(500, 400, 'CustReqRef1-3', 'Rejected', 'MCOL-20240627140000-000000001',
 3, 'MCOL-20240627140000-000000001-0000003', now(), now(), null,
 0, false, null, 'mcolJudgment', 0,
 'individualPayload1-3', null, null, null);

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(501, 400, 'CustReqRef1-1', 'Accepted', 'MCOL-20240627140000-000000001',
 1, 'MCOL-20240627140000-000000001-0000001', now(), now(), null,
 0, false, null, 'mcolJudgment', 0,
 'individualPayload1-1', null, null, null);

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(502, 400, 'CustReqRef1-6', 'Received', 'MCOL-20240627140000-000000001',
 6, 'MCOL-20240627140000-000000001-0000006', now(), now(), null,
 0, false, null, 'mcolJudgment', 0,
 'individualPayload1-6', null, null, null);

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(503, 400, 'CustReqRef1-2', 'Initially Accepted', 'MCOL-20240627140000-000000001',
 2, 'MCOL-20240627140000-000000001-0000002', now(), now(), null,
 0, false, null, 'mcolJudgment', 0,
 'individualPayload1-2', null, null, null);

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(504, 400, 'CustReqRef1-4', 'Awaiting Data', 'MCOL-20240627140000-000000001',
 4, 'MCOL-20240627140000-000000001-0000004', now(), now(), null,
 0, false, null, 'mcolJudgment', 0,
 'individualPayload1-4', null, null, null);

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(505, 400, 'CustReqRef1-5', 'Forwarded', 'MCOL-20240627140000-000000001',
 5, 'MCOL-20240627140000-000000001-0000005', now(), now(), null,
 1, false, null, 'mcolJudgment', 0,
 'individualPayload1-5', null, null, null);

INSERT INTO error_logs
(error_log_id, individual_request_id, error_code, created_date, updated_date,
 version_number, error_text)
VALUES
(600, 500, '99', now(), null,
 0, 'Error 99 error text');

UPDATE individual_requests
SET error_log_id = 600
WHERE individual_request_id = 500;

-- Service request for a bulk submission with three individual requests.
-- Two of the individual requests have target application responses.
INSERT INTO service_requests
(service_request_id, request_payload, request_timestamp, response_payload, response_timestamp,
 request_type, sdt_customer_id, sdt_bulk_reference, server_host_name, version_number)
VALUES
(301, 'requestPayload2', now(), 'responsePayload2', now(),
 'mcolClaim', 10000001, 'MCOL-20240627140000-000000002', 'localhost', 0);

INSERT INTO bulk_submissions
(bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 customer_reference, created_date, number_of_requests, bulk_submission_status, completed_date,
 updated_date, error_code, error_text, version_number, bulk_payload)
VALUES
(401, 100, 1, 301, 'MCOL-20240627140000-000000002',
 'CustRef2', now(), 3, 'Validated', null,
 null, null, null, 0, 'bulkPayload2');

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(510, 401, 'CustReqRef2-3', 'Accepted', 'MCOL-20240627140000-000000002',
 3, 'MCOL-20240627140000-000000002-0000003', now(), now(), null,
 0, false, null, 'mcolClaim', 0,
 'individualPayload2-3', 'targetAppResponse2-3', null, null);

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(511, 401, 'CustReqRef2-1', 'Accepted', 'MCOL-20240627140000-000000002',
 1, 'MCOL-20240627140000-000000002-0000001', now(), now(), null,
 0, false, null, 'mcolClaim', 0,
 'individualPayload2-1', 'targetAppResponse2-1', null, null);

INSERT INTO individual_requests
(individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference,
 line_number, sdt_request_reference, created_date, updated_date, completed_date,
 forwarding_attempts, dead_letter, internal_system_error, request_type, version_number,
 individual_payload, target_application_response, error_log_id, issued_date
 )
VALUES
(512, 401, 'CustReqRef2-2', 'Initially Accepted', 'MCOL-20240627140000-000000002',
 2, 'MCOL-20240627140000-000000002-0000002', now(), now(), null,
 0, false, null, 'mcolClaim', 0,
 'individualPayload2-2', null, null, null);
