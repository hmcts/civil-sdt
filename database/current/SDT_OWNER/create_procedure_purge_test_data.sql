SET search_path TO sdt_owner;

INSERT INTO service_requests(
	service_request_id, request_payload, request_timestamp, response_payload, response_timestamp, request_type, sdt_customer_id, sdt_bulk_reference, server_host_name, version_number)
	VALUES (101, 'test 1', '2022-11-18 09:01:02', 'unknown payload 1', '2022-11-18 09:02:03', 'type1', 901, 'REF18', 'www.host1.com', 1);
INSERT INTO service_requests(
	service_request_id, request_payload, request_timestamp, response_payload, response_timestamp, request_type, sdt_customer_id, sdt_bulk_reference, server_host_name, version_number)
	VALUES (102, 'test 2', '2022-11-18 10:02:03', 'unknown payload 2', '2022-11-18 10:03:04', 'type2', 902, 'REF18', 'www.host2.com', 1);
INSERT INTO service_requests(
	service_request_id, request_payload, request_timestamp, response_payload, response_timestamp, request_type, sdt_customer_id, sdt_bulk_reference, server_host_name, version_number)
	VALUES (103, 'test 3', '2022-11-18 11:03:04', 'unknown payload 3', '2022-11-18 11:04:05', 'type3', 903, 'REF18', 'www.host3.com', 1);
INSERT INTO service_requests(
	service_request_id, request_payload, request_timestamp, response_payload, response_timestamp, request_type, sdt_customer_id, sdt_bulk_reference, server_host_name, version_number)
	VALUES (104, 'test 4', '2022-11-18 12:04:05', 'unknown payload 4', '2022-11-18 12:11:13', 'type4', 904, 'REF18', 'www.host4.com', 1);


INSERT INTO bulk_customers(
	bulk_customer_id, sdt_customer_id, version_number)
	VALUES (201, 901, 1);


INSERT INTO bulk_submissions(
	bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference, customer_reference, created_date, number_of_requests, bulk_submission_status, completed_date, updated_date, error_code, error_text, version_number, bulk_payload)
	VALUES (301, 201, 1, 101, 'BR001', 'CR001', '2022-11-18 06:30:45', 4, 'STATUS1', '2022-11-19 11:12:13', '2022-11-19 11:29:52', 'ERR23', 'Error message 3', 1, 'Payload 3355');
INSERT INTO bulk_submissions(
	bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference, customer_reference, created_date, number_of_requests, bulk_submission_status, completed_date, updated_date, error_code, error_text, version_number, bulk_payload)
	VALUES (302, 201, 1, 101, 'BR002', 'CR002', '2022-11-18 06:33:48', 4, 'STATUS2', '2022-11-19 11:15:24', '2022-11-19 11:24:45', 'ERR23', 'Error message 4', 1, 'Payload 3356');
INSERT INTO bulk_submissions(
	bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference, customer_reference, created_date, number_of_requests, bulk_submission_status, completed_date, updated_date, error_code, error_text, version_number, bulk_payload)
	VALUES (303, 201, 1, 101, 'BR003', 'CR003', '2022-11-18 06:40:53', 4, 'STATUS3', '2022-11-19 11:20:36', '2022-11-19 11:38:52', 'ERR23', 'Error message 5', 1, 'Payload 3357');
INSERT INTO bulk_submissions(
	bulk_submission_id, bulk_customer_id, target_application_id, service_request_id, sdt_bulk_reference, customer_reference, created_date, number_of_requests, bulk_submission_status, completed_date, updated_date, error_code, error_text, version_number, bulk_payload)
	VALUES (304, 201, 1, 101, 'BR004', 'CR004', '2022-11-18 06:45:57', 4, 'STATUS4', '2022-11-19 11:26:45', '2022-11-19 11:39:57', 'ERR23', 'Error message 6', 1, 'Payload 3358');


INSERT INTO individual_requests(
	individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference, line_number, sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts, dead_letter, internal_system_error, request_type, version_number, individual_payload, target_application_response)
	VALUES (401, 301, 'CR101', 'STATUS3', 'BREF11', 1, 'RREF001', '2022-11-18 14:35:45', '2022-11-18 14:35:56', '2022-11-18 14:53:56', 2, 'N', 'Int Syst Err', 'ReqType1', 1, 'Ind pay 1', 'Targ app 1');
INSERT INTO individual_requests(
	individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference, line_number, sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts, dead_letter, internal_system_error, request_type, version_number, individual_payload, target_application_response)
	VALUES (402, 301, 'CR101', 'STATUS2', 'BREF12', 2, 'RREF001', '2022-11-18 14:38:47', '2022-11-18 14:39:56', '2022-11-18 14:52:45', 2, 'N', 'Int Syst Err', 'ReqType1', 1, 'Ind pay 2', 'Targ app 2');
INSERT INTO individual_requests(
	individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference, line_number, sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts, dead_letter, internal_system_error, request_type, version_number, individual_payload, target_application_response)
	VALUES (403, 301, 'CR101', 'STATUS4', 'BREF13', 3, 'RREF001', '2022-11-18 14:43:49', '2022-11-18 14:46:59', '2022-11-18 14:50:34', 2, 'N', 'Int Syst Err', 'ReqType1', 1, 'Ind pay 3', 'Targ app 3');
INSERT INTO individual_requests(
	individual_request_id, bulk_submission_id, customer_request_ref, request_status, sdt_bulk_reference, line_number, sdt_request_reference, created_date, updated_date, completed_date, forwarding_attempts, dead_letter, internal_system_error, request_type, version_number, individual_payload, target_application_response)
	VALUES (404, 301, 'CR101', 'STATUS1', 'BREF14', 4, 'RREF001', '2022-11-18 14:48:53', '2022-11-18 14:57:34', '2022-11-18 14:58:21', 2, 'N', 'Int Syst Err', 'ReqType1', 1, 'Ind pay 4', 'Targ app 4');

INSERT INTO error_logs(
	error_log_id, individual_request_id, error_code, created_date, updated_date, version_number, error_text)
	VALUES (501, 401, 'ERR01', '2022-11-18 14:35:45', '2022-11-18 14:35:45', 1, 'Error text 1');
INSERT INTO error_logs(
	error_log_id, individual_request_id, error_code, created_date, updated_date, version_number, error_text)
	VALUES (502, 402, 'ERR02', '2022-11-18 14:38:47', '2022-11-18 14:38:47', 1, 'Error text 2');
INSERT INTO error_logs(
	error_log_id, individual_request_id, error_code, created_date, updated_date, version_number, error_text)
	VALUES (503, 403, 'ERR03', '2022-11-18 14:43:49', '2022-11-18 14:43:49', 1, 'Error text 3');
INSERT INTO error_logs(
	error_log_id, individual_request_id, error_code, created_date, updated_date, version_number, error_text)
	VALUES (504, 404, 'ERR04', '2022-11-18 14:48:53', '2022-11-18 14:48:53', 1, 'Error text 4');


