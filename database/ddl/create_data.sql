alter session set current_schema=sdt_user;

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
vALUES ( 1,'REQUEST_RETRY_COUNT',10
,'Number of retry attempts made to transmit an individual request to a third party');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
vALUES ( 2,'BULK_REQUEST_RETENTION_PERIOD',90
,'Duration in days, to retain data in the tables subject to a prescribed purge');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
vALUES ( 3,'MCOL_ACKNOWLEDGEMENT_TIMEOUT',30
,'Period in seconds, to wait for an acknowledgement of receipt from MCOL');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
vALUES ( 4,'MAX_CONCURRENT_SDT_REQUESTS',10
,'Used to determine the maximum number of SDT requests that can be forwarded for processing at any point in time');

INSERT INTO global_parameters ( global_parameter_id,parameter_name,parameter_value,parameter_description)
vALUES ( 5,'MONITORING_INTERVAL',60
,'Time in seconds between ...');


INSERT INTO request_types (request_type_id,request_type_name,request_type_status)
VALUES (1,'NEW CLAIM','A');
INSERT INTO request_types (request_type_id,request_type_name,request_type_status)
VALUES (2,'JUDGEMENT','A');
INSERT INTO request_types (request_type_id,request_type_name,request_type_status)
VALUES (3,'WARRANT','A');
INSERT INTO request_types (request_type_id,request_type_name,request_type_status)
VALUES (4,'JUDGEMENT FORTHWITH','A');
INSERT INTO request_types (request_type_id,request_type_name,request_type_status)
VALUES (5,'PAID/WRITTEN OFF/DISCONTINUED/WITHDRAWN REQUESTS','A');



INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (1,'MCOL','mcol live service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (2,'PCOL','pcol live service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (3,'C_MC','mcol commissioning service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (4,'C_PC','pcol commissioning service');

COMMIT;
