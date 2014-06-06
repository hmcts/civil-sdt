alter session set current_schema=sdt_owner;

DEFINE bca_bulk_customer_i           = 'TABLESPACE users'
DEFINE bca_target_application_i      = 'TABLESPACE users'
DEFINE bs_sdt_bulk_reference_i       = 'TABLESPACE users'
DEFINE bs_target_application_id_i    = 'TABLESPACE users'
DEFINE bs_bulk_customer_id_i         = 'TABLESPACE users'
DEFINE bs_service_request_id_i       = 'TABLESPACE users'
DEFINE el_individual_request_id_i    = 'TABLESPACE users'
DEFINE ir_bulk_reference_i           = 'TABLESPACE users'
DEFINE ir_bulk_submission_id_i       = 'TABLESPACE users'
DEFINE ir_sdt_request_reference_i    = 'TABLESPACE users'
DEFINE sr_service_type_i             = 'TABLESPACE users'
DEFINE sr_target_application_i       = 'TABLESPACE users'
DEFINE st_service_type_name_i        = 'TABLESPACE users'
DEFINE ta_target_application_name_i  = 'TABLESPACE users'

--
-- bulk_customer_applications
--

CREATE INDEX bca_bulk_customer_i
ON bulk_customer_applications ( bulk_customer_id)
&bca_bulk_customer_i
;

CREATE INDEX bca_target_application_i
ON bulk_customer_applications ( target_application_id)
&bca_target_application_i
;

--
-- bulk_submissions
--

CREATE INDEX bs_sdt_bulk_reference_i
ON bulk_submissions ( sdt_bulk_reference )
&bs_sdt_bulk_reference_i
;

CREATE INDEX bs_target_application_id_i
ON bulk_submissions ( target_application_id )
&bs_target_application_id_i;

CREATE INDEX bs_bulk_customer_id_i
ON bulk_submissions ( bulk_customer_id )
&bs_bulk_customer_id_i;

CREATE INDEX bs_service_request_id_i
ON bulk_submissions( service_request_id )
&bs_service_request_id_i;

--
-- error_logs
--

CREATE INDEX el_individual_request_id_i
ON error_logs(individual_request_id)
&el_individual_request_id_i;

--
--individual_requests
--

CREATE INDEX ir_bulk_reference_i
ON individual_requests ( sdt_bulk_reference )
&ir_bulk_reference_i;

CREATE INDEX ir_bulk_submission_id_i
ON individual_requests ( bulk_submission_id )
&ir_bulk_submission_id_i;

CREATE INDEX ir_sdt_request_reference_i
ON individual_requests( sdt_request_reference )
&ir_sdt_request_reference_i;


--
-- service_routings
--
CREATE INDEX sr_service_type_i
ON service_routings (service_type_id)
&sr_service_type_i
;

CREATE INDEX sr_target_application_i
ON service_routings ( target_application_id)
&sr_target_application_i
;

--
-- service_types
--
CREATE INDEX st_service_type_name
ON service_types ( service_type_name )
&st_service_type_name_i
;

-- 
-- target_applications
--
CREATE INDEX ta_target_application_name 
ON target_applications ( target_application_name)
&ta_target_application_name_i
;


