INSERT INTO bulk_customers (
 bulk_customer_id, sdt_customer_id, version_number
) VALUES (
 1, '10000001', 0
);

-- Bulk Submission completed: All accepted
INSERT INTO service_requests (
 service_request_id, request_timestamp, version_number
) VALUES (
 101, now(), 0
);

INSERT INTO bulk_submissions (
 bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 created_date, updated_date, completed_date, number_of_requests, bulk_submission_status,
 bulk_payload, version_number
) VALUES (
 201, 1, 1, 101, 'MCOL-20240528120000-000000001',
 now(), null, null, 1, 'Validated',
 'empty', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 301, 201, 'Forwarded', 'MCOL-20240528120000-000000001', 1,
 'MCOL-20240528120000-000000001-0000001', now(), null, null, 1,
 false, 'mcolClaim', 0
);

-- Bulk Submission completed: All rejected
INSERT INTO service_requests (
 service_request_id, request_timestamp, version_number
) VALUES (
 102, now(), 0
);

INSERT INTO bulk_submissions (
 bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 created_date, updated_date, completed_date, number_of_requests, bulk_submission_status,
 bulk_payload, version_number
) VALUES (
 202, 1, 1, 102, 'MCOL-20240528120000-000000002',
 now(), null, null, 1, 'Validated',
 'empty', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 302, 202, 'Forwarded', 'MCOL-20240528120000-000000002', 1,
 'MCOL-20240528120000-000000002-0000001', now(), null, null, 1,
 false, 'mcolClaim', 0
);

-- Bulk Submission completed: All accepted or rejected
INSERT INTO service_requests (
 service_request_id, request_timestamp, version_number
) VALUES (
 103, now(), 0
);

INSERT INTO bulk_submissions (
 bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 created_date, updated_date, completed_date, number_of_requests, bulk_submission_status,
 bulk_payload, version_number
) VALUES (
 203, 1, 1, 103, 'MCOL-20240528120000-000000003',
 now(), null, null, 2, 'Validated',
 'empty', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 303, 203, 'Forwarded', 'MCOL-20240528120000-000000003', 1,
 'MCOL-20240528120000-000000003-0000001', now(), null, null, 1,
 false, 'mcolClaim', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 304, 203, 'Accepted', 'MCOL-20240528120000-000000003', 2,
 'MCOL-20240528120000-000000003-0000002', now(), now(), now(), 0,
 false, 'mcolClaim', 0
);

-- Bulk Submission not completed: Not all accepted
INSERT INTO service_requests (
 service_request_id, request_timestamp, version_number
) VALUES (
 104, now(), 0
);

INSERT INTO bulk_submissions (
 bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 created_date, updated_date, completed_date, number_of_requests, bulk_submission_status,
 bulk_payload, version_number
) VALUES (
 204, 1, 1, 104, 'MCOL-20240528120000-000000004',
 now(), null, null, 2, 'Validated',
 'empty', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 305, 204, 'Forwarded', 'MCOL-20240528120000-000000004', 1,
 'MCOL-20240528120000-000000004-0000001', now(), null, null, 1,
 false, 'mcolClaim', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 306, 204, 'Forwarded', 'MCOL-20240528120000-000000004', 2,
 'MCOL-20240528120000-000000004-0000002', now(), null, null, 1,
 false, 'mcolClaim', 0
);

-- Bulk Submission not completed: Not all rejected
INSERT INTO service_requests (
 service_request_id, request_timestamp, version_number
) VALUES (
 105, now(), 0
);

INSERT INTO bulk_submissions (
 bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 created_date, updated_date, completed_date, number_of_requests, bulk_submission_status,
 bulk_payload, version_number
) VALUES (
 205, 1, 1, 105, 'MCOL-20240528120000-000000005',
 now(), null, null, 2, 'Validated',
 'empty', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 307, 205, 'Forwarded', 'MCOL-20240528120000-000000005', 1,
 'MCOL-20240528120000-000000005-0000001', now(), null, null, 1,
 false, 'mcolClaim', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 308, 205, 'Forwarded', 'MCOL-20240528120000-000000005', 2,
 'MCOL-20240528120000-000000005-0000002', now(), null, null, 1,
 false, 'mcolClaim', 0
);

-- Bulk Submission not completed: Not all accepted or rejected
INSERT INTO service_requests (
 service_request_id, request_timestamp, version_number
) VALUES (
 106, now(), 0
);

INSERT INTO bulk_submissions (
 bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 created_date, updated_date, completed_date, number_of_requests, bulk_submission_status,
 bulk_payload, version_number
) VALUES (
 206, 1, 1, 106, 'MCOL-20240528120000-000000006',
 now(), null, null, 3, 'Validated',
 'empty', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 309, 206, 'Forwarded', 'MCOL-20240528120000-000000006', 1,
 'MCOL-20240528120000-000000006-0000001', now(), null, null, 1,
 false, 'mcolClaim', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 310, 206, 'Accepted', 'MCOL-20240528120000-000000006', 2,
 'MCOL-20240528120000-000000006-0000002', now(), now(), now(), 0,
 false, 'mcolClaim', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 311, 206, 'Forwarded', 'MCOL-20240528120000-000000006', 3,
 'MCOL-20240528120000-000000006-0000003', now(), null, null, 1,
 false, 'mcolClaim', 0
);

-- Bulk Submission not completed: All resubmit (no accepted or rejected)
INSERT INTO service_requests (
 service_request_id, request_timestamp, version_number
) VALUES (
 107, now(), 0
);

INSERT INTO bulk_submissions (
 bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference,
 created_date, updated_date, completed_date, number_of_requests, bulk_submission_status,
 bulk_payload, version_number
) VALUES (
 207, 1, 1, 107, 'MCOL-20240528120000-000000007',
 now(), null, null, 1, 'Validated',
 'empty', 0
);

INSERT INTO individual_requests (
 individual_request_id, bulk_submission_id, request_status, sdt_bulk_reference, line_number,
 sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts,
 dead_letter, request_type, version_number
) VALUES (
 312, 207, 'Forwarded', 'MCOL-20240528120000-000000007', 1,
 'MCOL-20240528120000-000000007-0000001', now(), null, null, 1,
 false, 'mcolClaim', 0
);
