alter session set current_schema=sdt_owner;


DEFINE bs_sdt_bulk_reference_i       = 'TABLESPACE users'
DEFINE ir_bulk_reference_i           = 'TABLESPACE users'
DEFINE rt_request_type_name_i        = 'TABLESPACE users'
DEFINE ta_target_application_name_i  = 'TABLESPACE users'

--
-- bulk_submissions
--

CREATE INDEX bs_sdt_bulk_reference_i
ON bulk_submissions ( sdt_bulk_reference )
&bs_sdt_bulk_reference_i
;

--
--individual_requests
--

CREATE INDEX ir_bulk_reference_i
ON individual_requests ( sdt_bulk_reference )
&ir_bulk_reference_i
;

--
-- request_types
--
CREATE INDEX rt_request_type_name
ON request_types ( request_type_name )
&rt_request_type_name_i
;

-- 
-- target_applications
--
CREATE INDEX ta_target_application_name 
ON target_applications ( target_application_name)
&ta_target_application_name_i
;
