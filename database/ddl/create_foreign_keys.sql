connect sdt_owner/sdt_owner

ALTER TABLE bulk_customer_services
ADD CONSTRAINT bcs_valid_service_fk
FOREIGN KEY (valid_service_id)
REFERENCES valid_services(valid_service_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE bulk_customer_services
ADD CONSTRAINT bcs_customer_id_fk
FOREIGN KEY (bulk_customer_id)
REFERENCES bulk_customers(bulk_customer_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE bulk_submissions
ADD CONSTRAINT bs_valid_service_fk
FOREIGN KEY (valid_service_id)
REFERENCES valid_services(valid_service_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE bulk_submissions
ADD CONSTRAINT bs_customer_id_fk
FOREIGN KEY (bulk_customer_id)
REFERENCES bulk_customers(bulk_customer_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE error_log
ADD CONSTRAINT el_individual_request_fk
FOREIGN KEY (individual_request_id)
REFERENCES individual_requests(individual_request_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE error_log
ADD CONSTRAINT el_error_code_fk
FOREIGN KEY (error_code)
REFERENCES error_messages(error_code)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE individual_requests
ADD CONSTRAINT ir_request_type_fk
FOREIGN KEY (request_type_id)
REFERENCES request_types(request_type_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE individual_requests
ADD CONSTRAINT ir_bulk_submission_fk
FOREIGN KEY (sdt_bulk_reference)
REFERENCES bulk_submissions(sdt_bulk_reference)
NOT DEFERRABLE INITIALLY IMMEDIATE
;


ALTER TABLE routing_table
ADD CONSTRAINT rt_valid_service_fk
FOREIGN KEY (valid_service_id)
REFERENCES valid_services(valid_service_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE routing_table
ADD CONSTRAINT rt_request_type_fk
FOREIGN KEY (request_type_id)
REFERENCES request_types(request_type_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;



