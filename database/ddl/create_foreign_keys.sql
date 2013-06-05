connect sdt_owner/sdt_owner

ALTER TABLE bulk_customer_applications
ADD CONSTRAINT bca_target_application_fk
FOREIGN KEY (target_application_id)
REFERENCES target_applications(target_application_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE bulk_customer_applications
ADD CONSTRAINT bca_bulk_customer_fk
FOREIGN KEY (bulk_customer_id)
REFERENCES bulk_customers(bulk_customer_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE bulk_submissions
ADD CONSTRAINT bs_target_application_fk
FOREIGN KEY (target_application_id)
REFERENCES target_applications(target_application_id)
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
ADD CONSTRAINT el_error_message_fk
FOREIGN KEY (error_message_id)
REFERENCES error_messages(error_message_id)
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


ALTER TABLE request_routings
ADD CONSTRAINT rr_target_application_fk
FOREIGN KEY (target_application_id)
REFERENCES target_applications(target_application_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE request_routings
ADD CONSTRAINT rr_request_type_fk
FOREIGN KEY (request_type_id)
REFERENCES request_types(request_type_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;



