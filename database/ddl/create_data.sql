alter session set current_schema=sdt_user;

INSERT INTO request_types (request_type_id,request_type_name)
VALUES (1,'NEW CLAIM');
INSERT INTO request_types (request_type_id,request_type_name)
VALUES (2,'JUDGEMENT');
INSERT INTO request_types (request_type_id,request_type_name)
VALUES (3,'WARRANT');
INSERT INTO request_types (request_type_id,request_type_name)
VALUES (4,'JUDGEMENT FORTHWITH');
INSERT INTO request_types (request_type_id,request_type_name)
VALUES (5,'PAID/WRITTEN OFF/DISCONTINUED/WITHDRAWN REQUESTS');



INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (1,'MCOL','mcol live service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (2,'PCOL','pcol live service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (3,'C_MC','mcol commissioning service');
INSERT INTO target_applications ( target_application_id,target_application_code,target_application_name) 
VALUES (4,'C_PC','pcol commissioning service');

COMMIT;
