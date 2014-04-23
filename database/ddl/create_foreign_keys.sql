alter session set current_schema=sdt_owner;

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

ALTER TABLE bulk_submissions
ADD CONSTRAINT bs_service_request_id_fk
FOREIGN KEY (service_request_id)
REFERENCES service_requests(service_request_id)
ON DELETE CASCADE
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE error_logs
ADD CONSTRAINT el_individual_request_fk
FOREIGN KEY (individual_request_id)
REFERENCES individual_requests(individual_request_id)
ON DELETE CASCADE
NOT DEFERRABLE INITIALLY IMMEDIATE
;


ALTER TABLE individual_requests
ADD CONSTRAINT ir_bulk_submission_fk
FOREIGN KEY (bulk_submission_id)
REFERENCES bulk_submissions(bulk_submission_id)
ON DELETE CASCADE
NOT DEFERRABLE INITIALLY IMMEDIATE
;


ALTER TABLE service_routings
ADD CONSTRAINT sr_target_application_fk
FOREIGN KEY (target_application_id)
REFERENCES target_applications(target_application_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;

ALTER TABLE service_routings
ADD CONSTRAINT sr_service_type_fk
FOREIGN KEY (service_type_id)
REFERENCES service_types(service_type_id)
NOT DEFERRABLE INITIALLY IMMEDIATE
;



