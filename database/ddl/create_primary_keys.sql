connect sdt_owner/sdt_owner


define bulk_customers_pk              = 'TABLESPACE users'
define bulk_customer_applications_pk  = 'TABLESPACE users'
define bulk_submissions_pk            = 'TABLESPACE users'
define error_log_pk                   = 'TABLESPACE users'
define error_messages_pk              = 'TABLESPACE users'
define global_parameters_pk           = 'TABLESPACE users'
define individual_requests_pk         = 'TABLESPACE users'
define request_types_pk               = 'TABLESPACE users'
define request_routings_pk             = 'TABLESPACE users'
define target_applications_pk         = 'TABLESPACE users'

--
-- bulk_customers     
--

CREATE INDEX bulk_customers_pk
ON bulk_customers 
( bulk_customer_id )
&bulk_customers_pk
;

ALTER TABLE bulk_customers
ADD CONSTRAINT bulk_customers_pk
PRIMARY KEY ( bulk_customer_id )
USING INDEX bulk_customers_pk
;

--
-- bulk_customer_applications
--

CREATE INDEX bulk_customer_applications_pk
ON bulk_customer_applications
(bulk_customer_id ,target_application_id ) 
&bulk_customer_applications_pk
;

ALTER TABLE bulk_customer_applications
ADD CONSTRAINT bulk_customer_applications_pk
PRIMARY KEY (bulk_customer_id ,target_application_id )
USING INDEX bulk_customer_applications_pk
;

--
-- bulk_submissions
--
 
CREATE INDEX bulk_submissions_pk
ON bulk_submissions
( bulk_submission_id ) 
&bulk_submissions_pk
;

ALTER TABLE bulk_submissions
ADD CONSTRAINT bulk_submissions_pk
PRIMARY KEY ( bulk_submission_id )
USING INDEX bulk_submissions_pk
;

--
-- error_log
--

CREATE INDEX error_log_pk
ON error_log
( error_log_id ) 
&error_log_pk
;

ALTER TABLE error_log
ADD CONSTRAINT error_log_pk
PRIMARY KEY ( error_log_id )
USING INDEX  error_log_pk
;

--
-- error_messages
--

CREATE INDEX error_messages_pk
ON error_messages    
( error_message_id ) 
&error_messages_pk
;

ALTER TABLE error_messages
ADD CONSTRAINT error_messages_pk
PRIMARY KEY ( error_message_id )
USING INDEX error_messages_pk
;


--
-- global_parameters
--
CREATE INDEX global_parameters_pk
ON global_parameters 
(global_parameter_id)
&global_parameters_pk
;

ALTER TABLE global_parameters
ADD CONSTRAINT global_parameters_pk
PRIMARY KEY (global_parameter_id)
USING INDEX global_parameters_pk
;

--
-- individual_requests
--

CREATE INDEX individual_requests_pk
ON individual_requests  
( individual_request_id ) 
&individual_requests_pk 
;

ALTER TABLE individual_requests
ADD CONSTRAINT individual_requests_pk
PRIMARY KEY ( individual_request_id )
USING INDEX individual_requests_pk
;

--
-- request_types
--

CREATE INDEX request_types_pk
ON request_types
( request_type_id ) 
&request_types_pk
;

ALTER TABLE request_types
ADD CONSTRAINT request_types_pk
PRIMARY KEY ( request_type_id )
USING INDEX request_types_pk
;

--
-- request_routings
--

CREATE INDEX request_routings_pk
ON request_routings
( request_type_id, target_application_id ) 
&request_routings_pk
;

ALTER TABLE request_routings
ADD CONSTRAINT request_routings_pk
PRIMARY KEY ( request_type_id, target_application_id )
USING INDEX request_routings_pk
;


--
-- target_applications     
--

CREATE INDEX target_applications_pk
ON target_applications
( target_application_id) 
&target_applications_pk
;

ALTER TABLE target_applications
ADD CONSTRAINT target_applications_pk
PRIMARY KEY ( target_application_id)
USING INDEX target_applications_pk;

