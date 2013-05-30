connect sdt_owner/sdt_owner


define bulk_customers_pk          = 'TABLESPACE users'
define bulk_customer_services_pk  = 'TABLESPACE users'
define bulk_submissions_pk        = 'TABLESPACE users'
define error_log_pk               = 'TABLESPACE users'
define error_messages_pk          = 'TABLESPACE users'
define global_parameters_pk       = 'TABLESPACE users'
define individual_requests_pk     = 'TABLESPACE users'
define request_types_pk           = 'TABLESPACE users'
define routing_table_pk           = 'TABLESPACE users'
define valid_services_pk          = 'TABLESPACE users'

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
-- bulk_customer_services    
--

CREATE INDEX bulk_customer_services_pk
ON bulk_customer_services
(bulk_customer_id ,valid_service_id ) 
&bulk_customer_services_pk
;

ALTER TABLE bulk_customer_services
ADD CONSTRAINT bulk_customer_services_pk
PRIMARY KEY (bulk_customer_id ,valid_service_id )
USING INDEX bulk_customer_services_pk
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
-- error _messages
--

CREATE INDEX error_messages_pk
ON error_messages    
( error_code ) 
&error_messages_pk
;

ALTER TABLE error_messages
ADD CONSTRAINT error_messages_pk
PRIMARY KEY ( error_code )
USING INDEX error_messages_pk
;


--
-- global_parameters
--
CREATE INDEX global_parameters_pk
ON global_parameters 
(parameter_id)
&global_parameters_pk
;

ALTER TABLE global_parameters
ADD CONSTRAINT global_parameters_pk
PRIMARY KEY (parameter_id)
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
-- routing_table
--

CREATE INDEX routing_table_pk
ON routing_table
( request_type_id, valid_service_id ) 
&routing_table_pk
;

ALTER TABLE routing_table
ADD CONSTRAINT routing_table_pk
PRIMARY KEY ( request_type_id, valid_service_id )
USING INDEX routing_table_pk
;


--
-- valid_services
--

CREATE INDEX valid_services_pk
ON valid_services
( valid_service_id) 
&valid_services_pk
;

ALTER TABLE valid_services
ADD CONSTRAINT valid_services_pk
PRIMARY KEY ( valid_service_id)
USING INDEX valid_services_pk;

