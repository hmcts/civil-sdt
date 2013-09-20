alter session set current_schema=sdt_user;

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
VALUES ( 1,'DATA_RETENTION_PERIOD',90
,'Duration in days, to retain data in the tables subject to a prescribed purge');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
VALUES ( 2,'MCOL_ACKNOWLEDGEMENT_TIMEOUT',15
,'Period in seconds, to wait for an acknowledgement of receipt from MCOL');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
VALUES ( 3,'MAX_FORWARDING_ATTEMPTS',3
,'Number of forwarding attempts made to transmit an individual request to target application');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
VALUES ( 4,'MCOL_MAX_CONCURRENT_INDV_REQ',5
,'Used by the SDT Service to determine the maximum number of concurrent Individual Requests that can be forwarded to MCOL');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
VALUES ( 5,'MCOL_INDV_REQ_DELAY',10
,'Used by the SDT Service to determine the time delay before processing the next Individual Request that can be forwarded to MCOL');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
VALUES ( 6,'MCOL_MAX_CONCURRENT_QUERY_REQ',5
,'Used by the SDT Service to determine the maximum number of concurrent Submit Query Requests that can be forwarded to MCOL');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
VALUES ( 7,'CONTACT_DETAILS','tbc'
,'Used by SDT to hold the current contact details for outgoing SDT application messages to Bulk Customer System');


INSERT INTO service_types (service_type_id,service_type_name,service_type_status, service_type_description)
VALUES (1,'SUBMIT_INDIVIDUAL','A', 'Submit individual request web service');
INSERT INTO service_types (service_type_id,service_type_name,service_type_status, service_type_description)
VALUES (2,'SUBMIT_QUERY','A', 'Submit query web service');


INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (1,'MCOL','mcol live service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (2,'PCOL','pcol live service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (3,'C_MC','mcol commissioning service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (4,'C_PC','pcol commissioning service');

INSERT INTO service_routings (service_type_id,target_application_id,web_service_endpoint)
VALUES (1,1,'http://localhost:8888/mcol-webservices/service');
INSERT INTO service_routings (service_type_id,target_application_id,web_service_endpoint)
VALUES (2,1,'http://localhost:8888/mcol-webservices/service');


COMMIT;
