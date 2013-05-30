connect sdt_user/sdt_user

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



INSERT INTO valid_services ( valid_service_id,valid_service_name) 
VALUES ('MCOL','mcol live service');
INSERT INTO valid_services ( valid_service_id,valid_service_name) 
VALUES ('PCOL','pcol live service');
INSERT INTO valid_services ( valid_service_id,valid_service_name) 
VALUES ('C_MC','mcol commissioning service');
INSERT INTO valid_services ( valid_service_id,valid_service_name) 
VALUES ('C_PC','pcol commissioning service');

COMMIT;
